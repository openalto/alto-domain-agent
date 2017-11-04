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
import org.snlab.unicorn.model.odl.PathQueryResponse;
import org.snlab.unicorn.model.odl.QueryDesc;
import org.snlab.unicorn.model.odl.ResourceQueryResponse;

public class ODLAdapter implements ControllerAdapter {

    private final static Logger LOG = LoggerFactory.getLogger(ODLAdapter.class);
    private final static String UNICORN_PATH_QUERY_URI = "/operations/alto-unicorn:path-query";
    private final static String UNICORN_RESOURCE_QUERY_URI = "/operations/alto-unicorn:resource-query";

    private URI baseUri;
    private Credentials auth;
    private Executor executor;

    public ODLAdapter(URI baseUri, Credentials auth) {
        this.baseUri = baseUri;
        if (auth == null) {
            this.auth = new UsernamePasswordCredentials("admin", "admin");
        } else {
            this.auth = auth;
        }
        executor = Executor.newInstance().auth(this.auth);
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

    private PathQueryResponse convertJsonStringToPathQueryResponse(String data) {
        return new PathQueryResponse();
    }

    private ResourceQueryResponse convertJsonStringToResourceQueryResponse(String data) {
        return new ResourceQueryResponse();
    }

    public PathQueryResponse getAsPath(List<QueryDesc> querySet) {
        try {
            Response response = executor
                    .execute(getRestconfRequest(UNICORN_PATH_QUERY_URI, querySet.toString()));
            if (response.returnResponse().getStatusLine().getStatusCode() / 100 != 2) {
                return new PathQueryResponse();
            } else {
                return convertJsonStringToPathQueryResponse(response.returnContent().asString());
            }
        } catch (IOException e) {
            LOG.error("Fail to handle http request:", e);
        }
        return null;
    }

    public ResourceQueryResponse getResource(List<QueryDesc> querySet) {
        try {
            Response response = executor
                    .execute(getRestconfRequest(UNICORN_RESOURCE_QUERY_URI, querySet.toString()));
            if (response.returnResponse().getStatusLine().getStatusCode() / 100 != 2) {
                return new ResourceQueryResponse();
            } else {
                return convertJsonStringToResourceQueryResponse(response.returnContent().asString());
            }
        } catch (IOException e) {
            LOG.error("Fail to handle http request:", e);
        }
        return null;
    }

    @Override
    public void addAsPathListener() {
        // TODO: Inject a callback. Once receiving as path changed notification, call the method.
    }

    @Override
    public void addResourceListener() {
        // TODO: Inject a callback. Once receiving resource changed notification, call the method.
    }
}