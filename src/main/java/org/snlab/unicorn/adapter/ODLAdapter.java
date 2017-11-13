package org.snlab.unicorn.adapter;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snlab.unicorn.model.Ane;
import org.snlab.unicorn.model.AneMatrix;
import org.snlab.unicorn.model.PathQueryResponseBody;
import org.snlab.unicorn.model.QueryItem;
import org.snlab.unicorn.model.ResourceQueryResponse;
import org.snlab.unicorn.model.ResourceQueryResponseBody;
import org.snlab.unicorn.model.odl.ODLAne;
import org.snlab.unicorn.model.odl.ODLAneFlowCoefficient;
import org.snlab.unicorn.model.odl.ODLFlowDesc;
import org.snlab.unicorn.model.odl.ODLNextIngressPoint;
import org.snlab.unicorn.model.odl.ODLPathQueryInput;
import org.snlab.unicorn.model.odl.ODLPathQueryOutput;
import org.snlab.unicorn.model.odl.ODLPathQueryRequest;
import org.snlab.unicorn.model.odl.ODLPathQueryResponse;
import org.snlab.unicorn.model.odl.ODLQueryDesc;
import org.snlab.unicorn.model.odl.ODLResourceQueryInput;
import org.snlab.unicorn.model.odl.ODLResourceQueryRequest;
import org.snlab.unicorn.model.odl.ODLResourceQueryResponse;

public class ODLAdapter implements ControllerAdapter {

    private final static Logger LOG = LoggerFactory.getLogger(ODLAdapter.class);
    private final static String UNICORN_PATH_QUERY_URI = "/operations/alto-unicorn:path-query";
    private final static String UNICORN_RESOURCE_QUERY_URI = "/operations/alto-unicorn:resource-query";

    private static ObjectMapper mapper = new ObjectMapper();

    private URI baseUri;
    private Credentials auth;
    private Executor executor;
    private boolean isAsPathChanged = false;
    private boolean isResourceChanged = false;
    private ODLNotificationClient pathManagerSocketClient;

    public ODLAdapter(URI baseUri, Credentials auth) {
        this.baseUri = baseUri;
        if (auth == null) {
            this.auth = new UsernamePasswordCredentials("admin", "admin");
        } else {
            this.auth = auth;
        }
        executor = Executor.newInstance().auth(this.auth);
        mapper.setSerializationInclusion(Include.NON_NULL);
        setupWebsocketToListenUpdate();
    }

    private void setupWebsocketToListenUpdate() {
        pathManagerSocketClient = new ODLNotificationClient(baseUri, auth);
        pathManagerSocketClient.connect(ODLConstants.UNICORN_PATH_MANAGER_SUBSCRIPTION);
    }

    private Request getRestconfRequest(String path, String data) {
        URI restconfUri;
        try {
            restconfUri = new URI(baseUri.toString() + "/restconf" + path);
        } catch (URISyntaxException e) {
            LOG.info("The requested uri is invalid on path: {}", path);
            return null;
        }
        if (data != null) {
            return Request.Post(restconfUri).bodyString(data, ContentType.APPLICATION_JSON);
        } else {
            return Request.Get(restconfUri);
        }
    }

    private List<String> getFlowIdByOrder(List<QueryItem> queryDescs) {
        List<String> queryDescOrder = new ArrayList<>();
        for (QueryItem desc : queryDescs) {
            queryDescOrder.add(desc.getFlow().getFlowId());
        }
        return queryDescOrder;
    }

    private Map<String, String> mapFlowIdToIngressPoint(ODLPathQueryOutput output) {
        Map<String, String> nextIngressPointMap = new HashMap<>();
        if (output.getNextIngressPoint() != null) {
            for (ODLNextIngressPoint nextIngressPoint : output.getNextIngressPoint()) {
                nextIngressPointMap.put(nextIngressPoint.getFlowId().toString(), nextIngressPoint.getIngressPoint());
            }
        }
        return nextIngressPointMap;
    }

    private PathQueryResponseBody convertJsonStringToPathQueryResponse(String data, List<String> list)
            throws JsonParseException, JsonMappingException, IOException {
        ODLPathQueryResponse odlResponse = mapper.readValue(data, ODLPathQueryResponse.class);
        ODLPathQueryOutput output = odlResponse.getOutput();
        Map<String, String> nextIngressPointMap = mapFlowIdToIngressPoint(output);
        PathQueryResponseBody body = new PathQueryResponseBody();
        List<String> response = new ArrayList<>();
        for (String flowId : list) {
            // TODO: Not consider the flow-id in output does not match the one in input.
            response.add(nextIngressPointMap.get(flowId));
        }
        body.setResponse(response);
        return body;
    }

    private ResourceQueryResponseBody convertJsonStringToResourceQueryResponse(String data)
            throws JsonParseException, JsonMappingException, IOException {
        ODLResourceQueryResponse odlResponse = mapper.readValue(data, ODLResourceQueryResponse.class);
        ResourceQueryResponseBody body = new ResourceQueryResponseBody();
        ResourceQueryResponse response = new ResourceQueryResponse();
        List<Ane> aneList = new ArrayList<>();
        List<List<AneMatrix>> aneMatrix = new ArrayList<>();
        for (ODLAne odlAne : odlResponse.getOutput().getAnes()) {
            Ane ane = new Ane();
            ane.setAvailbw(odlAne.getAvailbw());
            aneList.add(ane);
            List<AneMatrix> matrixRow = new ArrayList<>();
            for (ODLAneFlowCoefficient coefficient : odlAne.getAneneFlowCoefficients()) {
                AneMatrix matrixElem = new AneMatrix();
                matrixElem.setFlowId(coefficient.getFlowId().toString());
                matrixElem.setCoefficient(coefficient.getCoefficient());
                matrixRow.add(matrixElem);
            }
            aneMatrix.add(matrixRow);
        }
        response.setAnes(aneList);
        response.setAneMatrix(aneMatrix);
        body.setResponse(response);
        return body;
    }

    private String getLongestPrefixForIp(String address) {
        if (address == null) {
            return null;
        }
        return address + "/32";
    }

    private List<ODLQueryDesc> convertToODLQueryDesc(List<QueryItem> queryDescs) {
        List<ODLQueryDesc> odlQueryDescs = new ArrayList<>();
        for (QueryItem item : queryDescs) {
            ODLQueryDesc desc = new ODLQueryDesc();
            desc.setFlowId(Integer.valueOf(item.getFlow().getFlowId()));
            ODLFlowDesc flow = new ODLFlowDesc();
            flow.setSrcIp(getLongestPrefixForIp(item.getFlow().getSrcIp()));
            flow.setDstIp(getLongestPrefixForIp(item.getFlow().getDstIp()));
            flow.setDstPort(item.getFlow().getDstPort());
            flow.setProtocol(item.getFlow().getProtocol());
            desc.setFlow(flow);
            desc.setIngressPoint(item.getIngressPoint());
            odlQueryDescs.add(desc);
        }
        return odlQueryDescs;
    }

    private String convertQueryDescsToPathQueryRequestString(List<QueryItem> queryDescs) throws JsonProcessingException {
        ODLPathQueryRequest request = new ODLPathQueryRequest();
        ODLPathQueryInput input = new ODLPathQueryInput();
        List<ODLQueryDesc> odlQueryDescs = convertToODLQueryDesc(queryDescs);
        input.setPathQueryDescs(odlQueryDescs);
        request.setInput(input);
        return mapper.writeValueAsString(request);
    }

    private String convertQueryDescsToResourceQueryRequestString(List<QueryItem> queryDescs) throws JsonProcessingException {
        ODLResourceQueryRequest request = new ODLResourceQueryRequest();
        ODLResourceQueryInput input = new ODLResourceQueryInput();
        List<ODLQueryDesc> odlQueryDescs = convertToODLQueryDesc(queryDescs);
        input.setPathQueryDescs(odlQueryDescs);
        request.setInput(input);
        return mapper.writeValueAsString(request);
    }

    public PathQueryResponseBody getAsPath(List<QueryItem> queryDescs) {
        try {
            Response response = executor
                    .execute(getRestconfRequest(UNICORN_PATH_QUERY_URI,
                            convertQueryDescsToPathQueryRequestString(queryDescs)));
            if (response.returnResponse().getStatusLine().getStatusCode() / 100 != 2) {
                return null;
            } else {
                return convertJsonStringToPathQueryResponse(response.returnContent().asString(),
                        getFlowIdByOrder(queryDescs));
            }
        } catch (JsonProcessingException e) {
            LOG.error("Invalid json value:", e);
        } catch (IOException e) {
            LOG.error("Fail to handle http request:", e);
        }
        return null;
    }

    public ResourceQueryResponseBody getResource(List<QueryItem> queryDescs) {
        try {
            Response response = executor
                    .execute(getRestconfRequest(UNICORN_RESOURCE_QUERY_URI,
                            convertQueryDescsToResourceQueryRequestString(queryDescs)));
            return convertJsonStringToResourceQueryResponse(response.returnContent().asString());
        } catch (JsonProcessingException e) {
            LOG.error("Invalid json value:", e);
        } catch (IOException e) {
            LOG.error("Fail to handle http request:", e);
        }
        return null;
    }

    @Override
    public boolean ifAsPathChangedThenCleanState() {
        if (isAsPathChanged == true) {
            isAsPathChanged = false;
            return true;
        }
        return false;
    }

    @Override
    public boolean ifResourceChangedThenCleanState() {
        if (isResourceChanged == true) {
            isResourceChanged = false;
            return true;
        } else if (pathManagerSocketClient != null) {
            return pathManagerSocketClient.readStateAndClean();
        }
        return false;
    }
}