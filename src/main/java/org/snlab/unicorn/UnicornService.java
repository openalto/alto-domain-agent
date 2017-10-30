package org.snlab.unicorn;

import java.util.UUID;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseEventSink;

/**
 * Root resource (exposed at "unicorn" path)
 */
@Path("unicorn")
public class UnicornService {

    private final static String UPDATES_STREAM_CONTROL_TYPE = "application/updatestreamcontrol";

    @Context
    private UriInfo uriInfo;

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
        return "Got it!";
    }

    @GET
    @Path("updates")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public void establishUpdateStream(@Context SseEventSink eventSink, @Context Sse sse) {
        UUID newControlStreamId = UUID.randomUUID();
        eventSink.send(sse.newEventBuilder()
                .name(UPDATES_STREAM_CONTROL_TYPE)
                .data(uriInfo.getBaseUri() + "/" + newControlStreamId)
                .build());
        // TODO: Track update stream and handle the following dynamic updates
        new Thread(() -> {
        }).start();
    }
}
