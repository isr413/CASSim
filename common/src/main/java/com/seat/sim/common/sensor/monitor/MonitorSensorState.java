package com.seat.sim.common.sensor.monitor;

import com.seat.sim.common.json.JSONException;
import com.seat.sim.common.json.JSONObject;
import com.seat.sim.common.json.JSONObjectBuilder;
import com.seat.sim.common.json.JSONOption;
import com.seat.sim.common.sensor.SensorState;

/** A serializable Monitor Sensor state. */
public class MonitorSensorState extends SensorState {
    public static final String MONITOR_ID = "monitor_id";

    private String monitorID;

    public MonitorSensorState(String sensorModel, String sensorID, boolean active) {
        this(sensorModel, sensorID, active, null);
    }

    public MonitorSensorState(String sensorModel, String sensorID, boolean active, String monitorID) {
        super(sensorModel, sensorID, active);
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
    protected JSONObjectBuilder getJSONBuilder() throws JSONException {
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