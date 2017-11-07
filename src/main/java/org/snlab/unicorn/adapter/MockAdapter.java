package org.snlab.unicorn.adapter;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    @Override
    public PathQueryResponseBody getAsPath(List<QueryItem> querySet) {
        PathQueryResponseBody body = new PathQueryResponseBody();
        List<String> response = new ArrayList<>();
        for (int i = 0; i < querySet.size(); i++) {
            try {
                byte[] address = new byte[4];
                randomGen.nextBytes(address);
				response.add(Inet4Address.getByAddress(address).toString());
			} catch (UnknownHostException e) {
                response.add(DEFAULT_IPV4_ADDR);
			}
        }
        body.setResponse(response);
        return body;
    }

    @Override
    public ResourceQueryResponseBody getResource(List<QueryItem> querySet) {
        ResourceQueryResponseBody body = new ResourceQueryResponseBody();
        ResourceQueryResponse response = new ResourceQueryResponse();

        List<Ane> anes = new ArrayList<>();
        List<List<AneMatrix>> matrix = new ArrayList<>();
        int aneNum = randomGen.nextInt(MAX_ANE_NUM) + 1;
        for (int i = 0; i < aneNum; i++) {
            Ane ane = new Ane();
            ane.setAvailbw(randomGen.nextLong());
            anes.add(ane);
            List<AneMatrix> matrixRow = new ArrayList<>();
            for (QueryItem item : querySet) {
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
        return false;
    }

    @Override
    public boolean ifResourceChangedThenCleanState() {
        return false;
    }

}