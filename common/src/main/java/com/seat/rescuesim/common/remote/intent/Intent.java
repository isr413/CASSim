package com.seat.rescuesim.common.remote.intent;

import java.util.ArrayList;
import java.util.HashSet;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.math.Vector;

public class Intent {

    public static IntentionType decodeIntentionType(JSONOption option) throws JSONException {
        if (option.isSomeObject()) {
            return IntentionType.values()[option.someObject().getInt(Intention.INTENTION_TYPE)];
        }
        throw new JSONException(String.format("Cannot decode intention type of %s", option.toString()));
    }

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

    public static Intention Some(JSONOption option) throws JSONException {
        switch (Intent.decodeIntentionType(option)) {
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
