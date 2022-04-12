package com.seat.sim.common.remote.mobile.ground;

import com.seat.sim.common.json.JSONException;
import com.seat.sim.common.json.JSONOption;
import com.seat.sim.common.remote.mobile.MobileRemoteController;

public class VictimRemoteController extends MobileRemoteController {

    public VictimRemoteController(String remoteID) {
        super(remoteID);
    }

    public VictimRemoteController(JSONOption option) throws JSONException {
        super(option);
    }

}
