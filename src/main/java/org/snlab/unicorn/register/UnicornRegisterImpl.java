package org.snlab.unicorn.register;

import org.snlab.unicorn.orchestrator.OrchestratorInfo;
import org.snlab.unicorn.server.ServerInfo;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import java.util.Collection;
import java.util.HashMap;

public class UnicornRegisterImpl implements UnicornRegister {
    protected Collection<OrchestratorInfo> infos;

    UnicornRegisterImpl(Collection<OrchestratorInfo> infos) {
        this.infos = infos;
    }

    @Override
    public void register() {
        // Build register json
        ServerInfo serverInfo = ServerInfo.getInstance();
        JsonBuilderFactory factory = Json.createBuilderFactory(new HashMap<>());
        JsonArrayBuilder hostsBuilder = factory.createArrayBuilder();
        for (String host : serverInfo.getHosts()) {
            hostsBuilder.add(host);
        }

        JsonArrayBuilder ingressPointsBuilder = factory.createArrayBuilder();
        for (String ingressPoint : serverInfo.getIngressPoints()) {
            ingressPointsBuilder.add(ingressPoint);
        }
        JsonObject object = factory.createObjectBuilder()
                .add("domain-name", serverInfo.getDomainName())
                .add("controller-url", serverInfo.getControlURL())
                .add("hosts", hostsBuilder)
                .add("ingress-points", ingressPointsBuilder)
                .build();

        // Send the register object to remote
        for (OrchestratorInfo info: this.infos){
            String remoteURL = "http://" + info.getIp() + ":" + info.getPort() + info.getRegistry();
            //TODO: send to remote
        }
    }
}
