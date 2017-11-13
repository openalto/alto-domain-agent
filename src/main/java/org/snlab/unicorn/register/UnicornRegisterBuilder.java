package org.snlab.unicorn.register;

import org.snlab.unicorn.model.Host;
import org.snlab.unicorn.orchestrator.OrchestratorInfo;
import org.snlab.unicorn.server.ServerInfo;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class UnicornRegisterBuilder {
    protected List<OrchestratorInfo> orchestratorInfos;

    public UnicornRegisterBuilder() {
        orchestratorInfos = new ArrayList<>();
    }

    public UnicornRegisterBuilder addOrchestrator(OrchestratorInfo info) {
        orchestratorInfos.add(info);
        return this;
    }

    public UnicornRegisterBuilder addOrchestrators(Collection<OrchestratorInfo> infos) {
        orchestratorInfos.addAll(infos);
        return this;
    }

    public UnicornRegister build() {
        return new UnicornRegisterImpl(this.orchestratorInfos);
    }

    public static class UnicornRegisterImpl implements UnicornRegister {
        protected Collection<OrchestratorInfo> infos;

        UnicornRegisterImpl(Collection<OrchestratorInfo> infos) {
            this.infos = infos;
        }

        private String buildCompleteURL(String domainIP, int port, String urlRoute, String protocol) {
            return protocol + "://" + domainIP + ":" + port + urlRoute;
        }

        private JsonObject buildRegisterJSON() {
            ServerInfo serverInfo = ServerInfo.getInstance();
            JsonBuilderFactory factory = Json.createBuilderFactory(new HashMap<>());
            JsonArrayBuilder hostsBuilder = factory.createArrayBuilder();
            for (Host host : serverInfo.getHosts()) {
                JsonObject hostObject = factory.createObjectBuilder()
                        .add("host-ip", host.getHostIp())
                        .add("management-ip", host.getManagementIp())
                        .build();
                hostsBuilder.add(hostObject);
            }

            JsonArrayBuilder ingressPointsBuilder = factory.createArrayBuilder();
            for (String ingressPoint : serverInfo.getIngressPoints()) {
                ingressPointsBuilder.add(ingressPoint);
            }
            JsonObject object = factory.createObjectBuilder()
                    .add("domain-name", serverInfo.getDomainName())
                    .add("update-url", buildCompleteURL(
                            serverInfo.getDomainIp(),
                            serverInfo.getHttpPort(),
                            serverInfo.getUpdateURL(),
                            "http"
                    ))
                    .add("deploy-url", buildCompleteURL(
                            serverInfo.getDomainIp(),
                            serverInfo.getHttpPort(),
                            serverInfo.getDeployURL(),
                            "http"
                    ))
                    .add("hosts", hostsBuilder)
                    .add("ingress-points", ingressPointsBuilder)
                    .build();
            return object;
        }

        @Override
        public void register() {
            // Build register json
            JsonObject object = buildRegisterJSON();

            // Send the register json to every domain
            for (OrchestratorInfo info : this.infos) {
                String remoteURL = "http://" + info.getIp() + ":" + info.getPort() + info.getRegistry();
                try {
                    URL url = new URL(remoteURL);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    // Headers Setting
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setRequestProperty("Content-Length", Integer.toString(object.toString().getBytes().length));
                    connection.setRequestProperty("Content-Language", "en-US");
                    connection.setUseCaches(false);
                    connection.setDoOutput(true);

                    // Send Request
                    DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
                    wr.writeBytes(object.toString());
                    wr.close();

                    // Get Response
                    InputStream is = connection.getInputStream();
                    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = rd.readLine()) != null) {
                        response.append(line);
                        response.append('\r');
                    }
                    rd.close();
                    String responseString = response.toString();

                    // TODO: check response string
                    System.out.println(responseString);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
