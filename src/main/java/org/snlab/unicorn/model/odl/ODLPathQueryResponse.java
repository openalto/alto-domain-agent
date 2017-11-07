package org.snlab.unicorn.model.odl;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class ODLPathQueryResponse {
    private ODLPathQueryOutput output;

    /**
     * @return the output
     */
    @JsonGetter("output")
    public ODLPathQueryOutput getOutput() {
        return output;
    }

    /**
     * @param output the output to set
     */
    @JsonSetter("output")
    public void setOutput(ODLPathQueryOutput output) {
        this.output = output;
    }
}