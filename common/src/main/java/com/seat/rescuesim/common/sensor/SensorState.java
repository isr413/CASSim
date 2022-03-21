package com.seat.rescuesim.common.sensor;

import com.seat.rescuesim.common.json.*;
import com.seat.rescuesim.common.util.Debugger;

public abstract class SensorState extends JSONAble {
    private static final String SENSOR_ID = "sensor_id";
    private static final String SENSOR_TYPE = "sensor_type";

    public static SensorType decodeSensorType(JSONObject json) throws JSONException {
        return SensorType.values()[json.getInt(SensorState.SENSOR_TYPE)];
    }

    public static SensorType decodeSensorType(JSONOption option) throws JSONException {
        if (option.isSomeObject()) {
            return SensorState.decodeSensorType(option.someObject());
        }
        Debugger.logger.err(String.format("Cannot decode sensor type of %s", option.toString()));
        return null;
    }

    public static SensorType decodeSensorType(String encoding) throws JSONException {
        return SensorState.decodeSensorType(JSONOption.String(encoding));
    }

    protected String sensorID;
    protected SensorType type;

    public SensorState(JSONObject json) throws JSONException {
        super(json);
    }

    public SensorState(JSONOption option) throws JSONException {
        super(option);
    }

    public SensorState(String encoding) throws JSONException {
        super(encoding);
    }

    public SensorState(SensorType type, String sensorID) {
        this.type = type;
        this.sensorID = sensorID;
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        this.type = SensorType.values()[json.getInt(SensorState.SENSOR_TYPE)];
        this.sensorID = json.getString(SensorState.SENSOR_ID);
    }

    public String getSensorID() {
        return this.sensorID;
    }

    public SensorType getSpecType() {
        return this.type;
    }

    protected JSONObjectBuilder getJSONBuilder() {
        JSONObjectBuilder json = JSONBuilder.Object();
        json.put(SensorState.SENSOR_TYPE, this.type.getType());
        json.put(SensorState.SENSOR_ID, this.sensorID);
        return json;
    }

    public abstract JSONOption toJSON();

    public boolean equals(SensorState state) {
        return this.type.equals(state.type) && this.sensorID.equals(state.sensorID);
    }

}
