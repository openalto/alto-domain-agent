package org.snlab.unicorn.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import org.snlab.unicorn.dataprovider.QueryDataProvider;

public class Query {
    private QueryAction action;
    private String queryId;
    private QueryType queryType;
    private Set<QueryItem> queryDesc;

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

    @JsonGetter("query-desc")
    public Set<QueryItem> getQueryDesc() {
        return queryDesc;
    }

    @JsonSetter("query-desc")
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
