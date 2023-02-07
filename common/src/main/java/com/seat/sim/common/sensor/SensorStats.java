package com.seat.sim.common.sensor;

import java.util.Optional;

import com.seat.sim.common.json.Json;
import com.seat.sim.common.json.JsonBuilder;
import com.seat.sim.common.json.JsonException;
import com.seat.sim.common.json.JsonObject;
import com.seat.sim.common.json.JsonObjectBuilder;
import com.seat.sim.common.json.Jsonable;

public class SensorStats extends Jsonable {
  public static final String ACCURACY = "accuracy";
  public static final String BATTERY_USAGE = "battery_usage";
  public static final String DELAY = "delay";
  public static final String RANGE = "range";

  private double accuracy;
  private double batteryUsage;
  private double delay;
  private Optional<Double> range;

  public SensorStats() {
    this(0, 0, 0, null);
  }

  public SensorStats(double batteryUsage, double accuracy, double delay) {
    this(batteryUsage, accuracy, delay, null);
  }

  public SensorStats(double batteryUsage, double accuracy, double delay, double range) {
    this(batteryUsage, accuracy, delay, Optional.of(range));
  }

  private SensorStats(double batteryUsage, double accuracy, double delay, Optional<Double> range) {
    this.batteryUsage = batteryUsage;
    this.accuracy = accuracy;
    this.delay = delay;
    this.range = (range != null) ? range : Optional.empty();
  }

  public SensorStats(Json json) throws JsonException {
    super(json);
  }

  @Override
  protected void decode(JsonObject json) throws JsonException {
    this.batteryUsage = (json.hasKey(SensorStats.BATTERY_USAGE)) ? json.getDouble(SensorStats.BATTERY_USAGE) : 0;
    this.accuracy = (json.hasKey(SensorStats.ACCURACY)) ? json.getDouble(SensorStats.ACCURACY) : 0;
    this.delay = (json.hasKey(SensorStats.DELAY)) ? json.getDouble(SensorStats.DELAY) : 0;
    this.range = (json.hasKey(SensorStats.RANGE)) ? Optional.of(json.getDouble(SensorStats.RANGE))
        : Optional.empty();
  }

  protected JsonObjectBuilder getJsonBuilder() throws JsonException {
    JsonObjectBuilder json = JsonBuilder.Object();
    if (this.hasBatteryUsage()) {
      json.put(SensorStats.BATTERY_USAGE, this.batteryUsage);
    }
    if (this.hasAccuracy()) {
      json.put(SensorStats.ACCURACY, this.accuracy);
    }
    if (this.hasDelay()) {
      json.put(SensorStats.DELAY, this.delay);
    }
    if (this.hasRange()) {
      json.put(SensorStats.RANGE, this.getRange());
    }
    return json;
  }

  public double getAccuracy() {
    return this.accuracy;
  }

  public double getBatteryUsage() {
    return this.batteryUsage;
  }

  public double getDelay() {
    return this.delay;
  }

  public double getRange() {
    return this.range.get();
  }

  public boolean hasAccuracy() {
    return Double.isFinite(this.accuracy) && this.accuracy > 0;
  }

  public boolean hasBatteryUsage() {
    return Double.isFinite(this.batteryUsage) && this.batteryUsage > 0;
  }

  public boolean hasDelay() {
    return Double.isFinite(this.delay) && this.delay > 0;
  }

  public boolean hasRange() {
    return this.range.isPresent() && Double.isFinite(this.getRange());
  }

  public Json toJson() throws JsonException {
    return this.getJsonBuilder().toJson();
  }
}
