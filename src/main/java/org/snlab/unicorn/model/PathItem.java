package org.snlab.unicorn.model;

import java.util.ArrayList;
import java.util.List;

public class PathItem implements Comparable {
    private int id;
    private int priority;
    private String src_ip;
    private String dst_ip;
    private List<String> links;

    public PathItem(int id,
                    int priority,
                    String src_ip,
                    String dst_ip){
        this.id = id;
        this.priority = priority;
        this.src_ip = src_ip;
        this.dst_ip = dst_ip;

        links = new ArrayList<>();
    }

    public void addHop(String hop) {
        links.add(hop);
    }

    public boolean match(String src_ip, String dst_ip) {
        return this.src_ip.startsWith(src_ip) && this.dst_ip.startsWith(dst_ip);
    }

    public List<String> getLinks(){
        return this.links;
    }

    public int getId(){
        return this.id;
    }

    @Override
    public int compareTo(Object o) {
        return this.priority - ((PathItem)o).priority;
    }
}
