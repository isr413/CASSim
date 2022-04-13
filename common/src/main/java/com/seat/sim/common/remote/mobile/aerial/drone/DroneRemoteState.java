package com.seat.sim.common.remote.mobile.aerial.drone;

import java.util.Collection;

import com.seat.sim.common.core.TeamColor;
import com.seat.sim.common.json.JSONException;
import com.seat.sim.common.json.JSONOption;
import com.seat.sim.common.math.Vector;
import com.seat.sim.common.remote.mobile.MobileRemoteState;
import com.seat.sim.common.sensor.SensorState;

/** A serializable state of a Drone Remote. */
public class DroneRemoteState extends MobileRemoteState {

    public DroneRemoteState(String remoteID, TeamColor team, Vector location, double battery, boolean active,
            Vector velocity, Vector acceleration) {
        this(remoteID, team, location, battery, active, null, velocity, acceleration);
    }

    public DroneRemoteState(String remoteID, TeamColor team, Vector location, double battery, boolean active,
            Collection<SensorState> sensors, Vector velocity, Vector acceleration) {
        super(remoteID, team, location, battery, active, sensors, velocity, acceleration);
    }

    public DroneRemoteState(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    public String getLabel() {
        return String.format("%s:<drone>", this.getRemoteID());
    }

}