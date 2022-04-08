package com.seat.rescuesim.common.remote.base;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.remote.RemoteController;

public class BaseRemoteController extends RemoteController {

    public BaseRemoteController(String remoteID) {
        super(remoteID);
    }

    public BaseRemoteController(JSONOption option) throws JSONException {
        super(option);
    }

}
