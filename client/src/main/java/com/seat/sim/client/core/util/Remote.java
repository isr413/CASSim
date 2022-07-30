package com.seat.sim.client.core.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.seat.sim.common.math.Vector;
import com.seat.sim.common.math.Zone;
import com.seat.sim.common.remote.RemoteState;

public class Remote {

    private List<Zone> assignments;
    private Set<String> connections;
    private Zone connectionZone;
    private String remoteID;
    private RemoteState remoteState;

    public Remote(String remoteID) {
        this.remoteID = remoteID;
        this.assignments = new ArrayList<>();
        this.connections = new HashSet<>();
        this.connectionZone = null;
        this.remoteState = null;
    }

    public boolean addAssignment(Zone zone) {
        if (this.hasAssignment(zone)) return true;
        this.assignments.add(zone);
        return true;
    }

    public boolean addConnections(Collection<String> others) {
        this.connections.addAll(others);
        return true;
    }

    public boolean addConnectionTo(String other) {
        if (this.hasConnectionTo(other)) return true;
        this.connections.add(other);
        return true;
    }

    public Zone getAssignment() {
        if (!this.hasAssignment()) return null;
        return this.assignments.get(0);
    }

    public List<Zone> getAssignments() {
        return this.assignments;
    }

    public Set<String> getConnections() {
        return this.connections;
    }

    public Zone getConnectionZone() {
        return this.connectionZone;
    }

    public Vector getLocation() {
        return (this.hasLocation()) ? this.remoteState.getLocation() : null;
    }

    public Zone getNextAssignment() {
        if (!this.hasNextAssignment()) return null;
        return this.assignments.get(1);
    }

    public String getRemoteID() {
        return this.remoteID;
    }

    public RemoteState getRemoteState() {
        return this.remoteState;
    }

    public boolean hasAssignment() {
        return !this.assignments.isEmpty();
    }

    public boolean hasAssignment(Zone zone) {
        return this.assignments.contains(zone);
    }

    public boolean hasConnections() {
        return !this.connections.isEmpty();
    }

    public boolean hasConnectionTo(String other) {
        return this.connections.contains(other);
    }

    public boolean hasConnectionZone() {
        return this.connectionZone != null;
    }

    public boolean hasLocation() {
        return this.hasRemoteState() && this.remoteState.hasLocation();
    }

    public boolean hasNextAssignment() {
        return this.assignments.size() >= 2;
    }

    public boolean hasRemoteState() {
        return this.remoteState != null;
    }

    public boolean isActive() {
        return this.hasRemoteState() && this.remoteState.isActive();
    }

    public boolean isActiveAndOperational() {
        return this.isActive() && this.isOperational();
    }

    public boolean isEnabled() {
        return this.hasRemoteState() && this.remoteState.isEnabled();
    }

    public boolean isMobile() {
        return this.hasRemoteState() && this.remoteState.isMobile();
    }

    public boolean isOperational() {
        return this.isEnabled() && this.hasLocation();
    }

    public Zone removeAssignment() {
        if (!this.hasAssignment()) return null;
        return this.assignments.remove(0);
    }

    public boolean removeAssignments() {
        this.assignments = new ArrayList<>();
        return true;
    }

    public boolean removeConnections() {
        this.connections = new HashSet<>();
        this.connectionZone = null;
        return true;
    }

    public boolean removeConnectionTo(String other) {
        if (!this.hasConnectionTo(other)) return true;
        this.connections.remove(other);
        if (!this.hasConnections()) this.connectionZone = null;
        return true;
    }

    public boolean removeConnectionsTo(Collection<String> others) {
        for (String other : others) {
            this.removeConnectionTo(other);
        }
        return true;
    }

    public void setConnectionZone(Zone zone) {
        this.connectionZone = zone;
    }

    public void update(RemoteState state) {
        this.remoteState = state;
    }

}
