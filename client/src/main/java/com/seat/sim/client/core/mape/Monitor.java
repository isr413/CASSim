package com.seat.sim.client.core.mape;

import com.seat.sim.client.core.util.Remote;
import com.seat.sim.common.scenario.Snapshot;

public class Monitor {

    private Knowledge knowledge;

    public Monitor(Knowledge knowledge) {
        this.knowledge = knowledge;
    }

    public void update(Snapshot snap) {
        for (Remote remote : this.knowledge.getRemotes()) {
            if (!snap.hasRemoteWithID(remote.getRemoteID())) {
                remote.update(null);
                continue;
            }
            remote.update(snap.getRemoteStateWithID(remote.getRemoteID()));
        }
    }

}
