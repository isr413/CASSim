package com.seat.rescuesim.common.remote.kinetic.victim;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.remote.kinetic.KineticRemoteController;

public class VictimController extends KineticRemoteController {

    public VictimController(String remoteID) {
        super(remoteID);
    }

    public VictimController(JSONOption option) throws JSONException {
        super(option);
    }

}
