package com.seat.sim.common.sensor;

import com.seat.sim.common.json.JSONAble;
import com.seat.sim.common.json.JSONBuilder;
import com.seat.sim.common.json.JSONException;
import com.seat.sim.common.json.JSONObject;
import com.seat.sim.common.json.JSONObjectBuilder;
import com.seat.sim.common.json.JSONOption;

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

    public SensorState(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        this.sensorModel = (json.hasKey(SensorProto.SENSOR_MODEL)) ?
            json.getString(SensorProto.SENSOR_MODEL) :
            SensorProto.DEFAULT_SENSOR_MODEL;
        this.sensorID = json.getString(SensorState.SENSOR_ID);
        this.active = json.getBoolean(SensorState.ACTIVE);
    }

    protected JSONObjectBuilder getJSONBuilder() throws JSONException {
        JSONObjectBuilder json = JSONBuilder.Object();
        json.put(SensorRegistry.SENSOR_TYPE, this.getSensorType());
        if (this.hasSensorModel()) {
            json.put(SensorProto.SENSOR_MODEL, this.sensorModel);
        }
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

    public boolean hasSensorModel() {
        return !(this.sensorModel == null || this.sensorModel.isEmpty() || this.sensorModel.isEmpty() ||
            this.sensorModel.equals(SensorProto.DEFAULT_SENSOR_MODEL));
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
        return this.getSensorType().equals(state.getSensorType()) && this.sensorModel.equals(state.sensorModel) &&
            this.sensorID.equals(state.sensorID) && this.active == state.active;
    }

}
