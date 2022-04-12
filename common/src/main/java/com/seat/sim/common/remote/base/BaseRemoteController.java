package com.seat.sim.common.remote.base;

import com.seat.sim.common.json.JSONException;
import com.seat.sim.common.json.JSONOption;
import com.seat.sim.common.remote.RemoteController;

public class BaseRemoteController extends RemoteController {

    public BaseRemoteController(String remoteID) {
        super(remoteID);
    }

    public BaseRemoteController(JSONOption option) throws JSONException {
        super(option);
    }

}
