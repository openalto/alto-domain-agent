package org.snlab.unicorn.adapter;

import java.util.Set;

import org.snlab.unicorn.model.PathQueryResponseBody;
import org.snlab.unicorn.model.QueryItem;
import org.snlab.unicorn.model.ResourceQueryResponseBody;

public class KytosAdapter implements ControllerAdapter {

    @Override
    public PathQueryResponseBody getAsPath(Set<QueryItem> querySet) {
        // TODO: Implement stateless unicorn path query API.
        return null;
    }

    @Override
    public ResourceQueryResponseBody getResource(Set<QueryItem> querySet) {
        // TODO: Implement stateless unicorn resource query API.
        return null;
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
    public void requirePathQuery() {
        // TODO: Send an event to adapter to make it do a path query.
    }

    @Override
    public void requireResourceQuery() {
        // TODO: Send an event to adapter to make it do a resource query.
    }
}