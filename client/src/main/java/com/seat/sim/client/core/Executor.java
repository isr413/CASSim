package com.seat.sim.client.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.seat.sim.common.math.Vector;
import com.seat.sim.common.math.Zone;
import com.seat.sim.common.remote.RemoteState;
import com.seat.sim.common.remote.intent.IntentionSet;
import com.seat.sim.common.remote.mobile.MobileRemoteController;
import com.seat.sim.common.scenario.Snapshot;

public class Executor {

    private Knowledge knowledge;

    public Executor(Knowledge knowledge) {
        this.knowledge = knowledge;
    }

    public Collection<IntentionSet> update(Snapshot snap) {
        List<IntentionSet> intentions = new ArrayList<>();
        for (String remoteID : this.knowledge.getDroneIDs()) {
            if (!snap.hasActiveRemoteWithID(remoteID)) continue;
            RemoteState remoteState = snap.getRemoteStateWithID(remoteID);
            if (remoteState.isDisabled() || !remoteState.hasLocation() || !remoteState.isMobile()) continue;
            if (!this.knowledge.hasAssignment(remoteID)) continue;
            MobileRemoteController controller = new MobileRemoteController(remoteID);
            Zone goalZone = this.knowledge.getAssignment(remoteID);
            double distanceToGoal = Vector.subtract(goalZone.getLocation(), remoteState.getLocation()).getMagnitude();
            if (distanceToGoal <= Knowledge.DOUBLE_PRECISION) {
                controller.stop();
            } else {
                controller.goToLocation(goalZone.getLocation());
            }
            intentions.add(controller.getIntentions());
        }
        return intentions;
    }

}
