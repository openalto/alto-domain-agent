package org.snlab.unicorn;

import org.glassfish.jersey.media.sse.SseFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnicornApplication extends ResourceConfig {
    private final static Logger LOG = LoggerFactory.getLogger(UnicornApplication.class);

    public UnicornApplication() {
        super(UnicornService.class, SseFeature.class);
        LOG.info("Starting unicorn application...");
    }
}
