package com.seat.rescuesim.simserver.sensor;

import com.seat.rescuesim.common.sensor.SensorProto;
import com.seat.rescuesim.common.sensor.SensorState;
import com.seat.rescuesim.simserver.core.SimException;
import com.seat.rescuesim.simserver.remote.Remote;
import com.seat.rescuesim.simserver.scenario.SimScenario;

public class Sensor {

    private boolean active;
    private SensorProto proto;
    private String sensorID;

    public Sensor(SensorProto proto, String sensorID, boolean active) {
        this.proto = proto;
        this.sensorID = sensorID;
        this.active = active;
    }

    public double getAccuracy() {
        return this.proto.getSensorAccuracy();
    }

    public double getBatteryUsage() {
        return this.proto.getBatteryUsage();
    }

    public String getLabel() {
        return this.getSensorID();
    }

    public SensorProto getProto() {
        return this.proto;
    }

    public double getRange() {
        return this.proto.getSensorRange();
    }

    public String getSensorID() {
        return this.sensorID;
    }

    public String getSensorType() {
        return this.proto.getSensorType();
    }

    public SensorState getState() {
        return new SensorState(this.sensorID, this.active);
    }

    public boolean hasAccuracy() {
        return this.proto.hasAccuracy();
    }

    public boolean hasBatteryUsage() {
        return this.proto.hasBatteryUsage();
    }

    public boolean hasImperfectAccuracy() {
        return this.proto.hasImperfectAccuracy();
    }

    public boolean hasLimitedRange() {
        return this.proto.hasLimitedRange();
    }

    public boolean hasRange() {
        return this.proto.hasRange();
    }

    public boolean hasUnlimitedRange() {
        return this.proto.hasUnlimitedRange();
    }

    public boolean isActive() {
        return this.active;
    }

    public boolean isInactive() {
        return !this.isActive();
    }

    public void setActive() {
        this.active = true;
    }

    public void setInactive() {
        this.active = false;
    }

    public String toString() {
        return this.getLabel();
    }

    public void update(SimScenario scenario, Remote remote, double stepSize) throws SimException {
        if (this.isActive() && remote.isInactive()) {
            this.setInactive();
        }
    }

    public boolean equals(Sensor sensor) {
        if (sensor == null) return false;
        return this.sensorID.equals(sensor.sensorID);
    }

}
