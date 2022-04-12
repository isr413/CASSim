package com.seat.sim.common.remote.mobile.aerial;

import com.seat.sim.common.json.JSONException;
import com.seat.sim.common.json.JSONOption;
import com.seat.sim.common.remote.mobile.MobileRemoteController;

public class DroneRemoteController extends MobileRemoteController {

    public DroneRemoteController(String remoteID) {
        super(remoteID);
    }

    public DroneRemoteController(JSONOption option) throws JSONException {
        super(option);
    }

}
