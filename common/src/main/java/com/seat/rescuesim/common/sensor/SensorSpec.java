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
    public static final String ACCURACY = "accuracy";
    public static final String BATTERY_USAGE = "battery_usage";
    public static final String RANGE = "range";

    protected static final double DEFAULT_ACCURACY = 1.0;
    protected static final double DEFAULT_BATTERY_USAGE = 0.0;
    protected static final double DEFAULT_RANGE = Double.POSITIVE_INFINITY;
    protected static final SensorType DEFAULT_SENSOR_TYPE = SensorType.GENERIC;

    private double accuracy;
    private double batteryUsage;
    private double range;
    private SensorType sensorType;

    public SensorSpec() {
        this(SensorSpec.DEFAULT_SENSOR_TYPE, SensorSpec.DEFAULT_RANGE, SensorSpec.DEFAULT_ACCURACY,
            SensorSpec.DEFAULT_BATTERY_USAGE);
    }

    public SensorSpec(double range) {
        this(SensorSpec.DEFAULT_SENSOR_TYPE, range, SensorSpec.DEFAULT_ACCURACY, SensorSpec.DEFAULT_BATTERY_USAGE);
    }

    public SensorSpec(double range, double accuracy) {
        this(SensorSpec.DEFAULT_SENSOR_TYPE, range, accuracy, SensorSpec.DEFAULT_BATTERY_USAGE);
    }

    public SensorSpec(double range, double accuracy, double batteryUsage) {
        this(SensorSpec.DEFAULT_SENSOR_TYPE, range, accuracy, batteryUsage);
    }

    protected SensorSpec(SensorType sensorType) {
        this(sensorType, SensorSpec.DEFAULT_RANGE, SensorSpec.DEFAULT_ACCURACY, SensorSpec.DEFAULT_BATTERY_USAGE);
    }

    protected SensorSpec(SensorType sensorType, double range) {
        this(sensorType, range, SensorSpec.DEFAULT_ACCURACY, SensorSpec.DEFAULT_BATTERY_USAGE);
    }

    protected SensorSpec(SensorType sensorType, double range, double accuracy) {
        this(sensorType, range, accuracy, SensorSpec.DEFAULT_BATTERY_USAGE);
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
        this.accuracy = json.getDouble(SensorSpec.ACCURACY);
        this.range = json.getDouble(SensorSpec.RANGE);
        this.batteryUsage = json.getDouble(SensorSpec.BATTERY_USAGE);
    }

    protected JSONObjectBuilder getJSONBuilder() {
        JSONObjectBuilder json = JSONBuilder.Object();
        json.put(SensorType.SENSOR_TYPE, this.sensorType.getType());
        json.put(SensorSpec.RANGE, this.range);
        json.put(SensorSpec.ACCURACY, this.accuracy);
        json.put(SensorSpec.BATTERY_USAGE, this.batteryUsage);
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
        return SensorSpec.DEFAULT_SENSOR_TYPE;
    }

    public JSONOption toJSON() {
        return this.getJSONBuilder().toJSON();
    }

    public boolean equals(SensorSpec spec) {
        return this.sensorType.equals(spec.sensorType) && this.range == spec.range && this.accuracy == spec.accuracy &&
            this.batteryUsage == spec.batteryUsage;
    }

}
