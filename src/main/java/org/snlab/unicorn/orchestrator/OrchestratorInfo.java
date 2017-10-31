package org.snlab.unicorn.orchestrator;

import com.sun.org.apache.xpath.internal.operations.Or;

public class OrchestratorInfo {
    public OrchestratorInfo() { }

    public OrchestratorInfo(int id, String ip, String registry){
        this.id = id;
        this.ip = ip;
        this.registry = registry;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getRegistry() {
        return registry;
    }

    public void setRegistry(String registry) {
        this.registry = registry;
    }

    protected int id;
    protected String ip;
    protected String registry;
}
