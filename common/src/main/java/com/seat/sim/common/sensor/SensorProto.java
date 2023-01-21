package com.seat.sim.common.sensor;

import java.util.HashSet;
import java.util.Set;

import com.seat.sim.common.json.*;

/** A serializable prototype of a generic Sensor. */
public class SensorProto extends Jsonable {
    public static final String ACCURACY = "accuracy";
    public static final String BATTERY_USAGE = "battery_usage";
    public static final String DELAY = "delay";
    public static final String RANGE = "range";
    public static final String SENSOR_GROUPS = "sensor_groups";
    public static final String SENSOR_MATCHERS = "sensor_matchers";
    public static final String SENSOR_MODEL = "sensor_model";

    private double accuracy;
    private double batteryUsage;
    private double delay;
    private double range;
    private Set<String> sensorGroups;
    private Set<String> sensorMatchers;
    private String sensorModel;

    public SensorProto(String sensorModel, Set<String> sensorGroups, Set<String> sensorMatchers, double range, 
            double accuracy, double delay, double batteryUsage) {
        this.sensorModel = sensorModel;
        this.sensorGroups = (sensorGroups != null) ? new HashSet<>(sensorGroups) : new HashSet<>();
        this.sensorMatchers = (sensorMatchers != null) ? new HashSet<>(sensorMatchers) : new HashSet<>();
        this.range = range;
        this.accuracy = accuracy;
        this.delay = delay;
        this.batteryUsage = batteryUsage;
    }

    public SensorProto(Json json) throws JsonException {
        super(json);
    }

    @Override
    protected void decode(JsonObject json) throws JsonException {
        this.sensorModel = json.getString(SensorProto.SENSOR_MODEL);
        this.sensorGroups = (json.hasKey(SensorProto.SENSOR_GROUPS)) ?
            new HashSet<>(json.getJsonArray(SensorProto.SENSOR_GROUPS).toList(String.class)) :
            new HashSet<>();
        this.sensorMatchers = (json.hasKey(SensorProto.SENSOR_MATCHERS)) ?
            new HashSet<>(json.getJsonArray(SensorProto.SENSOR_MATCHERS).toList(String.class)) :
            new HashSet<>();
        this.range = (json.hasKey(SensorProto.RANGE)) ?
            json.getDouble(SensorProto.RANGE) :
            Double.POSITIVE_INFINITY;
        this.accuracy = (json.hasKey(SensorProto.ACCURACY)) ?
            json.getDouble(SensorProto.ACCURACY) :
            1;
        this.delay = (json.hasKey(SensorProto.DELAY)) ?
            json.getDouble(SensorProto.DELAY) :
            0;
        this.batteryUsage = json.getDouble(SensorProto.BATTERY_USAGE);
    }

    protected JsonObjectBuilder getJsonBuilder() throws JsonException {
        JsonObjectBuilder json = JsonBuilder.Object();
        json.put(SensorProto.SENSOR_MODEL, this.sensorModel);
        if (this.hasSensorGroups()) {
            json.put(SensorProto.SENSOR_GROUPS, JsonBuilder.toJsonArray(this.sensorGroups));
        }
        if (this.hasSensorMatchers()) {
            json.put(SensorProto.SENSOR_MATCHERS, JsonBuilder.toJsonArray(this.sensorMatchers));
        }
        if (this.hasLimitedRange()) {
            json.put(SensorProto.RANGE, this.range);
        }
        if (this.hasLimitedAccuracy()) {
            json.put(SensorProto.ACCURACY, this.accuracy);
        }
        if (this.hasDelay()) {
            json.put(SensorProto.DELAY, this.delay);
        }
        json.put(SensorProto.BATTERY_USAGE, this.batteryUsage);
        return json;
    }

    public boolean equals(SensorProto proto) {
        if (proto == null) return false;
        return this.sensorModel.equals(proto.sensorModel) && this.sensorGroups.equals(proto.sensorGroups);
    }

    public double getBatteryUsage() {
        return this.batteryUsage;
    }

    public String getLabel() {
        return String.format("s:<%s>", this.sensorModel);
    }

    public double getSensorAccuracy() {
        return this.accuracy;
    }

    public double getSensorDelay() {
        return this.delay;
    }

    public Set<String> getSensorGroups() {
        return this.sensorGroups;
    }

    public Set<String> getSensorMatchers() {
        return this.sensorMatchers;
    }

    public String getSensorModel() {
        return this.sensorModel;
    }

    public double getSensorRange() {
        return this.range;
    }

    public boolean hasAccuracy() {
        return this.accuracy > 0;
    }

    public boolean hasBatteryUsage() {
        return this.batteryUsage > 0;
    }

    public boolean hasDelay() {
        return this.delay > 0;
    }

    public boolean hasLimitedAccuracy() {
        return this.hasAccuracy() && this.accuracy < 1;
    }

    public boolean hasLimitedRange() {
        return this.hasRange() && this.range != Double.POSITIVE_INFINITY;
    }

    public boolean hasRange() {
        return this.range > 0;
    }

    public boolean hasSensorGroups() {
        return !this.sensorGroups.isEmpty();
    }

    public boolean hasSensorGroupWithTag(String groupTag) {
        return this.sensorGroups.contains(groupTag);
    }

    public boolean hasSensorMatchers() {
        return !this.sensorMatchers.isEmpty();
    }

    public boolean hasSensorMatcherWithTag(String matcherTag) {
        return this.sensorMatchers.contains(matcherTag);
    }

    public boolean hasSensorModel(String sensorModel) {
        if (sensorModel == null || this.sensorModel == null) return this.sensorModel == sensorModel;
        return this.sensorModel.equals(sensorModel);
    }

    public Json toJson() throws JsonException {
        return this.getJsonBuilder().toJson();
    }
}
