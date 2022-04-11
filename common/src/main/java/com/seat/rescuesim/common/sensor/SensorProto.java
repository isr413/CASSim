package com.seat.rescuesim.common.sensor;

import com.seat.rescuesim.common.json.JSONAble;
import com.seat.rescuesim.common.json.JSONBuilder;
import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.JSONObjectBuilder;
import com.seat.rescuesim.common.json.JSONOption;

/** A serializable prototype of a generic Sensor. */
public class SensorProto extends JSONAble {
    public static final String ACCURACY = "accuracy";
    public static final String BATTERY_USAGE = "battery_usage";
    public static final String RANGE = "range";

    protected static final double DEFAULT_ACCURACY = 1.0;
    protected static final double DEFAULT_BATTERY_USAGE = 0.0;
    protected static final double DEFAULT_RANGE = Double.POSITIVE_INFINITY;

    private double accuracy;
    private double batteryUsage;
    private double range;

    public SensorProto(double range, double accuracy, double batteryUsage) {
        this.range = range;
        this.accuracy = accuracy;
        this.batteryUsage = batteryUsage;
    }

    public SensorProto(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        this.accuracy = json.getDouble(SensorProto.ACCURACY);
        this.range = (json.hasKey(SensorProto.RANGE)) ?
            json.getDouble(SensorProto.RANGE) :
            SensorProto.DEFAULT_RANGE;
        this.batteryUsage = json.getDouble(SensorProto.BATTERY_USAGE);
    }

    protected JSONObjectBuilder getJSONBuilder() throws JSONException {
        JSONObjectBuilder json = JSONBuilder.Object();
        json.put(SensorRegistry.SENSOR_TYPE, this.getSensorType());
        if (this.hasLimitedRange()) {
            json.put(SensorProto.RANGE, this.range);
        }
        json.put(SensorProto.ACCURACY, this.accuracy);
        json.put(SensorProto.BATTERY_USAGE, this.batteryUsage);
        return json;
    }

    public double getBatteryUsage() {
        return this.batteryUsage;
    }

    public String getLabel() {
        return "s:<sensor>";
    }

    public double getSensorAccuracy() {
        return this.accuracy;
    }

    public double getSensorRange() {
        return this.range;
    }

    public String getSensorType() {
        return this.getClass().getName();
    }

    public boolean hasAccuracy() {
        return this.accuracy > 0;
    }

    public boolean hasBatteryUsage() {
        return this.batteryUsage > 0;
    }

    public boolean hasImperfectAccuracy() {
        return !this.hasPerfectAccuracy();
    }

    public boolean hasLimitedRange() {
        return !this.hasUnlimitedRange();
    }

    public boolean hasPerfectAccuracy() {
        return this.accuracy >= 1;
    }

    public boolean hasRange() {
        return this.range > 0;
    }

    public boolean hasUnlimitedRange() {
        return this.range == Double.POSITIVE_INFINITY;
    }

    public JSONOption toJSON() throws JSONException {
        return this.getJSONBuilder().toJSON();
    }

    public boolean equals(SensorProto proto) {
        if (proto == null) return false;
        return this.getSensorType().equals(proto.getSensorType()) && this.range == proto.range &&
            this.accuracy == proto.accuracy && this.batteryUsage == proto.batteryUsage;
    }

}
