package org.snlab.unicorn.model;

import org.snlab.unicorn.dataprovider.QueryDataProvider;

import java.util.HashSet;
import java.util.Set;

public class Query {
    private QueryAction action;
    private String queryId;
    private QueryType queryType;
    private Set<QueryItem> queryDesc;

    public QueryAction getAction() {
        return action;
    }

    public void setAction(QueryAction action) {
        this.action = action;
    }

    public String getQueryId() {
        return queryId;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    public QueryType getQueryType() {
        return queryType;
    }

    public void setQueryType(QueryType queryType) {
        this.queryType = queryType;
    }

    public Set<QueryItem> getQueryDesc() {
        return queryDesc;
    }

    public void setQueryDesc(Set<QueryItem> queryDesc) {
        this.queryDesc = queryDesc;
    }

    public Set<QueryItem> manipulate(){
        //Find query id in query data provider
        Set<QueryItem> items;
        if(QueryDataProvider.getInstance().hasQuery(this.queryId))
            items = QueryDataProvider.getInstance().getItems(this.queryId);
        else
            items = new HashSet<>();

        if(this.action == QueryAction.ADD || this.action == QueryAction.NEW || this.action == QueryAction.MERGE)
            items.addAll(this.queryDesc);
        else if(this.action == QueryAction.ERASE || this.action == QueryAction.DELETE)
            items.clear();

        return items;
    }

    public enum QueryType {
        PATH_QUERY,
        RESOURCE_QUERY
    }

    public enum QueryAction {
        ADD,
        DELETE,
        MERGE,
        ERASE,
        NEW
    }

    public enum Protocol {
        TCP,
        UDP,
        SCTP
    }
}
