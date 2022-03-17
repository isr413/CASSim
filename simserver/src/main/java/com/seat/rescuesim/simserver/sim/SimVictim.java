package com.seat.rescuesim.simserver.sim;

import java.util.ArrayList;

import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.remote.intent.GotoIntention;
import com.seat.rescuesim.common.remote.intent.Intent;
import com.seat.rescuesim.common.remote.intent.Intention;
import com.seat.rescuesim.common.remote.intent.MoveIntention;
import com.seat.rescuesim.common.sensor.SensorState;
import com.seat.rescuesim.common.victim.VictimSpec;
import com.seat.rescuesim.common.victim.VictimState;

public class SimVictim extends KineticSimAsset {

    public SimVictim(String label, VictimSpec spec, Vector location) {
        super(label, spec, location);
    }

    public SimVictim(String label, VictimSpec spec, Vector location, double battery) {
        super(label, spec, location, battery);
    }

    public SimVictim(String label, VictimSpec spec, Vector location, Vector velocity) {
        super(label, spec, location);
        this.velocity = velocity;
    }

    public SimVictim(String label, VictimSpec spec, Vector location, double battery, Vector velocity,
            Vector acceleration) {
        super(label, spec, location, battery, velocity, acceleration);
    }

    public VictimSpec getSpec() {
        return (VictimSpec) this.spec;
    }

    public VictimState getState() {
        return new VictimState(
            this.getSpec().getSpecType(),
            this.label,
            this.location,
            this.battery,
            new ArrayList<SensorState>(),
            this.velocity,
            this.acceleration
        );
    }

    private void updateAcceleration(Vector jerk) {
        this.updateAcceleration(jerk, this.getSpec().getMaxAcceleration());
    }

    private void updateAcceleration(Vector jerk, double maxAcceleration) {
        maxAcceleration = Math.min(maxAcceleration, this.getSpec().getMaxAcceleration());
        if (this.getSpec().getMaxJerk() < jerk.getMagnitude()) {
            jerk = Vector.scale(jerk.getUnitVector(), this.getSpec().getMaxJerk());
        }
        this.acceleration = Vector.add(this.acceleration, jerk);
        if (maxAcceleration < this.acceleration.getMagnitude()) {
            this.acceleration = Vector.scale(this.acceleration.getUnitVector(), maxAcceleration);
        }
    }

    private void updateLocation() {
        this.location = Vector.add(this.location, this.velocity);
    }

    private void updateLocationTo(Vector location, double maxVelocity, double maxAcceleration) {
        Vector jerk = Vector.subtract(location, this.getNextLocation());
        this.updateAcceleration(jerk, maxAcceleration);
        this.updateVelocity(maxVelocity);
        this.updateLocation();
    }

    private void updateVelocity() {
        this.updateVelocity(this.getSpec().getMaxVelocity());
    }

    private void updateVelocity(double maxVelocity) {
        maxVelocity = Math.min(maxVelocity, this.getSpec().getMaxVelocity());
        this.velocity = Vector.add(this.velocity, this.acceleration);
        if (maxVelocity < this.velocity.getMagnitude()) {
            this.velocity = Vector.scale(this.velocity.getUnitVector(), maxVelocity);
        }
    }

    private void updateVelocityTo(Vector nextVelocity) {
        Vector jerk = Vector.subtract(nextVelocity, Vector.add(this.velocity, this.acceleration));
        this.updateAcceleration(jerk);
        this.updateVelocity();
    }

    public VictimState update(ArrayList<Intention> intentions, double stepSize) throws SimException {
        if (intentions == null || intentions.isEmpty() ||
                (intentions.size() == 1 && intentions.get(0).equals(Intent.None()))) {
            this.updateVelocity();
            this.updateLocation();
            return this.getState();
        }
        for (Intention intent : intentions) {
            switch (intent.getIntentionType()) {
                case DONE:
                case STOP:
                    this.updateVelocityTo(new Vector());
                    this.updateLocation();
                    continue;
                case SHUTDOWN:
                    this.updateVelocityTo(new Vector());
                    this.updateLocation();
                    continue;
                case MOVE:
                    this.updateAcceleration(((MoveIntention) intent).getJerk());
                    this.updateVelocity();
                    this.updateLocation();
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
        return this.getState();
    }

}
