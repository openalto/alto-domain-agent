package org.snlab.unicorn.register;

import org.snlab.unicorn.orchestrator.OrchestratorInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UnicornRegisterBuilder {
    protected List<OrchestratorInfo> orchestratorInfos;

    public UnicornRegisterBuilder() {
        orchestratorInfos = new ArrayList<>();
    }

    public UnicornRegisterBuilder addOrchestrator(OrchestratorInfo info){
        orchestratorInfos.add(info);
        return this;
    }

    public UnicornRegisterBuilder addOrchestrators(Collection<OrchestratorInfo> infos){
        orchestratorInfos.addAll(infos);
        return this;
    }

    public UnicornRegister build(){
        return new UnicornRegisterImpl(this.orchestratorInfos);
    }
}
