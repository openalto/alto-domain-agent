package org.snlab.unicorn.model.odl;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class ODLQueryDesc {
    private Integer flowId;
    private ODLFlowDesc flow;
    private String ingressPoint;

    @JsonGetter("flow-id")
    public Integer getFlowId() {
        return flowId;
    }

    @JsonSetter("flow-id")
    public void setFlowId(Integer flowId) {
        this.flowId = flowId;
    }

    @JsonGetter("flow")
    public ODLFlowDesc getFlow() {
        return flow;
    }

    @JsonSetter("flow")
    public void setFlow(ODLFlowDesc flow) {
        this.flow = flow;
    }

    @JsonGetter("ingress-point")
    public String getIngressPoint() {
        return ingressPoint;
    }

    @JsonSetter("ingress-point")
    public void setIngressPoint(String ingressPoint) {
        this.ingressPoint = ingressPoint;
    }
}