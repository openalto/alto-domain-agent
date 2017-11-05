package org.snlab.unicorn.adapter;

import java.util.List;

import org.snlab.unicorn.model.odl.PathQueryResponse;
import org.snlab.unicorn.model.odl.QueryDesc;
import org.snlab.unicorn.model.odl.ResourceQueryResponse;


/**
 * This is a general interface to adapt different SDN os platforms / controllers.
 */
public interface ControllerAdapter {

    /**
     * Implements stateless path query API.
     * @param querySet a list of query descriptor
     * @return the response of path query
     */
    public PathQueryResponse getAsPath(List<QueryDesc> querySet);

    /**
     * Implements stateless resource query API.
     * @param querySet a list of query descriptor
     * @return the response of resource query
     */
    public ResourceQueryResponse getResource(List<QueryDesc> querySet);

    /**
     * Check if as-path changed and then clean the state for stateful path query API.
     */
    public boolean ifAsPathChangedThenCleanState();

    /**
     * Check if resource changed and then clean the state for stateful resource query API.
     */
    public boolean ifResourceChangedThenCleanState();
}