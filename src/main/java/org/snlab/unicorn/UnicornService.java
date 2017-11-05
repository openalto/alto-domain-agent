package org.snlab.unicorn;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseEventSink;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * Root resource (exposed at "unicorn" path)
 */
@Path("unicorn")
public class UnicornService {

    private final static String UPDATES_STREAM_CONTROL_TYPE = "application/updatestreamcontrol";
    private final static String UPDATE_STREAM_ROUTE = "updates";
    private final static String CONTROL_STREAM_ROUTE = "controls";

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
    @Path(UPDATE_STREAM_ROUTE)
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public void establishUpdateStream(@Context SseEventSink eventSink, @Context Sse sse) {
        UUID newControlStreamId = UUID.randomUUID();
        eventSink.send(sse.newEventBuilder()
                .name(UPDATES_STREAM_CONTROL_TYPE)
                .data(uriInfo.getBaseUri().toString() + Paths.get("unicorn", CONTROL_STREAM_ROUTE, newControlStreamId.toString()).toString())
                .build());
        // TODO: Track update stream and handle the following dynamic updates
        new Thread(() -> {
        }).start();
    }

    @Path(CONTROL_STREAM_ROUTE)
    public ControlServiceResource controlServiceResource() {
        return new ControlServiceResource();
    }

    public class ControlServiceResource {
        @POST
        @Path("{id}")
        public void getControlStreamQuery(@PathParam("id") String id) {
            System.out.println(id);
            //TODO
        }
    }
}
