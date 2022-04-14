package com.seat.sim.server.remote;

import com.seat.sim.common.core.TeamColor;
import com.seat.sim.common.math.Vector;
import com.seat.sim.common.remote.intent.GoToIntention;
import com.seat.sim.common.remote.intent.IntentionSet;
import com.seat.sim.common.remote.intent.IntentionType;
import com.seat.sim.common.remote.intent.MoveIntention;
import com.seat.sim.common.remote.intent.SteerIntention;
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

    public double getBrakeDistance() {
        return this.getBrakeDistance(this.getVelocity().getMagnitude(), this.getMaxAcceleration());
    }

    public double getBrakeDistance(double velocity, double acceleration) {
        if (velocity == 0 || acceleration == Double.POSITIVE_INFINITY) {
            return 0;
        }
        return 0.5 * velocity * velocity / acceleration;
    }

    public double getMaxAcceleration() {
        return this.getProto().getMaxAcceleration();
    }

    public double getMaxVelocity() {
        return this.getProto().getMaxVelocity();
    }

    public Vector getNextLocation(double stepSize) {
        return this.getNextLocation(this.velocity, this.acceleration, stepSize);
    }

    public Vector getNextLocation(Vector velocity, double stepSize) {
        return this.getNextLocation(velocity, new Vector(), stepSize);
    }

    public Vector getNextLocation(Vector velocity, Vector acceleration, double stepSize) {
        if (this.getMaxAcceleration() < acceleration.getMagnitude()) {
            acceleration = Vector.scale(acceleration.getUnitVector(), this.getMaxAcceleration());
        }
        velocity = Vector.add(velocity, Vector.scale(acceleration, stepSize));
        if (this.getMaxVelocity() < velocity.getMagnitude()) {
            velocity = Vector.scale(velocity.getUnitVector(), this.getMaxVelocity());
        }
        return Vector.add(this.getLocation(), Vector.scale(velocity, stepSize));
    }

    public Vector getNextVelocity(double stepSize) {
        return this.getNextVelocity(this.acceleration, stepSize);
    }

    public Vector getNextVelocity(Vector acceleration, double stepSize) {
        if (this.getMaxAcceleration() < acceleration.getMagnitude()) {
            acceleration = Vector.scale(acceleration.getUnitVector(), this.getMaxAcceleration());
        }
        Vector nextVelocity = Vector.add(this.velocity, Vector.scale(acceleration, stepSize));
        if (this.getMaxVelocity() < nextVelocity.getMagnitude()) {
            nextVelocity = Vector.scale(nextVelocity.getUnitVector(), this.getMaxVelocity());
        }
        return nextVelocity;
    }

    @Override
    public MobileRemoteProto getProto() {
        return (MobileRemoteProto) super.getProto();
    }

    @Override
    public MobileRemoteState getRemoteState() {
        return new MobileRemoteState(this.getRemoteID(), this.getTeam(), this.getLocation(), this.getBattery(),
            this.isActive(), this.velocity, this.acceleration);
    }

    public Vector getVelocity() {
        return this.velocity;
    }

    public boolean hasAcceleration() {
        return this.acceleration != null && this.acceleration.getMagnitude() > 0;
    }

    public boolean hasMaxAcceleration() {
        return this.getProto().hasMaxAcceleration();
    }

    public boolean hasMaxVelocity() {
        return this.getProto().hasMaxVelocity();
    }

    public boolean hasVelocity() {
        return this.velocity != null && this.velocity.getMagnitude() > 0;
    }

    public boolean isInMotion() {
        return this.hasVelocity() || this.hasAcceleration();
    }

    public void setAcceleration(Vector acceleration) throws SimException {
        if (this.getMaxAcceleration() < acceleration.getMagnitude()) {
            throw new SimException(String.format("Remote %s cannot set acceleration to %s", this.getRemoteID(),
                acceleration.toString()));
        }
        this.acceleration = acceleration;
    }

    public void setVelocity(Vector velocity) throws SimException {
        if (this.getMaxVelocity() < velocity.getMagnitude()) {
            throw new SimException(String.format("Remote %s cannot set velocity to %s", this.getRemoteID(),
                velocity.toString()));
        }
        this.velocity = velocity;
    }

    public void updateAcceleration(Vector delta) {
        this.acceleration = Vector.add(this.acceleration, delta);
        if (this.getMaxAcceleration() < this.acceleration.getMagnitude()) {
            this.acceleration = Vector.scale(this.acceleration.getUnitVector(), this.getMaxAcceleration());
        }
    }

    public void updateAccelerationTo(Vector targetAcceleration) {
        if (this.getMaxAcceleration() < targetAcceleration.getMagnitude()) {
            targetAcceleration = Vector.scale(targetAcceleration.getUnitVector(), this.getMaxAcceleration());
        }
        Vector jerk = Vector.subtract(targetAcceleration, this.acceleration);
        this.updateAcceleration(jerk);
    }

    public void updateLocationTo(Vector targetLocation, double stepSize) {
        this.updateLocationTo(targetLocation, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, stepSize);
    }

    public void updateLocationTo(Vector targetLocation, double maxVelocity, double stepSize) {
        this.updateLocationTo(targetLocation, maxVelocity, Double.POSITIVE_INFINITY, stepSize);
    }

    public void updateLocationTo(Vector targetLocation, double maxVelocity, double maxAcceleration, double stepSize) {
        maxVelocity = Math.min(maxVelocity, this.getMaxVelocity());
        maxAcceleration = Math.min(maxAcceleration, this.getMaxAcceleration());
        Vector deltaLocation = Vector.subtract(targetLocation, this.getLocation());
        if (deltaLocation.getMagnitude() < 0.01 && this.velocity.getMagnitude() < maxAcceleration) {
            this.setAcceleration(new Vector());
            this.setVelocity(new Vector());
            Debugger.logger.warn("here");
            return;
        }
        Vector targetVelocity = deltaLocation;
        if (maxVelocity < targetVelocity.getMagnitude()) {
            targetVelocity = Vector.scale(targetVelocity, maxVelocity);
        }
        Vector targetAcceleration = Vector.subtract(targetVelocity, this.velocity);
        if (maxAcceleration < targetAcceleration.getMagnitude()) {
            targetAcceleration = Vector.scale(targetAcceleration, maxAcceleration);
        }
        if (this.velocity.getMagnitude() > 0 && maxAcceleration != Double.POSITIVE_INFINITY) {
            double brakeDistance = this.getBrakeDistance(this.velocity.getMagnitude(), maxAcceleration);
            if (deltaLocation.getMagnitude() <= brakeDistance) {
                if (this.velocity.getMagnitude() < maxAcceleration) {
                    this.setAcceleration(Vector.scale(this.velocity.getUnitVector(), -this.velocity.getMagnitude()));
                    this.setVelocity(new Vector());
                } else {
                    this.updateVelocity(Vector.scale(this.velocity.getUnitVector(), -maxAcceleration), stepSize);
                }
                this.updateLocation(this.velocity, stepSize);
                return;
            }
        }
        Vector nextVelocity = Vector.add(this.velocity, Vector.scale(targetAcceleration, stepSize));
        if (maxVelocity < nextVelocity.getMagnitude()) {
            nextVelocity = Vector.scale(nextVelocity.getUnitVector(), maxVelocity);
        }
        if (this.velocity.getMagnitude() > 0 && maxAcceleration != Double.POSITIVE_INFINITY) {
            double brakeDistance = this.getBrakeDistance(nextVelocity.getMagnitude(), maxAcceleration);
            Vector nextLocation = Vector.add(this.getLocation(), Vector.scale(nextVelocity, stepSize));
            if (Vector.subtract(targetLocation, nextLocation).getMagnitude() < brakeDistance) {
                double targetVelocityMagnitude = Math.sqrt(maxAcceleration) *
                    Math.sqrt(maxAcceleration + 2 * deltaLocation.getMagnitude()) - maxAcceleration;
                nextVelocity = Vector.scale(nextVelocity.getUnitVector(), targetVelocityMagnitude);
                Vector nextAcceleration = Vector.subtract(nextVelocity, this.velocity);
                if (maxAcceleration < nextAcceleration.getMagnitude()) {
                    nextAcceleration = Vector.scale(nextAcceleration, maxAcceleration);
                }
                this.updateVelocity(nextAcceleration, stepSize);
                this.updateLocation(this.velocity, stepSize);
                return;
            }
        }
        this.updateVelocity(targetAcceleration, stepSize);
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
        Debugger.logger.state(this.velocity.toString());
    }

    public void updateVelocityTo(Vector target, double stepSize) {
        this.updateVelocityTo(target, Double.POSITIVE_INFINITY, stepSize);
    }

    public void updateVelocityTo(Vector targetVelocity, double maxAcceleration, double stepSize) {
        if (this.getMaxVelocity() < targetVelocity.getMagnitude()) {
            targetVelocity = Vector.scale(targetVelocity.getUnitVector(), this.getMaxVelocity());
        }
        Vector targetAcceleration = Vector.subtract(targetVelocity, this.velocity);
        if (maxAcceleration < targetAcceleration.getMagnitude()) {
            targetAcceleration = Vector.scale(targetAcceleration.getUnitVector(), maxAcceleration);
        }
        this.updateAccelerationTo(targetAcceleration);
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
            if (intent.hasLocation()) {
                this.updateLocationTo(intent.getLocation(), intent.getMaxVelocity(), intent.getMaxAcceleration(),
                    stepSize);
            } else if (this.getProto().hasLocation()) {
                this.updateLocationTo(this.getProto().getLocation(), intent.getMaxVelocity(),
                    intent.getMaxAcceleration(), stepSize);
            }
        } else if (intentions.hasIntentionWithType(IntentionType.MOVE)) {
            MoveIntention intent = (MoveIntention) intentions.getIntentionWithType(IntentionType.MOVE);
            if (intent.hasAcceleration()) {
                this.updateAccelerationTo(intent.getAcceleration());
            }
            this.updateVelocity(this.acceleration, stepSize);
            this.updateLocation(this.velocity, stepSize);
        } else if (intentions.hasIntentionWithType(IntentionType.STEER)) {
            SteerIntention intent = (SteerIntention) intentions.getIntentionWithType(IntentionType.STEER);
            if (intent.hasDirection()) {
                this.updateVelocityTo(
                    Vector.scale(
                        intent.getDirection().getUnitVector(),
                        this.getVelocity().getMagnitude()
                    ),
                    stepSize);
                this.updateLocation(this.velocity, stepSize);
            } else if (this.getProto().hasLocation()) {
                this.updateVelocityTo(
                    Vector.scale(
                        Vector.subtract(this.getProto().getLocation(), this.getLocation()).getUnitVector(),
                        this.getVelocity().getMagnitude()
                    ),
                    stepSize
                );
                this.updateLocation(this.velocity, stepSize);
            }
        } else {
            this.updateVelocity(this.acceleration, stepSize);
            this.updateLocation(this.velocity, stepSize);
        }
    }

}
