package org.snlab.unicorn.model.odl;

public class ODLNextIngressPoint {
    private Integer flowId;
    private String ingressPoint;

    /**
     * @return the flowId
     */
    public Integer getFlowId() {
        return flowId;
    }

    /**
     * @param flowId the flowId to set
     */
    public void setFlowId(Integer flowId) {
        this.flowId = flowId;
    }

    /**
     * @return the ingressPoint
     */
    public String getIngressPoint() {
        return ingressPoint;
    }

    /**
     * @param ingressPoint the ingressPoint to set
     */
    public void setIngressPoint(String ingressPoint) {
        this.ingressPoint = ingressPoint;
    }
}