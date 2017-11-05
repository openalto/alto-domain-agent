package org.snlab.unicorn;

import org.glassfish.jersey.media.sse.SseFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.snlab.unicorn.orchestrator.OrchestratorInfo;
import org.snlab.unicorn.orchestrator.OrchestratorInfoReader;
import org.snlab.unicorn.register.UnicornRegister;
import org.snlab.unicorn.register.UnicornRegisterBuilder;

import java.util.Collection;

public class UnicornApplication extends ResourceConfig {
    public UnicornApplication() {
        super(UnicornService.class, SseFeature.class);

        // Register to orchestrator
        Collection<OrchestratorInfo> infos = OrchestratorInfoReader.read(UnicornDefinitions.OrchestratorConfig.CONFIG_PATH);
        UnicornRegister register = new UnicornRegisterBuilder().addOrchestrators(infos).build();
        register.register();
    }
}
