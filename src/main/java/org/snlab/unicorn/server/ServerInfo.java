package org.snlab.unicorn.server;

import org.snlab.unicorn.UnicornDefinitions;

import javax.json.*;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ServerInfo {
    private static ServerInfo instance = null;
    protected String domainName;
    protected String controlURL;
    protected String domainIp;
    protected int httpPort;
    protected List<String> hosts;
    protected List<String> ingressPoints;

    protected ServerInfo() {
        this.domainName = "";
        this.controlURL = "";
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

    public String getControlURL() {
        return controlURL;
    }

    public void setControlURL(String controlURL) {
        this.controlURL = controlURL;
    }

    public List<String> getHosts() {
        return hosts;
    }

    public void setHosts(List<String> hosts) {
        this.hosts = hosts;
    }

    public void addHost(String host) {
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
