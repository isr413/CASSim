package com.seat.rescuesim.common.sensor;

import com.seat.rescuesim.common.json.JSONAble;
import com.seat.rescuesim.common.json.JSONBuilder;
import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.JSONObjectBuilder;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.json.SerializableEnum;

/** A serializable Sensor Prototype. */
public class SensorSpec extends JSONAble {

    protected double accuracy;
    protected double batteryUsage;
    protected double range;
    protected SensorType sensorType;

    public SensorSpec() {
        this(SensorType.GENERIC);
    }

    public SensorSpec(double range) {
        this(SensorType.GENERIC, range);
    }

    public SensorSpec(double range, double accuracy) {
        this(SensorType.GENERIC, range, accuracy);
    }

    public SensorSpec(double range, double accuracy, double batteryUsage) {
        this(SensorType.GENERIC, range, accuracy, batteryUsage);
    }

    protected SensorSpec(SensorType sensorType) {
        this(sensorType, Double.POSITIVE_INFINITY, 1, 0);
    }

    protected SensorSpec(SensorType sensorType, double range) {
        this(sensorType, range, 1, 0);
    }

    protected SensorSpec(SensorType sensorType, double range, double accuracy) {
        this(sensorType, range, accuracy, 0);
    }

    protected SensorSpec(SensorType sensorType, double range, double accuracy, double batteryUsage) {
        this.sensorType = sensorType;
        this.range = range;
        this.accuracy = accuracy;
        this.batteryUsage = batteryUsage;
    }

    public SensorSpec(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        this.sensorType = SensorType.decodeType(json);
        this.accuracy = json.getDouble(SensorConst.ACCURACY);
        this.range = json.getDouble(SensorConst.RANGE);
        this.batteryUsage = json.getDouble(SensorConst.BATTERY_USAGE);
    }

    protected JSONObjectBuilder getJSONBuilder() {
        JSONObjectBuilder json = JSONBuilder.Object();
        json.put(SensorConst.SENSOR_TYPE, this.sensorType.getType());
        json.put(SensorConst.RANGE, this.range);
        json.put(SensorConst.ACCURACY, this.accuracy);
        json.put(SensorConst.BATTERY_USAGE, this.batteryUsage);
        return json;
    }

    public double getBatteryUsage() {
        return this.batteryUsage;
    }

    public String getLabel() {
        return String.format("s:%s:%s", this.sensorType.getLabel(), this.getSpecType().getLabel());
    }

    public double getSensorAccuracy() {
        return this.accuracy;
    }

    public double getSensorRange() {
        return this.range;
    }

    public SensorType getSensorType() {
        return this.sensorType;
    }

    public SerializableEnum getSpecType() {
        return SensorType.GENERIC;
    }

    public JSONOption toJSON() {
        return this.getJSONBuilder().toJSON();
    }

    public boolean equals(SensorSpec spec) {
        return this.sensorType.equals(spec.sensorType) && this.range == spec.range && this.accuracy == spec.accuracy &&
            this.batteryUsage == spec.batteryUsage;
    }

}
