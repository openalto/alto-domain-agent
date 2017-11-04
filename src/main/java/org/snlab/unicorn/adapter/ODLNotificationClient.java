package org.snlab.unicorn.adapter;

import java.io.IOException;
import java.net.URI;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.auth.Credentials;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snlab.unicorn.model.odl.NotificationSubscriptionInput;
import org.snlab.unicorn.model.odl.NotificationSubscriptionRequest;
import org.snlab.unicorn.model.odl.SalRemoteResponse;
import org.snlab.unicorn.model.odl.StreamLocation;

public class ODLNotificationClient {

    private final static Logger LOG = LoggerFactory.getLogger(ODLNotificationClient.class);

    private static ObjectMapper mapper = new ObjectMapper();
    private URI baseUri;
    private Credentials auth;
    private ODLNotificationSocket socket;
    private WebSocketClient client;

    public ODLNotificationClient(URI baseUri, Credentials auth) {
        this.baseUri = baseUri;
        this.auth = auth;
    }

    public ODLNotificationSocket connect(String path, String datastore, String scope) {
        return connect(getNotificationSubscriptionRequestJson(path, datastore, scope));
    }

    public ODLNotificationSocket connect(String data) {
        if (socket != null) {
            LOG.warn("Socket is existing. Cannot connect again.");
            return socket;
        }
        client = new WebSocketClient();
        socket = new ODLNotificationSocket();
        try {
            client.start();

            URI websocketUri = new URI(getSubscribedStream(data));
            ClientUpgradeRequest request = new ClientUpgradeRequest();
            client.connect(socket, websocketUri, request);
            LOG.info("Connecting to : {}", websocketUri);
        } catch (Throwable t) {
            LOG.error("Fail during connecting websocket:", t);
            try {
                client.stop();
            } catch (Exception e) {
                LOG.error("Fail to stop websocket client:", e);
            }
            client = null;
        }
        return socket;
    }

    public boolean readStateAndClean() {
        if (socket != null) {
            return socket.readStateAndClean();
        }
        return false;
    }

    private String getNotificationSubscriptionRequestJson(String path, String datastore, String scope) {
        String requestJson = ODLConstants.DEFAULT_NOTIFICATION_SUBSCRIPTION;
        try {
            requestJson = mapper.writeValueAsString(
                    new NotificationSubscriptionRequest(new NotificationSubscriptionInput(path, datastore, scope)));
        } catch (JsonProcessingException e) {
            LOG.error("Fail to generate json string for NotificationSubscriptionRequest", e);
            LOG.info("Use default notification subscription request: {}", requestJson);
        }
        return requestJson;
    }

    private String getSubscribedStream(String data) {
        Executor executor = Executor.newInstance().auth(auth);
        String websocketSteamUri = null;
        try {
            String outputStreamNameJson = executor.execute(Request
                    .Post(baseUri.toString() + ODLConstants.SAL_REMOTE_SUBSCRIPTION)
                    .bodyString(data, ContentType.APPLICATION_JSON))
                    .returnContent().asString();
            LOG.info("Got subscription response: {}", outputStreamNameJson);
            SalRemoteResponse outputStreamName = mapper.readValue(outputStreamNameJson, SalRemoteResponse.class);
            String websocketLocationJson = executor.execute(Request
                    .Get(baseUri.toString() + ODLConstants.RESTCONF_STREAM + outputStreamName.output.streamName))
                    .returnContent().asString();
            StreamLocation websocketLocation = mapper.readValue(websocketLocationJson, StreamLocation.class);
            websocketSteamUri = websocketLocation.location;
        } catch (IOException e) {
            LOG.error("Http request error:", e);
        }
        return websocketSteamUri;
    }
}