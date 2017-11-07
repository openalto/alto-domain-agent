package org.snlab.unicorn.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "coefficient",
    "flow-id"
})
public class AneFlowCoefficient {

    @JsonProperty("coefficient")
    private Double coefficient;

    /**
     * (Required)
     */
    @JsonProperty("flow-id")
    private Integer flowId;

    @JsonProperty("coefficient")
    public Double getCoefficient() {
        return coefficient;
    }

    @JsonProperty("coefficient")
    public void setCoefficient(Double coefficient) {
        this.coefficient = coefficient;
    }

    @JsonProperty("flow-id")
    public Integer getFlowId() {
        return flowId;
    }

    @JsonProperty("flow-id")
    public void setFlowId(Integer flowId) {
        this.flowId = flowId;
    }

}
