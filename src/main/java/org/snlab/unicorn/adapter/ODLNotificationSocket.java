package org.snlab.unicorn.adapter;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebSocket(maxTextMessageSize = 64 * 1024)
public class ODLNotificationSocket {
    private final static Logger LOG = LoggerFactory.getLogger(ODLNotificationSocket.class);

    private boolean isDataChanged;

    @SuppressWarnings("unused")
    private Session session;

    @OnWebSocketClose
    public void onClose(int statusCode, String reason)
    {
        LOG.info("Connection closed: {} - {}", statusCode, reason);
        this.session = null;
    }

    @OnWebSocketConnect
    public void onConnect(Session session)
    {
        LOG.info("Got connect: {}", session);
        this.session = session;
    }

    @OnWebSocketMessage
    public void onMessage(String msg)
    {
        LOG.info("Got msg: {}", msg);
        // TODO: parse notification and modify changed state to true.
        isDataChanged = true;
    }

    public boolean readStateAndClean() {
        if (isDataChanged) {
            isDataChanged = false;
            return true;
        } else {
            return false;
        }
    }
}