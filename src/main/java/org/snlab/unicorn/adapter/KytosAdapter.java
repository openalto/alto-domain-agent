package org.snlab.unicorn.adapter;

import java.util.List;

import org.snlab.unicorn.model.Endpoints;
import org.snlab.unicorn.model.PathQueryResponseBody;
import org.snlab.unicorn.model.QueryItem;
import org.snlab.unicorn.model.ResourceQueryResponseBody;

public class KytosAdapter implements ControllerAdapter {

    @Override
    public PathQueryResponseBody getAsPath(List<QueryItem> queryDescs) {
        // TODO: Implement stateless unicorn path query API.
        return null;
    }

    @Override
    public ResourceQueryResponseBody getResource(List<QueryItem> queryDescs) {
        // TODO: Implement stateless unicorn resource query API.
        return null;
    }

    @Override
    public String deployRoute(Endpoints endpoints) {
        // TODO: Implement stateless path setup API.
        return "{\"meta\":{\"code\": \"fail\", \"message\": \"Method deployRoute is not implemented.\"} }";
	}

    @Override
    public boolean ifAsPathChangedThenCleanState() {
        // TODO: Inject a callback. Once receiving as path changed notification, call the method.
        return false;
    }

    @Override
    public boolean ifResourceChangedThenCleanState() {
        // TODO: Inject a callback. Once receiving resource changed notification, call the method.
        return false;
    }

    @Override
    public String deployOnDemandRoute(String demand) {
        // Implements demand-based path setup API.
        return "{\"meta\":{\"code\": \"fail\", \"message\": \"Method deployOnDemandRoute is not implemented.\"} }";
    }
}