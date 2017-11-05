package org.snlab.unicorn.handlers;


import java.io.StringReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snlab.unicorn.adapter.ControllerAdapter;
import org.snlab.unicorn.exceptions.UnknownProtocol;
import org.snlab.unicorn.exceptions.UnknownQueryAction;
import org.snlab.unicorn.exceptions.UnknownQueryType;
import org.snlab.unicorn.model.Flow;
import org.snlab.unicorn.model.Query;
import org.snlab.unicorn.model.QueryItem;

public class OrchestratorQueryHandler {
    private final static Logger LOG = LoggerFactory.getLogger(OrchestratorQueryHandler.class);

    private static Map<String, OrchestratorQueryHandler> handlerMap = new HashMap<>();
    private String identifier;
    private ControllerAdapter adapter;

    private OrchestratorQueryHandler(String identifier, ControllerAdapter adapter) {
        this.identifier = identifier;
        this.adapter = adapter;
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

    private Set<QueryItem> parseBody(String body) throws UnknownQueryAction, UnknownQueryType, UnknownProtocol {
        Set<QueryItem> items;
        JsonReader jsonReader = Json.createReader(new StringReader(body));
        JsonObject object = jsonReader.readObject();
        Query query = new Query();

        // Get query id
        String queryId = object.getString("query-id");

        // Get query action
        String actionString = object.getString("action");
        Query.QueryAction action;
        switch (actionString) {
            case "add":
                action = Query.QueryAction.ADD;
                break;
            case "delete":
                action = Query.QueryAction.DELETE;
                break;
            case "merge":
                action = Query.QueryAction.MERGE;
                break;
            case "erase":
                action = Query.QueryAction.ERASE;
                break;
            case "new":
                action = Query.QueryAction.NEW;
                break;
            default:
                throw new UnknownQueryAction();
        }

        // Get query type
        String queryTypeString = object.getString("query-type");
        Query.QueryType queryType;
        switch (queryTypeString) {
            case "path-query":
                queryType = Query.QueryType.PATH_QUERY;
                break;
            case "resource-query":
                queryType = Query.QueryType.RESOURCE_QUERY;
                break;
            default:
                throw new UnknownQueryType();
        }

        // Get query items
        Set<QueryItem> queryItems = new HashSet<>();
        JsonArray queryDesc = object.getJsonArray("query-desc");
        for (JsonObject queryItem : queryDesc.getValuesAs(JsonObject.class)) {
            QueryItem item = new QueryItem();
            Flow flow = new Flow();
            flow.setFlowId(queryItem.getString("flow-id"));
            if (queryItem.containsKey("src-ip"))
                flow.setSrcIp(queryItem.getString("src-ip"));
            if (queryItem.containsKey("dst-ip"))
                flow.setDstIp(queryItem.getString("dst-ip"));
            if (queryItem.containsKey("dst-port"))
                flow.setDstPort(queryItem.getString("dst-port"));
            String protocol = queryItem.getString("protocol");
            switch (protocol) {
                case "tcp":
                    flow.setProtocol(Flow.Protocol.TCP);
                    break;
                case "udp":
                    flow.setProtocol(Flow.Protocol.UDP);
                    break;
                case "sctp":
                    flow.setProtocol(Flow.Protocol.SCTP);
                    break;
                default:
                    throw new UnknownProtocol();
            }
            item.setFlow(flow);
            item.setIngressPoint(queryItem.getString("ingress-point"));
            queryItems.add(item);
        }

        // Set all fileds
        query.setQueryId(queryId);
        query.setQueryDesc(queryItems);
        query.setAction(action);
        query.setQueryType(queryType);

        return query.manipulate();
    }

    public String handle(String body) {
        Set<QueryItem> items;
        try {
            items = parseBody(body);
        } catch (UnknownQueryAction | UnknownProtocol | UnknownQueryType exception) {
            exception.printStackTrace();
            return "{\"meta\": { \"code\": \"Unknown type\"}}";
        }
        if (items.size() == 0){
            return "{\"meta\": { \"code\": \"success\"}}";
        }

        //TODO: Handle QueryItems
        return "{\"meta\": { \"code\": \"success\"}}";
    }
}
