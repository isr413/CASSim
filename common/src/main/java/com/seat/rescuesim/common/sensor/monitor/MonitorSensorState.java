package com.seat.rescuesim.common.sensor.monitor;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.JSONObjectBuilder;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.sensor.SensorState;
import com.seat.rescuesim.common.sensor.SensorType;

public class MonitorSensorState extends SensorState {
    public static final String MONITOR_ID = "monitor_id";

    protected static final MonitorSensorType DEFAULT_SPEC_TYPE = MonitorSensorType.GENERIC;

    private String monitorID;
    private MonitorSensorType specType;

    public MonitorSensorState(String sensorID) {
        this(MonitorSensorState.DEFAULT_SPEC_TYPE, sensorID, SensorState.DEFAULT_ACTIVE, null);
    }

    public MonitorSensorState(String sensorID, boolean active, String monitorID) {
        this(MonitorSensorState.DEFAULT_SPEC_TYPE, sensorID, active, monitorID);
    }

    public MonitorSensorState(MonitorSensorType specType, String sensorID) {
        this(specType, sensorID, SensorState.DEFAULT_ACTIVE, null);
    }

    public MonitorSensorState(MonitorSensorType specType, String sensorID, boolean active, String monitorID) {
        super(SensorType.MONITOR, sensorID, active);
        this.specType = specType;
        this.monitorID = monitorID;
    }

    public MonitorSensorState(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        super.decode(json);
        this.specType = MonitorSensorType.decodeType(json);
        if (json.hasKey(MonitorSensorState.MONITOR_ID)) {
            this.monitorID = json.getString(MonitorSensorState.MONITOR_ID);
        } else {
            this.monitorID = null;
        }
    }

    @Override
    protected JSONObjectBuilder getJSONBuilder() {
        JSONObjectBuilder json = super.getJSONBuilder();
        json.put(MonitorSensorType.MONITOR_SENSOR_TYPE, this.specType.getType());
        if (this.hasMonitorID()) {
            json.put(MonitorSensorState.MONITOR_ID, this.monitorID);
        }
        return json;
    }

    public String getMonitorID() {
        return (this.hasMonitorID()) ? this.monitorID : "";
    }

    @Override
    public MonitorSensorType getSpecType() {
        return this.specType;
    }

    public boolean hasMonitorID() {
        return !(this.monitorID == null || this.monitorID.isEmpty() || this.monitorID.isBlank());
    }

    public boolean equals(MonitorSensorState state) {
        return super.equals(state) && this.specType.equals(state.specType) && this.monitorID.equals(state.monitorID);
    }

}
