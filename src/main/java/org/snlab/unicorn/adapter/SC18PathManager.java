package org.snlab.unicorn.adapter;

import org.snlab.unicorn.model.Flow;
import org.snlab.unicorn.model.PathItem;
import org.snlab.unicorn.server.ServerInfo;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.InputStream;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class SC18PathManager {
    private Queue<PathItem> pathItems;

    public SC18PathManager(String adapterConfigPath) {
        pathItems = new PriorityQueue<>();
        configReader(adapterConfigPath);
    }

    private void configReader(String adapterConfigPath) {
        InputStream stream = SC18Adapter.class.getClassLoader().getResourceAsStream(adapterConfigPath);
        String domainName = ServerInfo.getInstance().getDomainName();
        JsonObject object = Json.createReader(stream).readObject().getJsonObject(domainName);
        JsonArray routes = object.getJsonArray("route");
        for (int i = 0; i < routes.size(); i++){
            JsonObject route = routes.getJsonObject(i);
            PathItem item = new PathItem(
                    route.getInt("id"),
                    route.getInt("priority"),
                    route.getJsonObject("flow-spec").getString("src-ip"),
                    route.getJsonObject("flow-spec").getString("dst-ip")
            );
            JsonArray links = route.getJsonArray("links");
            for (int j = 0; j < links.size(); j++) {
                item.addHop(links.getJsonObject(j).getString("link"));
            }
            pathItems.add(item);
        }
    }

    public PathItem match(String src_ip, String dst_ip) {
        for(PathItem item: this.pathItems) {
            if (item.match(src_ip, dst_ip)) {
                return item;
            }
        }
        return null;
    }

    public PathItem match(Flow flow) {
        String src_ip = flow.getSrcIp();
        String dst_ip = flow.getDstIp();
        return match(src_ip, dst_ip);
    }
}
