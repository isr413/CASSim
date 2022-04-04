package com.seat.rescuesim.common.remote.stat.base;

import java.util.Collection;

import com.seat.rescuesim.common.remote.RemoteController;
import com.seat.rescuesim.common.remote.RemoteType;
import com.seat.rescuesim.common.remote.intent.ActivateIntention;
import com.seat.rescuesim.common.remote.intent.DeactivateIntention;
import com.seat.rescuesim.common.remote.intent.DoneIntention;
import com.seat.rescuesim.common.remote.intent.NoneIntention;
import com.seat.rescuesim.common.remote.intent.ShutdownIntention;
import com.seat.rescuesim.common.remote.intent.StartupIntention;

public class BaseController extends RemoteController {

    public BaseController(String remoteID) {
        super(RemoteType.STATIC, remoteID);
    }

    public void activate(String sensor) {
        ActivateIntention intent = new ActivateIntention();
        intent.addActivation(sensor);
        super.addIntention(intent);
    }

    public void activateAll(Collection<String> sensors) {
        ActivateIntention intent = new ActivateIntention();
        intent.addActivations(sensors);
        super.addIntention(intent);
    }

    public void deactivate(String sensor) {
        DeactivateIntention intent = new DeactivateIntention();
        intent.addDeactivation(sensor);
        super.addIntention(intent);
    }

    public void deactivateAll(Collection<String> sensors) {
        DeactivateIntention intent = new DeactivateIntention();
        intent.addDeactivations(sensors);
        super.addIntention(intent);
    }

    public void done() {
        super.addIntention(new DoneIntention());
    }

    public void none() {
        super.addIntention(new NoneIntention());
    }

    public void shutdown() {
        super.addIntention(new ShutdownIntention());
    }

    public void startup() {
        super.addIntention(new StartupIntention());
    }

}
