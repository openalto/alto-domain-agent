package org.snlab.unicorn.adapter;

import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snlab.unicorn.UnicornDefinitions;
import org.snlab.unicorn.model.*;
import org.snlab.unicorn.server.ServerInfo;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URI;
import java.util.*;

public class SC18Adapter implements ControllerAdapter {
    private final static Logger LOG = LoggerFactory.getLogger(SC18Adapter.class);

    private final static String BWMONITOR_QUERY_URI = "";

    private Map<String, String> pathResult;

    private URI baseUri;
    private Credentials auth;
    private Executor executor;
    private String dpiNode;
    private SC18PathManager pathManager;

    public SC18Adapter(URI baseUri, Credentials auth, String dpiNode) {
        this.baseUri = baseUri;
        this.dpiNode = dpiNode;
        if (auth == null)
            this.auth = new UsernamePasswordCredentials("admin", "admin");
        else
            this.auth = auth;

        executor = Executor.newInstance().auth(auth);

        pathResult = new HashMap<>();
        pathManager = new SC18PathManager(UnicornDefinitions.AdapterConfig.SC18_CONFIG_PATH);
        configReader(UnicornDefinitions.AdapterConfig.SC18_CONFIG_PATH);
    }

    private void configReader(String adapterConfigPath) {
        InputStream stream = SC18Adapter.class.getClassLoader().getResourceAsStream(adapterConfigPath);
        String domainName = ServerInfo.getInstance().getDomainName();

        // Read path
        JsonObject object = Json.createReader(stream).readObject().getJsonObject(domainName);

        JsonObject paths = object.getJsonObject("path");
        Set<String> pathDstIps = paths.keySet();
        for (String dstIp : pathDstIps)
            this.pathResult.put(dstIp, paths.getString(dstIp));
    }

    @Override
    public PathQueryResponseBody getAsPath(List<QueryItem> queryDescs) {
        // Use mock AS path
        PathQueryResponseBody body = new PathQueryResponseBody();
        List<String> response = new ArrayList<>();
        for (QueryItem item : queryDescs) {
            String dstIp = item.getFlow().getDstIp();
            if (ServerInfo.getInstance().getHostIPs().contains(dstIp) || ServerInfo.getInstance().getIngressPoints().contains(dstIp))
                response.add("");
            else {
                String nextIngressPoint = this.pathResult.get(dstIp);
                response.add(nextIngressPoint);
            }
        }
        body.setResponse(response);
        return body;
    }

    @Override
    public ResourceQueryResponseBody getResource(List<QueryItem> queryDescs) {
        ResourceQueryResponseBody body = new ResourceQueryResponseBody();
        ResourceQueryResponse response = new ResourceQueryResponse();

        List<Ane> anes = new ArrayList<>();
        List<List<AneMatrix>> matrix = new ArrayList<>();

        List<PathItem> pathItems = new ArrayList<>();

        // Get path of every queryItem
        for (QueryItem queryItem : queryDescs) {
            Flow flow = queryItem.getFlow();
            PathItem pathItem = pathManager.match(flow);
            pathItems.add(pathItem);
        }

        // Get port ids given by flow
        Set<String> requirePortIds = new HashSet<>();
        for (PathItem item : pathItems)
            requirePortIds.addAll(item.getLinks());

        // Find the availbw of every port
        Map<String, Long> bandwidthMap = new HashMap<>();
        for (String port : requirePortIds) {
            try {
                Response resp = executor.execute(Request.Post(this.baseUri + BWMONITOR_QUERY_URI));
                Long bw = convertBandwidthResponseToLong(resp.returnContent().asString());
                bandwidthMap.put(port, bw);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        SC18PathVectorReader pvReader = new SC18PathVectorReader(bandwidthMap);
        for (QueryItem item : queryDescs) {
            PathItem pathItem = pathManager.match(item.getFlow());
            pvReader.addPath(pathItem);
        }
        List<PathVectorItem> pvItems = pvReader.getResult();
        int flowNum = 0;
        for (PathVectorItem item : pvItems) {
            flowNum = Math.max(Collections.max(item.getXs()), flowNum);
        }
        flowNum += 1;
        for (PathVectorItem item : pvItems) {
            Set<Integer> xs = item.getXs();

            List<AneMatrix> matrixRow = new ArrayList<>();
            for (int i = 0; i < flowNum; i++) {
                AneMatrix matrixElem = new AneMatrix();
                matrixElem.setFlowId(queryDescs.get(i).getFlow().getFlowId());
                if (xs.contains(i)) {
                    matrixElem.setCoefficient(1D);
                } else
                    matrixElem.setCoefficient(0D);
                matrixRow.add(matrixElem);
            }
            matrix.add(matrixRow);

            Ane ane = new Ane();
            ane.setAvailbw(item.getY());
        }

        response.setAnes(anes);
        response.setAneMatrix(matrix);
        body.setResponse(response);
        return body;
    }

    private Long convertBandwidthResponseToLong(String resp) {
        JsonObject object = Json.createReader(new StringReader(resp)).readObject();
        // TODO: implement this
        return 0L;
    }

    @Override
    public String deployRoute(Endpoints endpoints) {
        return null;
    }

    @Override
    public String deployOnDemandRoute(String demand) {
        return null;
    }

    @Override
    public boolean ifAsPathChangedThenCleanState() {
        return false;
    }

    @Override
    public boolean ifResourceChangedThenCleanState() {
        return false;
    }
}
