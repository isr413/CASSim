package com.seat.rescuesim.common.sensor.monitor;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.JSONObjectBuilder;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.sensor.SensorState;
import com.seat.rescuesim.common.sensor.SensorType;

/** A serializable Monitor Sensor state. */
public class MonitorSensorState extends SensorState {
    public static final String MONITOR_ID = "monitor_id";

    private String monitorID;

    public MonitorSensorState(String sensorID) {
        this(MonitorSensorProto.DEFAULT_MONITOR_SENSOR_TYPE, sensorID, SensorState.DEFAULT_ACTIVE, null);
    }

    public MonitorSensorState(String sensorID, boolean active, String monitorID) {
        this(MonitorSensorProto.DEFAULT_MONITOR_SENSOR_TYPE, sensorID, active, monitorID);
    }

    public MonitorSensorState(SensorType sensorType, String sensorID) {
        this(sensorType, sensorID, SensorState.DEFAULT_ACTIVE, null);
    }

    public MonitorSensorState(SensorType sensorType, String sensorID, boolean active, String monitorID) {
        super(sensorType, sensorID, active);
        this.monitorID = monitorID;
    }

    public MonitorSensorState(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        super.decode(json);
        this.monitorID = (json.hasKey(MonitorSensorState.MONITOR_ID)) ?
            json.getString(MonitorSensorState.MONITOR_ID) :
            null;
    }

    @Override
    protected JSONObjectBuilder getJSONBuilder() {
        JSONObjectBuilder json = super.getJSONBuilder();
        if (this.hasMonitorID()) {
            json.put(MonitorSensorState.MONITOR_ID, this.monitorID);
        }
        return json;
    }

    public String getMonitorID() {
        return (this.hasMonitorID()) ? this.monitorID : "";
    }

    public boolean hasMonitorID() {
        return !(this.monitorID == null || this.monitorID.isEmpty() || this.monitorID.isBlank());
    }

    public boolean equals(MonitorSensorState state) {
        if (state == null) return false;
        return super.equals(state) &&
            ((this.hasMonitorID() && this.monitorID.equals(state.monitorID)) || this.monitorID == state.monitorID);
    }

}
