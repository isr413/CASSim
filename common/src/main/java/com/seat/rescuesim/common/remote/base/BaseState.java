package com.seat.rescuesim.common.remote.base;

import java.util.Collection;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.remote.RemoteState;
import com.seat.rescuesim.common.remote.RemoteType;
import com.seat.rescuesim.common.sensor.SensorState;

/** A serializable Base state. */
public class BaseState extends RemoteState {

    public BaseState(String remoteID, Vector location, double battery, boolean active) {
        this(remoteID, location, battery, active, null);
    }

    public BaseState(String remoteID, Vector location, double battery, boolean active,
            Collection<SensorState> sensors) {
        super(RemoteType.BASE, remoteID, location, battery, active, sensors);
    }

    public BaseState(JSONOption option) throws JSONException {
        super(option);
    }

}
