package org.snlab.unicorn;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
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

import org.apache.http.auth.UsernamePasswordCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snlab.unicorn.adapter.ControllerAdapter;
import org.snlab.unicorn.adapter.ODLAdapter;
import org.snlab.unicorn.handlers.OrchestratorQueryHandler;

/**
 * Root resource (exposed at "unicorn" path)
 */
@Path("unicorn")
public class UnicornService {

    private final static Logger LOG = LoggerFactory.getLogger(UnicornService.class);
    private final static String UPDATES_STREAM_CONTROL_TYPE = "application/updatestreamcontrol";
    private final static String UPDATE_STREAM_ROUTE = "updates";
    private final static String CONTROL_STREAM_ROUTE = "controls";
    private final static String PARAMETER_NAME_ADAPTER_TYPE = "adapter.type";
    private final static String PARAMETER_NAME_ADAPTER_BASEURI = "adapter.baseUri";
    private final static String PARAMETER_NAME_ADAPTER_AUTH_USERNAME = "adapter.auth.username";
    private final static String PARAMETER_NAME_ADAPTER_AUTH_PASSWORD = "adapter.auth.password";
    private final static String ADAPTER_TYPE_ODL = "odl"; // Maybe we can use an enum type

    // Maybe we can move this list to another place
    private static List<ControllerAdapter> adapterList = new ArrayList<>();

    @Context
    private UriInfo uriInfo;
    @Context
    private ServletContext servletContext;

    private ControllerAdapter getNewAdapterInstance() {
        // TODO: null is unsafe, setting a default/mock adapter is better
        ControllerAdapter adapter = null;
        if (ADAPTER_TYPE_ODL.equals(servletContext.getInitParameter(PARAMETER_NAME_ADAPTER_TYPE))) {
            try {
				adapter = new ODLAdapter(new URI(servletContext.getInitParameter(PARAMETER_NAME_ADAPTER_BASEURI)),
                    new UsernamePasswordCredentials(servletContext.getInitParameter(PARAMETER_NAME_ADAPTER_AUTH_USERNAME),
                            servletContext.getInitParameter(PARAMETER_NAME_ADAPTER_AUTH_PASSWORD)));
			} catch (URISyntaxException e) {
                LOG.error("Fail to create adapter: baseUri is invalid!");
			}
        }
        return adapter;
    }

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getServiceMetaData() {
        Enumeration<?> paramNames = servletContext.getInitParameterNames();
        String meta = "Unicorn Server Options:\n";
        while (paramNames.hasMoreElements()) {
            String name = (String) paramNames.nextElement();
            meta += "  " + name + ": " + servletContext.getInitParameter(name) + "\n";
        }
        return meta;
    }

    @GET
    @Path(UPDATE_STREAM_ROUTE)
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public void establishUpdateStream(@Context SseEventSink eventSink, @Context Sse sse) {
        UUID newControlStreamId = UUID.randomUUID();
        ControllerAdapter newAdapter = getNewAdapterInstance();
        while (!OrchestratorQueryHandler.setHandler(newControlStreamId.toString(), newAdapter)) {
            newControlStreamId = UUID.randomUUID();
        }
        OrchestratorQueryHandler newHandler = OrchestratorQueryHandler.getHandler(newControlStreamId.toString());
        eventSink.send(sse.newEventBuilder()
                .name(UPDATES_STREAM_CONTROL_TYPE)
                .data(Paths.get(uriInfo.getBaseUri().toString(), "unicorn", CONTROL_STREAM_ROUTE,
                        newControlStreamId.toString()).toString())
                .build());
        // TODO: Replace the lambda expression by a closable thread class
        new Thread(() -> {
            while (true) {
                if (newAdapter.ifAsPathChangedThenCleanState()) {
                    LOG.debug("As path data or request changed. Replay request.");
                    // TODO: Handle path query
                    String pathQueryResponse = "";
                    eventSink.send(sse.newEventBuilder()
                            .name(MediaType.APPLICATION_JSON)
                            .data(pathQueryResponse)
                            .build());
                }
            }
        }).start();
        new Thread(() -> {
            while (true) {
                if (newAdapter.ifResourceChangedThenCleanState()) {
                    LOG.debug("Resource data or request changed. Replay request.");
                    // TODO: Handle resource query
                    String resourceQueryResponse = "";
                    eventSink.send(sse.newEventBuilder()
                            .name(MediaType.APPLICATION_JSON)
                            .data(resourceQueryResponse)
                            .build());
                }
            }
        }).start();
    }

    @Path(CONTROL_STREAM_ROUTE)
    public ControlServiceResource controlServiceResource() {
        return new ControlServiceResource();
    }

    public class ControlServiceResource {
        @POST
        @Path("{id}")
        @Produces(MediaType.APPLICATION_JSON)
        @Consumes(MediaType.APPLICATION_JSON)
        public String getControlStreamQuery(@PathParam("id") String id, String body) {
            OrchestratorQueryHandler handler = OrchestratorQueryHandler.getHandler(id);
            return handler.handle(body);
        }
    }
}
