package com.seat.rescuesim.simserver.sim.sensor;

import com.seat.rescuesim.common.sensor.SensorSpec;
import com.seat.rescuesim.common.sensor.SensorState;
import com.seat.rescuesim.common.sensor.SensorType;
import com.seat.rescuesim.simserver.sim.SimException;
import com.seat.rescuesim.simserver.sim.SimScenario;
import com.seat.rescuesim.simserver.sim.remote.SimRemote;

public abstract class SimSensor {

    protected boolean active;
    protected String label;
    protected SensorSpec spec;

    public SimSensor(SensorSpec spec, String label) {
        this.spec = spec;
        this.label = label;
        this.active = false;
    }

    public double getAccuracy() {
        return this.spec.getSensorAccuracy();
    }

    public double getBatteryUsage() {
        return this.spec.getBatteryUsage();
    }

    public double getDelay() {
        return this.spec.getSensorDelay();
    }

    public String getLabel() {
        return this.label;
    }

    public double getRange() {
        return this.spec.getSensorRange();
    }

    public String getSensorID() {
        return this.getLabel();
    }

    public SensorType getSensorType() {
        return this.spec.getSpecType();
    }

    public SensorSpec getSpec() {
        return this.spec;
    }

    public abstract SensorState getState();

    public boolean hasAccuracy() {
        return this.spec.getSensorAccuracy() > 0;
    }

    public boolean hasBatteryUsage() {
        return this.spec.getBatteryUsage() > 0;
    }

    public boolean hasDelay() {
        return this.spec.getSensorDelay() > 0;
    }

    public boolean hasImperfectAccuracy() {
        return this.spec.getSensorAccuracy() < 1;
    }

    public boolean hasLimitedRange() {
        return this.spec.getSensorRange() != Double.POSITIVE_INFINITY;
    }

    public boolean hasRange() {
        return this.spec.getSensorRange() > 0;
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

    public void update(SimScenario scenario, SimRemote remote, double stepSize) throws SimException {
        if (this.isActive() && remote.isInactive()) {
            this.setInactive();
        } else if (this.isInactive() && remote.isActive()) {
            this.setActive();
        }
    }

}
