package com.seat.sim.client.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.seat.sim.common.math.Grid;
import com.seat.sim.common.math.Vector;
import com.seat.sim.common.math.Zone;

public class Knowledge {
    public static final double DOUBLE_PRECISION = 0.01;

    private Map<String, Zone> assignments;
    private Set<String> droneIDs;
    private Grid grid;
    private Vector homeLocation;

    public Knowledge(Grid grid) {
        this.grid = grid;
        this.assignments = new HashMap<>();
        this.droneIDs = new HashSet<>();
        this.homeLocation = null;
    }

    public boolean addAssignment(String remoteID, Zone zone) {
        if (this.hasAssignment(remoteID)) return false;
        this.assignments.put(remoteID, zone);
        return true;
    }

    public boolean addDroneID(String remoteID) {
        if (this.hasDroneID(remoteID)) return true;
        this.droneIDs.add(remoteID);
        return true;
    }

    public boolean addDroneIDs(Collection<String> droneIDs) {
        this.droneIDs.addAll(droneIDs);
        return true;
    }

    public Zone getAssignment(String remoteID) {
        if (!this.hasAssignment(remoteID)) return null;
        return this.assignments.get(remoteID);
    }

    public Map<String, Zone> getAssignments() {
        return this.assignments;
    }

    public Set<String> getDroneIDs() {
        return this.droneIDs;
    }

    public Grid getGrid() {
        return this.grid;
    }

    public Vector getHomeLocation() {
        return this.homeLocation;
    }

    public boolean hasAssignment(String remoteID) {
        return this.assignments.containsKey(remoteID);
    }

    public boolean hasAssignments() {
        return !this.assignments.isEmpty();
    }

    public boolean hasDroneID(String remoteID) {
        return this.droneIDs.contains(remoteID);
    }

    public boolean hasDroneIDs() {
        return !this.droneIDs.isEmpty();
    }

    public boolean hasHomeLocation() {
        return this.homeLocation != null;
    }

    public boolean removeAssignment(String remoteID) {
        if (!this.hasAssignment(remoteID)) return true;
        this.assignments.remove(remoteID);
        return true;
    }

    public boolean removeDroneID(String remoteID) {
        if (!this.hasDroneID(remoteID)) return true;
        this.droneIDs.remove(remoteID);
        return true;
    }

    public void setAssignment(String remoteID, Zone zone) {
        this.assignments.put(remoteID, zone);
    }

    public void setHomeLocation(Vector homeLocation) {
        this.homeLocation = homeLocation;
    }

}
