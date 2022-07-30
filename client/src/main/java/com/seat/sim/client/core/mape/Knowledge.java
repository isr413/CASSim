package com.seat.sim.client.core.mape;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.seat.sim.client.core.util.Remote;
import com.seat.sim.common.math.Grid;
import com.seat.sim.common.math.Vector;
import com.seat.sim.common.util.Random;

public class Knowledge {
    public static final double DOUBLE_PRECISION = 0.01;

    private Map<String, Remote> bases;
    private double droneBluetoothDiscoveryProbability;
    private Map<String, Remote> drones;
    private double droneVisionDiscoveryProbability;
    private Grid grid;
    private Vector homeLocation;
    private Random rng;
    private double utility;
    private Map<String, Remote> victims;
    private double victimStopProbability;

    public Knowledge(Grid grid) {
        this.grid = grid;
        this.bases = new HashMap<>();
        this.droneBluetoothDiscoveryProbability = 1;
        this.drones = new HashMap<>();
        this.droneVisionDiscoveryProbability = 1;
        this.homeLocation = null;
        this.rng = new Random();
        this.utility = 0.0;
        this.victims = new HashMap<>();
        this.victimStopProbability = 0;
    }

    public boolean addBase(String remoteID) {
        if (this.hasBaseWithID(remoteID)) return true;
        this.bases.put(remoteID, new Remote(remoteID));
        return true;
    }

    public boolean addBases(Collection<String> remoteIDs) {
        for (String remoteID : remoteIDs) {
            this.addBase(remoteID);
        }
        return true;
    }

    public boolean addConnectionBetween(String remoteID, String other) {
        if (!(this.hasRemoteWithID(remoteID) && this.hasRemoteWithID(other))) return false;
        this.getRemoteWithID(remoteID).addConnectionTo(other);
        this.getRemoteWithID(other).addConnectionTo(remoteID);
        return true;
    }

    public boolean addDrone(String remoteID) {
        if (this.hasDroneWithID(remoteID)) return true;
        this.drones.put(remoteID, new Remote(remoteID));
        return true;
    }

    public boolean addDrones(Collection<String> remoteIDs) {
        for (String remoteID : remoteIDs) {
            this.addDrone(remoteID);
        }
        return true;
    }

    public boolean addVictim(String remoteID) {
        if (this.hasVictimWithID(remoteID)) return true;
        this.victims.put(remoteID, new Remote(remoteID));
        return true;
    }

    public boolean addVictims(Collection<String> remoteIDs) {
        for (String remoteID : remoteIDs) {
            this.addVictim(remoteID);
        }
        return true;
    }

    public Set<String> getBaseIDs() {
        return this.bases.keySet();
    }

    public Collection<Remote> getBases() {
        return this.bases.values();
    }

    public Remote getBaseWithID(String remoteID) {
        if (!this.hasBaseWithID(remoteID)) return null;
        return this.bases.get(remoteID);
    }

    public double getDroneBluetoothDiscoveryProbability() {
        return this.droneBluetoothDiscoveryProbability;
    }

    public Set<String> getDroneIDs() {
        return this.drones.keySet();
    }

    public Collection<Remote> getDrones() {
        return this.drones.values();
    }

    public Remote getDroneWithID(String remoteID) {
        if (!this.hasDroneWithID(remoteID)) return null;
        return this.drones.get(remoteID);
    }

    public double getDroneVisionDiscoveryProbability() {
        return this.droneVisionDiscoveryProbability;
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
        Knowledge knowledge = this;
        return new HashSet<String>(){{
            addAll(knowledge.getBaseIDs());
            addAll(knowledge.getDroneIDs());
            addAll(knowledge.getVictimIDs());
        }};
    }

    public Collection<Remote> getRemotes() {
        Knowledge knowledge = this;
        return new ArrayList<Remote>(){{
            addAll(knowledge.getBases());
            addAll(knowledge.getDrones());
            addAll(knowledge.getVictims());
        }};
    }

    public Remote getRemoteWithID(String remoteID) {
        if (this.hasBaseWithID(remoteID)) return this.getBaseWithID(remoteID);
        if (this.hasDroneWithID(remoteID)) return this.getDroneWithID(remoteID);
        if (this.hasVictimWithID(remoteID)) return this.getVictimWithID(remoteID);
        return null;
    }

    public double getUtility() {
        return this.utility;
    }

    public Set<String> getVictimIDs() {
        return this.victims.keySet();
    }

    public Collection<Remote> getVictims() {
        return this.victims.values();
    }

    public Remote getVictimWithID(String remoteID) {
        if (!this.hasVictimWithID(remoteID)) return null;
        return this.victims.get(remoteID);
    }

    public double getVictimStopProbability() {
        return this.victimStopProbability;
    }

    public boolean hasBases() {
        return !this.bases.isEmpty();
    }

    public boolean hasBaseWithID(String remoteID) {
        return this.bases.containsKey(remoteID);
    }

    public boolean hasConnectionBetween(String remoteID, String other) {
        if (!(this.hasRemoteWithID(remoteID) && this.hasRemoteWithID(other))) return false;
        if (!this.getRemoteWithID(remoteID).hasConnectionTo(other)) return false;
        if (!this.getRemoteWithID(other).hasConnectionTo(remoteID)) return false;
        return true;
    }

    public boolean hasDroneBluetoothDiscoveryProbability() {
        return this.droneBluetoothDiscoveryProbability > 0;
    }

    public boolean hasDrones() {
        return !this.drones.isEmpty();
    }

    public boolean hasDroneVisionDiscoveryProbability() {
        return this.droneVisionDiscoveryProbability > 0;
    }

    public boolean hasDroneWithID(String remoteID) {
        return this.drones.containsKey(remoteID);
    }

    public boolean hasHomeLocation() {
        return this.homeLocation != null;
    }

    public boolean hasRemotes() {
        return this.hasBases() || this.hasDrones() || this.hasVictims();
    }

    public boolean hasRemoteWithID(String remoteID) {
        return this.hasBaseWithID(remoteID) || this.hasDroneWithID(remoteID) || this.hasVictimWithID(remoteID);
    }

    public boolean hasVictims() {
        return !this.victims.isEmpty();
    }

    public boolean hasVictimStopProbability() {
        return this.victimStopProbability > 0;
    }

    public boolean hasVictimWithID(String remoteID) {
        return this.victims.containsKey(remoteID);
    }

    public boolean removeBases() {
        this.bases = new HashMap<>();
        return true;
    }

    public boolean removeBaseWithID(String remoteID) {
        if (!this.hasBaseWithID(remoteID)) return true;
        this.bases.remove(remoteID);
        return true;
    }

    public boolean removeConnectionBetween(String remoteID, String other) {
        if (!(this.hasRemoteWithID(remoteID) && this.hasRemoteWithID(other))) return true;
        this.getRemoteWithID(remoteID).removeConnectionTo(other);
        this.getRemoteWithID(other).removeConnectionTo(remoteID);
        return true;
    }

    public boolean removeDrones() {
        this.drones = new HashMap<>();
        return true;
    }

    public boolean removeDroneWithID(String remoteID) {
        if (!this.hasDroneWithID(remoteID)) return true;
        this.drones.remove(remoteID);
        return true;
    }

    public boolean removeRemotes() {
        this.removeBases();
        this.removeDrones();
        this.removeVictims();
        return true;
    }

    public boolean removeRemoteWithID(String remoteID) {
        if (this.hasBaseWithID(remoteID)) return this.removeBaseWithID(remoteID);
        if (this.hasDroneWithID(remoteID)) return this.removeDroneWithID(remoteID);
        if (this.hasVictimWithID(remoteID)) return this.removeVictimWithID(remoteID);
        return true;
    }

    public boolean removeVictims() {
        this.victims = new HashMap<>();
        return true;
    }

    public boolean removeVictimWithID(String remoteID) {
        if (!this.hasVictimWithID(remoteID)) return true;
        this.victims.remove(remoteID);
        return true;
    }

    public boolean reserveConnectionBetween(String remoteID, String other, boolean remoteCanHaveMultipleConnections) {
        if (!(this.hasRemoteWithID(remoteID) && this.hasRemoteWithID(other))) return false;
        Remote remote = this.getRemoteWithID(remoteID);
        Remote otherRemote = this.getRemoteWithID(other);
        if (remote.hasConnectionTo(other) && otherRemote.hasConnectionTo(remoteID)) return true;
        if (otherRemote.hasConnectionTo(remoteID)) {
            if (remote.hasConnections() && !remoteCanHaveMultipleConnections) {
                otherRemote.removeConnectionTo(remoteID);
                return false;
            }
            remote.addConnectionTo(other);
            return true;
        }
        if (remote.hasConnectionTo(other)) remote.removeConnectionTo(other);
        if (otherRemote.hasConnections()) return false;
        if (remote.hasConnections() && !remoteCanHaveMultipleConnections) return false;
        remote.addConnectionTo(other);
        otherRemote.addConnectionTo(remoteID);
        return true;
    }

    public void setDroneBluetoothDiscoveryProbability(double p) {
        this.droneBluetoothDiscoveryProbability = p;
    }

    public void setDroneVisionDiscoveryProbability(double p) {
        this.droneVisionDiscoveryProbability = p;
    }

    public void setHomeLocation(Vector homeLocation) {
        this.homeLocation = homeLocation;
    }

    public void setRandomSeed(long seed) {
        this.rng = new Random(seed);
    }

    public void setUtility(double utility) {
        this.utility = utility;
    }

    public void setVictimStopProbability(double p) {
        if (p < 0 || 1 < p) return;
        this.victimStopProbability = p;
    }

}
