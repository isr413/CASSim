package com.seat.sim.simserver.sensor;

import java.util.Collection;
import java.util.HashSet;

import com.seat.sim.common.math.Vector;
import com.seat.sim.common.sensor.comms.CommsSensorProto;
import com.seat.sim.common.sensor.comms.CommsSensorState;
import com.seat.sim.common.util.Debugger;
import com.seat.sim.simserver.core.SimException;
import com.seat.sim.simserver.remote.Remote;
import com.seat.sim.simserver.scenario.SimScenario;

public class CommsSensor extends Sensor {

    private HashSet<String> connections;

    public CommsSensor(CommsSensorProto proto, String sensorID, boolean active) {
        super(proto, sensorID, active);
        this.connections = new HashSet<>();
    }

    public boolean addConnections(Collection<String> remoteIDs) {
        boolean flag = true;
        for (String remoteID : remoteIDs) {
            flag = this.addConnectionWithID(remoteID) && flag;
        }
        return flag;
    }

    public boolean addConnectionWithID(String remoteID) {
        if (this.hasConnectionWithID(remoteID)) {
            Debugger.logger.warn(String.format("Sensor %s is already connected to %s", this.getSensorID(), remoteID));
            return true;
        }
        this.connections.add(remoteID);
        return true;
    }

    public void clearConnections() {
        this.connections = new HashSet<>();
    }

    public Collection<String> getConnections() {
        return this.connections;
    }

    public double getDelay() {
        return this.getProto().getDelay();
    }

    @Override
    public CommsSensorProto getProto() {
        return (CommsSensorProto) super.getProto();
    }

    @Override
    public CommsSensorState getState() {
        return new CommsSensorState(this.getSensorModel(), this.getSensorID(), this.isActive(), this.connections);
    }

    public boolean hasConnections() {
        return !this.connections.isEmpty();
    }

    public boolean hasConnectionWithID(String remoteID) {
        return this.connections.contains(remoteID);
    }

    public boolean hasDelay() {
        return this.getProto().hasDelay();
    }

    public boolean removeConnections(Collection<String> remoteIDs) {
        boolean flag = true;
        for (String remoteID : remoteIDs) {
            flag = this.removeConnectionWithID(remoteID) && flag;
        }
        return flag;
    }

    public boolean removeConnectionWithID(String remoteID) {
        if (!this.hasConnectionWithID(remoteID)) {
            Debugger.logger.warn(String.format("Sensor %s is not connected to %s", this.getSensorID(), remoteID));
            return true;
        }
        this.connections.remove(remoteID);
        return true;
    }

    @Override
    public void update(SimScenario scenario, Remote remote, double stepSize) throws SimException {
        super.update(scenario, remote, stepSize);
        if (this.isInactive() || remote.isInactive() || !this.hasRange()) {
            if (this.hasConnections()) {
                this.clearConnections();
            }
            return;
        }
        for (String remoteID : scenario.getRemoteIDs()) {
            if (remoteID.equals(remote.getRemoteID())) {
                continue;
            }
            Vector location = scenario.getRemoteWithID(remoteID).getLocation();
            if ((this.hasUnlimitedRange() || Vector.dist(remote.getLocation(), location) <= this.getRange()) &&
                    scenario.getRemoteWithID(remoteID).hasSensorWithModel(this.getSensorModel())) {
                if (!this.hasConnectionWithID(remoteID)) {
                    this.connections.add(remoteID);
                }
            } else if (this.hasConnectionWithID(remoteID)) {
                this.removeConnectionWithID(remoteID);
            }
        }
    }

}
