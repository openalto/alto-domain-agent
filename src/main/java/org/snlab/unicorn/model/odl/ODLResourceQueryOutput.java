package org.snlab.unicorn.model.odl;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class ODLResourceQueryOutput {
    private List<ODLAne> anes;

    /**
     * @return the anes
     */
    @JsonGetter("anes")
    public List<ODLAne> getAnes() {
        return anes;
    }

    /**
     * @param anes the anes to set
     */
    @JsonSetter("anes")
    public void setAnes(List<ODLAne> anes) {
        this.anes = anes;
    }
}