package com.seat.rescuesim.common.remote.mobile;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.remote.RemoteController;
import com.seat.rescuesim.common.remote.intent.IntentRegistry;

public class MobileRemoteController extends RemoteController {

    public MobileRemoteController(String remoteID) {
        super(remoteID);
    }

    public MobileRemoteController(JSONOption option) throws JSONException {
        super(option);
    }

    public void goTo() {
        super.addIntention(IntentRegistry.GoTo());
    }

    public void goTo(double maxVelocity) {
        super.addIntention(IntentRegistry.GoTo(maxVelocity));
    }

    public void goTo(double maxVelocity, double maxAcceleration) {
        super.addIntention(IntentRegistry.GoTo(maxVelocity, maxAcceleration));
    }

    public void goTo(double maxVelocity, double maxAcceleration, double maxJerk) {
        super.addIntention(IntentRegistry.GoTo(maxVelocity, maxAcceleration, maxJerk));
    }

    public void goTo(Vector location) {
        super.addIntention(IntentRegistry.GoTo(location));
    }

    public void goTo(Vector location, double maxVelocity) {
        super.addIntention(IntentRegistry.GoTo(location, maxVelocity));
    }

    public void goTo(Vector location, double maxVelocity, double maxAcceleration) {
        super.addIntention(IntentRegistry.GoTo(location, maxVelocity, maxAcceleration));
    }

    public void goTo(Vector location, double maxVelocity, double maxAcceleration, double maxJerk) {
        super.addIntention(IntentRegistry.GoTo(location, maxVelocity, maxAcceleration, maxJerk));
    }

    public void move() {
        super.addIntention(IntentRegistry.Move());
    }

    public void move(Vector jerk) {
        super.addIntention(IntentRegistry.Move(jerk));
    }

}
