package com.seat.rescuesim.common.base;

import java.util.ArrayList;
import java.util.HashMap;

import com.seat.rescuesim.common.json.*;
import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.remote.RemoteState;
import com.seat.rescuesim.common.remote.RemoteType;
import com.seat.rescuesim.common.sensor.SensorState;

/** A serializable Base state. */
public class BaseState extends RemoteState {
    private static final String BASE_TYPE = "base_type";

    private BaseType type;

    public BaseState(JSONObject json) throws JSONException {
        super(json);
    }

    public BaseState(JSONOption option) throws JSONException {
        super(option);
    }

    public BaseState(String encoding) throws JSONException {
        super(encoding);
    }

    public BaseState(BaseType type, String remoteID, Vector location, double battery, ArrayList<SensorState> sensors) {
        super(RemoteType.BASE, remoteID, location, battery, sensors);
        this.type = type;
    }

    public BaseState(BaseType type, String remoteID, Vector location, double battery,
            HashMap<String, SensorState> sensors) {
        super(RemoteType.BASE, remoteID, location, battery, sensors);
        this.type = type;
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        super.decode(json);
        this.type = BaseType.values()[json.getInt(BaseState.BASE_TYPE)];
    }

    public BaseType getSpecType() {
        return this.type;
    }

    public JSONOption toJSON() {
        JSONObjectBuilder json = super.getJSONBuilder();
        json.put(BaseState.BASE_TYPE, this.type.getType());
        return json.toJSON();
    }

    public boolean equals(BaseState state) {
        return super.equals(state) && this.type.equals(state.type);
    }

}
