package com.seat.rescuesim.common.remote.intent;

import java.util.ArrayList;
import java.util.HashSet;

import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.util.Debugger;

public class Intent {

    public static Intention Activate() {
        return new ActivateIntention();
    }

    public static Intention Activate(ArrayList<String> sensors) {
        return new ActivateIntention(sensors);
    }

    public static Intention Activate(HashSet<String> sensors) {
        return new ActivateIntention(sensors);
    }

    public static Intention Deactive() {
        return new DeactivateIntention();
    }

    public static Intention Deactivate(ArrayList<String> sensors) {
        return new DeactivateIntention(sensors);
    }

    public static Intention Deactivate(HashSet<String> sensors) {
        return new DeactivateIntention(sensors);
    }

    public static Intention Done() {
        return new DoneIntention();
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

    public static Intention Move(Vector jerk) {
        return new MoveIntention(jerk);
    }

    public static Intention None() {
        return new DoneIntention();
    }

    public static Intention Shutdown() {
        return new DoneIntention();
    }

    public static Intention Some(JSONObject json) {
        IntentionType type = Intention.decodeIntentionType(json);
        switch (type) {
            case ACTIVATE:
                return new ActivateIntention(json);
            case DEACTIVATE:
                return new DeactivateIntention(json);
            case DONE:
                return new DoneIntention();
            case GOTO:
                return new GotoIntention(json);
            case MOVE:
                return new MoveIntention(json);
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

    public static Intention Some(JSONOption option) {
        if (option.isSomeObject()) {
            return Intent.Some(option.someObject());
        }
        Debugger.logger.err(String.format("Cannot decode intent from %s", option.toString()));
        return null;
    }

    public static Intention Some(String encoding) {
        return Intent.Some(JSONOption.String(encoding));
    }

    public static Intention Startup() {
        return new StartupIntention();
    }

    public static Intention Stop() {
        return new StopIntention();
    }

}
