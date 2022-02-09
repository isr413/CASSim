package com.seat.rescuesim.common.remote;

import java.util.HashSet;

import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.remote.intent.*;

public class Intent {

    public static Intention Activate(HashSet<String> sensors) {
        return new ActivateIntention(sensors);
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

    public static Intention Goto(Vector location, double maxAcceleration) {
        return new GotoIntention(location, maxAcceleration);
    }

    public static Intention Goto(Vector location, double maxAcceleration, double maxVelocity) {
        return new GotoIntention(location, maxAcceleration, maxVelocity);
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
        return null;
    }

    public static Intention Some(JSONOption option) {
        return null;
    }

    public static Intention Some(String encoding) {
        return null;
    }

    public static Intention Startup() {
        return new DoneIntention();
    }

    public static Intention Stop() {
        return new DoneIntention();
    }

}
