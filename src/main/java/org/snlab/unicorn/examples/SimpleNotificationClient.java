//
//  ========================================================================
//  Copyright (c) 1995-2017 Mort Bay Consulting Pty. Ltd.
//  ------------------------------------------------------------------------
//  All rights reserved. This program and the accompanying materials
//  are made available under the terms of the Eclipse Public License v1.0
//  and Apache License v2.0 which accompanies this distribution.
//
//      The Eclipse Public License is available at
//      http://www.eclipse.org/legal/epl-v10.html
//
//      The Apache License v2.0 is available at
//      http://www.opensource.org/licenses/apache2.0.php
//
//  You may elect to redistribute this code under either of these licenses.
//  ========================================================================
//

package org.snlab.unicorn.examples;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snlab.unicorn.model.odl.SalRemoteResponse;
import org.snlab.unicorn.model.odl.StreamLocation;


/**
 * Example of a simple Echo Client.
 */
public class SimpleNotificationClient
{
    private final static Logger LOG = LoggerFactory.getLogger(SimpleNotificationClient.class);
    private final static String DEFAULT_WEBSOCKET_URI = "ws://localhost:8185/data-change-event-subscription/opendaylight-inventory:nodes/datastore=CONFIGURATION/scope=SUBTREE";
    private final static String SAL_REMOTE_SUBSCRIPTION = "/restconf/operations/sal-remote:create-data-change-event-subscription";
    private final static String RESTCONF_STREAM = "";

    private static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args)
    {
        String destUri = DEFAULT_WEBSOCKET_URI;
        if (args.length > 0)
        {
            destUri = args[0];
        }
        if (args.length > 1)
        {
            destUri = getSubscribedStream(args[0], args[1]);
        }

        if (destUri == null) {
            LOG.info("No destination uri found!");
            return;
        }

        WebSocketClient client = new WebSocketClient();
        SimpleNotificationSocket socket = new SimpleNotificationSocket();
        try
        {
            client.start();

            URI echoUri = new URI(destUri);
            ClientUpgradeRequest request = new ClientUpgradeRequest();
            client.connect(socket, echoUri, request);
            LOG.info("Connecting to : {}", echoUri);

            // wait for closed socket connection.
            socket.awaitClose(120, TimeUnit.SECONDS);
        }
        catch (Throwable t)
        {
            LOG.error("Fail during running websocket client:", t);
        }
        finally
        {
            try
            {
                client.stop();
            }
            catch (Exception e)
            {
                LOG.error("Fail to stop websocket client:", e);
            }
        }
    }

    private static String getSubscribedStream(String baseUri, String data) {
        Executor executor = Executor.newInstance().auth("admin", "admin");
        String websocketSteamUri = null;
        try {
            String outputStreamNameJson = executor.execute(Request
                    .Post(baseUri + SAL_REMOTE_SUBSCRIPTION)
                    .bodyString(data, ContentType.APPLICATION_JSON))
                    .returnContent().asString();
            LOG.info("Got subscription response: {}", outputStreamNameJson);
            SalRemoteResponse outputStreamName = mapper.readValue(outputStreamNameJson, SalRemoteResponse.class);
            String websocketLocationJson = executor.execute(Request
                    .Get(baseUri + RESTCONF_STREAM + outputStreamName.output.streamName))
                    .returnContent().asString();
            StreamLocation websocketLocation = mapper.readValue(websocketLocationJson, StreamLocation.class);
            websocketSteamUri = websocketLocation.location;
        } catch (IOException e) {
            LOG.error("Http request error:", e);
        }
        return websocketSteamUri;
    }

}