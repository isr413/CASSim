package com.seat.sim.common.sensor;

import com.seat.sim.common.json.JSONAble;
import com.seat.sim.common.json.JSONBuilder;
import com.seat.sim.common.json.JSONException;
import com.seat.sim.common.json.JSONObject;
import com.seat.sim.common.json.JSONObjectBuilder;
import com.seat.sim.common.json.JSONOptional;

/** A serializable generic Sensor state. */
public class SensorState extends JSONAble {
    public static final String ACTIVE = "active";
    public static final String SENSOR_ID = "sensor_id";

    private boolean active;
    private String sensorID;
    private String sensorModel;

    public SensorState(String sensorModel, String sensorID, boolean active) {
        this.sensorModel = sensorModel;
        this.sensorID = sensorID;
        this.active = active;
    }

    public SensorState(JSONOptional optional) throws JSONException {
        super(optional);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        this.sensorModel = json.getString(SensorProto.SENSOR_MODEL);
        this.sensorID = json.getString(SensorState.SENSOR_ID);
        this.active = json.getBoolean(SensorState.ACTIVE);
    }

    protected JSONObjectBuilder getJSONBuilder() throws JSONException {
        JSONObjectBuilder json = JSONBuilder.Object();
        json.put(SensorRegistry.SENSOR_TYPE, this.getSensorType());
        json.put(SensorProto.SENSOR_MODEL, this.sensorModel);
        json.put(SensorState.SENSOR_ID, this.sensorID);
        json.put(SensorState.ACTIVE, this.active);
        return json;
    }

    public String getLabel() {
        return String.format("%s:<%s>", this.sensorID, this.sensorModel);
    }

    public String getSensorID() {
        return this.sensorID;
    }

    public String getSensorModel() {
        return this.sensorModel;
    }

    public String getSensorType() {
        return this.getClass().getName();
    }

    public boolean isActive() {
        return this.active;
    }

    public JSONOptional toJSON() throws JSONException {
        return this.getJSONBuilder().toJSON();
    }

    public boolean equals(SensorState state) {
        if (state == null) return false;
        return this.getSensorType().equals(state.getSensorType()) && this.sensorModel.equals(state.sensorModel) &&
            this.sensorID.equals(state.sensorID) && this.active == state.active;
    }

}
