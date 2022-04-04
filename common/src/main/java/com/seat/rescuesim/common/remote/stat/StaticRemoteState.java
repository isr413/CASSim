package com.seat.rescuesim.common.remote.stat;

import java.util.Collection;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.JSONObjectBuilder;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.remote.RemoteState;
import com.seat.rescuesim.common.remote.RemoteType;
import com.seat.rescuesim.common.sensor.SensorState;

/** A serializable state of a static Remote. */
public class StaticRemoteState extends RemoteState {

    protected StaticRemoteType specType;

    public StaticRemoteState(String remoteID, Vector location, double battery) {
        this(StaticRemoteType.GENERIC, remoteID, location, battery);
    }

    public StaticRemoteState(String remoteID, Vector location, double battery, Collection<SensorState> sensors) {
        this(StaticRemoteType.GENERIC, remoteID, location, battery, sensors);
    }

    public StaticRemoteState(StaticRemoteType specType, String remoteID, Vector location, double battery) {
        super(RemoteType.STATIC, remoteID, location, battery);
        this.specType = specType;
    }

    public StaticRemoteState(StaticRemoteType specType, String remoteID, Vector location, double battery,
            Collection<SensorState> sensors) {
        super(RemoteType.STATIC, remoteID, location, battery, sensors);
        this.specType = specType;
    }

    public StaticRemoteState(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        super.decode(json);
        this.specType = StaticRemoteType.decodeType(json);
    }

    @Override
    protected JSONObjectBuilder getJSONBuilder() {
        JSONObjectBuilder json = super.getJSONBuilder();
        json.put(StaticRemoteConst.STATIC_REMOTE_TYPE, this.specType.getType());
        return json;
    }

    @Override
    public StaticRemoteType getSpecType() {
        return this.specType;
    }

    public boolean equals(StaticRemoteState state) {
        return super.equals(state) && this.specType.equals(state.specType);
    }

}
