package com.seat.rescuesim.simserver.sim.sensor;

import com.seat.rescuesim.common.sensor.MonitorSensorState;
import com.seat.rescuesim.common.sensor.SensorSpec;
import com.seat.rescuesim.simserver.sim.SimScenario;
import com.seat.rescuesim.simserver.sim.remote.SimRemote;
import com.seat.rescuesim.simserver.sim.util.SimException;

public class SimMonitor extends SimSensor {

    private String monitorID;

    public SimMonitor(SensorSpec spec, String label) {
        super(spec, label);
        this.monitorID = "";
    }

    public void clearMonitor() {
        this.monitorID = "";
    }

    public String getMonitorID() {
        return this.monitorID;
    }

    public MonitorSensorState getState() {
        if (this.isActive()) {
            return new MonitorSensorState(this.getSensorType(), this.getSensorID(), this.isActive(), this.monitorID);
        } else {
            return new MonitorSensorState(this.getSensorType(), this.getSensorID());
        }
    }

    public boolean hasRemoteID() {
        return !(this.monitorID == null || this.monitorID.isEmpty() || this.monitorID.isBlank());
    }

    public void setMonitorID(String monitorID) {
        this.monitorID = monitorID;
    }

    @Override
    public void update(SimScenario scenario, SimRemote remote, double stepSize) throws SimException {
        super.update(scenario, remote, stepSize);
        if (remote.isInactive() || remote.isDone()) {
            this.clearMonitor();
            return;
        }
        this.setMonitorID(remote.getRemoteID());
    }

}
