package org.snlab.unicorn.model.odl;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class ODLResourceQueryRequest {
    private ODLResourceQueryInput input;

    /**
     * @return the input
     */
    @JsonGetter("input")
    public ODLResourceQueryInput getInput() {
        return input;
    }

    /**
     * @param input the input to set
     */
    @JsonSetter("input")
    public void setInput(ODLResourceQueryInput input) {
        this.input = input;
    }
}