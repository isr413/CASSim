package com.seat.sim.client.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.seat.sim.common.math.Vector;
import com.seat.sim.common.math.Zone;
import com.seat.sim.common.remote.RemoteState;
import com.seat.sim.common.scenario.Snapshot;
import com.seat.sim.common.util.Debugger;

public class Planner {

    private Knowledge knowledge;
    private List<Zone> zones;

    public Planner(Knowledge knowledge) {
        this.knowledge = knowledge;
        this.init();
    }

    private void init() {
        this.zones = new ArrayList<>();
        for (int row = 0; row < this.knowledge.getGrid().getHeightInZones(); row++) {
            for (int col = 0; col < this.knowledge.getGrid().getWidthInZones(); col++) {
                Zone zone = this.knowledge.getGrid().getZone(row, col);
                if (this.knowledge.hasHomeLocation()) {
                    double distanceToHome = Vector.subtract(zone.getLocation(),
                        this.knowledge.getHomeLocation()).getMagnitude();
                    if (distanceToHome <= Knowledge.DOUBLE_PRECISION) {
                        continue;
                    }
                }
                this.zones.add(zone);
            }
        }
        if (this.knowledge.hasHomeLocation()) {
            this.orderZonesByDistanceFromHome();
        }
        for (String remoteID : this.knowledge.getDroneIDs()) {
            if (this.zones.isEmpty()) break;
            if (this.knowledge.addAssignment(remoteID, this.zones.get(this.zones.size()-1))) {
                this.zones.remove(this.zones.size()-1);
            }
        }
    }

    private void assignNextClosestZoneToRemote(String remoteID, Vector remoteLocation) {
        double minZoneDist = Double.POSITIVE_INFINITY;
        int closestZoneIdx = -1;
        for (int i = 0; i < this.zones.size(); i++) {
            Zone zone = this.zones.get(i);
            double zoneDist = Vector.subtract(zone.getLocation(), remoteLocation).getMagnitude();
            if (closestZoneIdx < 0 || zoneDist < minZoneDist) {
                closestZoneIdx = i;
                minZoneDist = zoneDist;
            }
        }
        if (closestZoneIdx < 0) return;
        if (this.knowledge.addAssignment(remoteID, this.zones.get(closestZoneIdx))) {
            this.zones.remove(closestZoneIdx);
        }
    }

    private void assignAdjacentZoneAtRandom(String remoteID, Vector remoteLocation) {
        List<Zone> adjacentZones = new ArrayList<>();
        int zoneSize = this.knowledge.getGrid().getZoneSize();
        double x = remoteLocation.getX();
        double y = remoteLocation.getY();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) continue;
                if (x + j * zoneSize < 0) continue;
                if (this.knowledge.getGrid().getWidth() <= x + j * zoneSize) continue;
                if (y + i * zoneSize < 0) continue;
                if (this.knowledge.getGrid().getHeight() <= y + i * zoneSize) continue;
                adjacentZones.add(this.knowledge.getGrid().getZoneAtLocation(x + j * zoneSize, y + i * zoneSize));
            }
        }
        if (adjacentZones.isEmpty()) return;
        int randomIdx = this.knowledge.getRandom().getRandomNumber(adjacentZones.size());
        this.knowledge.addAssignment(remoteID, adjacentZones.get(randomIdx));
    }

    private void orderZonesByDistanceFromHome() {
        Vector homeLocation = this.knowledge.getHomeLocation();
        Collections.sort(this.zones, new Comparator<Zone>(){
            @Override
            public int compare(Zone z1, Zone z2) {
                Double z1DistFromHome = Vector.subtract(homeLocation, z1.getLocation()).getMagnitude();
                Double z2DistFromHome = Vector.subtract(homeLocation, z2.getLocation()).getMagnitude();
                return z2DistFromHome.compareTo(z1DistFromHome);
            }
        });
    }

    public void update(Snapshot snap) {
        for (String remoteID : this.knowledge.getDroneIDs()) {
            if (!snap.hasActiveRemoteWithID(remoteID)) continue;
            RemoteState remoteState = snap.getRemoteStateWithID(remoteID);
            if (remoteState.isDisabled() || !remoteState.hasLocation() || !remoteState.isMobile()) continue;
            if (!this.knowledge.hasAssignment(remoteID)) {
                if (this.knowledge.hasConnections(remoteID)) {
                    Debugger.logger.warn(this.knowledge.getConnections(remoteID));
                }
                if (this.zones.isEmpty()) continue;
                this.assignNextClosestZoneToRemote(remoteID, remoteState.getLocation());
            }
        }
        for (String remoteID : this.knowledge.getVictimIDs()) {
            if (!snap.hasActiveRemoteWithID(remoteID)) continue;
            RemoteState remoteState = snap.getRemoteStateWithID(remoteID);
            if (remoteState.isDisabled() || !remoteState.hasLocation() || !remoteState.isMobile()) continue;
            if (!this.knowledge.hasAssignment(remoteID)) {
                if (this.knowledge.hasVictimStopProbability()) {
                    if (this.knowledge.getRandom().getRandomProbability() < this.knowledge.getVictimStopProbability()) {
                        continue;
                    }
                }
                this.assignAdjacentZoneAtRandom(remoteID, remoteState.getLocation());
            }
        }
    }

}
