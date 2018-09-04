package org.snlab.unicorn.model.odl;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class ODLPathComputationRequest {
    private ODLPathComputationInput input;

    @JsonGetter("input")
    public ODLPathComputationInput getInput() {
        return input;
    }

    @JsonSetter("input")
    public void setInput(ODLPathComputationInput input) {
        this.input = input;
    }
}