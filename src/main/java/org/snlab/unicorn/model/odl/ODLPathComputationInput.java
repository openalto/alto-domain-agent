package org.snlab.unicorn.model.odl;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import org.snlab.unicorn.model.Endpoints;

public class ODLPathComputationInput {
    private Endpoints endpoints;
    private FlowLayer flowLayer = FlowLayer.L3;
    private List<ObjectiveMetric> objectiveMetrics = Arrays.asList(ObjectiveMetric.HOPCOUNT);

    @JsonGetter("endpoints")
    public Endpoints getEndpoints() {
        return endpoints;
    }

    @JsonSetter("endpoints")
    public void setEndpoints(Endpoints endpoints) {
        this.endpoints = endpoints;
    }

    @JsonGetter("flow-layer")
    public FlowLayer getFlowLayer() {
        return flowLayer;
    }

    @JsonSetter("flow-layer")
    public void setFlowLayer(FlowLayer flowLayer) {
        this.flowLayer = flowLayer;
    }

    @JsonGetter("objective-metrics")
    public List<ObjectiveMetric> getObjectiveMetrics() {
        return objectiveMetrics;
    }

    @JsonSetter("objective-metrics")
    public void setObjectiveMetrics(List<ObjectiveMetric> objectiveMetrics) {
        this.objectiveMetrics = objectiveMetrics;
    }

    public enum FlowLayer {
        @JsonProperty("L2")
        L2,
        @JsonProperty("L3")
        L3
    }

    public enum ObjectiveMetric {
        @JsonProperty("hopcount")
        HOPCOUNT,
        @JsonProperty("bandwidth")
        BANDWIDTH
    }
}
