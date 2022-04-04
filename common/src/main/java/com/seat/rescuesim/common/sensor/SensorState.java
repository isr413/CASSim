package com.seat.rescuesim.common.sensor;

import com.seat.rescuesim.common.json.JSONAble;
import com.seat.rescuesim.common.json.JSONBuilder;
import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.JSONObjectBuilder;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.util.SerializableEnum;

public class SensorState extends JSONAble {

    protected boolean active;
    protected String sensorID;
    protected SensorType sensorType;

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
        this.sensorID = json.getString(SensorConst.SENSOR_ID);
        this.active = json.getBoolean(SensorConst.ACTIVE);
    }

    protected JSONObjectBuilder getJSONBuilder() {
        JSONObjectBuilder json = JSONBuilder.Object();
        json.put(SensorConst.SENSOR_TYPE, this.sensorType.getType());
        json.put(SensorConst.SENSOR_ID, this.sensorID);
        json.put(SensorConst.ACTIVE, this.active);
        return json;
    }

    public String getLabel() {
        return String.format("%s%s", this.sensorID, this.sensorType.getLabel());
    }

    public String getSensorID() {
        return this.sensorID;
    }

    public SensorType getSensorType() {
        return this.sensorType;
    }

    public SerializableEnum getSpecType() {
        return SensorType.GENERIC;
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
        return this.sensorType.equals(state.sensorType) && this.sensorID.equals(state.sensorID);
    }

}
