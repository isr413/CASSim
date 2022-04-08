package com.seat.rescuesim.common.remote.mobile.ground;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.remote.mobile.MobileRemoteController;

public class VictimRemoteController extends MobileRemoteController {

    public VictimRemoteController(String remoteID) {
        super(remoteID);
    }

    public VictimRemoteController(JSONOption option) throws JSONException {
        super(option);
    }

}
