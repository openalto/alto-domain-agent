package org.snlab.unicorn;

import java.util.Collection;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snlab.unicorn.orchestrator.OrchestratorInfo;
import org.snlab.unicorn.orchestrator.OrchestratorInfoReader;
import org.snlab.unicorn.register.UnicornRegister;
import org.snlab.unicorn.register.UnicornRegisterBuilder;

public class Bootstrap extends HttpServlet {
    private static final long serialVersionUID = 2974591291665439150L;
    private final static Logger LOG = LoggerFactory.getLogger(Bootstrap.class);

    @Override
    public void init(ServletConfig config) throws ServletException {
        LOG.info("Starting bootstrap stage...");
        // Register to orchestrator
        Collection<OrchestratorInfo> infos = OrchestratorInfoReader.read(UnicornDefinitions.OrchestratorConfig.CONFIG_PATH);
        UnicornRegister register = new UnicornRegisterBuilder().addOrchestrators(infos).build();
        register.register();
    }
}