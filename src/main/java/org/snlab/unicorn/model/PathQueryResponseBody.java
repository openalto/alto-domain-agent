package org.snlab.unicorn.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "query-id",
    "response"
})
public class PathQueryResponseBody {

    /**
     * (Required)
     */
    @JsonProperty("query-id")
    private String queryId;

    /**
     * (Required)
     */
    @JsonProperty("response")
    private List<String> response = new ArrayList<String>();

    @JsonProperty("query-id")
    public String getQueryId() {
        return queryId;
    }

    @JsonProperty("query-id")
    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    @JsonProperty("response")
    public List<String> getResponse() {
        return response;
    }

    @JsonProperty("response")
    public void setResponse(List<String> response) {
        this.response = response;
    }

}
