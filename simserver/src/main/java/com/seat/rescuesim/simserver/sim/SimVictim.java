package com.seat.rescuesim.simserver.sim;

import java.util.ArrayList;

import com.seat.rescuesim.common.math.Vector;
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

    public VictimState update(double stepSize) {
        return this.update(null, stepSize);
    }

    public VictimState update(ArrayList<Intention> intentions, double stepSize) {
        if (intentions == null || intentions.isEmpty()) {
            this.velocity = Vector.add(this.velocity, Vector.scale(this.acceleration, stepSize));
            this.location = Vector.add(this.location, this.velocity);
        }
        return this.getState();
    }

}
