package com.seat.rescuesim.common.sensor;

import com.seat.rescuesim.common.json.JSONAble;
import com.seat.rescuesim.common.json.JSONBuilder;
import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.JSONObjectBuilder;
import com.seat.rescuesim.common.json.JSONOption;

/** A serializable generic Sensor state. */
public class SensorState extends JSONAble {
    public static final String ACTIVE = "active";
    public static final String SENSOR_ID = "sensor_id";

    private boolean active;
    private String sensorID;

    public SensorState(String sensorID, boolean active) {
        this.sensorID = sensorID;
        this.active = active;
    }

    public SensorState(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        this.sensorID = json.getString(SensorState.SENSOR_ID);
        this.active = json.getBoolean(SensorState.ACTIVE);
    }

    protected JSONObjectBuilder getJSONBuilder() throws JSONException {
        JSONObjectBuilder json = JSONBuilder.Object();
        json.put(SensorRegistry.SENSOR_TYPE, this.getSensorType());
        json.put(SensorState.SENSOR_ID, this.sensorID);
        json.put(SensorState.ACTIVE, this.active);
        return json;
    }

    public String getLabel() {
        return String.format("%s:<sensor>", this.sensorID);
    }

    public String getSensorID() {
        return this.sensorID;
    }

    public String getSensorType() {
        return this.getClass().getName();
    }

    public boolean isActive() {
        return this.active;
    }

    public boolean isInactive() {
        return !this.isActive();
    }

    public JSONOption toJSON() throws JSONException {
        return this.getJSONBuilder().toJSON();
    }

    public boolean equals(SensorState state) {
        if (state == null) return false;
        return this.getSensorType().equals(state.getSensorType()) && this.sensorID.equals(state.sensorID) &&
            this.active == state.active;
    }

}
