package com.seat.sim.common.remote.base;

import java.util.Collection;

import com.seat.sim.common.core.TeamColor;
import com.seat.sim.common.json.JSONException;
import com.seat.sim.common.json.JSONOptional;
import com.seat.sim.common.math.Vector;
import com.seat.sim.common.remote.RemoteState;
import com.seat.sim.common.sensor.SensorState;

/** A serializable state of a Base Remote. */
public class BaseRemoteState extends RemoteState {

    public BaseRemoteState(String remoteID, TeamColor team, Vector location, double battery, boolean active) {
        super(remoteID, team, location, battery, active);
    }

    public BaseRemoteState(String remoteID, TeamColor team, Vector location, double battery, boolean active,
            Collection<SensorState> sensors) {
        super(remoteID, team, location, battery, active, sensors);
    }

    public BaseRemoteState(JSONOptional optional) throws JSONException {
        super(optional);
    }

    @Override
    public String getLabel() {
        return String.format("%s:<base>", this.getRemoteID());
    }

}
