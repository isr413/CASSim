package com.seat.sim.server.sensor;

import com.seat.sim.common.sensor.monitor.MonitorSensorProto;
import com.seat.sim.common.sensor.monitor.MonitorSensorState;
import com.seat.sim.server.core.SimException;
import com.seat.sim.server.remote.Remote;
import com.seat.sim.server.scenario.Scenario;

public class MonitorSensor extends Sensor {

    private String monitorID;

    public MonitorSensor(MonitorSensorProto proto, String sensorID, boolean active) {
        super(proto, sensorID, active);
        this.monitorID = null;
    }

    public void clearMonitor() {
        this.monitorID = null;
    }

    public String getMonitorID() {
        return (this.hasMonitorID()) ? this.monitorID : "";
    }

    @Override
    public MonitorSensorProto getProto() {
        return (MonitorSensorProto) super.getProto();
    }

    @Override
    public MonitorSensorState getState() {
        return new MonitorSensorState(this.getSensorModel(), this.getSensorID(), this.isActive(), this.monitorID);
    }

    public boolean hasMonitorID() {
        return !(this.monitorID == null || this.monitorID.isEmpty() || this.monitorID.isBlank());
    }

    public void setMonitorID(String monitorID) {
        this.monitorID = monitorID;
    }

    @Override
    public void update(Scenario scenario, Remote remote, double stepSize) throws SimException {
        super.update(scenario, remote, stepSize);
        if (!this.isActive() || !remote.isActive()) {
            this.clearMonitor();
            return;
        }
        this.setMonitorID(remote.getRemoteID());
    }

}
