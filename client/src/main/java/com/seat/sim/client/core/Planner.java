package com.seat.sim.client.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.seat.sim.common.math.Vector;
import com.seat.sim.common.math.Zone;
import com.seat.sim.common.remote.RemoteState;
import com.seat.sim.common.scenario.Snapshot;

public class Planner {

    private Knowledge knowledge;
    private List<Zone> zones;

    public Planner(Knowledge knowledge) {
        this.knowledge = knowledge;
        this.init();
    }

    private void init() {
        this.zones = new ArrayList<>();
        for (int y = 0; y < this.knowledge.getGrid().getHeightInZones(); y++) {
            for (int x = 0; x < this.knowledge.getGrid().getWidthInZones(); x++) {
                Zone zone = this.knowledge.getGrid().getZone(x, y);
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
                if (this.zones.isEmpty()) continue;
                this.assignNextClosestZoneToRemote(remoteID, remoteState.getLocation());
            }
        }
    }

}
