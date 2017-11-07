package org.snlab.unicorn.model.odl;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class ODLAne {
    private List<ODLAneFlowCoefficient> aneFlowCoefficients;
    private Long availbw;

    /**
     * @return the aneneFlowCoefficients
     */
    @JsonGetter("ane-flow-coefficient")
    public List<ODLAneFlowCoefficient> getAneneFlowCoefficients() {
        return aneFlowCoefficients;
    }

    /**
     * @param aneneFlowCoefficients the aneneFlowCoefficients to set
     */
    @JsonSetter("ane-flow-coefficient")
    public void setAneneFlowCoefficients(List<ODLAneFlowCoefficient> aneneFlowCoefficients) {
        this.aneFlowCoefficients = aneneFlowCoefficients;
    }

    /**
     * @return the availbw
     */
    @JsonGetter("availbw")
    public Long getAvailbw() {
        return availbw;
    }

    /**
     * @param availbw the availbw to set
     */
    @JsonSetter("availbw")
    public void setAvailbw(Long availbw) {
        this.availbw = availbw;
    }
}