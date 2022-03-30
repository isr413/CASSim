package com.seat.rescuesim.common.sensor;

import com.seat.rescuesim.common.json.JSONAble;
import com.seat.rescuesim.common.json.JSONBuilder;
import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.JSONObjectBuilder;
import com.seat.rescuesim.common.json.JSONOption;

public abstract class SensorState extends JSONAble {

    protected boolean active;
    protected String sensorID;
    protected SensorType type;

    public SensorState(SensorType type, String sensorID, boolean active) {
        this.type = type;
        this.sensorID = sensorID;
        this.active = active;
    }

    public SensorState(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        this.type = SensorType.values()[json.getInt(SensorConst.SENSOR_TYPE)];
        this.sensorID = json.getString(SensorConst.SENSOR_ID);
        this.active = json.getBoolean(SensorConst.ACTIVE);
    }

    public String getSensorID() {
        return this.sensorID;
    }

    public SensorType getSpecType() {
        return this.type;
    }

    public boolean isActive() {
        return this.active;
    }

    public boolean isInactive() {
        return !this.isActive();
    }

    protected JSONObjectBuilder getJSONBuilder() {
        JSONObjectBuilder json = JSONBuilder.Object();
        json.put(SensorConst.SENSOR_TYPE, this.type.getType());
        json.put(SensorConst.SENSOR_ID, this.sensorID);
        json.put(SensorConst.ACTIVE, this.active);
        return json;
    }

    public abstract JSONOption toJSON();

    public boolean equals(SensorState state) {
        return this.type.equals(state.type) && this.sensorID.equals(state.sensorID);
    }

}
