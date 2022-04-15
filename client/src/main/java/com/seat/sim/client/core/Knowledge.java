package com.seat.sim.client.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.seat.sim.common.math.Grid;
import com.seat.sim.common.math.Vector;
import com.seat.sim.common.math.Zone;
import com.seat.sim.common.util.Random;

public class Knowledge {
    public static final double DOUBLE_PRECISION = 0.01;

    private Map<String, Zone> assignments;
    private Set<String> droneIDs;
    private Grid grid;
    private Vector homeLocation;
    private Random rng;
    private Set<String> victimIDs;
    private double victimStopProbability;

    public Knowledge(Grid grid) {
        this.grid = grid;
        this.assignments = new HashMap<>();
        this.droneIDs = new HashSet<>();
        this.homeLocation = null;
        this.rng = new Random();
        this.victimIDs = new HashSet<>();
        this.victimStopProbability = 0;
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

    public boolean addDroneIDs(Collection<String> remoteIDs) {
        this.droneIDs.addAll(remoteIDs);
        return true;
    }

    public boolean addVictimID(String remoteID) {
        if (this.hasVictimID(remoteID)) return true;
        this.victimIDs.add(remoteID);
        return true;
    }

    public boolean addVictimIDs(Collection<String> remoteIDs) {
        this.victimIDs.addAll(remoteIDs);
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

    public Random getRandom() {
        return this.rng;
    }

    public Set<String> getVictimIDs() {
        return this.victimIDs;
    }

    public double getVictimStopProbability() {
        return this.victimStopProbability;
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

    public boolean hasVictimID(String remoteID) {
        return this.victimIDs.contains(remoteID);
    }

    public boolean hasVictimIDs() {
        return !this.victimIDs.isEmpty();
    }

    public boolean hasVictimStopProbability() {
        return this.victimStopProbability > 0;
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

    public boolean removeVictimID(String remoteID) {
        if (!this.hasVictimID(remoteID)) return true;
        this.victimIDs.remove(remoteID);
        return true;
    }

    public void setAssignment(String remoteID, Zone zone) {
        this.assignments.put(remoteID, zone);
    }

    public void setHomeLocation(Vector homeLocation) {
        this.homeLocation = homeLocation;
    }

    public void setRandomSeed(long seed) {
        this.rng = new Random(seed);
    }

    public void setVictimStopProbability(double p) {
        if (p < 0 || 1 < p) return;
        this.victimStopProbability = p;
    }

}
