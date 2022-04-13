package com.seat.sim.common.remote.mobile.aerial;

import com.seat.sim.common.json.JSONException;
import com.seat.sim.common.json.JSONOption;
import com.seat.sim.common.remote.mobile.MobileRemoteController;

/** A controller to signal intents to an Aerial Remote. */
public class AerialRemoteController extends MobileRemoteController {

    public AerialRemoteController(String remoteID) {
        super(remoteID);
    }

    public AerialRemoteController(JSONOption option) throws JSONException {
        super(option);
    }

}
