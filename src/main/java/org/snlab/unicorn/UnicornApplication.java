package org.snlab.unicorn;

import org.glassfish.jersey.media.sse.SseFeature;
import org.glassfish.jersey.server.ResourceConfig;

public class UnicornApplication extends ResourceConfig {
    public UnicornApplication() {
        super(UnicornService.class, SseFeature.class);
    }
}
