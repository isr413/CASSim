package com.seat.rescuesim.common.sensor;

import com.seat.rescuesim.common.json.JSONAble;
import com.seat.rescuesim.common.json.JSONBuilder;
import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.JSONObjectBuilder;
import com.seat.rescuesim.common.json.JSONOption;

/** A serializable Sensor Prototype. */
public class SensorSpec extends JSONAble {

    private double accuracy;
    private double batteryUsage;
    private double delay;
    private double range;
    private SensorType type;

    public SensorSpec() {
        this(SensorType.None, 0, 0, 0, 0);
    }

    public SensorSpec(SensorType type) {
        this(type, Double.POSITIVE_INFINITY, 1, 0, 0);
    }

    public SensorSpec(SensorType type, double range) {
        this(type, range, 1, 0, 0);
    }

    public SensorSpec(SensorType type, double range, double accuracy) {
        this(type, range, accuracy, 0, 0);
    }

    public SensorSpec(SensorType type, double range, double accuracy, double batteryUsage) {
        this(type, range, accuracy, batteryUsage, 0);
    }

    public SensorSpec(SensorType type, double range, double accuracy, double batteryUsage, double delay) {
        this.type = type;
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
        this.type = SensorType.values()[json.getInt(SensorConst.SENSOR_TYPE)];
        this.accuracy = json.getDouble(SensorConst.ACCURACY);
        this.range = json.getDouble(SensorConst.RANGE);
        this.batteryUsage = json.getDouble(SensorConst.BATTERY_USAGE);
        this.delay = json.getDouble(SensorConst.DELAY);
    }

    public double getBatteryUsage() {
        return this.batteryUsage;
    }

    public String getLabel() {
        return String.format("s%s", this.type.getLabel());
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
        return this.type;
    }

    public JSONOption toJSON() {
        JSONObjectBuilder json = JSONBuilder.Object();
        json.put(SensorConst.SENSOR_TYPE, this.type.getType());
        json.put(SensorConst.RANGE, this.range);
        json.put(SensorConst.ACCURACY, this.accuracy);
        json.put(SensorConst.BATTERY_USAGE, this.batteryUsage);
        json.put(SensorConst.DELAY, this.delay);
        return json.toJSON();
    }

    public boolean equals(SensorSpec spec) {
        return this.type.equals(spec.type) && this.range == spec.range && this.accuracy == spec.accuracy &&
            this.batteryUsage == spec.batteryUsage && this.delay == spec.delay;
    }

}
