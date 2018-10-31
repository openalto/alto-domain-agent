package org.snlab.unicorn.model;

import java.util.Collection;
import java.util.Set;
import java.util.Collections;

public class PathVectorItem {
    private Set<Integer> xs;
    private Long y;

    public PathVectorItem(Set<Integer> x, Long y){
        this.xs = x;
        this.y = y;
    }

    public Set<Integer> getXs(){
        return this.xs;
    }

    public Long getY(){
        return this.y;
    }

    public Long updateY(Long y){
        this.y = y;
        return this.y;
    }

    public boolean match(Collection<Integer> xs) {
        for (Integer x: xs) {
            if (! this.xs.contains(x))
                return false;
        }
        for (Integer x: this.xs) {
            if (! xs.contains(x))
                return false;
        }
        return true;
    }
}
