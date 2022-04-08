package com.seat.rescuesim.common.remote.mobile.aerial;

import java.util.Collection;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.remote.RemoteType;
import com.seat.rescuesim.common.remote.mobile.MobileRemoteState;
import com.seat.rescuesim.common.sensor.SensorState;

/** A serializable Drone state. */
public class DroneRemoteState extends MobileRemoteState {

    public DroneRemoteState(String remoteID, Vector location, double battery, boolean active, Vector velocity,
            Vector acceleration) {
        this(remoteID, location, battery, active, null, velocity, acceleration);
    }

    public DroneRemoteState(String remoteID, Vector location, double battery, boolean active,
            Collection<SensorState> sensors, Vector velocity, Vector acceleration) {
        super(RemoteType.DRONE, remoteID, location, battery, active, sensors, velocity, acceleration);
    }

    public DroneRemoteState(JSONOption option) throws JSONException {
        super(option);
    }

}
