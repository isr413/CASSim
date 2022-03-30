package com.seat.rescuesim.common.base;

import java.util.ArrayList;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.JSONObjectBuilder;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.remote.RemoteState;
import com.seat.rescuesim.common.remote.RemoteType;
import com.seat.rescuesim.common.sensor.SensorState;

/** A serializable Base state. */
public class BaseState extends RemoteState {

    private BaseType type;

    public BaseState(BaseType type, String remoteID, Vector location, double battery) {
        this(type, remoteID, location, battery, new ArrayList<SensorState>());
    }

    public BaseState(BaseType type, String remoteID, Vector location, double battery, ArrayList<SensorState> sensors) {
        super(RemoteType.BASE, remoteID, location, battery, sensors);
        this.type = type;
    }

    public BaseState(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        super.decode(json);
        this.type = BaseType.values()[json.getInt(BaseConst.BASE_TYPE)];
    }

    public BaseType getSpecType() {
        return this.type;
    }

    public JSONOption toJSON() {
        JSONObjectBuilder json = super.getJSONBuilder();
        json.put(BaseConst.BASE_TYPE, this.type.getType());
        return json.toJSON();
    }

    public boolean equals(BaseState state) {
        return super.equals(state) && this.type.equals(state.type);
    }

}
