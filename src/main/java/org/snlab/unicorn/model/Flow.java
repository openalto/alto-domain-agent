package org.snlab.unicorn.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

public class Flow {
    private String flowId = null;
    private String srcIp = null;
    private String dstIp = null;
    private String dstPort = null;
    private Protocol protocol = null;

    @JsonGetter("protocol")
    public Protocol getProtocol() {
        return protocol;
    }

    @JsonSetter("protocol")
    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    @JsonGetter("flow-id")
    public String getFlowId() {
        return flowId;
    }

    @JsonSetter("flow-id")
    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    @JsonGetter("src-ip")
    public String getSrcIp() {
        return srcIp;
    }

    @JsonSetter("src-ip")
    public void setSrcIp(String srcIp) {
        this.srcIp = srcIp;
    }

    @JsonGetter("dst-ip")
    public String getDstIp() {
        return dstIp;
    }

    @JsonSetter("dst-ip")
    public void setDstIp(String dstIp) {
        this.dstIp = dstIp;
    }

    @JsonGetter("dst-port")
    public String getDstPort() {
        return dstPort;
    }

    @JsonSetter("dst-port")
    public void setDstPort(String dstPort) {
        this.dstPort = dstPort;
    }

    public enum Protocol {
        @JsonProperty("tcp")
        TCP,
        @JsonProperty("udp")
        UDP,
        @JsonProperty("sctp")
        SCTP
    }
}
