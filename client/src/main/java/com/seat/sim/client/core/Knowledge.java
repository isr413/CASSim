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
    private Set<String> baseIDs;
    private Map<String, Set<String>> connections;
    private Set<String> droneIDs;
    private Grid grid;
    private Vector homeLocation;
    private Random rng;
    private Set<String> victimIDs;
    private double victimStopProbability;

    public Knowledge(Grid grid) {
        this.grid = grid;
        this.assignments = new HashMap<>();
        this.baseIDs = new HashSet<>();
        this.connections = new HashMap<>();
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

    public boolean addBaseID(String remoteID) {
        if (this.hasBaseID(remoteID)) return true;
        this.baseIDs.add(remoteID);
        return true;
    }

    public boolean addBaseIDs(Collection<String> remoteIDs) {
        this.baseIDs.addAll(remoteIDs);
        return true;
    }

    public boolean addConnection(String remoteID, String other) {
        if (!this.hasConnections(remoteID)) this.connections.put(remoteID, new HashSet<>());
        if (this.connections.get(remoteID).contains(other)) return true;
        this.connections.get(remoteID).add(other);
        return true;
    }

    public boolean addConnections(String remoteID, Collection<String> others) {
        if (!this.hasConnections(remoteID)) this.connections.put(remoteID, new HashSet<>());
        this.connections.get(remoteID).addAll(others);
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

    public Set<String> getBaseIDs() {
        return this.baseIDs;
    }

    public Map<String, Set<String>> getConnections() {
        return this.connections;
    }

    public Set<String> getConnections(String remoteID) {
        if (!this.hasConnections(remoteID)) return new HashSet<>();
        return this.connections.get(remoteID);
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

    public boolean hasBaseID(String remoteID) {
        return this.baseIDs.contains(remoteID);
    }

    public boolean hasBaseIDs() {
        return !this.baseIDs.isEmpty();
    }

    public boolean hasConnection(String remoteID, String other) {
        if (!this.hasConnections(remoteID)) return false;
        return this.connections.get(remoteID).contains(other);
    }

    public boolean hasConnections() {
        return !this.connections.isEmpty();
    }

    public boolean hasConnections(String remoteID) {
        return this.connections.containsKey(remoteID) && !this.connections.get(remoteID).isEmpty();
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

    public boolean removeAssignments() {
        this.assignments = new HashMap<>();
        return true;
    }

    public boolean removeBaseID(String remoteID) {
        if (!this.hasBaseID(remoteID)) return true;
        this.baseIDs.remove(remoteID);
        return true;
    }

    public boolean removeBaseIDs() {
        this.baseIDs = new HashSet<>();
        return true;
    }

    public boolean removeConnection(String remoteID, String other) {
        if (!this.hasConnection(remoteID, other)) return true;
        this.connections.get(remoteID).remove(other);
        if (!this.hasConnections(remoteID)) {
            this.connections.remove(remoteID);
        }
        return true;
    }

    public boolean removeConnections() {
        this.connections = new HashMap<>();
        return true;
    }

    public boolean removeConnections(String remoteID) {
        if (!this.hasConnections(remoteID)) return true;
        this.connections.remove(remoteID);
        return true;
    }

    public boolean removeDroneID(String remoteID) {
        if (!this.hasDroneID(remoteID)) return true;
        this.droneIDs.remove(remoteID);
        return true;
    }

    public boolean removeDroneIDs() {
        this.droneIDs = new HashSet<>();
        return true;
    }

    public boolean removeVictimID(String remoteID) {
        if (!this.hasVictimID(remoteID)) return true;
        this.victimIDs.remove(remoteID);
        return true;
    }

    public boolean removeVictimIDs() {
        this.victimIDs = new HashSet<>();
        return true;
    }

    public void setAssignment(String remoteID, Zone zone) {
        this.assignments.put(remoteID, zone);
    }

    public void setConnections(String remoteID, Collection<String> others) {
        this.connections.put(remoteID, new HashSet<>(others));
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
