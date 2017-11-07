package org.snlab.unicorn.model.odl;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import java.util.List;

public class ODLResourceQueryInput {
    private List<ODLQueryDesc> resourceQueryDescs;

    /**
     * @return the resourceQueryDescs
     */
    @JsonGetter("resource-query-desc")
    public List<ODLQueryDesc> getPathQueryDescs() {
        return resourceQueryDescs;
    }

    /**
     * @param resourceQueryDescs the resourceQueryDescs to set
     */
    @JsonSetter("resource-query-desc")
    public void setPathQueryDescs(List<ODLQueryDesc> resourceQueryDescs) {
        this.resourceQueryDescs = resourceQueryDescs;
    }
}