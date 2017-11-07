package org.snlab.unicorn.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class QueryItem {
    private Flow flow;
    private String ingressPoint;

    @JsonGetter("flow")
    public Flow getFlow() {
        return flow;
    }

    @JsonSetter("flow")
    public void setFlow(Flow flow) {
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
