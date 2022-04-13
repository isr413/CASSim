package com.seat.sim.server.remote;

import com.seat.sim.common.core.TeamColor;
import com.seat.sim.common.math.Vector;
import com.seat.sim.common.remote.intent.GoToIntention;
import com.seat.sim.common.remote.intent.IntentionSet;
import com.seat.sim.common.remote.intent.IntentionType;
import com.seat.sim.common.remote.intent.MoveIntention;
import com.seat.sim.common.remote.mobile.MobileRemoteProto;
import com.seat.sim.common.remote.mobile.MobileRemoteState;
import com.seat.sim.common.util.Debugger;
import com.seat.sim.server.core.SimException;
import com.seat.sim.server.scenario.Scenario;

public class MobileRemote extends Remote {

    private Vector acceleration;
    private Vector velocity;

    public MobileRemote(MobileRemoteProto proto, String remoteID, TeamColor team, boolean active) {
        super(proto, remoteID, team, active);
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
        return new MobileRemoteState(this.getRemoteID(), this.getTeam(), this.getLocation(), this.getBattery(),
            this.isActive(), this.velocity, this.acceleration);
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

    public void updateAcceleration(Vector delta) {
        if (this.getMaxJerk() < delta.getMagnitude()) {
            delta = Vector.scale(delta.getUnitVector(), this.getMaxJerk());
        }
        this.acceleration = Vector.add(this.acceleration, delta);
        if (this.getMaxAcceleration() < this.acceleration.getMagnitude()) {
            this.acceleration = Vector.scale(this.acceleration.getUnitVector(), this.getMaxAcceleration());
        }
    }

    public void updateAccelerationTo(Vector target) {
        this.updateAccelerationTo(target, Double.POSITIVE_INFINITY);
    }

    public void updateAccelerationTo(Vector target, double maxJerk) {
        Vector jerk = Vector.subtract(target, this.acceleration);
        if (maxJerk < jerk.getMagnitude()) {
            jerk = Vector.scale(jerk.getUnitVector(), maxJerk);
        }
        this.updateAcceleration(jerk);
    }

    public void updateLocationTo(Vector dest, double stepSize) {
        this.updateLocationTo(dest, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY,
            stepSize);
    }

    public void updateLocationTo(Vector dest, double maxVelocity, double stepSize) {
        this.updateLocationTo(dest, maxVelocity, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, stepSize);
    }

    public void updateLocationTo(Vector dest, double maxVelocity, double maxAcceleration, double stepSize) {
        this.updateLocationTo(dest, maxVelocity, maxAcceleration, Double.POSITIVE_INFINITY, stepSize);
    }

    public void updateLocationTo(Vector dest, double maxVelocity, double maxAcceleration, double maxJerk,
            double stepSize) {
        Vector targetVelocity = Vector.subtract(dest, this.getLocation());
        if (maxVelocity < targetVelocity.getMagnitude()) {
            targetVelocity = Vector.scale(targetVelocity.getUnitVector(), maxVelocity);
        }
        this.updateVelocityTo(targetVelocity, maxAcceleration, maxJerk, stepSize);
        this.updateLocation(this.velocity, stepSize);
    }

    public void updateVelocity(Vector delta, double stepSize) {
        if (this.getMaxAcceleration() < delta.getMagnitude()) {
            delta = Vector.scale(delta.getUnitVector(), this.getMaxAcceleration());
        }
        this.velocity = Vector.add(this.velocity, Vector.scale(delta, stepSize));
        if (this.getMaxVelocity() < this.velocity.getMagnitude()) {
            this.velocity = Vector.scale(this.velocity.getUnitVector(), this.getMaxVelocity());
        }
    }

    public void updateVelocityTo(Vector target, double stepSize) {
        this.updateVelocityTo(target, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, stepSize);
    }

    public void updateVelocityTo(Vector target, double maxAcceleration, double stepSize) {
        this.updateVelocityTo(target, maxAcceleration, Double.POSITIVE_INFINITY, stepSize);
    }

    public void updateVelocityTo(Vector target, double maxAcceleration, double maxJerk, double stepSize) {
        Vector targetAcceleration = Vector.subtract(target, this.velocity);
        if (maxAcceleration < targetAcceleration.getMagnitude()) {
            targetAcceleration = Vector.scale(targetAcceleration.getUnitVector(), maxAcceleration);
        }
        this.updateAccelerationTo(targetAcceleration, maxJerk);
        this.updateVelocity(this.acceleration, stepSize);
    }

    @Override
    public void update(Scenario scenario, IntentionSet intentions, double stepSize) throws SimException {
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
            Debugger.logger.warn(String.format("%s: %s", this.getRemoteID(), intent.getLocation().toString()));
            this.updateLocationTo(intent.getLocation(), intent.getMaxVelocity(), intent.getMaxAcceleration(),
                intent.getMaxJerk(), stepSize);
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

}
