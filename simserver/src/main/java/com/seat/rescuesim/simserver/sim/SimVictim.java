package com.seat.rescuesim.simserver.sim;

import java.util.ArrayList;

import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.remote.intent.Intent;
import com.seat.rescuesim.common.remote.intent.Intention;
import com.seat.rescuesim.common.sensor.SensorState;
import com.seat.rescuesim.common.victim.VictimSpec;
import com.seat.rescuesim.common.victim.VictimState;

public class SimVictim {

    private Vector acceleration;
    private double battery;
    private String label;
    private Vector location;
    private VictimSpec spec;
    private Vector velocity;

    public SimVictim(String label, VictimSpec spec, Vector location) {
        this(label, spec, location, new Vector());
    }

    public SimVictim(String label, VictimSpec spec, Vector location, Vector velocity) {
        this.label = label;
        this.spec = spec;
        this.location = location;
        this.velocity = velocity;
        this.acceleration = new Vector();
        this.battery = this.spec.getMaxBatteryPower();
    }

    public Vector getAcceleration() {
        return this.acceleration;
    }

    public double getBattery() {
        return this.battery;
    }

    public String getLabel() {
        return this.label;
    }

    public Vector getLocation() {
        return this.location;
    }

    public Vector getNextLocation() {
        return this.getNextLocation(new Vector());
    }

    public Vector getNextLocation(Vector jerk) {
        if (this.spec.getMaxJerk() < jerk.getMagnitude()) {
            jerk = Vector.scale(jerk.getUnitVector(), this.spec.getMaxJerk());
        }
        Vector nextAcceleration = Vector.add(this.acceleration, jerk);
        if (this.spec.getMaxAcceleration() < nextAcceleration.getMagnitude()) {
            nextAcceleration = Vector.scale(nextAcceleration.getUnitVector(), this.spec.getMaxAcceleration());
        }
        Vector nextVelocity = Vector.add(this.velocity, nextAcceleration);
        if (this.spec.getMaxVelocity() < nextVelocity.getMagnitude()) {
            nextVelocity = Vector.scale(nextVelocity.getUnitVector(), this.spec.getMaxVelocity());
        }
        return Vector.add(this.location, nextVelocity);
    }

    public VictimSpec getSpec() {
        return this.spec;
    }

    public VictimState getState() {
        return new VictimState(
            this.spec.getSpecType(),
            this.label,
            this.location,
            this.battery,
            new ArrayList<SensorState>(),
            this.velocity,
            this.acceleration
        );
    }

    public Vector getVelocity() {
        return this.velocity;
    }

    public String getVictimID() {
        return this.getLabel();
    }

    public void setAcceleration(Vector acceleration) {
        this.acceleration = acceleration;
    }

    public void setBattery(double battery) {
        this.battery = battery;
    }

    public void setLocation(Vector location) {
        this.location = location;
    }

    public void setVelocity(Vector velocity) {
        this.velocity = velocity;
    }

    public VictimState update(double stepSize) {
        return this.update(null, stepSize);
    }

    public VictimState update(ArrayList<Intention> intentions, double stepSize) {
        if (intentions == null || intentions.isEmpty() ||
                (intentions.size() == 1 && intentions.get(0).equals(Intent.None()))) {
            this.velocity = Vector.add(this.velocity, Vector.scale(this.acceleration, stepSize));
            this.location = Vector.add(this.location, this.velocity);
        }
        return this.getState();
    }

}
