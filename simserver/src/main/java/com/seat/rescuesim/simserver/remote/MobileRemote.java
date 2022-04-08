package com.seat.rescuesim.simserver.remote;

import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.remote.intent.GoToIntention;
import com.seat.rescuesim.common.remote.intent.IntentionSet;
import com.seat.rescuesim.common.remote.intent.IntentionType;
import com.seat.rescuesim.common.remote.intent.MoveIntention;
import com.seat.rescuesim.common.remote.mobile.MobileRemoteProto;
import com.seat.rescuesim.common.remote.mobile.MobileRemoteState;
import com.seat.rescuesim.simserver.core.SimException;
import com.seat.rescuesim.simserver.scenario.SimScenario;

public class MobileRemote extends Remote {

    private Vector acceleration;
    private Vector velocity;

    public MobileRemote(MobileRemoteProto proto, String remoteID, boolean active) {
        super(proto, remoteID, active);
        this.velocity = new Vector();
        this.acceleration = new Vector();
    }

    public Vector getAcceleration() {
        return this.acceleration;
    }

    @Override
    public MobileRemoteProto getProto() {
        return (MobileRemoteProto) super.getProto();
    }

    public double getMaxAcceleration() {
        return this.getProto().getMaxAcceleration();
    }

    public double getMaxJerk() {
        return this.getProto().getMaxJerk();
    }

    public double getMaxVelocity() {
        return this.getProto().getMaxVelocity();
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
        Vector nextVelocity = Vector.add(this.velocity, Vector.scale(nextAcceleration, stepSize));
        if (this.getMaxVelocity() < nextVelocity.getMagnitude()) {
            nextVelocity = Vector.scale(nextVelocity.getUnitVector(), this.getMaxVelocity());
        }
        return Vector.add(this.getLocation(), Vector.scale(nextVelocity, stepSize));
    }

    @Override
    public MobileRemoteState getState() {
        return new MobileRemoteState(this.getRemoteID(), this.getLocation(), this.getBattery(), this.isActive(),
            this.velocity, this.acceleration);
    }

    public Vector getVelocity() {
        return this.velocity;
    }

    public boolean hasAcceleration() {
        return this.acceleration != null && this.acceleration.getMagnitude() > 0;
    }

    public boolean hasVelocity() {
        return this.velocity != null && this.velocity.getMagnitude() > 0;
    }

    public boolean isInMotion() {
        return this.hasVelocity() || this.hasAcceleration();
    }

    @Override
    public boolean isMobile() {
        return true;
    }

    public boolean isStagnant() {
        return !this.isInMotion();
    }

    public void setAcceleration(Vector acceleration) throws SimException {
        if (this.getProto().getMaxAcceleration() < acceleration.getMagnitude()) {
            throw new SimException(String.format("Remote %s cannot set acceleration to %s", this.getRemoteID(),
                acceleration.toString()));
        }
        this.acceleration = acceleration;
    }

    public void setVelocity(Vector velocity) throws SimException {
        if (this.getProto().getMaxVelocity() < velocity.getMagnitude()) {
            throw new SimException(String.format("Remote %s cannot set velocity to %s", this.getRemoteID(),
                velocity.toString()));
        }
        this.velocity = velocity;
    }

    @Override
    public void update(SimScenario scenario, IntentionSet intentions, double stepSize) throws SimException {
        super.update(scenario, intentions, stepSize);
        if (this.isDisabled() || this.isInactive() || this.isDone()) {
            if (this.isInMotion()) {
                this.updateVelocityTo(new Vector(), stepSize);
                this.updateLocation(this.velocity, stepSize);
            }
            return;
        }
        if (intentions == null) {
            this.updateVelocity(this.acceleration, stepSize);
            this.updateLocation(this.velocity, stepSize);
            return;
        }
        if (intentions.hasIntentionWithType(IntentionType.DONE) ||
                intentions.hasIntentionWithType(IntentionType.SHUTDOWN) ||
                intentions.hasIntentionWithType(IntentionType.STOP)) {
            this.updateVelocityTo(new Vector(), stepSize);
            this.updateLocation(this.velocity, stepSize);
        } else if (intentions.hasIntentionWithType(IntentionType.GOTO)) {
            GoToIntention intent = (GoToIntention) intentions.getIntentionWithType(IntentionType.GOTO);
            this.updateLocationTo(intent.getLocation(), intent.getMaxVelocity(), intent.getMaxAcceleration(),
                intent.getMaxJerk());
        } else if (intentions.hasIntentionWithType(IntentionType.MOVE)) {
            MoveIntention intent = (MoveIntention) intentions.getIntentionWithType(IntentionType.MOVE);
            this.updateAcceleration(intent.getJerk(), stepSize);
            this.updateVelocity(this.acceleration, stepSize);
            this.updateLocation(this.velocity, stepSize);
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
        Vector jerk = Vector.subtract(this.getLocation(), this.getNextLocation(stepSize));
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
