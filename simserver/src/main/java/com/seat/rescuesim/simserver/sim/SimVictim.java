package com.seat.rescuesim.simserver.sim;

import java.util.ArrayList;

import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.remote.intent.Intention;
import com.seat.rescuesim.common.victim.VictimSpec;
import com.seat.rescuesim.simserver.util.Random;

public class SimVictim {

    private Vector acceleration;
    private String label;
    private Vector location;
    private Random rng;
    private VictimSpec spec;
    private Vector velocity;

    public SimVictim(String label, VictimSpec spec, Vector location) {
        this(label, spec, new Random(), location, new Vector());
    }

    public SimVictim(String label, VictimSpec spec, Random rng, Vector location) {
        this(label, spec, rng, location, new Vector());
    }

    public SimVictim(String label, VictimSpec spec, Vector location, Vector velocity) {
        this(label, spec, new Random(), location, velocity);
    }

    public SimVictim(String label, VictimSpec spec, Random rng, Vector location, Vector velocity) {
        this.label = label;
        this.spec = spec;
        this.rng = rng;
        this.location = location;
        this.velocity = velocity;
        this.acceleration = new Vector();
    }

    public Vector getAcceleration() {
        return this.acceleration;
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

    public Vector getVelocity() {
        return this.velocity;
    }

    public String getVictimID() {
        return this.getLabel();
    }

    public void update(double stepSize) {
        this.update(null, stepSize);
    }

    public void update(ArrayList<Intention> intentions, double stepSize) {
        if (intentions == null || intentions.isEmpty()) {
            this.velocity = Vector.add(this.velocity, Vector.scale(this.acceleration, stepSize));
            this.location = Vector.add(this.location, this.velocity);
            return;
        }
    }

}
