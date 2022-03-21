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

    public MonitorSensorState(SensorType type, String sensorID) {
        this(type, sensorID, false, "");
    }

    public MonitorSensorState(SensorType type, String sensorID, boolean active, String remoteID) {
        super(type, sensorID, active);
        this.remoteID = remoteID;
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        super.decode(json);
        if (json.hasKey(MonitorSensorState.REMOTE_ID)) {
            this.remoteID = json.getString(MonitorSensorState.REMOTE_ID);
        } else {
            this.remoteID = "";
        }
    }

    public String getRemoteID() {
        return this.remoteID;
    }

    public boolean hasRemoteID() {
        return !(this.remoteID == null || this.remoteID.isEmpty() || this.remoteID.isBlank());
    }

    public JSONOption toJSON() {
        JSONObjectBuilder json = super.getJSONBuilder();
        if (this.hasRemoteID()) {
            json.put(MonitorSensorState.REMOTE_ID, this.remoteID);
        }
        return json.toJSON();
    }

}
