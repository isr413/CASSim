package com.seat.rescuesim.common.victim;

import java.util.ArrayList;
import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.JSONObjectBuilder;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.remote.KineticRemoteState;
import com.seat.rescuesim.common.remote.RemoteType;
import com.seat.rescuesim.common.sensor.SensorState;

/** A serializable Victim state. */
public class VictimState extends KineticRemoteState {

    private VictimType type;

    public VictimState(VictimType type, String remoteID, Vector location, double battery, Vector velocity,
            Vector acceleration) {
        this(type, remoteID, location, battery, new ArrayList<SensorState>(), velocity, acceleration);
    }

    public VictimState(VictimType type, String remoteID, Vector location, double battery,
            ArrayList<SensorState> sensors, Vector velocity, Vector acceleration) {
        super(RemoteType.VICTIM, remoteID, location, battery, sensors, velocity, acceleration);
        this.type = type;
    }

    public VictimState(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        super.decode(json);
        this.type = VictimType.values()[json.getInt(VictimConst.VICTIM_TYPE)];
    }

    public VictimType getSpecType() {
        return this.type;
    }

    public JSONOption toJSON() {
        JSONObjectBuilder json = super.getJSONBuilder();
        json.put(VictimConst.VICTIM_TYPE, this.type.getType());
        return json.toJSON();
    }

    public boolean equals(VictimState state) {
        return super.equals(state) && this.type.equals(state.type);
    }

}
