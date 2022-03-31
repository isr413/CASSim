package com.seat.rescuesim.common.remote.stat.base;

import java.util.ArrayList;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.remote.stat.StaticRemoteState;
import com.seat.rescuesim.common.remote.stat.StaticRemoteType;
import com.seat.rescuesim.common.sensor.SensorState;

/** A serializable Base state. */
public class BaseState extends StaticRemoteState {

    public BaseState(String remoteID, Vector location, double battery) {
        super(StaticRemoteType.BASE, remoteID, location, battery);
    }

    public BaseState(String remoteID, Vector location, double battery, ArrayList<SensorState> sensors) {
        super(StaticRemoteType.BASE, remoteID, location, battery, sensors);
    }

    public BaseState(JSONOption option) throws JSONException {
        super(option);
    }

}
