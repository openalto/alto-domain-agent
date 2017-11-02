package org.snlab.unicorn.server;

import javax.json.*;
import java.io.InputStream;

public class ServerInfoReader {
    public static void read(String serverInfoPath) {
        InputStream stream = ServerInfoReader.class.getClassLoader().getResourceAsStream(serverInfoPath);
        ServerInfo info = ServerInfo.getInstance();
        JsonReader rdr = Json.createReader(stream);
        JsonObject obj = rdr.readObject();
        info.setDomainName(obj.getString("domain-name"));
        info.setUpdateURL(obj.getString("control-url"));
        info.setDomainIp(obj.getString("domain-ip"));
        info.setHttpPort(obj.getInt("http-port"));

        JsonArray hosts = obj.getJsonArray("hosts");
        for (JsonValue host : hosts) {
            info.addHost(host.toString());
        }

        JsonArray ingressPoints = obj.getJsonArray("ingress-points");
        for (JsonValue ingressPoint : ingressPoints) {
            info.addIngressPoint(ingressPoint.toString());
        }
    }
}
