package com.seat.rescuesim.common;

import com.seat.rescuesim.common.json.*;

public class SensorSpecification extends JSONAble {
    private static final String SENSOR_ACCURACY = "accuracy";
    private static final String SENSOR_BATTERY_USAGE = "battery_usage";
    private static final String SENSOR_DELAY = "delay";
    private static final String SENSOR_RANGE = "range";
    private static final String SENSOR_TYPE = "type";
    private static final String SPEC_ID = "spec_id";

    private double accuracy;
    private double batteryUsage;
    private double delay;
    private double range;
    private String specID;
    private SensorType type;

    public SensorSpecification(JSONObject json) {
        super(json);
    }

    public SensorSpecification(JSONOption option) {
        super(option);
    }

    public SensorSpecification(String encoding) {
        super(encoding);
    }

    public SensorSpecification(SensorType type, double range, double batteryUsage) {
        this(type.toString(), type, range, batteryUsage, 1, 0);
    }

    public SensorSpecification(String specID, SensorType type, double range, double batteryUsage) {
        this(specID, type, range, batteryUsage, 1, 0);
    }

    public SensorSpecification(SensorType type, double range, double batteryUsage, double accuracy) {
        this(type.toString(), type, range, batteryUsage, accuracy, 0);
    }

    public SensorSpecification(String specID, SensorType type, double range, double batteryUsage, double accuracy) {
        this(specID, type, range, batteryUsage, accuracy, 0);
    }

    public SensorSpecification(SensorType type, double range, double batteryUsage, double accuracy, double delay) {
        this(type.toString(), type, range, batteryUsage, accuracy, delay);
    }

    public SensorSpecification(String specID, SensorType type, double range, double batteryUsage, double accuracy,
            double delay) {
        this.specID = specID;
        this.type = type;
        this.range = range;
        this.batteryUsage = batteryUsage;
        this.accuracy = accuracy;
        this.delay = delay;
    }

    @Override
    protected void decode(JSONObject json) {
        this.specID = json.getString(SensorSpecification.SPEC_ID);
        this.type = SensorType.values()[json.getInt(SensorSpecification.SENSOR_TYPE)];
        this.range = json.getDouble(SensorSpecification.SENSOR_RANGE);
        this.batteryUsage = json.getDouble(SensorSpecification.SENSOR_BATTERY_USAGE);
        this.accuracy = json.getDouble(SensorSpecification.SENSOR_ACCURACY);
        this.delay = json.getDouble(SensorSpecification.SENSOR_DELAY);
    }

    public double getBatteryUsage() {
        return this.batteryUsage;
    }

    public String getLabel() {
        return String.format("<%s>", this.specID);
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

    public SensorType getSensorType() {
        return this.type;
    }

    public String getSpecID() {
        return this.specID;
    }

    public JSONOption toJSON() {
        JSONObjectBuilder json = JSONBuilder.Object();
        json.put(SensorSpecification.SPEC_ID, this.specID);
        json.put(SensorSpecification.SENSOR_TYPE, this.type.getType());
        json.put(SensorSpecification.SENSOR_RANGE, this.range);
        json.put(SensorSpecification.SENSOR_BATTERY_USAGE, this.batteryUsage);
        json.put(SensorSpecification.SENSOR_ACCURACY, this.accuracy);
        json.put(SensorSpecification.SENSOR_DELAY, this.delay);
        return json.toJSON();
    }

    public boolean equals(SensorSpecification spec) {
        return this.specID.equals(spec.specID);
    }

}
