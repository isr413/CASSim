package com.seat.rescuesim.simserver.sim.sensor;

import java.util.HashSet;
import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.sensor.SensorSpec;
import com.seat.rescuesim.common.sensor.VisionSensorState;
import com.seat.rescuesim.common.util.Debugger;
import com.seat.rescuesim.simserver.sim.SimException;
import com.seat.rescuesim.simserver.sim.SimScenario;
import com.seat.rescuesim.simserver.sim.remote.SimRemote;

public class SimVision extends SimSensor {

    private HashSet<String> observations;

    public SimVision(SensorSpec spec, String label) {
        super(spec, label);
        this.observations = new HashSet<>();
    }

    public boolean addObservations(HashSet<String> remoteIDs) {
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

    public HashSet<String> getObservations() {
        return this.observations;
    }

    public VisionSensorState getState() {
        if (this.isActive()) {
            return new VisionSensorState(this.getSensorType(), this.getSensorID(), this.isActive(), this.observations);
        } else {
            return new VisionSensorState(this.getSensorType(), this.getSensorID());
        }
    }

    public boolean hasObservations() {
        return !this.observations.isEmpty();
    }

    public boolean hasObservationWithID(String remoteID) {
        return this.observations.contains(remoteID);
    }

    public boolean removeObservations(HashSet<String> remoteIDs) {
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
    public void update(SimScenario scenario, SimRemote remote, double stepSize) throws SimException {
        super.update(scenario, remote, stepSize);
        if (remote.isInactive() || remote.isDone() || !this.hasRange()) {
            if (this.hasObservations()) {
                this.clearObservations();
            }
            return;
        }
        if (!this.hasLimitedRange()) {
            if (this.observations.size()+1 >= scenario.getRemotes().size()) {
                return;
            }
            for (String remoteID : scenario.getRemotes()) {
                if (remote.getRemoteID().equals(remoteID)) {
                    continue;
                }
                if (!this.hasObservationWithID(remoteID)) {
                    this.addObservationWithID(remoteID);
                }
            }
            return;
        }
        for (String remoteID : scenario.getRemotes()) {
            if (remoteID.equals(remote.getRemoteID())) {
                continue;
            }
            Vector location = scenario.getRemoteWithID(remoteID).getLocation();
            if (Vector.dist(remote.getLocation(), location) <= this.getRange()) {
                if (!this.hasObservationWithID(remoteID)) {
                    this.addObservationWithID(remoteID);
                }
            } else if (this.hasObservationWithID(remoteID)) {
                this.removeObservationWithID(remoteID);
            }
        }
    }

}
