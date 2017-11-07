package org.snlab.unicorn.adapter;

import java.util.Set;

import org.snlab.unicorn.model.PathQueryResponseBody;
import org.snlab.unicorn.model.QueryItem;
import org.snlab.unicorn.model.ResourceQueryResponseBody;


/**
 * This is a general interface to adapt different SDN os platforms / controllers.
 */
public interface ControllerAdapter {

    /**
     * Implements stateless path query API.
     * @param querySet a set of query descriptor
     * @return the response of path query
     */
    public PathQueryResponseBody getAsPath(Set<QueryItem> querySet);

    /**
     * Implements stateless resource query API.
     * @param querySet a set of query descriptor
     * @return the response of resource query
     */
    public ResourceQueryResponseBody getResource(Set<QueryItem> querySet);

    /**
     * Check if as-path changed and then clean the state for stateful path query API.
     */
    public boolean ifAsPathChangedThenCleanState();

    /**
     * Check if resource changed and then clean the state for stateful resource query API.
     */
    public boolean ifResourceChangedThenCleanState();
}