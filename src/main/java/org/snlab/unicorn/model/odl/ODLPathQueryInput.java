package org.snlab.unicorn.model.odl;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class ODLPathQueryInput {
    private List<ODLQueryDesc> pathQueryDescs;

    /**
     * @return the pathQueryDescs
     */
    @JsonGetter("path-query-desc")
    public List<ODLQueryDesc> getPathQueryDescs() {
        return pathQueryDescs;
    }

    /**
     * @param pathQueryDescs the pathQueryDescs to set
     */
    @JsonSetter("path-query-desc")
    public void setPathQueryDescs(List<ODLQueryDesc> pathQueryDescs) {
        this.pathQueryDescs = pathQueryDescs;
    }
}