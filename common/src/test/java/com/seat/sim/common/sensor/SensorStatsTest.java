package com.seat.sim.common.sensor;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

import com.seat.sim.common.json.JsonBuilder;
import com.seat.sim.common.json.JsonObject;
import com.seat.sim.common.json.JsonObjectBuilder;
import com.seat.sim.common.math.Vector;

public class SensorStatsTest {

  public static SensorStats MockSensorStats() {
    return SensorStatsTest.MockSensorStats(false);
  }

  public static SensorStats MockSensorStats(boolean usePartialConstructor) {
    return (usePartialConstructor)
        ? new SensorStats(1.0, 2.0, 3.0)
        : new SensorStats(1.0, 2.0, 3.0, 4.0);
  }

  @Test
  public void sensorStatsToJson() {
    SensorStats stats = SensorStatsTest.MockSensorStats();
    JsonObject json = stats.toJson().getJsonObject();
    assertThat(
        json.getDouble(SensorStats.BATTERY_USAGE),
        closeTo(stats.getBatteryUsage(), Vector.PRECISION)
      );
    assertThat(
        json.getDouble(SensorStats.ACCURACY),
        closeTo(stats.getAccuracy(), Vector.PRECISION)
      );
    assertThat(json.getDouble(SensorStats.DELAY), closeTo(stats.getDelay(), Vector.PRECISION));
    assertThat(json.getDouble(SensorStats.RANGE), closeTo(stats.getRange(), Vector.PRECISION));
  }

  @Test
  public void jsonToSensorStats() {
    SensorStats stats = SensorStatsTest.MockSensorStats();
    JsonObjectBuilder json = JsonBuilder.Object();
    json.put(SensorStats.BATTERY_USAGE, stats.getBatteryUsage());
    json.put(SensorStats.ACCURACY, stats.getAccuracy());
    json.put(SensorStats.DELAY, stats.getDelay());
    json.put(SensorStats.RANGE, stats.getRange());
    assertThat(new SensorStats(json.toJson()), is(stats));
  }

  @Test
  public void sensorStatsDecode() {
    SensorStats stats = SensorStatsTest.MockSensorStats();
    assertThat(new SensorStats(stats.toJson()), is(stats));
  }

  @Test
  public void sensorStatsPartialDecode() {
    SensorStats stats = SensorStatsTest.MockSensorStats(true);
    JsonObject json = stats.toJson().getJsonObject();
    assertThat(
        json.getDouble(SensorStats.BATTERY_USAGE),
        closeTo(stats.getBatteryUsage(), Vector.PRECISION)
      );
    assertThat(
        json.getDouble(SensorStats.ACCURACY),
        closeTo(stats.getAccuracy(), Vector.PRECISION)
      );
    assertThat(json.getDouble(SensorStats.DELAY), closeTo(stats.getDelay(), Vector.PRECISION));
    assertThat(json.hasKey(SensorStats.RANGE), is(false));
    assertThat(new SensorStats(json.toJson()), is(stats));
  }
}
