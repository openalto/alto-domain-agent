package org.snlab.unicorn.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class Endpoints {
    private String src;
    private String dst;

    @JsonGetter
    public String getSrc() {
        return this.src;
    }

    @JsonSetter
    /**
     * @param src the src to set
     */
    public void setSrc(String src) {
        this.src = src;
    }

    @JsonGetter
    public String getDst() {
        return this.dst;
    }

    @JsonSetter
    /**
     * @param dst the dst to set
     */
    public void setDst(String dst) {
        this.dst = dst;
    }
}
