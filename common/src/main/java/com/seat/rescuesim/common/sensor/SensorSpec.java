package com.seat.rescuesim.common.sensor;

import com.seat.rescuesim.common.json.JSONAble;
import com.seat.rescuesim.common.json.JSONBuilder;
import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.JSONObjectBuilder;
import com.seat.rescuesim.common.json.JSONOption;

/** A serializable Sensor Prototype. */
public class SensorSpec extends JSONAble {

    protected double accuracy;
    protected double batteryUsage;
    protected double delay;
    protected double range;
    protected SensorType sensorType;

    public SensorSpec(SensorType sensorType) {
        this(sensorType, Double.POSITIVE_INFINITY, 1, 0, 0);
    }

    public SensorSpec(SensorType sensorType, double range) {
        this(sensorType, range, 1, 0, 0);
    }

    public SensorSpec(SensorType sensorType, double range, double accuracy) {
        this(sensorType, range, accuracy, 0, 0);
    }

    public SensorSpec(SensorType sensorType, double range, double accuracy, double batteryUsage) {
        this(sensorType, range, accuracy, batteryUsage, 0);
    }

    public SensorSpec(SensorType sensorType, double range, double accuracy, double batteryUsage, double delay) {
        this.sensorType = sensorType;
        this.range = range;
        this.accuracy = accuracy;
        this.batteryUsage = batteryUsage;
        this.delay = delay;
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
        this.delay = json.getDouble(SensorConst.DELAY);
    }

    public double getBatteryUsage() {
        return this.batteryUsage;
    }

    public String getLabel() {
        return String.format("s%s", this.sensorType.getLabel());
    }

    public double getSensorAccuracy() {
        return this.accuracy;
    }

    public double getSensorDelay() {
        return this.delay;
    }

    public double getSensorRange() {
        return this.range;
    }

    public SensorType getSpecType() {
        return this.sensorType;
    }

    public JSONOption toJSON() {
        JSONObjectBuilder json = JSONBuilder.Object();
        json.put(SensorConst.SENSOR_TYPE, this.sensorType.getType());
        json.put(SensorConst.RANGE, this.range);
        json.put(SensorConst.ACCURACY, this.accuracy);
        json.put(SensorConst.BATTERY_USAGE, this.batteryUsage);
        json.put(SensorConst.DELAY, this.delay);
        return json.toJSON();
    }

    public boolean equals(SensorSpec spec) {
        return this.sensorType.equals(spec.sensorType) && this.range == spec.range && this.accuracy == spec.accuracy &&
            this.batteryUsage == spec.batteryUsage && this.delay == spec.delay;
    }

}
