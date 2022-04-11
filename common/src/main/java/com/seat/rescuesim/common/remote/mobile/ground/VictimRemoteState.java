package com.seat.rescuesim.common.remote.mobile.ground;

import java.util.Collection;

import com.seat.rescuesim.common.core.TeamColor;
import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.remote.mobile.MobileRemoteState;
import com.seat.rescuesim.common.sensor.SensorState;

/** A serializable Victim state. */
public class VictimRemoteState extends MobileRemoteState {

    public VictimRemoteState(String remoteID, TeamColor team, Vector location, double battery, boolean active,
            Vector velocity, Vector acceleration) {
        this(remoteID, team, location, battery, active, null, velocity, acceleration);
    }

    public VictimRemoteState(String remoteID, TeamColor team, Vector location, double battery, boolean active,
            Collection<SensorState> sensors, Vector velocity, Vector acceleration) {
        super(remoteID, team, location, battery, active, sensors, velocity, acceleration);
    }

    public VictimRemoteState(JSONOption option) throws JSONException {
        super(option);
    }

}
