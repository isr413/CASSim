package com.seat.sim.common.remote.mobile;

import com.seat.sim.common.json.JSONException;
import com.seat.sim.common.json.JSONOptional;
import com.seat.sim.common.math.Vector;
import com.seat.sim.common.remote.RemoteController;
import com.seat.sim.common.remote.intent.IntentRegistry;

/** A controller to signal intents to a Mobile Remote. */
public class MobileRemoteController extends RemoteController {

    public MobileRemoteController(String remoteID) {
        super(remoteID);
    }

    public MobileRemoteController(JSONOptional optional) throws JSONException {
        super(optional);
    }

    public void goHome() {
        super.addIntention(IntentRegistry.GoTo());
    }

    public void goToLocation(double maxVelocity) {
        super.addIntention(IntentRegistry.GoTo(maxVelocity));
    }

    public void goToLocation(double maxVelocity, double maxAcceleration) {
        super.addIntention(IntentRegistry.GoTo(maxVelocity, maxAcceleration));
    }

    public void goToLocation(Vector location) {
        super.addIntention(IntentRegistry.GoTo(location));
    }

    public void goToLocation(Vector location, double maxVelocity) {
        super.addIntention(IntentRegistry.GoTo(location, maxVelocity));
    }

    public void goToLocation(Vector location, double maxVelocity, double maxAcceleration) {
        super.addIntention(IntentRegistry.GoTo(location, maxVelocity, maxAcceleration));
    }

    public void move(Vector acceleration) {
        super.addIntention(IntentRegistry.Move(acceleration));
    }

    public void moveForward() {
        super.addIntention(IntentRegistry.Move());
    }

    public void steer(Vector direction) {
        super.addIntention(IntentRegistry.Steer(direction));
    }

    public void steerHome() {
        super.addIntention(IntentRegistry.Steer());
    }

    public void stop() {
        super.addIntention(IntentRegistry.Stop());
    }

}
