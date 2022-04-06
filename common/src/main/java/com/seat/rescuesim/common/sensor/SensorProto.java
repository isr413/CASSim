package com.seat.rescuesim.common.sensor;

import com.seat.rescuesim.common.json.JSONAble;
import com.seat.rescuesim.common.json.JSONBuilder;
import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.JSONObjectBuilder;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.json.SerializableEnum;

/** A serializable prototype of a generic Sensor. */
public class SensorProto extends JSONAble {
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

    public SensorProto() {
        this(SensorProto.DEFAULT_SENSOR_TYPE, SensorProto.DEFAULT_RANGE, SensorProto.DEFAULT_ACCURACY,
            SensorProto.DEFAULT_BATTERY_USAGE);
    }

    public SensorProto(double range) {
        this(SensorProto.DEFAULT_SENSOR_TYPE, range, SensorProto.DEFAULT_ACCURACY, SensorProto.DEFAULT_BATTERY_USAGE);
    }

    public SensorProto(double range, double accuracy) {
        this(SensorProto.DEFAULT_SENSOR_TYPE, range, accuracy, SensorProto.DEFAULT_BATTERY_USAGE);
    }

    public SensorProto(double range, double accuracy, double batteryUsage) {
        this(SensorProto.DEFAULT_SENSOR_TYPE, range, accuracy, batteryUsage);
    }

    protected SensorProto(SensorType sensorType) {
        this(sensorType, SensorProto.DEFAULT_RANGE, SensorProto.DEFAULT_ACCURACY, SensorProto.DEFAULT_BATTERY_USAGE);
    }

    protected SensorProto(SensorType sensorType, double range) {
        this(sensorType, range, SensorProto.DEFAULT_ACCURACY, SensorProto.DEFAULT_BATTERY_USAGE);
    }

    protected SensorProto(SensorType sensorType, double range, double accuracy) {
        this(sensorType, range, accuracy, SensorProto.DEFAULT_BATTERY_USAGE);
    }

    protected SensorProto(SensorType sensorType, double range, double accuracy, double batteryUsage) {
        this.sensorType = sensorType;
        this.range = range;
        this.accuracy = accuracy;
        this.batteryUsage = batteryUsage;
    }

    public SensorProto(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        this.sensorType = SensorType.decodeType(json);
        this.accuracy = json.getDouble(SensorProto.ACCURACY);
        this.range = json.getDouble(SensorProto.RANGE);
        this.batteryUsage = json.getDouble(SensorProto.BATTERY_USAGE);
    }

    protected JSONObjectBuilder getJSONBuilder() {
        JSONObjectBuilder json = JSONBuilder.Object();
        json.put(SensorType.SENSOR_TYPE, this.sensorType.getType());
        json.put(SensorProto.RANGE, this.range);
        json.put(SensorProto.ACCURACY, this.accuracy);
        json.put(SensorProto.BATTERY_USAGE, this.batteryUsage);
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
        return SensorProto.DEFAULT_SENSOR_TYPE;
    }

    public JSONOption toJSON() {
        return this.getJSONBuilder().toJSON();
    }

    public boolean equals(SensorProto proto) {
        if (proto == null) return false;
        return this.sensorType.equals(proto.sensorType) && this.range == proto.range &&
            this.accuracy == proto.accuracy && this.batteryUsage == proto.batteryUsage;
    }

}
