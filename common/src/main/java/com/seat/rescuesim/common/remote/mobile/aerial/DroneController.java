package com.seat.rescuesim.common.remote.mobile.aerial;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.remote.mobile.MobileRemoteController;

public class DroneController extends MobileRemoteController {

    public DroneController(String remoteID) {
        super(remoteID);
    }

    public DroneController(JSONOption option) throws JSONException {
        super(option);
    }

}
