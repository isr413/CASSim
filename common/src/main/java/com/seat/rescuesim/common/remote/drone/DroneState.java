package com.seat.rescuesim.common.remote.drone;

import java.util.ArrayList;
import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.JSONObjectBuilder;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.remote.KineticRemoteState;
import com.seat.rescuesim.common.remote.RemoteType;
import com.seat.rescuesim.common.sensor.SensorState;

/** A serializable Drone state. */
public class DroneState extends KineticRemoteState {

    private DroneType type;

    public DroneState(DroneType type, String remoteID, Vector location, double battery, Vector velocity,
            Vector acceleration) {
        this(type, remoteID, location, battery, new ArrayList<SensorState>(), velocity, acceleration);
    }

    public DroneState(DroneType type, String remoteID, Vector location, double battery, ArrayList<SensorState> sensors,
            Vector velocity, Vector acceleration) {
        super(RemoteType.DRONE, remoteID, location, battery, sensors, velocity, acceleration);
        this.type = type;
    }

    public DroneState(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        super.decode(json);
        this.type = DroneType.values()[json.getInt(DroneConst.DRONE_TYPE)];
    }

    public DroneType getSpecType() {
        return this.type;
    }

    public JSONOption toJSON() {
        JSONObjectBuilder json = super.getJSONBuilder();
        json.put(DroneConst.DRONE_TYPE, this.type.getType());
        return json.toJSON();
    }

    public boolean equals(DroneState state) {
        return super.equals(state) && this.type.equals(state.type);
    }

}
