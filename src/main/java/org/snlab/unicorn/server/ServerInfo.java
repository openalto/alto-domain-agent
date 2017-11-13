package org.snlab.unicorn.server;

import org.snlab.unicorn.UnicornDefinitions;
import org.snlab.unicorn.model.Host;

import java.util.ArrayList;
import java.util.List;

public class ServerInfo {
    private static ServerInfo instance = null;
    protected String domainName;
    protected String updateURL;
    protected String deployURL;
    protected String domainIp;
    protected int httpPort;
    protected List<Host> hosts;
    protected List<String> ingressPoints;

    protected ServerInfo() {
        this.domainName = "";
        this.updateURL = "";
        this.deployURL = "";
        this.domainIp = "";
        this.httpPort = 0;
        this.hosts = new ArrayList<>();
        this.ingressPoints = new ArrayList<>();
    }

    public static ServerInfo getInstance() {
        if (instance == null) {
            instance = new ServerInfo();
            ServerInfoReader.read(UnicornDefinitions.ServerConfig.CONFIG_PATH);
        }
        return instance;
    }

    public String getDeployURL() {
        return deployURL;
    }

    public void setDeployURL(String deployURL) {
        this.deployURL = deployURL;
    }

    public String getDomainIp() {
        return domainIp;
    }

    public void setDomainIp(String domainIp) {
        this.domainIp = domainIp;
    }

    public int getHttpPort() {
        return httpPort;
    }

    public void setHttpPort(int httpPort) {
        this.httpPort = httpPort;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getUpdateURL() {
        return updateURL;
    }

    public void setUpdateURL(String updateURL) {
        this.updateURL = updateURL;
    }

    public List<Host> getHosts() {
        return hosts;
    }

    public void setHosts(List<Host> hosts) {
        this.hosts = hosts;
    }

    public List<String> getHostIPs() {
        List<String> hostIps = new ArrayList<>();
        for (Host host : this.hosts) {
            hostIps.add(host.getHostIp());
        }
        return hostIps;
    }

    public void addHost(Host host) {
        this.hosts.add(host);
    }

    public List<String> getIngressPoints() {
        return ingressPoints;
    }

    public void setIngressPoints(List<String> ingressPoints) {
        this.ingressPoints = ingressPoints;
    }

    public void addIngressPoint(String ingressPoint) {
        this.ingressPoints.add(ingressPoint);
    }
}
