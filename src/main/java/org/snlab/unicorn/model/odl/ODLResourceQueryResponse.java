package org.snlab.unicorn.model.odl;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class ODLResourceQueryResponse {
    private ODLResourceQueryOutput output;

    /**
     * @return the output
     */
    @JsonGetter("output")
    public ODLResourceQueryOutput getOutput() {
        return output;
    }

    /**
     * @param output the output to set
     */
    @JsonSetter("output")
    public void setOutput(ODLResourceQueryOutput output) {
        this.output = output;
    }
}