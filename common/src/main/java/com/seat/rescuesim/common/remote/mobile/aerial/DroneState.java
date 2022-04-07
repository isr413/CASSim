package com.seat.rescuesim.common.remote.mobile.aerial;

import java.util.Collection;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.remote.RemoteType;
import com.seat.rescuesim.common.remote.mobile.MobileRemoteState;
import com.seat.rescuesim.common.sensor.SensorState;

/** A serializable Drone state. */
public class DroneState extends MobileRemoteState {

    public DroneState(String remoteID, Vector location, double battery, Vector velocity, Vector acceleration) {
        this(remoteID, location, battery, null, velocity, acceleration);
    }

    public DroneState(String remoteID, Vector location, double battery, Collection<SensorState> sensors,
            Vector velocity, Vector acceleration) {
        super(RemoteType.DRONE, remoteID, location, battery, sensors, velocity, acceleration);
    }

    public DroneState(JSONOption option) throws JSONException {
        super(option);
    }

}
