package com.seat.sim.client.core;

import java.util.Set;
import java.util.stream.Collectors;

import com.seat.sim.common.math.Vector;
import com.seat.sim.common.math.Zone;
import com.seat.sim.common.remote.RemoteState;
import com.seat.sim.common.scenario.Snapshot;
import com.seat.sim.common.sensor.comms.CommsSensorState;

public class Analyzer {

    private Knowledge knowledge;

    public Analyzer(Knowledge knowledge) {
        this.knowledge = knowledge;
    }

    public void update(Snapshot snap) {
        for (String remoteID : this.knowledge.getRemoteIDs()) {
            if (!snap.hasActiveRemoteWithID(remoteID)) continue;
            RemoteState remoteState = snap.getRemoteStateWithID(remoteID);
            if (remoteState.isDisabled() || !remoteState.hasLocation()) continue;

            if (remoteState.isMobile() && this.knowledge.hasAssignment(remoteID)) {
                Zone goalZone = this.knowledge.getAssignment(remoteID);
                double distanceToGoal = Vector.subtract(
                        goalZone.getLocation(),
                        remoteState.getLocation()
                    ).getMagnitude();
                if (distanceToGoal <= Knowledge.DOUBLE_PRECISION) {
                    this.knowledge.removeAssignment(remoteID);
                }
            }

            if (this.knowledge.hasDroneID(remoteID) && !this.knowledge.hasAssignment(remoteID)) {
                Set<String> remoteConnections = remoteState.getSensorStatesWithType(CommsSensorState.class).stream()
                    .filter(sensorState -> sensorState.hasConnections())
                    .map(sensorState -> sensorState.getConnections())
                    .flatMap(connections -> connections.stream())
                    .filter(other -> !this.knowledge.hasBaseID(other))
                    .filter(other -> !this.knowledge.hasDroneID(other))
                    .collect(Collectors.toSet());
                if (remoteConnections.isEmpty()) {
                    this.knowledge.removeConnections(remoteID);
                    continue;
                }
                for (String other : remoteConnections) {
                    if (this.knowledge.hasConnection(remoteID, other) &&
                            this.knowledge.hasConnection(other, remoteID)) continue;
                    if (this.knowledge.hasConnection(remoteID, other)) {
                        this.knowledge.removeConnection(remoteID, other);
                    }
                    if (this.knowledge.hasConnection(other, remoteID)) {
                        this.knowledge.addConnection(remoteID, other);
                    } else {
                        this.knowledge.addReservedConnection(remoteID, other, true);
                    }
                    if (this.knowledge.hasConnection(other, remoteID)) {
                        Zone connectionZone = this.knowledge.getGrid().getZoneAtLocation(remoteState.getLocation());
                        this.knowledge.setConnectionZone(remoteID, connectionZone);
                        this.knowledge.setConnectionZone(other, connectionZone);
                    }
                }
            }

        }
    }

}
