package com.seat.sim.common.remote.intent;

import java.util.Collection;

import com.seat.sim.common.json.JSONException;
import com.seat.sim.common.json.JSONOption;
import com.seat.sim.common.math.Vector;

public class IntentRegistry {

    public static Intention Activate() {
        return new ActivateIntention();
    }

    public static Intention Activate(String sensor) {
        return new ActivateIntention(sensor);
    }

    public static Intention Activate(Collection<String> sensors) {
        return new ActivateIntention(sensors);
    }

    public static Intention Deactivate() {
        return new DeactivateIntention();
    }

    public static Intention Deactivate(String sensor) {
        return new DeactivateIntention(sensor);
    }

    public static Intention Deactivate(Collection<String> sensors) {
        return new DeactivateIntention(sensors);
    }

    public static Intention Done() {
        return new DoneIntention();
    }

    public static Intention GoTo() {
        return new GoToIntention();
    }

    public static Intention GoTo(double maxVelocity) {
        return new GoToIntention(maxVelocity);
    }

    public static Intention GoTo(double maxVelocity, double maxAcceleration) {
        return new GoToIntention(maxVelocity, maxAcceleration);
    }

    public static Intention GoTo(double maxVelocity, double maxAcceleration, double maxJerk) {
        return new GoToIntention(maxVelocity, maxAcceleration, maxJerk);
    }

    public static Intention GoTo(Vector location) {
        return new GoToIntention(location);
    }

    public static Intention GoTo(Vector location, double maxVelocity) {
        return new GoToIntention(location, maxVelocity);
    }

    public static Intention GoTo(Vector location, double maxVelocity, double maxAcceleration) {
        return new GoToIntention(location, maxVelocity, maxAcceleration);
    }

    public static Intention GoTo(Vector location, double maxVelocity, double maxAcceleration, double maxJerk) {
        return new GoToIntention(location, maxVelocity, maxAcceleration, maxJerk);
    }

    public static Intention Move() {
        return new MoveIntention();
    }

    public static Intention Move(Vector jerk) {
        return new MoveIntention(jerk);
    }

    public static Intention None() {
        return new DoneIntention();
    }

    public static Intention Shutdown() {
        return new DoneIntention();
    }

    public static Intention Some(JSONOption option) throws JSONException {
        if (!option.isSomeObject()) {
            throw new JSONException(String.format("Cannot decode intention type of %s", option.toString()));
        }
        switch (IntentionType.decodeType(option.someObject())) {
            case ACTIVATE:
                return new ActivateIntention(option);
            case DEACTIVATE:
                return new DeactivateIntention(option);
            case DONE:
                return new DoneIntention();
            case GOTO:
                return new GoToIntention(option);
            case MOVE:
                return new MoveIntention(option);
            case SHUTDOWN:
                return new ShutdownIntention();
            case STARTUP:
                return new StartupIntention();
            case STOP:
                return new StopIntention();
            default:
                return new NoneIntention();
        }
    }

    public static Intention Startup() {
        return new StartupIntention();
    }

    public static Intention Stop() {
        return new StopIntention();
    }

}
