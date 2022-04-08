package com.seat.rescuesim.common.remote.mobile.aerial;

import java.util.Collection;

import com.seat.rescuesim.common.core.TeamColor;
import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.remote.RemoteType;
import com.seat.rescuesim.common.remote.mobile.MobileRemoteState;
import com.seat.rescuesim.common.sensor.SensorState;

/** A serializable Drone state. */
public class DroneRemoteState extends MobileRemoteState {

    public DroneRemoteState(String remoteID, TeamColor team, Vector location, double battery, boolean active,
            Vector velocity, Vector acceleration) {
        this(remoteID, team, location, battery, active, null, velocity, acceleration);
    }

    public DroneRemoteState(String remoteID, TeamColor team, Vector location, double battery, boolean active,
            Collection<SensorState> sensors, Vector velocity, Vector acceleration) {
        super(RemoteType.DRONE, remoteID, team, location, battery, active, sensors, velocity, acceleration);
    }

    public DroneRemoteState(JSONOption option) throws JSONException {
        super(option);
    }

}
