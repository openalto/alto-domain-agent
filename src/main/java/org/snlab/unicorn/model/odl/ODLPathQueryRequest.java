package org.snlab.unicorn.model.odl;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class ODLPathQueryRequest {
    private ODLPathQueryInput input;

    /**
     * @return the input
     */
    @JsonGetter("input")
    public ODLPathQueryInput getInput() {
        return input;
    }

    /**
     * @param input the input to set
     */
    @JsonSetter("input")
    public void setInput(ODLPathQueryInput input) {
        this.input = input;
    }
}