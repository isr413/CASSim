package com.seat.sim.common.remote.intent;

import java.util.Collection;

import com.seat.sim.common.json.JSONException;
import com.seat.sim.common.json.JSONOption;
import com.seat.sim.common.math.Vector;

public class IntentRegistry {

    public static ActivateIntention Activate() {
        return new ActivateIntention();
    }

    public static ActivateIntention Activate(String sensorID) {
        return new ActivateIntention(sensorID);
    }

    public static ActivateIntention Activate(Collection<String> sensorIDs) {
        return new ActivateIntention(sensorIDs);
    }

    public static DeactivateIntention Deactivate() {
        return new DeactivateIntention();
    }

    public static DeactivateIntention Deactivate(String sensorID) {
        return new DeactivateIntention(sensorID);
    }

    public static DeactivateIntention Deactivate(Collection<String> sensorIDs) {
        return new DeactivateIntention(sensorIDs);
    }

    public static DoneIntention Done() {
        return new DoneIntention();
    }

    public static GoToIntention GoTo() {
        return new GoToIntention();
    }

    public static GoToIntention GoTo(double maxVelocity) {
        return new GoToIntention(maxVelocity);
    }

    public static GoToIntention GoTo(double maxVelocity, double maxAcceleration) {
        return new GoToIntention(maxVelocity, maxAcceleration);
    }

    public static GoToIntention GoTo(Vector location) {
        return new GoToIntention(location);
    }

    public static GoToIntention GoTo(Vector location, double maxVelocity) {
        return new GoToIntention(location, maxVelocity);
    }

    public static GoToIntention GoTo(Vector location, double maxVelocity, double maxAcceleration) {
        return new GoToIntention(location, maxVelocity, maxAcceleration);
    }

    public static MoveIntention Move() {
        return new MoveIntention();
    }

    public static MoveIntention Move(Vector acceleration) {
        return new MoveIntention(acceleration);
    }

    public static NoneIntention None() {
        return new NoneIntention();
    }

    public static ShutdownIntention Shutdown() {
        return new ShutdownIntention();
    }

    public static Intention Some(JSONOption option) throws JSONException {
        if (!option.isSomeObject()) {
            throw new JSONException(String.format("Cannot decode intention type of %s", option.toString()));
        }
        switch (IntentionType.decodeType(option.someObject())) {
            case ACTIVATE: return new ActivateIntention(option);
            case DEACTIVATE: return new DeactivateIntention(option);
            case DONE: return new DoneIntention();
            case GOTO: return new GoToIntention(option);
            case MOVE: return new MoveIntention(option);
            case SHUTDOWN: return new ShutdownIntention();
            case STARTUP: return new StartupIntention();
            case STEER: return new SteerIntention(option);
            case STOP: return new StopIntention();
            default: return new NoneIntention();
        }
    }

    public static StartupIntention Startup() {
        return new StartupIntention();
    }

    public static SteerIntention Steer() {
        return new SteerIntention();
    }

    public static SteerIntention Steer(Vector direction) {
        return new SteerIntention(direction);
    }

    public static StopIntention Stop() {
        return new StopIntention();
    }

}
