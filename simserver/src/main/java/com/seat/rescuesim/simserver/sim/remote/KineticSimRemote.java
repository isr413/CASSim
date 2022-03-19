package com.seat.rescuesim.simserver.sim.remote;

import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.remote.KineticRemoteSpec;
import com.seat.rescuesim.common.remote.KineticRemoteState;
import com.seat.rescuesim.common.remote.RemoteController;
import com.seat.rescuesim.common.remote.intent.IntentionType;
import com.seat.rescuesim.common.remote.intent.GotoIntention;
import com.seat.rescuesim.common.remote.intent.MoveIntention;
import com.seat.rescuesim.simserver.sim.SimException;

public abstract class KineticSimRemote extends SimRemote {

    protected Vector acceleration;
    protected Vector velocity;

    public KineticSimRemote(KineticRemoteSpec spec, String label) {
        super(spec, label);
        this.velocity = new Vector();
        this.acceleration = new Vector();
    }

    public Vector getAcceleration() {
        return this.acceleration;
    }

    public double getMaxAcceleration() {
        return this.getSpec().getMaxAcceleration();
    }

    public double getMaxJerk() {
        return this.getSpec().getMaxJerk();
    }

    public double getMaxVelocity() {
        return this.getSpec().getMaxVelocity();
    }

    public Vector getNextLocation(double stepSize) {
        return this.getNextLocation(new Vector(), stepSize);
    }

    public Vector getNextLocation(Vector jerk, double stepSize) {
        if (this.getMaxJerk() < jerk.getMagnitude()) {
            jerk = Vector.scale(jerk.getUnitVector(), this.getMaxJerk());
        }
        Vector nextAcceleration = Vector.add(this.acceleration, Vector.scale(jerk, stepSize));
        if (this.getMaxAcceleration() < nextAcceleration.getMagnitude()) {
            nextAcceleration = Vector.scale(nextAcceleration.getUnitVector(), this.getMaxAcceleration());
        }
        Vector nextVelocity = Vector.add(this.velocity, Vector.scale(this.acceleration, stepSize));
        if (this.getMaxVelocity() < nextVelocity.getMagnitude()) {
            nextVelocity = Vector.scale(nextVelocity.getUnitVector(), this.getMaxVelocity());
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

    public void setAcceleration(Vector acceleration) throws SimException {
        if (this.getSpec().getMaxAcceleration() < acceleration.getMagnitude()) {
            throw new SimException(String.format("Remote %s cannot set acceleration to %s", this.getRemoteID(),
                acceleration.toString()));
        }
        this.acceleration = acceleration;
    }

    public void setVelocity(Vector velocity) throws SimException {
        if (this.getSpec().getMaxVelocity() < velocity.getMagnitude()) {
            throw new SimException(String.format("Remote %s cannot set velocity to %s", this.getRemoteID(),
                velocity.toString()));
        }
        this.velocity = velocity;
    }

    @Override
    public void update(RemoteController controller, double stepSize) throws SimException {
        super.update(controller, stepSize);
        if (this.isInactive() || this.isDone()) {
            this.updateVelocityTo(new Vector(), stepSize);
            this.updateLocation(this.velocity, stepSize);
            return;
        }
        if (controller != null) {
            if (controller.hasIntentionWithType(IntentionType.DONE) ||
                    controller.hasIntentionWithType(IntentionType.SHUTDOWN) ||
                    controller.hasIntentionWithType(IntentionType.STOP)) {
                this.updateVelocityTo(new Vector(), stepSize);
                this.updateLocation(this.velocity, stepSize);
            } else if (controller.hasIntentionWithType(IntentionType.GOTO)) {
                GotoIntention intent = (GotoIntention) controller.getIntentionWithType(IntentionType.GOTO);
                this.updateLocationTo(intent.getLocation(), intent.getMaxVelocity(), intent.getMaxAcceleration(),
                    intent.getMaxJerk());
            } else if (controller.hasIntentionWithType(IntentionType.MOVE)) {
                MoveIntention intent = (MoveIntention) controller.getIntentionWithType(IntentionType.MOVE);
                this.updateAcceleration(intent.getJerk(), stepSize);
                this.updateVelocity(this.acceleration, stepSize);
                this.updateLocation(this.velocity, stepSize);
            } else {
                this.updateVelocity(this.acceleration, stepSize);
                this.updateLocation(this.velocity, stepSize);
            }
        } else {
            this.updateVelocity(this.acceleration, stepSize);
            this.updateLocation(this.velocity, stepSize);
        }
    }

    protected void updateAcceleration(Vector delta, double stepSize) {
        if (this.getMaxJerk() < delta.getMagnitude()) {
            delta = Vector.scale(delta.getUnitVector(), this.getMaxJerk());
        }
        this.acceleration = Vector.add(this.acceleration, Vector.scale(delta, stepSize));
        if (this.getMaxAcceleration() < this.acceleration.getMagnitude()) {
            this.acceleration = Vector.scale(this.acceleration.getUnitVector(), this.getMaxAcceleration());
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
        if (this.getMaxAcceleration() < delta.getMagnitude()) {
            delta = Vector.scale(delta.getUnitVector(), this.getMaxAcceleration());
        }
        this.velocity = Vector.add(this.velocity, Vector.scale(delta, stepSize));
        if (this.getMaxVelocity() < this.velocity.getMagnitude()) {
            this.velocity = Vector.scale(this.velocity.getUnitVector(), this.getMaxVelocity());
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
