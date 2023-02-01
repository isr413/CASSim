package com.seat.sim.common.sensor;

import java.util.HashSet;
import java.util.Optional;
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

    protected static final String DEFAULT_MODEL = "default";

    private Optional<Double> accuracy;
    private Optional<Double> batteryUsage;
    private Optional<Double> delay;
    private Optional<Double> range;
    private Set<String> sensorGroups;
    private Set<String> sensorMatchers;
    private String sensorModel;

    public SensorProto() {
        this(SensorProto.DEFAULT_MODEL);
    }

    public SensorProto(String sensorModel) {
        this(sensorModel, null, null, null, null, null, null);
    }

    public SensorProto(String sensorModel, Set<String> sensorGroups) {
        this(sensorModel, sensorGroups, null, null, null, null, null);
    }

    public SensorProto(String sensorModel, double range, double accuracy, double delay, double batteryUsage) {
        this(sensorModel, null, range, accuracy, delay, batteryUsage, null);
    }

    public SensorProto(String sensorModel, Set<String> sensorGroups, double range, double accuracy, double delay,
            double batteryUsage) {
        this(sensorModel, sensorGroups, range, accuracy, delay, batteryUsage, null);
    }

    public SensorProto(String sensorModel, double range, double accuracy, double delay, double batteryUsage,
            Set<String> sensorMatchers) {
        this(sensorModel, null, range, accuracy, delay, batteryUsage, sensorMatchers);
    }

    public SensorProto(String sensorModel, Set<String> sensorGroups, double range, double accuracy, double delay,
            double batteryUsage, Set<String> sensorMatchers) {
        this(sensorModel, sensorGroups, Optional.of(range), Optional.of(accuracy), Optional.of(delay),
                Optional.of(batteryUsage), sensorMatchers);
    }

    private SensorProto(String sensorModel, Set<String> sensorGroups, Optional<Double> range, Optional<Double> accuracy,
            Optional<Double> delay, Optional<Double> batteryUsage, Set<String> sensorMatchers) {
        this.sensorModel = sensorModel;
        this.sensorGroups = (sensorGroups != null) ? new HashSet<>(sensorGroups) : new HashSet<>();
        this.range = (range != null) ? range : Optional.empty();
        this.accuracy = (accuracy != null) ? accuracy : Optional.empty();
        this.delay = (delay != null) ? delay : Optional.empty();
        this.batteryUsage = (batteryUsage != null) ? batteryUsage : Optional.empty();
        this.sensorMatchers = (sensorMatchers != null) ? new HashSet<>(sensorMatchers) : new HashSet<>();
        if (this.batteryUsage.isPresent() && !Double.isFinite(this.batteryUsage.get())) {
            throw new RuntimeException(
                    String.format("cannot create Sensor with non-finite battery usage `%f`", this.batteryUsage.get()));
        }
    }

    public SensorProto(Json json) throws JsonException {
        super(json);
    }

    @Override
    protected void decode(JsonObject json) throws JsonException {
        this.sensorModel = json.getString(SensorProto.SENSOR_MODEL);
        this.sensorGroups = (json.hasKey(SensorProto.SENSOR_GROUPS))
                ? new HashSet<>(json.getJsonArray(SensorProto.SENSOR_GROUPS).toList(String.class))
                : new HashSet<>();
        this.range = (json.hasKey(SensorProto.RANGE)) ? Optional.of(json.getDouble(SensorProto.RANGE))
                : Optional.empty();
        this.accuracy = (json.hasKey(SensorProto.ACCURACY)) ? Optional.of(json.getDouble(SensorProto.ACCURACY))
                : Optional.empty();
        this.delay = (json.hasKey(SensorProto.DELAY)) ? Optional.of(json.getDouble(SensorProto.DELAY))
                : Optional.empty();
        this.batteryUsage = (json.hasKey(SensorProto.BATTERY_USAGE))
                ? Optional.of(json.getDouble(SensorProto.BATTERY_USAGE))
                : Optional.empty();
        this.sensorMatchers = (json.hasKey(SensorProto.SENSOR_MATCHERS))
                ? new HashSet<>(json.getJsonArray(SensorProto.SENSOR_MATCHERS).toList(String.class))
                : new HashSet<>();
    }

    protected JsonObjectBuilder getJsonBuilder() throws JsonException {
        JsonObjectBuilder json = JsonBuilder.Object();
        json.put(SensorProto.SENSOR_MODEL, this.sensorModel);
        if (this.hasSensorGroups()) {
            json.put(SensorProto.SENSOR_GROUPS, JsonBuilder.toJsonArray(this.sensorGroups));
        }
        if (this.hasRange()) {
            json.put(SensorProto.RANGE, this.range);
        }
        if (this.hasAccuracy()) {
            json.put(SensorProto.ACCURACY, this.accuracy);
        }
        if (this.hasDelay()) {
            json.put(SensorProto.DELAY, this.delay);
        }
        if (this.hasBatteryUsage()) {
            json.put(SensorProto.BATTERY_USAGE, this.batteryUsage);
        }
        if (this.hasSensorMatchers()) {
            json.put(SensorProto.SENSOR_MATCHERS, JsonBuilder.toJsonArray(this.sensorMatchers));
        }
        return json;
    }

    public boolean equals(SensorProto proto) {
        if (proto == null)
            return false;
        return this.sensorModel.equals(proto.sensorModel) && this.sensorGroups.equals(proto.sensorGroups);
    }

    public double getBatteryUsage() {
        return this.batteryUsage.get();
    }

    public String getLabel() {
        return String.format("s:<%s>", this.sensorModel);
    }

    public double getSensorAccuracy() {
        return this.accuracy.get();
    }

    public double getSensorDelay() {
        return this.delay.get();
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
        return this.range.get();
    }

    public boolean hasAccuracy() {
        return this.accuracy.isPresent() && Double.isFinite(this.getSensorAccuracy());
    }

    public boolean hasBatteryUsage() {
        return this.batteryUsage.isPresent() && Double.isFinite(this.getBatteryUsage()) && this.getBatteryUsage() > 0;
    }

    public boolean hasDelay() {
        return this.delay.isPresent() && Double.isFinite(this.getSensorDelay()) && this.getSensorDelay() > 0;
    }

    public boolean hasRange() {
        return this.range.isPresent() && Double.isFinite(this.getSensorRange());
    }

    public boolean hasSensorGroups() {
        return !this.sensorGroups.isEmpty();
    }

    public boolean hasSensorGroupWithTag(String groupTag) {
        return this.sensorGroups.contains(groupTag);
    }

    public boolean hasSensorMatch(Set<String> matchers) {
        for (String matcher : matchers) {
            if (this.hasSensorGroupWithTag(matcher))
                return true;
        }
        return false;
    }

    public boolean hasSensorMatchers() {
        return !this.sensorMatchers.isEmpty();
    }

    public boolean hasSensorMatcherWithTag(String matcherTag) {
        return this.sensorMatchers.contains(matcherTag);
    }

    public boolean hasSensorModel(String sensorModel) {
        if (sensorModel == null || this.sensorModel == null)
            return this.sensorModel == sensorModel;
        return this.sensorModel.equals(sensorModel);
    }

    public Json toJson() throws JsonException {
        return this.getJsonBuilder().toJson();
    }
}
