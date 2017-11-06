package org.snlab.unicorn.dataprovider;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.snlab.unicorn.model.Query;
import org.snlab.unicorn.model.QueryItem;

public class QueryDataProvider {
    private static QueryDataProvider instance = null;
    private Map<String, Query> queryMap;
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

    public Query getQuery(String id) {
        return this.queryMap.get(id);
    }

    public void addQuery(String id, Query query) {
        lock.lock();
        try {
            this.queryMap.put(id, query);
        } finally {
            lock.unlock();
        }
    }

    public Set<QueryItem> getItems(String id) {
        return this.queryMap.get(id).getQueryDesc();
    }

    public void addQueryItem(String id, QueryItem item) {
        lock.lock();
        try {
            if (!this.queryMap.containsKey(id))
                this.queryMap.put(id, new Query());
            this.queryMap.get(id).getQueryDesc().add(item);
        } finally {
            lock.unlock();
        }
    }

    public void addQueryItems(String id, Collection<QueryItem> items) {
        lock.lock();
        try {
            if (!this.queryMap.containsKey(id))
                this.queryMap.put(id, new Query());
            this.queryMap.get(id).getQueryDesc().addAll(items);
        } finally {
            lock.unlock();
        }
    }
}
