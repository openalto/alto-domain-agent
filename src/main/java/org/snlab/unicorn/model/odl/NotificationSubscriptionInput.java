package org.snlab.unicorn.model.odl;

public class NotificationSubscriptionInput {
    public String path;
    public String datastore;
    public String scope;

    public NotificationSubscriptionInput(String path, String datastore, String scope) {
        this.path = path;
        this.datastore = datastore;
        this.scope = scope;
    }
}