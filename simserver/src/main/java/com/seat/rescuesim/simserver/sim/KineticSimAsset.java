package com.seat.rescuesim.simserver.sim;

import java.util.ArrayList;

import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.remote.KineticRemoteSpec;
import com.seat.rescuesim.common.remote.KineticRemoteState;
import com.seat.rescuesim.common.remote.intent.Intention;

public abstract class KineticSimAsset extends SimAsset {

    protected Vector acceleration;
    protected Vector velocity;

    public KineticSimAsset(String label, KineticRemoteSpec spec, Vector location) {
        super(label, spec, location);
        this.velocity = new Vector();
        this.acceleration = new Vector();
    }

    public KineticSimAsset(String label, KineticRemoteSpec spec, Vector location, double battery) {
        super(label, spec, location, battery);
        this.velocity = new Vector();
        this.acceleration = new Vector();
    }

    public KineticSimAsset(String label, KineticRemoteSpec spec, Vector location, double battery, Vector velocity,
            Vector acceleration) {
        super(label, spec, location, battery);
        this.velocity = velocity;
        this.acceleration = acceleration;
    }

    public Vector getAcceleration() {
        return this.acceleration;
    }

    public Vector getNextLocation() {
        return this.getNextLocation(new Vector());
    }

    public Vector getNextLocation(Vector jerk) {
        if (this.getSpec().getMaxJerk() < jerk.getMagnitude()) {
            jerk = Vector.scale(jerk.getUnitVector(), this.getSpec().getMaxJerk());
        }
        Vector nextAcceleration = Vector.add(this.acceleration, jerk);
        if (this.getSpec().getMaxAcceleration() < nextAcceleration.getMagnitude()) {
            nextAcceleration = Vector.scale(nextAcceleration.getUnitVector(), this.getSpec().getMaxAcceleration());
        }
        Vector nextVelocity = Vector.add(this.velocity, nextAcceleration);
        if (this.getSpec().getMaxVelocity() < nextVelocity.getMagnitude()) {
            nextVelocity = Vector.scale(nextVelocity.getUnitVector(), this.getSpec().getMaxVelocity());
        }
        return Vector.add(this.location, nextVelocity);
    }

    public KineticRemoteSpec getSpec() {
        return (KineticRemoteSpec) this.spec;
    }

    public abstract KineticRemoteState getState();

    public Vector getVelocity() {
        return this.velocity;
    }

    public void setAcceleration(Vector acceleration) {
        this.acceleration = acceleration;
    }

    public void setVelocity(Vector velocity) {
        this.velocity = velocity;
    }

    public KineticRemoteState update(double stepSize) throws SimException {
        return this.update(null, stepSize);
    }

    public abstract KineticRemoteState update(ArrayList<Intention> intentions, double stepSize) throws SimException;

}
