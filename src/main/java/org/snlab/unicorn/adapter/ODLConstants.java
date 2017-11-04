package org.snlab.unicorn.adapter;

public class ODLConstants {

    public final static String SAL_REMOTE_SUBSCRIPTION = "/restconf/operations/sal-remote:create-data-change-event-subscription";
    public final static String RESTCONF_STREAM = "/restconf/streams/stream/";
    public final static String DEFAULT_NOTIFICATION_SUBSCRIPTION = "{\"input\":{\"path\":\"/opendaylight-inventory:nodes\",\"datastore\":\"OPERATIONAL\",\"scope\":\"SUBTREE\"}}";
    public final static String UNICORN_PATH_MANAGER_SUBSCRIPTION = "{\"input\":{\"path\":\"/alto-pathmanager:path-manager\",\"datastore\":\"OPERATIONAL\",\"scope\":\"SUBTREE\"}}";
    public final static String UNICORN_BANDWIDTH_MONITOR_SUBSCRIPTION = "{\"input\":{\"path\":\"/alto-bwmonitor:speeds\",\"datastore\":\"OPERATIONAL\",\"scope\":\"SUBTREE\"}}";
}