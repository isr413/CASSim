package com.seat.rescuesim.common.remote.base;

import java.util.Collection;

import com.seat.rescuesim.common.core.TeamColor;
import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.remote.RemoteState;
import com.seat.rescuesim.common.sensor.SensorState;

/** A serializable Base state. */
public class BaseRemoteState extends RemoteState {

    public BaseRemoteState(String remoteID, TeamColor team, Vector location, double battery, boolean active) {
        this(remoteID, team, location, battery, active, null);
    }

    public BaseRemoteState(String remoteID, TeamColor team, Vector location, double battery, boolean active,
            Collection<SensorState> sensors) {
        super(remoteID, team, location, battery, active, sensors);
    }

    public BaseRemoteState(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    public String getLabel() {
        return String.format("%s:<base>", this.getRemoteID());
    }

}
