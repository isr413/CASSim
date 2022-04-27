package com.seat.sim.client.core.mape;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import java.util.stream.Collectors;

import com.seat.sim.client.core.util.Remote;
import com.seat.sim.common.math.Vector;
import com.seat.sim.common.math.Zone;
import com.seat.sim.common.remote.RemoteController;
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
        for (Remote remote : this.knowledge.getRemotes()) {
            if (!remote.isActiveAndOperational()) continue;
            if (!remote.isMobile()) {
                RemoteController controller = new RemoteController(remote.getRemoteID());
                controllers.add(controller);
                controller.none();
                continue;
            }
            MobileRemoteController controller = new MobileRemoteController(remote.getRemoteID());
            controllers.add(controller);
            if (remote.hasConnectionZone()) {
                controller.goToLocation(remote.getConnectionZone().getLocation());
                continue;
            }
            if (!remote.hasAssignment()) {
                if (this.knowledge.hasDroneWithID(remote.getRemoteID()) && this.knowledge.hasHomeLocation()) {
                    controller.goToLocation(this.knowledge.getHomeLocation());
                    continue;
                }
                controller.stop();
                continue;
            }
            Zone goalZone = remote.getAssignment();
            double distanceToGoal = Vector.subtract(goalZone.getLocation(), remote.getLocation()).getMagnitude();
            if (distanceToGoal <= Knowledge.DOUBLE_PRECISION) {
                controller.stop();
                continue;
            }
            controller.goToLocation(goalZone.getLocation());
        }
        return controllers.stream().map(controller -> controller.getIntentions()).collect(Collectors.toList());
    }

}
