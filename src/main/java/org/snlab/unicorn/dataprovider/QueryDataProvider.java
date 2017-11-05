package org.snlab.unicorn.dataprovider;

import org.snlab.unicorn.model.QueryItem;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class QueryDataProvider {
    private static QueryDataProvider instance = null;
    private Map<String, Set<QueryItem>> queryMap;
    private Lock lock = new ReentrantLock();

    protected QueryDataProvider() {
        queryMap = new HashMap<>();
    }

    public static QueryDataProvider getInstance() {
        if (instance == null) {
            instance = new QueryDataProvider();
        }
        return instance;
    }

    public boolean hasQuery(String id) {
        return this.queryMap.containsKey(id);
    }

    public Set<QueryItem> getItems(String id) {
        return this.queryMap.get(id);
    }

    public void addQueryItem(String id, QueryItem item) {
        lock.lock();
        try {
            if (!this.queryMap.containsKey(id))
                this.queryMap.put(id, new HashSet<>());
            this.queryMap.get(id).add(item);
        } finally {
            lock.unlock();
        }
    }

    public void addQueryItems(String id, Collection<QueryItem> items) {
        lock.lock();
        try {
            if (!this.queryMap.containsKey(id))
                this.queryMap.put(id, new HashSet<>());
            this.queryMap.get(id).addAll(items);
        } finally {
            lock.unlock();
        }
    }
}
