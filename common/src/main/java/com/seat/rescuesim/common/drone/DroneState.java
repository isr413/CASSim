package com.seat.rescuesim.common.drone;

import java.util.ArrayList;
import java.util.HashMap;

import com.seat.rescuesim.common.json.*;
import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.remote.KineticRemoteState;
import com.seat.rescuesim.common.remote.RemoteType;
import com.seat.rescuesim.common.sensor.SensorState;

/** A serializable Drone state. */
public class DroneState extends KineticRemoteState {
    private static final String DRONE_TYPE = "drone_type";

    private DroneType type;

    public DroneState(JSONObject json) {
        super(json);
    }

    public DroneState(JSONOption option) {
        super(option);
    }

    public DroneState(String encoding) {
        super(encoding);
    }

    public DroneState(DroneType type, String remoteID, Vector location, double battery, ArrayList<SensorState> sensors,
            Vector velocity, Vector acceleration) {
        super(RemoteType.DRONE, remoteID, location, battery, sensors, velocity, acceleration);
        this.type = type;
    }

    public DroneState(DroneType type, String remoteID, Vector location, double battery,
            HashMap<String, SensorState> sensors, Vector velocity, Vector acceleration) {
        super(RemoteType.DRONE, remoteID, location, battery, sensors, velocity, acceleration);
        this.type = type;
    }

    @Override
    protected void decode(JSONObject json) {
        super.decode(json);
        this.type = DroneType.values()[json.getInt(DroneState.DRONE_TYPE)];
    }

    public DroneType getSpecType() {
        return this.type;
    }

    public JSONOption toJSON() {
        JSONObjectBuilder json = super.getJSONBuilder();
        json.put(DroneState.DRONE_TYPE, this.type.getType());
        return json.toJSON();
    }

    public boolean equals(DroneState state) {
        return super.equals(state) && this.type.equals(state.type);
    }

}
