package com.seat.rescuesim.common.sensor.monitor;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.JSONObjectBuilder;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.sensor.SensorState;

/** A serializable Monitor Sensor state. */
public class MonitorSensorState extends SensorState {
    public static final String MONITOR_ID = "monitor_id";

    private String monitorID;
    private String monitorType;

    public MonitorSensorState(String monitorType, String sensorID, boolean active) {
        this(monitorType, sensorID, active, null);
    }

    public MonitorSensorState(String monitorType, String sensorID, boolean active, String monitorID) {
        super(sensorID, active);
        this.monitorType = monitorType;
        this.monitorID = monitorID;
    }

    public MonitorSensorState(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        super.decode(json);
        this.monitorType = (json.hasKey(MonitorSensorProto.MONITOR_TYPE)) ?
            json.getString(MonitorSensorProto.MONITOR_TYPE) :
            MonitorSensorProto.MONITOR_TYPE;
        this.monitorID = (json.hasKey(MonitorSensorState.MONITOR_ID)) ?
            json.getString(MonitorSensorState.MONITOR_ID) :
            null;
    }

    @Override
    protected JSONObjectBuilder getJSONBuilder() throws JSONException {
        JSONObjectBuilder json = super.getJSONBuilder();
        json.put(MonitorSensorProto.MONITOR_TYPE, this.monitorType);
        if (this.hasMonitorID()) {
            json.put(MonitorSensorState.MONITOR_ID, this.monitorID);
        }
        return json;
    }

    @Override
    public String getLabel() {
        return String.format("%s:<%s>", this.getSensorID(), this.monitorType);
    }

    public String getMonitorID() {
        return (this.hasMonitorID()) ? this.monitorID : "";
    }

    public String getMonitorType() {
        return this.monitorType;
    }

    public boolean hasMonitorID() {
        return !(this.monitorID == null || this.monitorID.isEmpty() || this.monitorID.isBlank());
    }

    public boolean equals(MonitorSensorState state) {
        if (state == null) return false;
        return super.equals(state) && this.monitorType.equals(state.monitorType) &&
            ((this.hasMonitorID() && this.monitorID.equals(state.monitorID)) || this.monitorID == state.monitorID);
    }

}
