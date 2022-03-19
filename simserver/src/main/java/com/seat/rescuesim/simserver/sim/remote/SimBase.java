package com.seat.rescuesim.simserver.sim.remote;

import com.seat.rescuesim.common.base.BaseSpec;
import com.seat.rescuesim.common.base.BaseState;
import com.seat.rescuesim.common.base.BaseType;

public class SimBase extends SimRemote {

    public SimBase(BaseSpec spec, String label) {
        super(spec, label);
    }

    public BaseSpec getSpec() {
        return (BaseSpec) this.spec;
    }

    public BaseType getSpecType() {
        return this.getSpec().getSpecType();
    }

    public BaseState getState() {
        return new BaseState(
            this.getSpecType(),
            this.label,
            this.location,
            this.battery,
            this.getSensorState()
        );
    }

}
