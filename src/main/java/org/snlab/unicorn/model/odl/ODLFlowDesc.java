package org.snlab.unicorn.model.odl;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

import org.snlab.unicorn.model.Flow.Protocol;

public class ODLFlowDesc {

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
}