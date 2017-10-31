package org.snlab.unicorn.register;

import com.sun.org.apache.xpath.internal.operations.Or;
import org.snlab.unicorn.orchestrator.OrchestratorInfo;

import java.util.Collection;

public class UnicornRegisterImpl implements UnicornRegister {
    protected Collection<OrchestratorInfo> infos;

    UnicornRegisterImpl(Collection<OrchestratorInfo> infos) {
        this.infos = infos;
    }

    @Override
    public void register() {
        //TODO
        System.out.println("Register");
    }
}
