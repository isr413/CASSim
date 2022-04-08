package com.seat.rescuesim.common.remote.mobile.aerial;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.remote.mobile.MobileRemoteController;

public class DroneRemoteController extends MobileRemoteController {

    public DroneRemoteController(String remoteID) {
        super(remoteID);
    }

    public DroneRemoteController(JSONOption option) throws JSONException {
        super(option);
    }

}
