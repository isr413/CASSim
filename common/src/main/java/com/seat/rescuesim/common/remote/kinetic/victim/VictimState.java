package com.seat.rescuesim.common.remote.kinetic.victim;

import java.util.ArrayList;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.remote.kinetic.KineticRemoteState;
import com.seat.rescuesim.common.remote.kinetic.KineticRemoteType;
import com.seat.rescuesim.common.sensor.SensorState;

/** A serializable Victim state. */
public class VictimState extends KineticRemoteState {

    public VictimState(String remoteID, Vector location, double battery, Vector velocity, Vector acceleration) {
        this(remoteID, location, battery, new ArrayList<SensorState>(), velocity, acceleration);
    }

    public VictimState(String remoteID, Vector location, double battery, ArrayList<SensorState> sensors,
            Vector velocity, Vector acceleration) {
        super(KineticRemoteType.VICTIM, remoteID, location, battery, sensors, velocity, acceleration);
    }

    public VictimState(JSONOption option) throws JSONException {
        super(option);
    }

}
