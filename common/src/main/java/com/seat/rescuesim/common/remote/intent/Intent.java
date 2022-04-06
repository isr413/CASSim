package com.seat.rescuesim.common.remote.intent;

import java.util.Collection;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.math.Vector;

public class Intent {

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

    public static Intention Goto() {
        return new GotoIntention();
    }

    public static Intention Goto(double maxVelocity) {
        return new GotoIntention(maxVelocity);
    }

    public static Intention Goto(double maxVelocity, double maxAcceleration) {
        return new GotoIntention(maxVelocity, maxAcceleration);
    }

    public static Intention Goto(double maxVelocity, double maxAcceleration, double maxJerk) {
        return new GotoIntention(maxVelocity, maxAcceleration, maxJerk);
    }

    public static Intention Goto(Vector location) {
        return new GotoIntention(location);
    }

    public static Intention Goto(Vector location, double maxVelocity) {
        return new GotoIntention(location, maxVelocity);
    }

    public static Intention Goto(Vector location, double maxVelocity, double maxAcceleration) {
        return new GotoIntention(location, maxVelocity, maxAcceleration);
    }

    public static Intention Goto(Vector location, double maxVelocity, double maxAcceleration, double maxJerk) {
        return new GotoIntention(location, maxVelocity, maxAcceleration, maxJerk);
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
                return new GotoIntention(option);
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
