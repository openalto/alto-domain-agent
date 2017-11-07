package org.snlab.unicorn.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "ane-matrix",
    "anes"
})
public class ResourceQueryResponse {

    /**
     * (Required)
     */
    @JsonProperty("ane-matrix")
    private List<List<AneMatrix>> aneMatrix = new ArrayList<List<AneMatrix>>();

    /**
     * (Required)
     */
    @JsonProperty("anes")
    private List<Ane> anes = new ArrayList<Ane>();

    @JsonProperty("ane-matrix")
    public List<List<AneMatrix>> getAneMatrix() {
        return aneMatrix;
    }

    @JsonProperty("ane-matrix")
    public void setAneMatrix(List<List<AneMatrix>> aneMatrix) {
        this.aneMatrix = aneMatrix;
    }

    @JsonProperty("anes")
    public List<Ane> getAnes() {
        return anes;
    }

    @JsonProperty("anes")
    public void setAnes(List<Ane> anes) {
        this.anes = anes;
    }

}
