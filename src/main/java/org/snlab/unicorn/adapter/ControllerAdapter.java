package org.snlab.unicorn.adapter;

import java.util.List;

import org.snlab.unicorn.model.PathQueryResponseBody;
import org.snlab.unicorn.model.QueryItem;
import org.snlab.unicorn.model.ResourceQueryResponseBody;


/**
 * This is a general interface to adapt different SDN os platforms / controllers.
 */
public interface ControllerAdapter {

    /**
     * Implements stateless path query API.
     * @param queryDescs a set of query descriptor
     * @return the response of path query
     */
    public PathQueryResponseBody getAsPath(List<QueryItem> queryDescs);

    /**
     * Implements stateless resource query API.
     * @param queryDescs a set of query descriptor
     * @return the response of resource query
     */
    public ResourceQueryResponseBody getResource(List<QueryItem> queryDescs);

    /**
     * Check if as-path changed and then clean the state for stateful path query API.
     */
    public boolean ifAsPathChangedThenCleanState();

    /**
     * Check if resource changed and then clean the state for stateful resource query API.
     */
    public boolean ifResourceChangedThenCleanState();
}