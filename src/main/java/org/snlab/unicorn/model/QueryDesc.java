package org.snlab.unicorn.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class QueryDesc {
    protected List<QueryItem> queryDesc = new ArrayList<>();

    @JsonGetter("query-desc")
    public List<QueryItem> getQueryDesc() {
        return queryDesc;
    }

    @JsonSetter("query-desc")
    public void setQueryDesc(List<QueryItem> queryDesc) {
        this.queryDesc = queryDesc;
    }

}
