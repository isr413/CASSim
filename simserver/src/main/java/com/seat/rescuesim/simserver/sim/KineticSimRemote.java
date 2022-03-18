package com.seat.rescuesim.simserver.sim;

import java.util.ArrayList;

import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.remote.KineticRemoteSpec;
import com.seat.rescuesim.common.remote.KineticRemoteState;
import com.seat.rescuesim.common.remote.intent.Intent;
import com.seat.rescuesim.common.remote.intent.Intention;
import com.seat.rescuesim.common.remote.intent.GotoIntention;
import com.seat.rescuesim.common.remote.intent.MoveIntention;
import com.seat.rescuesim.common.util.Debugger;

public abstract class KineticSimRemote extends SimRemote {

    protected Vector acceleration;
    protected Vector velocity;

    public KineticSimRemote(String label, KineticRemoteSpec spec, Vector location) {
        super(label, spec, location);
        this.velocity = new Vector();
        this.acceleration = new Vector();
    }

    public KineticSimRemote(String label, KineticRemoteSpec spec, Vector location, double battery) {
        super(label, spec, location, battery);
        this.velocity = new Vector();
        this.acceleration = new Vector();
    }

    public KineticSimRemote(String label, KineticRemoteSpec spec, Vector location, double battery, Vector velocity,
            Vector acceleration) {
        super(label, spec, location, battery);
        this.velocity = velocity;
        this.acceleration = acceleration;
    }

    public Vector getAcceleration() {
        return this.acceleration;
    }

    public Vector getNextLocation(double stepSize) {
        return this.getNextLocation(new Vector(), stepSize);
    }

    public Vector getNextLocation(Vector jerk, double stepSize) {
        if (this.getSpec().getMaxJerk() < jerk.getMagnitude()) {
            jerk = Vector.scale(jerk.getUnitVector(), this.getSpec().getMaxJerk());
        }
        Vector nextAcceleration = Vector.add(this.acceleration, Vector.scale(jerk, stepSize));
        if (this.getSpec().getMaxAcceleration() < nextAcceleration.getMagnitude()) {
            nextAcceleration = Vector.scale(nextAcceleration.getUnitVector(), this.getSpec().getMaxAcceleration());
        }
        Vector nextVelocity = Vector.add(this.velocity, Vector.scale(this.acceleration, stepSize));
        if (this.getSpec().getMaxVelocity() < nextVelocity.getMagnitude()) {
            nextVelocity = Vector.scale(nextVelocity.getUnitVector(), this.getSpec().getMaxVelocity());
        }
        return Vector.add(this.location, Vector.scale(this.velocity, stepSize));
    }

    public KineticRemoteSpec getSpec() {
        return (KineticRemoteSpec) this.spec;
    }

    public abstract KineticRemoteState getState();

    public Vector getVelocity() {
        return this.velocity;
    }

    @Override
    public boolean isKinetic() {
        return true;
    }

    public void setAcceleration(Vector acceleration) {
        if (this.getSpec().getMaxAcceleration() < acceleration.getMagnitude()) {
            Debugger.logger.err(String.format("Acceleration exceeds max acceleration of remote %s",
                this.getRemoteID()));
        }
        this.acceleration = acceleration;
    }

    public void setVelocity(Vector velocity) {
        if (this.getSpec().getMaxVelocity() < velocity.getMagnitude()) {
            Debugger.logger.err(String.format("Velocity exceeds max velocity of remote %s", this.getRemoteID()));
        }
        this.velocity = velocity;
    }

    @Override
    public void update(ArrayList<Intention> intentions, double stepSize) throws SimException {
        super.update(intentions, stepSize);
        if (intentions == null || intentions.isEmpty() ||
                (intentions.size() == 1 && intentions.get(0).equals(Intent.None()))) {
            this.updateVelocity(this.acceleration, stepSize);
            this.updateLocation(this.velocity, stepSize);
            return;
        }
        for (Intention intent : intentions) {
            switch (intent.getIntentionType()) {
                case DONE:
                case STOP:
                    this.updateVelocity(this.acceleration, stepSize);
                    this.updateLocation(this.velocity, stepSize);
                    continue;
                case SHUTDOWN:
                    this.updateVelocityTo(new Vector(), stepSize);
                    this.updateLocation(this.velocity, stepSize);
                    continue;
                case MOVE:
                    this.updateAcceleration(((MoveIntention) intent).getJerk(), stepSize);
                    this.updateVelocity(this.acceleration, stepSize);
                    this.updateLocation(this.velocity, stepSize);
                    continue;
                case GOTO:
                    this.updateLocationTo(
                        ((GotoIntention) intent).getLocation(),
                        ((GotoIntention) intent).getMaxVelocity(),
                        ((GotoIntention) intent).getMaxAcceleration()
                    );
                    continue;
                default:
                    continue;
            }
        }
    }

    protected void updateAcceleration(Vector delta, double stepSize) {
        if (this.getSpec().getMaxJerk() < delta.getMagnitude()) {
            delta = Vector.scale(delta.getUnitVector(), this.getSpec().getMaxJerk());
        }
        this.acceleration = Vector.add(this.acceleration, Vector.scale(delta, stepSize));
        if (this.getSpec().getMaxAcceleration() < this.acceleration.getMagnitude()) {
            this.acceleration = Vector.scale(this.acceleration.getUnitVector(), this.getSpec().getMaxAcceleration());
        }
    }

    protected void updateAccelerationTo(Vector target, double stepSize) {
        this.updateAccelerationTo(target, stepSize, Double.POSITIVE_INFINITY);
    }

    protected void updateAccelerationTo(Vector target, double stepSize, double maxJerk) {
        Vector jerk = Vector.subtract(target, this.acceleration);
        if (maxJerk < jerk.getMagnitude()) {
            jerk = Vector.scale(jerk.getUnitVector(), maxJerk);
        }
        this.updateAcceleration(jerk, stepSize);
    }

    protected void updateLocationTo(Vector dest, double stepSize) {
        this.updateLocationTo(dest, stepSize, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY,
            Double.POSITIVE_INFINITY);
    }

    protected void updateLocationTo(Vector dest, double stepSize, double maxVelocity) {
        this.updateLocationTo(dest, stepSize, maxVelocity, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    protected void updateLocationTo(Vector dest, double stepSize, double maxVelocity, double maxAcceleration) {
        this.updateLocationTo(dest, stepSize, maxVelocity, maxAcceleration, Double.POSITIVE_INFINITY);
    }

    protected void updateLocationTo(Vector dest, double stepSize, double maxVelocity, double maxAcceleration,
            double maxJerk) {
        Vector jerk = Vector.subtract(location, this.getNextLocation(stepSize));
        if (maxJerk < jerk.getMagnitude()) {
            jerk = Vector.scale(jerk.getUnitVector(), maxJerk);
        }
        this.updateAcceleration(jerk, stepSize);
        if (maxAcceleration < this.acceleration.getMagnitude()) {
            this.acceleration = Vector.scale(this.acceleration.getUnitVector(), maxAcceleration);
        }
        this.updateVelocity(this.acceleration, stepSize);
        if (maxVelocity < this.velocity.getMagnitude()) {
            this.velocity = Vector.scale(this.velocity.getUnitVector(), maxVelocity);
        }
        this.updateLocation(this.velocity, stepSize);
    }

    protected void updateVelocity(Vector delta, double stepSize) {
        if (this.getSpec().getMaxAcceleration() < delta.getMagnitude()) {
            delta = Vector.scale(delta.getUnitVector(), this.getSpec().getMaxAcceleration());
        }
        this.velocity = Vector.add(this.velocity, Vector.scale(delta, stepSize));
        if (this.getSpec().getMaxVelocity() < this.velocity.getMagnitude()) {
            this.velocity = Vector.scale(this.velocity.getUnitVector(), this.getSpec().getMaxVelocity());
        }
    }

    protected void updateVelocityTo(Vector target, double stepSize) {
        this.updateVelocityTo(target, stepSize, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    protected void updateVelocityTo(Vector target, double stepSize, double maxAcceleration) {
        this.updateVelocityTo(target, stepSize, maxAcceleration, Double.POSITIVE_INFINITY);
    }

    protected void updateVelocityTo(Vector target, double stepSize, double maxAcceleration, double maxJerk) {
        Vector jerk = Vector.subtract(target, Vector.add(this.velocity, this.acceleration));
        if (maxJerk < jerk.getMagnitude()) {
            jerk = Vector.scale(jerk.getUnitVector(), maxJerk);
        }
        this.updateAcceleration(jerk, stepSize);
        if (maxAcceleration < this.acceleration.getMagnitude()) {
            this.acceleration = Vector.scale(this.acceleration.getUnitVector(), maxAcceleration);
        }
        this.updateVelocity(this.acceleration, stepSize);
    }

}
