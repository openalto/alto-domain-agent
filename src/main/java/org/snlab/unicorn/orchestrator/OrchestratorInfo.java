package org.snlab.unicorn.orchestrator;

public class OrchestratorInfo {
    protected int id;
    protected String ip;
    protected int port;
    protected String registry;

    public OrchestratorInfo() {
    }

    public OrchestratorInfo(int id, String ip, int port, String registry) {
        this.id = id;
        this.ip = ip;
        this.port = port;
        this.registry = registry;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
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
}
