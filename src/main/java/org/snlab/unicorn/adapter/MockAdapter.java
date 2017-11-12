package org.snlab.unicorn.adapter;

import org.snlab.unicorn.UnicornDefinitions;
import org.snlab.unicorn.model.Ane;
import org.snlab.unicorn.model.AneMatrix;
import org.snlab.unicorn.model.PathQueryResponseBody;
import org.snlab.unicorn.model.QueryItem;
import org.snlab.unicorn.model.ResourceQueryResponse;
import org.snlab.unicorn.model.ResourceQueryResponseBody;
import org.snlab.unicorn.server.ServerInfo;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class MockAdapter implements ControllerAdapter {

    private final static String DEFAULT_IPV4_ADDR = "10.10.10.10";
    private final static int MAX_ANE_NUM = 10;
    private Random randomGen = new Random();
    private boolean changeSignal = false;

    protected Map<String, String> pathResult;

    public MockAdapter() {
        new Thread(() -> {
            while (true) {
                try {
                    TimeUnit.MINUTES.sleep(1L);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                changeSignal = true;
            }
        }).start();

        pathResult = new HashMap<>();
        configReader(UnicornDefinitions.AdapterConfig.MOCK_CONFIG_PATH);
    }

    protected void configReader(String adapterConfigPath) {
        InputStream stream = MockAdapter.class.getClassLoader().getResourceAsStream(adapterConfigPath);
        String domainName = ServerInfo.getInstance().getDomainName();
        JsonObject object = Json.createReader(stream).readObject().getJsonObject(domainName);

        JsonObject paths = object.getJsonObject("path");
        Set<String> dstIps = paths.keySet();
        for (String dstIp : dstIps)
            this.pathResult.put(dstIp, paths.getString(dstIp));

        JsonObject resources = object.getJsonObject("resource");
        // TODO: mock resource adapter
    }

    @Override
    public PathQueryResponseBody getAsPath(List<QueryItem> queryDescs) {
        PathQueryResponseBody body = new PathQueryResponseBody();
        List<String> response = new ArrayList<>();
        for (QueryItem item : queryDescs) {
            String dstIp = item.getFlow().getDstIp();
            response.add(this.pathResult.getOrDefault(dstIp, DEFAULT_IPV4_ADDR));
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
        int aneNum = randomGen.nextInt(MAX_ANE_NUM) + 1;
        for (int i = 0; i < aneNum; i++) {
            Ane ane = new Ane();
            ane.setAvailbw(randomGen.nextInt(100000000) * 1000L);
            anes.add(ane);
            List<AneMatrix> matrixRow = new ArrayList<>();
            for (QueryItem item : queryDescs) {
                AneMatrix matrixElem = new AneMatrix();
                matrixElem.setFlowId(item.getFlow().getFlowId());
                matrixElem.setCoefficient(randomGen.nextDouble());
                matrixRow.add(matrixElem);
            }
            matrix.add(matrixRow);
        }
        response.setAnes(anes);
        response.setAneMatrix(matrix);
        body.setResponse(response);
        return body;
    }

    @Override
    public boolean ifAsPathChangedThenCleanState() {
        if (changeSignal) {
            changeSignal = false;
            return true;
        }
        return false;
    }

    @Override
    public boolean ifResourceChangedThenCleanState() {
        return ifAsPathChangedThenCleanState();
    }

}
