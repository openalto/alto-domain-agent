package org.snlab.unicorn.adapter;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snlab.unicorn.model.PathQueryResponseBody;
import org.snlab.unicorn.model.QueryItem;
import org.snlab.unicorn.model.ResourceQueryResponseBody;

public class ODLAdapter implements ControllerAdapter {

    private final static Logger LOG = LoggerFactory.getLogger(ODLAdapter.class);
    private final static String UNICORN_PATH_QUERY_URI = "/operations/alto-unicorn:path-query";
    private final static String UNICORN_RESOURCE_QUERY_URI = "/operations/alto-unicorn:resource-query";

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

    private PathQueryResponseBody convertJsonStringToPathQueryResponse(String data) {
        // TODO: convert path query response from the json string
        return new PathQueryResponseBody();
    }

    private ResourceQueryResponseBody convertJsonStringToResourceQueryResponse(String data) {
        // TODO: convert resource query response from the json string
        return new ResourceQueryResponseBody();
    }

    public PathQueryResponseBody getAsPath(List<QueryItem> querySet) {
        try {
            Response response = executor
                    .execute(getRestconfRequest(UNICORN_PATH_QUERY_URI, querySet.toString()));
            if (response.returnResponse().getStatusLine().getStatusCode() / 100 != 2) {
                return null;
            } else {
                return convertJsonStringToPathQueryResponse(response.returnContent().asString());
            }
        } catch (IOException e) {
            LOG.error("Fail to handle http request:", e);
        }
        return null;
    }

    public ResourceQueryResponseBody getResource(List<QueryItem> querySet) {
        try {
            Response response = executor
                    .execute(getRestconfRequest(UNICORN_RESOURCE_QUERY_URI, querySet.toString()));
            if (response.returnResponse().getStatusLine().getStatusCode() / 100 != 2) {
                return null;
            } else {
                return convertJsonStringToResourceQueryResponse(response.returnContent().asString());
            }
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