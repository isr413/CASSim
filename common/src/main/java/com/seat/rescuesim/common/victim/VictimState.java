package com.seat.rescuesim.common.victim;

import java.util.ArrayList;
import java.util.HashMap;

import com.seat.rescuesim.common.json.*;
import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.remote.KineticRemoteState;
import com.seat.rescuesim.common.remote.RemoteType;
import com.seat.rescuesim.common.sensor.SensorState;

/** A serializable Victim state. */
public class VictimState extends KineticRemoteState {
    private static final String VICTIM_TYPE = "victim_type";

    private VictimType type;

    public VictimState(JSONObject json) {
        super(json);
    }

    public VictimState(JSONOption option) {
        super(option);
    }

    public VictimState(String encoding) {
        super(encoding);
    }

    public VictimState(VictimType type, String remoteID, Vector location, double battery,
            ArrayList<SensorState> sensors, Vector velocity, Vector acceleration) {
        super(RemoteType.VICTIM, remoteID, location, battery, sensors, velocity, acceleration);
        this.type = type;
    }

    public VictimState(VictimType type, String remoteID, Vector location, double battery,
            HashMap<String, SensorState> sensors, Vector velocity, Vector acceleration) {
        super(RemoteType.VICTIM, remoteID, location, battery, sensors, velocity, acceleration);
        this.type = type;
    }

    @Override
    protected void decode(JSONObject json) {
        super.decode(json);
        this.type = VictimType.values()[json.getInt(VictimState.VICTIM_TYPE)];
    }

    public VictimType getSpecType() {
        return this.type;
    }

    public JSONOption toJSON() {
        JSONObjectBuilder json = super.getJSONBuilder();
        json.put(VictimState.VICTIM_TYPE, this.type.getType());
        return json.toJSON();
    }

    public boolean equals(VictimState state) {
        return super.equals(state) && this.type.equals(state.type);
    }

}
