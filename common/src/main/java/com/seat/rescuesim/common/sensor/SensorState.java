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
    private SensorType sensorType;

    public SensorState(String sensorID, boolean active) {
        this(SensorProto.DEFAULT_SENSOR_TYPE, sensorID, active);
    }

    public SensorState(SensorType sensorType, String sensorID, boolean active) {
        this.sensorType = sensorType;
        this.sensorID = sensorID;
        this.active = active;
    }

    public SensorState(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        this.sensorType = SensorType.decodeType(json);
        this.sensorID = json.getString(SensorState.SENSOR_ID);
        this.active = json.getBoolean(SensorState.ACTIVE);
    }

    protected JSONObjectBuilder getJSONBuilder() {
        JSONObjectBuilder json = JSONBuilder.Object();
        json.put(SensorType.SENSOR_TYPE, this.sensorType.getType());
        json.put(SensorState.SENSOR_ID, this.sensorID);
        json.put(SensorState.ACTIVE, this.active);
        return json;
    }

    public String getLabel() {
        return String.format("%s:%s", this.sensorID, this.sensorType.getLabel());
    }

    public String getSensorID() {
        return this.sensorID;
    }

    public SensorType getSensorType() {
        return this.sensorType;
    }

    public boolean isActive() {
        return this.active;
    }

    public boolean isInactive() {
        return !this.isActive();
    }

    public JSONOption toJSON() {
        return this.getJSONBuilder().toJSON();
    }

    public boolean equals(SensorState state) {
        if (state == null) return false;
        return this.sensorType.equals(state.sensorType) && this.sensorID.equals(state.sensorID) &&
            this.active == state.active;
    }

}
