package com.seat.sim.client.core;

import com.seat.sim.common.scenario.Snapshot;

public class Monitor {

    private Knowledge knowledge;

    public Monitor(Knowledge knowledge) {
        this.knowledge = knowledge;
    }

    public double update(Snapshot snap) {
        return 0.0;
    }

}
