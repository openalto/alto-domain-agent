package org.snlab.unicorn.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import org.snlab.unicorn.dataprovider.QueryDataProvider;

public class Query extends QueryDesc {
    protected QueryAction action;
    protected String queryId;
    protected QueryType queryType;

    @JsonGetter("action")
    public QueryAction getAction() {
        return action;
    }

    @JsonSetter("action")
    public void setAction(QueryAction action) {
        this.action = action;
    }

    @JsonGetter("query-id")
    public String getQueryId() {
        return queryId;
    }

    @JsonSetter("query-id")
    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    @JsonGetter("query-type")
    public QueryType getQueryType() {
        return queryType;
    }

    @JsonSetter("query-type")
    public void setQueryType(QueryType queryType) {
        this.queryType = queryType;
    }

    public Query manipulate(){
        //Find query id in query data provider
        List<QueryItem> items;
        Query query;
        if (QueryDataProvider.getInstance().hasQuery(this.queryId)) {
            query = QueryDataProvider.getInstance().getQuery(this.queryId);
            items = query.getQueryDesc();
        }
        else {
            query = new Query();
            query.setQueryId(this.queryId);
            query.setQueryType(this.queryType);
            QueryDataProvider.getInstance().addQuery(this.queryId, query);
            items = query.getQueryDesc();
        }

        if(this.action == QueryAction.ADD || this.action == QueryAction.NEW || this.action == QueryAction.MERGE)
            items.addAll(this.queryDesc);
        else if(this.action == QueryAction.ERASE || this.action == QueryAction.DELETE)
            items.clear();

        return query;
    }

    public enum QueryType {
        @JsonProperty("path-query")
        PATH_QUERY,
        @JsonProperty("resource-query")
        RESOURCE_QUERY
    }

    public enum QueryAction {
        @JsonProperty("add")
        ADD,
        @JsonProperty("delete")
        DELETE,
        @JsonProperty("merge")
        MERGE,
        @JsonProperty("erase")
        ERASE,
        @JsonProperty("new")
        NEW
    }

}
