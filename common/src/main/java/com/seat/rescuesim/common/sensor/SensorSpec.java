package com.seat.rescuesim.common.sensor;

import com.seat.rescuesim.common.json.*;

/** A serializable Sensor Prototype. */
public class SensorSpec extends JSONAble {
    private static final String SENSOR_ACCURACY = "accuracy";
    private static final String SENSOR_BATTERY_USAGE = "battery_usage";
    private static final String SENSOR_DELAY = "delay";
    private static final String SENSOR_RANGE = "range";
    private static final String SENSOR_TYPE = SensorFactory.SENSOR_TYPE;

    private double accuracy;
    private double batteryUsage;
    private double delay;
    private double range;
    private SensorType type;

    public SensorSpec(JSONObject json) throws JSONException {
        super(json);
    }

    public SensorSpec(JSONOption option) throws JSONException {
        super(option);
    }

    public SensorSpec(String encoding) throws JSONException {
        super(encoding);
    }

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

    @Override
    protected void decode(JSONObject json) throws JSONException {
        this.type = SensorType.values()[json.getInt(SensorSpec.SENSOR_TYPE)];
        this.accuracy = json.getDouble(SensorSpec.SENSOR_ACCURACY);
        this.range = json.getDouble(SensorSpec.SENSOR_RANGE);
        this.batteryUsage = json.getDouble(SensorSpec.SENSOR_BATTERY_USAGE);
        this.delay = json.getDouble(SensorSpec.SENSOR_DELAY);
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
        json.put(SensorSpec.SENSOR_TYPE, this.type.getType());
        json.put(SensorSpec.SENSOR_RANGE, this.range);
        json.put(SensorSpec.SENSOR_ACCURACY, this.accuracy);
        json.put(SensorSpec.SENSOR_BATTERY_USAGE, this.batteryUsage);
        json.put(SensorSpec.SENSOR_DELAY, this.delay);
        return json.toJSON();
    }

    public boolean equals(SensorSpec spec) {
        return this.type.equals(spec.type) && this.range == spec.range && this.accuracy == spec.accuracy &&
            this.batteryUsage == spec.batteryUsage && this.delay == spec.delay;
    }

}
