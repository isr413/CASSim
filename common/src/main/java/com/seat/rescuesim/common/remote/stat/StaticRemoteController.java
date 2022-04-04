package com.seat.rescuesim.common.remote.stat;

import java.util.Collection;

import com.seat.rescuesim.common.remote.RemoteController;
import com.seat.rescuesim.common.remote.intent.Intent;

public class StaticRemoteController extends RemoteController {

    public StaticRemoteController(String remoteID) {
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
