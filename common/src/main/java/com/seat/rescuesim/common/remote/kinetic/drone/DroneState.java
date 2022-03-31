package com.seat.rescuesim.common.remote.kinetic.drone;

import java.util.ArrayList;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.remote.kinetic.KineticRemoteState;
import com.seat.rescuesim.common.remote.kinetic.KineticRemoteType;
import com.seat.rescuesim.common.sensor.SensorState;

/** A serializable Drone state. */
public class DroneState extends KineticRemoteState {

    public DroneState(String remoteID, Vector location, double battery, Vector velocity, Vector acceleration) {
        this(remoteID, location, battery, new ArrayList<SensorState>(), velocity, acceleration);
    }

    public DroneState(String remoteID, Vector location, double battery, ArrayList<SensorState> sensors, Vector velocity,
            Vector acceleration) {
        super(KineticRemoteType.DRONE, remoteID, location, battery, sensors, velocity, acceleration);
    }

    public DroneState(JSONOption option) throws JSONException {
        super(option);
    }

}