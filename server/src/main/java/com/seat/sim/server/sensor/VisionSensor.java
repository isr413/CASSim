package com.seat.sim.server.sensor;

import java.util.Collection;
import java.util.HashSet;

import com.seat.sim.common.math.Vector;
import com.seat.sim.common.sensor.vision.VisionSensorProto;
import com.seat.sim.common.sensor.vision.VisionSensorState;
import com.seat.sim.common.util.Debugger;
import com.seat.sim.server.core.SimException;
import com.seat.sim.server.remote.Remote;
import com.seat.sim.server.scenario.Scenario;

public class VisionSensor extends Sensor {

    private HashSet<String> observations;

    public VisionSensor(VisionSensorProto proto, String sensorID, boolean active) {
        super(proto, sensorID, active);
        this.observations = new HashSet<>();
    }

    public boolean addObservations(Collection<String> remoteIDs) {
        boolean flag = true;
        for (String remoteID : remoteIDs) {
            flag = this.addObservationWithID(remoteID) && flag;
        }
        return flag;
    }

    public boolean addObservationWithID(String remoteID) {
        if (this.hasObservationWithID(remoteID)) {
            Debugger.logger.warn(String.format("Sensor %s already observes %s", this.getSensorID(), remoteID));
            return true;
        }
        this.observations.add(remoteID);
        return true;
    }

    public void clearObservations() {
        this.observations = new HashSet<>();
    }

    public Collection<String> getObservations() {
        return this.observations;
    }

    @Override
    public VisionSensorProto getProto() {
        return (VisionSensorProto) super.getProto();
    }

    @Override
    public VisionSensorState getState() {
        return new VisionSensorState(this.getSensorModel(), this.getSensorID(), this.isActive(), this.observations);
    }

    public boolean hasObservations() {
        return !this.observations.isEmpty();
    }

    public boolean hasObservationWithID(String remoteID) {
        return this.observations.contains(remoteID);
    }

    public boolean removeObservations(Collection<String> remoteIDs) {
        boolean flag = true;
        for (String remoteID : remoteIDs) {
            flag = this.removeObservationWithID(remoteID) && flag;
        }
        return flag;
    }

    public boolean removeObservationWithID(String remoteID) {
        if (!this.hasObservationWithID(remoteID)) {
            Debugger.logger.warn(String.format("Sensor %s does not observe %s", this.getSensorID(), remoteID));
            return true;
        }
        this.observations.remove(remoteID);
        return true;
    }

    @Override
    public void update(Scenario scenario, Remote remote, double stepSize) throws SimException {
        super.update(scenario, remote, stepSize);
        if (this.isInactive() || remote.isInactive() || !this.hasRange()) {
            if (this.hasObservations()) {
                this.clearObservations();
            }
            return;
        }
        for (String remoteID : scenario.getRemoteIDs()) {
            if (remoteID.equals(remote.getRemoteID())) {
                continue;
            }
            Vector location = scenario.getRemoteWithID(remoteID).getLocation();
            if (this.hasUnlimitedRange() || Vector.dist(remote.getLocation(), location) <= this.getRange()) {
                if (!this.hasObservationWithID(remoteID)) {
                    this.addObservationWithID(remoteID);
                }
            } else if (this.hasObservationWithID(remoteID)) {
                this.removeObservationWithID(remoteID);
            }
        }
    }

}
