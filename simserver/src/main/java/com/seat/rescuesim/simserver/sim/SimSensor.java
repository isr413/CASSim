package com.seat.rescuesim.simserver.sim;

import com.seat.rescuesim.common.sensor.SensorSpec;
import com.seat.rescuesim.common.sensor.SensorType;

public class SimSensor {

    private String label;
    private SensorSpec spec;

    public SimSensor(String label, SensorSpec spec) {
        this.label = label;
        this.spec = spec;
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

    public SensorType getSensorType() {
        return this.spec.getSpecType();
    }

    public SensorSpec getSpec() {
        return this.spec;
    }

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

    public String toString() {
        return this.getLabel();
    }

    public boolean equals(SimSensor sensor) {
        return this.label.equals(sensor.label) && this.spec.equals(sensor.spec);
    }

}
