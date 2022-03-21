package com.seat.rescuesim.common.sensor;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.JSONObjectBuilder;
import com.seat.rescuesim.common.json.JSONOption;

public class MonitorSensorState extends SensorState {

    private String monitorID;

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

    public MonitorSensorState(SensorType type, String sensorID, boolean active, String monitorID) {
        super(type, sensorID, active);
        this.monitorID = monitorID;
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        super.decode(json);
        if (json.hasKey(SensorConst.MONITOR_ID)) {
            this.monitorID = json.getString(SensorConst.MONITOR_ID);
        } else {
            this.monitorID = "";
        }
    }

    public String getMonitorID() {
        return this.monitorID;
    }

    public boolean hasMonitorID() {
        return !(this.monitorID == null || this.monitorID.isEmpty() || this.monitorID.isBlank());
    }

    public JSONOption toJSON() {
        JSONObjectBuilder json = super.getJSONBuilder();
        if (this.hasMonitorID()) {
            json.put(SensorConst.MONITOR_ID, this.monitorID);
        }
        return json.toJSON();
    }

}
