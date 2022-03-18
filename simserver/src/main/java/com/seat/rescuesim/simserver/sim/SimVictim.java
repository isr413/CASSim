package com.seat.rescuesim.simserver.sim;

import java.util.ArrayList;

import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.sensor.SensorState;
import com.seat.rescuesim.common.victim.VictimSpec;
import com.seat.rescuesim.common.victim.VictimState;

public class SimVictim extends KineticSimRemote {

    public SimVictim(String label, VictimSpec spec, Vector location) {
        super(label, spec, location);
    }

    public SimVictim(String label, VictimSpec spec, Vector location, double battery) {
        super(label, spec, location, battery);
    }

    public SimVictim(String label, VictimSpec spec, Vector location, Vector velocity) {
        super(label, spec, location);
        this.velocity = velocity;
    }

    public SimVictim(String label, VictimSpec spec, Vector location, double battery, Vector velocity,
            Vector acceleration) {
        super(label, spec, location, battery, velocity, acceleration);
    }

    public VictimSpec getSpec() {
        return (VictimSpec) this.spec;
    }

    public VictimState getState() {
        return new VictimState(
            this.getSpec().getSpecType(),
            this.label,
            this.location,
            this.battery,
            new ArrayList<SensorState>(),
            this.velocity,
            this.acceleration
        );
    }

}
