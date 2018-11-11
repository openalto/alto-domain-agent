package org.snlab.unicorn.adapter;

import org.snlab.unicorn.model.PathItem;
import org.snlab.unicorn.model.PathVectorItem;

import java.util.*;

public class SC18PathVectorReader {

    private Map<String, Long> bandwidthMap;
    private List<PathItem> paths;

    public SC18PathVectorReader(Map<String, Long> bandwidthMap){
        this.bandwidthMap = bandwidthMap;
        this.paths = new ArrayList<>();
    }

    public SC18PathVectorReader(Map<String, Long> bandwidthMap, List<PathItem> paths) {
        this.bandwidthMap = bandwidthMap;
        this.paths = paths;
    }

    public void addPath(PathItem path){
        paths.add(path);
    }

    public List<PathVectorItem> getResult() {
        Map<String, Set<Integer>> portFlowMap = new HashMap<>();
        int pathIndex = 0;
        for(PathItem path: paths) {
            for (String port: path.getLinks()) {
                if (port.contains("/")) {
                    String[] ports = port.split("/");
                    for (String port_id: ports) {
                        if (port_id.startsWith("openflow")){
                            port=port_id;
                            break;
                        }
                    }
                }
                if (bandwidthMap.containsKey(port)) {
                    if (!portFlowMap.containsKey(port))
                        portFlowMap.put(port, new HashSet<>());
                    portFlowMap.get(port).add(pathIndex);
                }
            }
            pathIndex += 1;
        }
        List<PathVectorItem> pvItems = new LinkedList<>();
        for(String port: portFlowMap.keySet()){
            boolean matched = false;
            for(PathVectorItem pvItem: pvItems) {
                if(pvItem.match(portFlowMap.get(port))) {
                    pvItem.updateY(Math.min(pvItem.getY(), bandwidthMap.get(port)));
                    matched = true;
                }
            }
            if (!matched){
                pvItems.add(new PathVectorItem(portFlowMap.get(port), bandwidthMap.get(port)));
            }
        }
        return pvItems;
    }
}
