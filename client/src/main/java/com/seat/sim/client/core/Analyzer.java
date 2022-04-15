package com.seat.sim.client.core;

import java.util.HashSet;
import com.seat.sim.common.math.Vector;
import com.seat.sim.common.math.Zone;
import com.seat.sim.common.remote.RemoteState;
import com.seat.sim.common.scenario.Snapshot;

public class Analyzer {

    private Knowledge knowledge;

    public Analyzer(Knowledge knowledge) {
        this.knowledge = knowledge;
    }

    public void update(Snapshot snap) {
        for (String remoteID : new HashSet<>(this.knowledge.getAssignments().keySet())) {
            if (!snap.hasActiveRemoteWithID(remoteID)) continue;
            RemoteState remoteState = snap.getRemoteStateWithID(remoteID);
            if (remoteState.isDisabled() || !remoteState.hasLocation() || !remoteState.isMobile()) continue;
            Zone goalZone = this.knowledge.getAssignment(remoteID);
            double distanceToGoal = Vector.subtract(goalZone.getLocation(), remoteState.getLocation()).getMagnitude();
            if (distanceToGoal <= Knowledge.DOUBLE_PRECISION) {
                this.knowledge.removeAssignment(remoteID);
            }
        }
    }

}