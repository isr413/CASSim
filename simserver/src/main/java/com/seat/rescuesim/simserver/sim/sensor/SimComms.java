package com.seat.rescuesim.simserver.sim.sensor;

import java.util.HashSet;
import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.sensor.CommsSensorState;
import com.seat.rescuesim.common.sensor.SensorSpec;
import com.seat.rescuesim.common.util.Debugger;
import com.seat.rescuesim.simserver.sim.SimException;
import com.seat.rescuesim.simserver.sim.SimScenario;
import com.seat.rescuesim.simserver.sim.remote.SimRemote;

public class SimComms extends SimSensor {

    private HashSet<String> connections;

    public SimComms(SensorSpec spec, String label) {
        super(spec, label);
        this.connections = new HashSet<>();
    }

    public boolean addConnections(HashSet<String> remoteIDs) {
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

    public HashSet<String> getConnections() {
        return this.connections;
    }

    public CommsSensorState getState() {
        if (this.isActive()) {
            return new CommsSensorState(this.getSensorType(), this.getSensorID(), this.isActive(), this.connections);
        } else {
            return new CommsSensorState(this.getSensorType(), this.getSensorID());
        }
    }

    public boolean hasConnections() {
        return !this.connections.isEmpty();
    }

    public boolean hasConnectionWithID(String remoteID) {
        return this.connections.contains(remoteID);
    }

    public boolean removeConnections(HashSet<String> remoteIDs) {
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
    public void update(SimScenario scenario, SimRemote remote, double stepSize) throws SimException {
        super.update(scenario, remote, stepSize);
        if (remote.isInactive() || remote.isDone() || !this.hasRange()) {
            if (this.hasConnections()) {
                this.clearConnections();
            }
            return;
        }
        if (!this.hasLimitedRange()) {
            for (String remoteID : scenario.getRemoteIDs()) {
                if (remote.getRemoteID().equals(remoteID)) {
                    continue;
                }
                if (!this.hasConnectionWithID(remoteID)) {
                    if (scenario.getRemoteWithID(remoteID).hasSensorWithType(this.getSensorType())) {
                        this.connections.add(remoteID);
                    }
                }
            }
            return;
        }
        for (String remoteID : scenario.getRemoteIDs()) {
            if (remoteID.equals(remote.getRemoteID())) {
                continue;
            }
            Vector location = scenario.getRemoteWithID(remoteID).getLocation();
            if (Vector.dist(remote.getLocation(), location) <= this.getRange() &&
                    scenario.getRemoteWithID(remoteID).hasSensorWithType(this.getSensorType())) {
                if (!this.hasConnectionWithID(remoteID)) {
                    this.connections.add(remoteID);
                }
            } else if (this.hasConnectionWithID(remoteID)) {
                this.removeConnectionWithID(remoteID);
            }
        }
    }

}
