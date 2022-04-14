package com.seat.sim.common.remote.mobile.aerial;

import java.util.Collection;

import com.seat.sim.common.core.TeamColor;
import com.seat.sim.common.json.JSONException;
import com.seat.sim.common.json.JSONOptional;
import com.seat.sim.common.math.Vector;
import com.seat.sim.common.remote.mobile.MobileRemoteState;
import com.seat.sim.common.sensor.SensorState;

/** A serializable state of an Aerial Remote. */
public class AerialRemoteState extends MobileRemoteState {

    public AerialRemoteState(String remoteID, TeamColor team, Vector location, double battery, boolean active,
            Vector velocity, Vector acceleration) {
        this(remoteID, team, location, battery, active, null, velocity, acceleration);
    }

    public AerialRemoteState(String remoteID, TeamColor team, Vector location, double battery, boolean active,
            Collection<SensorState> sensors, Vector velocity, Vector acceleration) {
        super(remoteID, team, location, battery, active, sensors, velocity, acceleration);
    }

    public AerialRemoteState(JSONOptional optional) throws JSONException {
        super(optional);
    }

    @Override
    public String getLabel() {
        return String.format("%s:<aerial>", this.getRemoteID());
    }

}
