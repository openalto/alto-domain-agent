package org.snlab.unicorn.server;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonValue;
import java.io.InputStream;

public class ServerInfoReader {
    public static void read(String serverInfoPath) {
        InputStream stream = ServerInfoReader.class.getClassLoader().getResourceAsStream(serverInfoPath);
        ServerInfo info = ServerInfo.getInstance();
        JsonReader rdr = Json.createReader(stream);
        JsonObject obj = rdr.readObject();
        info.setDomainName(obj.getString("domain-name"));
        info.setUpdateURL(obj.getString("control-url"));
        info.setDeployURL(obj.getString("deploy-url"));
        info.setDomainIp(obj.getString("domain-ip"));
        info.setHttpPort(obj.getInt("http-port"));

        JsonArray hosts = obj.getJsonArray("hosts");
        for (JsonValue host : hosts) {
            info.addHost(((JsonString) host).getString());
        }

        JsonArray ingressPoints = obj.getJsonArray("ingress-points");
        for (JsonValue ingressPoint : ingressPoints) {
            info.addIngressPoint(((JsonString) ingressPoint).getString());
        }
    }
}
