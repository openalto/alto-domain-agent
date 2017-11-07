package org.snlab.unicorn.adapter;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.snlab.unicorn.model.Ane;
import org.snlab.unicorn.model.AneMatrix;
import org.snlab.unicorn.model.PathQueryResponseBody;
import org.snlab.unicorn.model.QueryItem;
import org.snlab.unicorn.model.ResourceQueryResponse;
import org.snlab.unicorn.model.ResourceQueryResponseBody;

public class MockAdapter implements ControllerAdapter {

    private final static String DEFAULT_IPV4_ADDR = "10.10.10.10";
    private final static int MAX_ANE_NUM = 10;
    private Random randomGen = new Random();
    private boolean changeSignal = false;

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
    }

    @Override
    public PathQueryResponseBody getAsPath(List<QueryItem> queryDescs) {
        PathQueryResponseBody body = new PathQueryResponseBody();
        List<String> response = new ArrayList<>();
        for (int i = 0; i < queryDescs.size(); i++) {
            try {
                byte[] address = new byte[4];
                randomGen.nextBytes(address);
				response.add(Inet4Address.getByAddress(address).getHostAddress());
			} catch (UnknownHostException e) {
                response.add(DEFAULT_IPV4_ADDR);
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
        if (changeSignal == true) {
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