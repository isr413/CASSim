package com.seat.rescuesim.common.remote.mobile.ground;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.remote.mobile.MobileRemoteController;

public class VictimController extends MobileRemoteController {

    public VictimController(String remoteID) {
        super(remoteID);
    }

    public VictimController(JSONOption option) throws JSONException {
        super(option);
    }

}
