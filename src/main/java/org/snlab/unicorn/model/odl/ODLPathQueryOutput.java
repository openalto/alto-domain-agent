package org.snlab.unicorn.model.odl;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class ODLPathQueryOutput {
    List<ODLNextIngressPoint> NextIngressPoint;

    /**
     * @return the nextIngressPoint
     */
    @JsonGetter("next-ingress-point")
    public List<ODLNextIngressPoint> getNextIngressPoint() {
        return NextIngressPoint;
    }

    /**
     * @param nextIngressPoint the nextIngressPoint to set
     */
    @JsonSetter("next-ingress-point")
    public void setNextIngressPoint(List<ODLNextIngressPoint> nextIngressPoint) {
        this.NextIngressPoint = nextIngressPoint;
    }
}