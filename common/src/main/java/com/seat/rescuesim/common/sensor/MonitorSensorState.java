package com.seat.rescuesim.common.sensor;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.JSONObjectBuilder;
import com.seat.rescuesim.common.json.JSONOption;

public class MonitorSensorState extends SensorState {
    private static final String REMOTE_ID = "remote_id";

    private String remoteID;

    public MonitorSensorState(JSONObject json) throws JSONException {
        super(json);
    }

    public MonitorSensorState(JSONOption option) throws JSONException {
        super(option);
    }

    public MonitorSensorState(String encoding) throws JSONException {
        super(encoding);
    }

    public MonitorSensorState(SensorType type, String sensorID, String remoteID) {
        super(type, sensorID);
        this.remoteID = remoteID;
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        super.decode(json);
        this.remoteID = json.getString(MonitorSensorState.REMOTE_ID);
    }

    public String getRemoteID() {
        return this.remoteID;
    }

    public JSONOption toJSON() {
        JSONObjectBuilder json = super.getJSONBuilder();
        json.put(MonitorSensorState.REMOTE_ID, this.remoteID);
        return json.toJSON();
    }

    public boolean equals(MonitorSensorState state) {
        return super.equals(state) && this.remoteID.equals(state.remoteID);
    }

}
