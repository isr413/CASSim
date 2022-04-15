package com.seat.sim.client.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import java.util.stream.Collectors;
import com.seat.sim.common.math.Vector;
import com.seat.sim.common.math.Zone;
import com.seat.sim.common.remote.RemoteController;
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
        List<RemoteController> controllers = new ArrayList<>();
        for (String remoteID : this.knowledge.getRemoteIDs()) {
            if (!snap.hasActiveRemoteWithID(remoteID)) continue;
            RemoteState remoteState = snap.getRemoteStateWithID(remoteID);
            if (remoteState.isDisabled() || !remoteState.hasLocation() || !remoteState.isMobile()) continue;
            MobileRemoteController controller = new MobileRemoteController(remoteID);
            controllers.add(controller);
            if (this.knowledge.hasConnections(remoteID) && this.knowledge.hasConnectionZone(remoteID)) {
                controller.goToLocation(this.knowledge.getConnectionZone(remoteID).getLocation());
                continue;
            }
            if (!this.knowledge.hasAssignment(remoteID)) {
                controller.stop();
                continue;
            }
            Zone goalZone = this.knowledge.getAssignment(remoteID);
            double distanceToGoal = Vector.subtract(goalZone.getLocation(), remoteState.getLocation()).getMagnitude();
            if (distanceToGoal <= Knowledge.DOUBLE_PRECISION) {
                controller.stop();
                continue;
            }
            controller.goToLocation(goalZone.getLocation());
        }
        return controllers.stream().map(controller -> controller.getIntentions()).collect(Collectors.toList());
    }

}
