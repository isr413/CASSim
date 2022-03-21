package com.seat.rescuesim.common.sensor;

import com.seat.rescuesim.common.json.*;

public abstract class SensorState extends JSONAble {
    private static final String ACTIVE = "active";
    private static final String SENSOR_ID = "sensor_id";
    private static final String SENSOR_TYPE = SensorFactory.SENSOR_TYPE;

    protected boolean active;
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

    public SensorState(SensorType type, String sensorID, boolean active) {
        this.type = type;
        this.sensorID = sensorID;
        this.active = active;
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        this.type = SensorType.values()[json.getInt(SensorState.SENSOR_TYPE)];
        this.sensorID = json.getString(SensorState.SENSOR_ID);
        this.active = json.getBoolean(SensorState.ACTIVE);
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
        json.put(SensorState.SENSOR_TYPE, this.type.getType());
        json.put(SensorState.SENSOR_ID, this.sensorID);
        json.put(SensorState.ACTIVE, this.active);
        return json;
    }

    public abstract JSONOption toJSON();

    public boolean equals(SensorState state) {
        return this.type.equals(state.type) && this.sensorID.equals(state.sensorID);
    }

}
