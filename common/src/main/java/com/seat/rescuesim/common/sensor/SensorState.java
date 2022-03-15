package com.seat.rescuesim.common.sensor;

import com.seat.rescuesim.common.json.*;

public class SensorState extends JSONAble {
    private static final String SENSOR_DATA = "data";
    private static final String SENSOR_ID = "sensor_id";
    private static final String SENSOR_TYPE = "sensor_type";

    private String data;
    protected String sensorID;
    protected SensorType type;

    public SensorState(JSONObject json) {
        super(json);
    }

    public SensorState(JSONOption option) {
        super(option);
    }

    public SensorState(String encoding) {
        super(encoding);
    }

    public SensorState(SensorType type, String sensorID) {
        this(type, sensorID, "");
    }

    public SensorState(SensorType type, String sensorID, String data) {
        this.type = type;
        this.sensorID = sensorID;
        this.data = data;
    }

    @Override
    protected void decode(JSONObject json) {
        this.type = SensorType.values()[json.getInt(SensorState.SENSOR_TYPE)];
        this.sensorID = json.getString(SensorState.SENSOR_ID);
        if (json.hasKey(SensorState.SENSOR_DATA)) {
            this.data = json.getString(SensorState.SENSOR_DATA);
        } else {
            this.data = "";
        }
    }

    public String getData() {
        return this.data;
    }

    public String getSensorID() {
        return this.sensorID;
    }

    public SensorType getSpecType() {
        return this.type;
    }

    public boolean hasData() {
        return !this.data.isEmpty();
    }

    @Override
    public JSONOption toJSON() {
        JSONObjectBuilder json = JSONBuilder.Object();
        json.put(SensorState.SENSOR_TYPE, this.type.getType());
        json.put(SensorState.SENSOR_ID, this.sensorID);
        if (this.hasData()) {
            json.put(SensorState.SENSOR_DATA, this.data);
        }
        return json.toJSON();
    }

    public boolean equals(SensorState state) {
        return this.type.equals(state.type) && this.sensorID.equals(state.sensorID) && this.data.equals(state.data);
    }

}
