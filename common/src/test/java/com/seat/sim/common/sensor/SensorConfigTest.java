package com.seat.sim.common.sensor;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.util.Set;

import org.junit.Test;

import com.seat.sim.common.json.JsonArrayBuilder;
import com.seat.sim.common.json.JsonBuilder;
import com.seat.sim.common.json.JsonObject;
import com.seat.sim.common.json.JsonObjectBuilder;

public class SensorConfigTest {

  public static SensorConfig MockSensorConfig() {
    return SensorConfigTest.MockSensorConfig(false);
  }

  public static SensorConfig MockSensorConfig(boolean usePartialConstructor) {
    return (usePartialConstructor)
        ? new SensorConfig(SensorProtoTest.MockSensorProto(), 2, true)
        : new SensorConfig(SensorProtoTest.MockSensorProto(), Set.of("s1", "s2"), true);
  }

  @Test
  public void sensorconfigToJson() {
    SensorConfig config = SensorConfigTest.MockSensorConfig();
    JsonObject json = config.toJson().getJsonObject();
    assertThat(new SensorProto(json.getJson(SensorConfig.PROTO)), is(config.getProto()));
    assertThat(
        json.getJsonArray(SensorConfig.SENSOR_IDS).toList(String.class),
        containsInAnyOrder(config.getSensorIDs().toArray())
      );
    assertThat(json.getInt(SensorConfig.COUNT), equalTo(config.getCount()));
    assertThat(json.getBoolean(SensorConfig.ACTIVE), is(config.isActive()));
  }

  @Test
  public void jsonToSensorConfig() {
    SensorConfig config = SensorConfigTest.MockSensorConfig();
    JsonObjectBuilder json = JsonBuilder.Object();
    json.put(SensorConfig.PROTO, config.getProto().toJson());
    JsonArrayBuilder jsonSensors = JsonBuilder.Array();
    config.getSensorIDs().forEach(jsonSensors::put);
    json.put(SensorConfig.SENSOR_IDS, jsonSensors.toJsonArray());
    json.put(SensorConfig.COUNT, config.getCount());
    json.put(SensorConfig.ACTIVE, config.isActive());
    assertThat(new SensorConfig(json.toJson()), is(config));
  }

  @Test
  public void sensorconfigDecode() {
    SensorConfig config = SensorConfigTest.MockSensorConfig();
    assertThat(new SensorConfig(config.toJson()), is(config));
  }

  @Test
  public void sensorconfigPartialDecode() {
    SensorConfig config = SensorConfigTest.MockSensorConfig(true);
    JsonObject json = config.toJson().getJsonObject();
    assertThat(new SensorProto(json.getJson(SensorConfig.PROTO)), is(config.getProto()));
    assertThat(
        json.getJsonArray(SensorConfig.SENSOR_IDS).toList(String.class),
        containsInAnyOrder(config.getSensorIDs().toArray())
      );
    assertThat(json.getInt(SensorConfig.COUNT), equalTo(config.getCount()));
    assertThat(json.getBoolean(SensorConfig.ACTIVE), is(config.isActive()));
    assertThat(new SensorConfig(config.toJson()), is(config));
  }
}
