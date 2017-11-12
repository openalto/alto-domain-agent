package org.snlab.unicorn.handlers;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseEventSink;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snlab.unicorn.adapter.ControllerAdapter;
import org.snlab.unicorn.dataprovider.QueryDataProvider;
import org.snlab.unicorn.model.PathQueryResponseBody;
import org.snlab.unicorn.model.Query;
import org.snlab.unicorn.model.QueryItem;
import org.snlab.unicorn.model.ResourceQueryResponseBody;

public class OrchestratorQueryHandler {
    private final static Logger LOG = LoggerFactory.getLogger(OrchestratorQueryHandler.class);

    private static Map<String, OrchestratorQueryHandler> handlerMap = new HashMap<>();
    private static ObjectMapper mapper = new ObjectMapper();
    private String identifier;
    private ControllerAdapter adapter;
    private Set<String> queryIds = new HashSet<>();
    private Thread pathQueryLoop;
    private Thread resourceQueryLoop;
    private Queue<String> requiredQueryIds = new LinkedList<>();

    private OrchestratorQueryHandler(String identifier, ControllerAdapter adapter) {
        this.identifier = identifier;
        this.adapter = adapter;
    }

    private static String callNovaForRSA(String response) {
        Runtime runtime = Runtime.getRuntime();
        String command = "nova '" + response + "'";
        Process process;
        String newResponse = "";
        String line;
        try {
            process = runtime.exec(command);
            process.waitFor();
            BufferedReader bri = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((line = bri.readLine()) != null) {
                newResponse += line;
            }
        } catch (IOException | InterruptedException e) {
            LOG.error("Error occurs when executing nova", e);
            newResponse = response;
        }
        return newResponse;
    }

    public static boolean setHandler(String identifier, ControllerAdapter adapter) {
        boolean result = true;
        if (handlerMap.containsKey(identifier)) {
            LOG.info("Identifier is existing. Cannot put a new handler.");
            result = false;
        } else {
            handlerMap.put(identifier, new OrchestratorQueryHandler(identifier, adapter));
        }
        return result;
    }

    public static OrchestratorQueryHandler getHandler(String identifier) {
        return handlerMap.get(identifier);
    }

    public static Collection<OrchestratorQueryHandler> getAllHandlers() {
        return handlerMap.values();
    }

    private Query parseBody(String body) throws IOException {
        Query query;
        query = mapper.readValue(body, Query.class);
        return query.manipulate();
    }

    public String handle(String body) {
        Query query;
        List<QueryItem> items;
        try {
            query = parseBody(body);
            items = query.getQueryDesc();
        } catch (IOException e) {
            LOG.error("Invalid query body:", e);
            return "{\"meta\": { \"code\": \"Unknown type\"}}";
        }
        queryIds.add(query.getQueryId());
        if (items.size() == 0){
            return "{\"meta\": { \"code\": \"success\"}}";
        }

        requireQuery(query.getQueryId());
        return "{\"meta\": { \"code\": \"success\"}}";
    }

    /**
     * Play a query record with controller adapter by a given query id.
     * @param id the query id.
     * @return the query result from controller adapter.
     */
    public String doQuery(String id) {
        Query query = QueryDataProvider.getInstance().getQuery(id);
        String result = "{\"meta\": { \"code\": \"Unknown error\"}}";
        switch (query.getQueryType()) {
            case PATH_QUERY:
                result = doPathQuery(query, result);
                break;
            case RESOURCE_QUERY:
                result = doResourceQuery(query, result);
        }
        return result;
    }

    private String doPathQuery(Query query, String defaultResult) {
        PathQueryResponseBody body = adapter.getAsPath(query.getQueryDesc());
        body.setQueryId(query.getQueryId());
        try {
            defaultResult = mapper.writeValueAsString(body);
        } catch (JsonProcessingException e) {
            LOG.error("Invalid json string:", e);
        }
        return defaultResult;
    }

    private String doResourceQuery(Query query, String defaultResult) {
        ResourceQueryResponseBody body = adapter.getResource(query.getQueryDesc());
        body.setQueryId(query.getQueryId());
        try {
            defaultResult = mapper.writeValueAsString(body);
        } catch (JsonProcessingException e) {
            LOG.error("Invalid json string:", e);
        }
        return callNovaForRSA(defaultResult);
    }

    private void requireQuery(String id) {
        // TODO: unsafe without lock
        requiredQueryIds.add(id);
        LOG.info("Require a new query to handle. id: {}", id);
    }

    private Set<String> getRequiredQueries(Set<String> queries) {
        // TODO: unsafe without lock
        while (!requiredQueryIds.isEmpty()) {
            String id = requiredQueryIds.poll();
            LOG.info("Fetch a new query from required queue. id: {}", id);
            queries.add(id);
        }
        return queries;
    }

    public void loopForQueryUpdate(SseEventSink eventSink, Sse sse) {
        if (pathQueryLoop == null) {
            pathQueryLoop = new Thread(() -> {
                Set<String> currentQueryIds = new HashSet<>();
                while (true) {
                    currentQueryIds.clear();
                    getRequiredQueries(currentQueryIds);
                    if (adapter.ifAsPathChangedThenCleanState()) {
                        LOG.info("As path data changed. Replay all requests.");
                        currentQueryIds.addAll(queryIds);
                    }
                    if (adapter.ifResourceChangedThenCleanState()) {
                        LOG.info("Resource data changed. Replay all requests.");
                        currentQueryIds.addAll(queryIds);
                    }
                    for (String id : currentQueryIds) {
                        LOG.info("Going to handle a query. id: {}", id);
                        String pathQueryResponse = doQuery(id);
                        eventSink.send(sse.newEventBuilder()
                                .name(MediaType.APPLICATION_JSON)
                                .data(pathQueryResponse)
                                .build());
                    }
                }
            });
            pathQueryLoop.start();
        } else {
            LOG.info("The path query loop has been running. [handler: {}]", identifier);
        }
    }

    public void stop() {
        if (pathQueryLoop != null) {
            pathQueryLoop.interrupt();
            pathQueryLoop = null;
        }
        if (resourceQueryLoop != null) {
            resourceQueryLoop.interrupt();
            resourceQueryLoop = null;
        }
    }
}
