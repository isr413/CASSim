package com.seat.sim.client.core.mape;

import java.util.Set;
import java.util.stream.Collectors;

import com.seat.sim.client.core.util.Remote;
import com.seat.sim.common.math.Vector;
import com.seat.sim.common.math.Zone;
import com.seat.sim.common.scenario.Snapshot;
import com.seat.sim.common.sensor.comms.CommsSensorState;

public class Analyzer {

    private Knowledge knowledge;

    public Analyzer(Knowledge knowledge) {
        this.knowledge = knowledge;
    }

    public void update(Snapshot snap) {
        for (Remote remote : this.knowledge.getRemotes()) {
            if (!remote.isActiveAndOperational()) continue;

            if (remote.isMobile() && remote.hasAssignment()) {
                Zone goalZone = remote.getAssignment();
                double distanceToGoal = Vector.subtract(goalZone.getLocation(), remote.getLocation()).getMagnitude();
                if (distanceToGoal <= Knowledge.DOUBLE_PRECISION) {
                    remote.removeAssignment();
                }
            }

            if (this.knowledge.hasDroneWithID(remote.getRemoteID()) && !remote.hasAssignment()) {
                Set<String> remoteConnections = remote.getRemoteState()
                    .getSensorStatesWithType(CommsSensorState.class)
                    .stream()
                    .filter(sensorState -> sensorState.hasConnections())
                    .map(sensorState -> sensorState.getConnections())
                    .flatMap(connections -> connections.stream())
                    .filter(other -> this.knowledge.hasVictimWithID(other))
                    .filter(other -> this.knowledge.getVictimWithID(other).isActiveAndOperational())
                    .collect(Collectors.toSet());
                if (remoteConnections.isEmpty()) {
                    remote.removeConnections();
                    continue;
                }
                Set<String> lostConnections = remote.getConnections().stream()
                    .filter(other -> !remoteConnections.contains(other))
                    .collect(Collectors.toSet());
                if (!lostConnections.isEmpty()) {
                    remote.removeConnectionsTo(lostConnections);
                }
                for (String other : remoteConnections) {
                    if (!this.knowledge.hasRemoteWithID(other)) continue;
                    this.knowledge.reserveConnectionBetween(remote.getRemoteID(), other, true);
                    if (this.knowledge.hasConnectionBetween(remote.getRemoteID(), other)) {
                        Zone connectionZone = remote.getConnectionZone();
                        if (connectionZone == null) {
                            connectionZone = this.knowledge.getGrid().getZoneAtLocation(remote.getLocation());
                            remote.setConnectionZone(connectionZone);
                        }
                        this.knowledge.getRemoteWithID(other).setConnectionZone(connectionZone);
                    }
                }
            }

        }
    }

}
