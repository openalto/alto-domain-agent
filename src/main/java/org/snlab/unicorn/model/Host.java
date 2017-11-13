package org.snlab.unicorn.model;

public class Host {
    private String hostIp;
    private String managementIp;

    public Host(String hostIp, String managementIp) {
        this.hostIp = hostIp;
        this.managementIp = managementIp;
    }

    public String getHostIp() {
        return hostIp;
    }

    public String getManagementIp() {
        return managementIp;
    }
}
