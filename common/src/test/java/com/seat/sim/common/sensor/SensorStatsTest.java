package com.seat.sim.common.sensor;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

import com.seat.sim.common.json.JsonBuilder;
import com.seat.sim.common.json.JsonObject;
import com.seat.sim.common.json.JsonObjectBuilder;
import com.seat.sim.common.math.Vector;

public class SensorStatsTest {

  @Test
  public void sensorStatsToJson() {
    SensorStats stats = new SensorStats(1.0, 2.0, 3.0, 4.0);
    JsonObject json = stats.toJson().getJsonObject();
    assertThat(json.getDouble(SensorStats.BATTERY_USAGE), closeTo(1.0, Vector.PRECISION));
    assertThat(json.getDouble(SensorStats.ACCURACY), closeTo(2.0, Vector.PRECISION));
    assertThat(json.getDouble(SensorStats.DELAY), closeTo(3.0, Vector.PRECISION));
    assertThat(json.getDouble(SensorStats.RANGE), closeTo(4.0, Vector.PRECISION));
  }

  @Test
  public void jsonToSensorStats() {
    JsonObjectBuilder json = JsonBuilder.Object();
    json.put(SensorStats.BATTERY_USAGE, 1.0);
    json.put(SensorStats.ACCURACY, 2.0);
    json.put(SensorStats.DELAY, 3.0);
    json.put(SensorStats.RANGE, 4.0);
    SensorStats stats = new SensorStats(json.toJson());
    assertThat(stats.getBatteryUsage(), closeTo(1.0, Vector.PRECISION));
    assertThat(stats.getAccuracy(), closeTo(2.0, Vector.PRECISION));
    assertThat(stats.getDelay(), closeTo(3.0, Vector.PRECISION));
    assertThat(stats.getRange(), closeTo(4.0, Vector.PRECISION));
  }

  @Test
  public void sensorStatsDecode() {
    SensorStats stats = new SensorStats(1.0, 2.0, 3.0, 4.0);
    assertThat(new SensorStats(stats.toJson()), is(stats));
  }

  @Test
  public void sensorStatsPartialDecode() {
    SensorStats stats = new SensorStats(1.0, 2.0, 3.0);
    JsonObject json = stats.toJson().getJsonObject();
    assertThat(json.getDouble(SensorStats.BATTERY_USAGE), closeTo(1.0, Vector.PRECISION));
    assertThat(json.getDouble(SensorStats.ACCURACY), closeTo(2.0, Vector.PRECISION));
    assertThat(json.getDouble(SensorStats.DELAY), closeTo(3.0, Vector.PRECISION));
    assertThat(json.hasKey(SensorStats.RANGE), is(false));
    stats = new SensorStats(json.toJson());
    assertThat(stats.getBatteryUsage(), closeTo(1.0, Vector.PRECISION));
    assertThat(stats.getAccuracy(), closeTo(2.0, Vector.PRECISION));
    assertThat(stats.getDelay(), closeTo(3.0, Vector.PRECISION));
    assertThat(stats.getRange(), equalTo(Double.POSITIVE_INFINITY));
  }
}
