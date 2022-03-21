package com.seat.rescuesim.simserver.sim.sensor;

import com.seat.rescuesim.common.sensor.MonitorSensorState;
import com.seat.rescuesim.common.sensor.SensorSpec;
import com.seat.rescuesim.simserver.sim.SimException;
import com.seat.rescuesim.simserver.sim.SimScenario;
import com.seat.rescuesim.simserver.sim.remote.SimRemote;

public class SimMonitor extends SimSensor {

    private String remoteID;

    public SimMonitor(SensorSpec spec, String label) {
        super(spec, label);
        this.remoteID = "";
    }

    public void clearMonitor() {
        this.remoteID = "";
    }

    public String getRemoteID() {
        return this.remoteID;
    }

    public MonitorSensorState getState() {
        if (this.isActive()) {
            return new MonitorSensorState(this.getSensorType(), this.getSensorID(), this.isActive(), this.remoteID);
        } else {
            return new MonitorSensorState(this.getSensorType(), this.getSensorID());
        }
    }

    public boolean hasRemoteID() {
        return !(this.remoteID == null || this.remoteID.isEmpty() || this.remoteID.isBlank());
    }

    public void setRemoteID(String remoteID) {
        this.remoteID = remoteID;
    }

    @Override
    public void update(SimScenario scenario, SimRemote remote, double stepSize) throws SimException {
        super.update(scenario, remote, stepSize);
        if (remote.isInactive() || remote.isDone()) {
            this.clearMonitor();
            return;
        }
        this.setRemoteID(remote.getRemoteID());
    }

}
