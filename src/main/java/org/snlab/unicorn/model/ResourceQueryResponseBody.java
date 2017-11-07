package org.snlab.unicorn.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "query-id",
    "response"
})
public class ResourceQueryResponseBody {

    /**
     * (Required)
     */
    @JsonProperty("query-id")
    private String queryId;

    /**
     * (Required)
     */
    @JsonProperty("response")
    private ResourceQueryResponse response;

    @JsonProperty("query-id")
    public String getQueryId() {
        return queryId;
    }

    @JsonProperty("query-id")
    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    @JsonProperty("response")
    public ResourceQueryResponse getResponse() {
        return response;
    }

    @JsonProperty("response")
    public void setResponse(ResourceQueryResponse response) {
        this.response = response;
    }

}
