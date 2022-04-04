package com.seat.rescuesim.common.remote.kinetic;

import java.util.Collection;

import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.remote.RemoteController;
import com.seat.rescuesim.common.remote.intent.Intent;

public class KineticRemoteController extends RemoteController {

    public KineticRemoteController(String remoteID) {
        super(remoteID);
    }

    public void activate(String sensor) {
        super.addIntention(Intent.Activate(sensor));
    }

    public void activateAll() {
        super.addIntention(Intent.Activate());
    }

    public void activateAll(Collection<String> sensors) {
        super.addIntention(Intent.Activate(sensors));
    }

    public void deactivate(String sensor) {
        super.addIntention(Intent.Deactivate(sensor));
    }

    public void deactivateAll() {
        super.addIntention(Intent.Deactivate());
    }

    public void deactivateAll(Collection<String> sensors) {
        super.addIntention(Intent.Deactivate(sensors));
    }

    public void done() {
        super.addIntention(Intent.Done());
    }

    public void goTo(Vector location) {
        super.addIntention(Intent.Goto(location));
    }

    public void goTo(Vector location, double maxVelocity) {
        super.addIntention(Intent.Goto(location, maxVelocity));
    }

    public void goTo(Vector location, double maxVelocity, double maxAcceleration) {
        super.addIntention(Intent.Goto(location, maxVelocity, maxAcceleration));
    }

    public void goTo(Vector location, double maxVelocity, double maxAcceleration, double maxJerk) {
        super.addIntention(Intent.Goto(location, maxVelocity, maxAcceleration, maxJerk));
    }

    public void move() {
        super.addIntention(Intent.Move());
    }

    public void move(Vector jerk) {
        super.addIntention(Intent.Move(jerk));
    }

    public void none() {
        super.addIntention(Intent.None());
    }

    public void shutdown() {
        super.addIntention(Intent.Shutdown());
    }

    public void startup() {
        super.addIntention(Intent.Startup());
    }

}
