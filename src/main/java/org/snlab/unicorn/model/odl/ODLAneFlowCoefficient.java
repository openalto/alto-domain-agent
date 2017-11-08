package org.snlab.unicorn.model.odl;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class ODLAneFlowCoefficient {
    private Integer flowId;
    private Double coefficient;

    /**
     * @return the flowId
     */
    @JsonGetter("flow-id")
    public Integer getFlowId() {
        return flowId;
    }

    /**
     * @param flowId the flowId to set
     */
    @JsonSetter("flow-id")
    public void setFlowId(Integer flowId) {
        this.flowId = flowId;
    }

    /**
     * @return the coefficient
     */
    @JsonGetter("coefficient")
    public Double getCoefficient() {
        return coefficient;
    }

    /**
     * @param coefficient the coefficient to set
     */
    @JsonSetter("coefficient")
    public void setCoefficient(Double coefficient) {
        this.coefficient = coefficient;
    }
}