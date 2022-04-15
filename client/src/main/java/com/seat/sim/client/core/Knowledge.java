package com.seat.sim.client.core;

import java.util.Arrays;
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
    private Map<String, Zone> connectionZones;
    private Set<String> droneIDs;
    private Grid grid;
    private Vector homeLocation;
    private Set<String> remoteIDs;
    private Random rng;
    private Set<String> victimIDs;
    private double victimStopProbability;

    public Knowledge(Grid grid) {
        this.grid = grid;
        this.assignments = new HashMap<>();
        this.baseIDs = new HashSet<>();
        this.connections = new HashMap<>();
        this.connectionZones = new HashMap<>();
        this.droneIDs = new HashSet<>();
        this.homeLocation = null;
        this.remoteIDs = new HashSet<>();
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
        this.remoteIDs.add(remoteID);
        return true;
    }

    public boolean addBaseIDs(Collection<String> remoteIDs) {
        this.baseIDs.addAll(remoteIDs);
        this.remoteIDs.addAll(remoteIDs);
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
        this.remoteIDs.add(remoteID);
        return true;
    }

    public boolean addDroneIDs(Collection<String> remoteIDs) {
        this.droneIDs.addAll(remoteIDs);
        this.remoteIDs.addAll(remoteIDs);
        return true;
    }

    public boolean addReservedConnection(String remoteID, String other, boolean remoteCanHaveMultipleConnections) {
        if (this.hasConnection(other, remoteID)) return true;
        if (this.hasConnections(other)) return false;
        if (!remoteCanHaveMultipleConnections) {
            this.setConnection(remoteID, other);
            this.setConnection(other, remoteID);
            return true;
        }
        if (this.addConnection(remoteID, other)) {
            this.setConnection(other, remoteID);
            return true;
        }
        return false;
    }

    public boolean addVictimID(String remoteID) {
        if (this.hasVictimID(remoteID)) return true;
        this.victimIDs.add(remoteID);
        this.remoteIDs.add(remoteID);
        return true;
    }

    public boolean addVictimIDs(Collection<String> remoteIDs) {
        this.victimIDs.addAll(remoteIDs);
        this.remoteIDs.addAll(remoteIDs);
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

    public Zone getConnectionZone(String remoteID) {
        if (!this.hasConnectionZone(remoteID)) return null;
        return this.connectionZones.get(remoteID);
    }

    public Map<String, Zone> getConnectionZones() {
        return this.connectionZones;
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

    public Set<String> getRemoteIDs() {
        return this.remoteIDs;
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

    public boolean hasConnectionZone(String remoteID) {
        return this.connectionZones.containsKey(remoteID);
    }

    public boolean hasConnectionZones() {
        return !this.connectionZones.isEmpty();
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

    public boolean hasRemoteID(String remoteID) {
        return this.remoteIDs.contains(remoteID);
    }

    public boolean hasRemoteIDs() {
        return !this.remoteIDs.isEmpty();
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
        this.remoteIDs.remove(remoteID);
        this.baseIDs.remove(remoteID);
        return true;
    }

    public boolean removeBaseIDs() {
        this.remoteIDs.removeAll(this.baseIDs);
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

    public boolean removeConnectionZone(String remoteID) {
        if (!this.hasConnectionZone(remoteID)) return true;
        this.connectionZones.remove(remoteID);
        return true;
    }

    public boolean removeConnectionZones() {
        this.connectionZones = new HashMap<>();
        return true;
    }

    public boolean removeDroneID(String remoteID) {
        if (!this.hasDroneID(remoteID)) return true;
        this.remoteIDs.remove(remoteID);
        this.droneIDs.remove(remoteID);
        return true;
    }

    public boolean removeDroneIDs() {
        this.remoteIDs.removeAll(this.droneIDs);
        this.droneIDs = new HashSet<>();
        return true;
    }

    public boolean removeVictimID(String remoteID) {
        if (!this.hasVictimID(remoteID)) return true;
        this.remoteIDs.remove(remoteID);
        this.victimIDs.remove(remoteID);
        return true;
    }

    public boolean removeVictimIDs() {
        this.remoteIDs.removeAll(this.victimIDs);
        this.victimIDs = new HashSet<>();
        return true;
    }

    public void setAssignment(String remoteID, Zone zone) {
        this.assignments.put(remoteID, zone);
    }

    public void setConnection(String remoteID, String other) {
        this.connections.put(remoteID, new HashSet<>(Arrays.asList(other)));
    }

    public void setConnections(String remoteID, Collection<String> others) {
        this.connections.put(remoteID, new HashSet<>(others));
    }

    public void setConnectionZone(String remoteID, Zone zone) {
        this.connectionZones.put(remoteID, zone);
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
