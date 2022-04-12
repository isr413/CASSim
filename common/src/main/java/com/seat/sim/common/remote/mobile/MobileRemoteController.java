package com.seat.sim.common.remote.mobile;

import com.seat.sim.common.json.JSONException;
import com.seat.sim.common.json.JSONOption;
import com.seat.sim.common.math.Vector;
import com.seat.sim.common.remote.RemoteController;
import com.seat.sim.common.remote.intent.IntentRegistry;

public class MobileRemoteController extends RemoteController {

    public MobileRemoteController(String remoteID) {
        super(remoteID);
    }

    public MobileRemoteController(JSONOption option) throws JSONException {
        super(option);
    }

    public void goToLocation() {
        super.addIntention(IntentRegistry.GoTo());
    }

    public void goToLocation(double maxVelocity) {
        super.addIntention(IntentRegistry.GoTo(maxVelocity));
    }

    public void goToLocation(double maxVelocity, double maxAcceleration) {
        super.addIntention(IntentRegistry.GoTo(maxVelocity, maxAcceleration));
    }

    public void goToLocation(double maxVelocity, double maxAcceleration, double maxJerk) {
        super.addIntention(IntentRegistry.GoTo(maxVelocity, maxAcceleration, maxJerk));
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

    public void goToLocation(Vector location, double maxVelocity, double maxAcceleration, double maxJerk) {
        super.addIntention(IntentRegistry.GoTo(location, maxVelocity, maxAcceleration, maxJerk));
    }

    public void move() {
        super.addIntention(IntentRegistry.Move());
    }

    public void move(Vector jerk) {
        super.addIntention(IntentRegistry.Move(jerk));
    }

}
