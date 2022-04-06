package com.seat.rescuesim.common.remote.base;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.remote.RemoteController;

public class BaseController extends RemoteController {

    public BaseController(String remoteID) {
        super(remoteID);
    }

    public BaseController(JSONOption option) throws JSONException {
        super(option);
    }

}
