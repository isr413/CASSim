package com.seat.rescuesim.simserver.sim;

import com.seat.rescuesim.common.victim.VictimSpec;
import com.seat.rescuesim.common.victim.VictimState;
import com.seat.rescuesim.common.victim.VictimType;

public class SimVictim extends KineticSimRemote {

    public SimVictim(VictimSpec spec, String label) {
        super(spec, label);
    }

    public VictimSpec getSpec() {
        return (VictimSpec) this.spec;
    }

    public VictimType getSpecType() {
        return this.getSpec().getSpecType();
    }

    public VictimState getState() {
        return new VictimState(
            this.getSpecType(),
            this.label,
            this.location,
            this.battery,
            this.getSensorState(),
            this.velocity,
            this.acceleration
        );
    }

}
