package com.seat.rescuesim.common.remote.kinetic.drone;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.remote.kinetic.KineticRemoteController;

public class DroneController extends KineticRemoteController {

    public DroneController(String remoteID) {
        super(remoteID);
    }

    public DroneController(JSONOption option) throws JSONException {
        super(option);
    }

}
