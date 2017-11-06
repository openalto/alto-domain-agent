package org.snlab.unicorn.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "availbw"
})
public class Ane {

    /**
     * (Required)
     */
    @JsonProperty("availbw")
    private Long availbw;

    @JsonProperty("availbw")
    public Long getAvailbw() {
        return availbw;
    }

    @JsonProperty("availbw")
    public void setAvailbw(Long availbw) {
        this.availbw = availbw;
    }

}
