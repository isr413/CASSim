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

  @Test
  public void sensorconfigToJson() {
    SensorProto proto = new SensorProto(
        "model",
        Set.of("tag1", "tag2"),
        Set.of("matcher1", "matcher2"),
        new SensorStats(1.0, 2.0, 3.0, 4.0)
      );
    SensorConfig config = new SensorConfig(proto, Set.of("sensor1", "sensor2"), true);
    JsonObject json = config.toJson().getJsonObject();
    assertThat(new SensorProto(json.getJson(SensorConfig.PROTO)), is(proto));
    assertThat(
        json.getJsonArray(SensorConfig.SENSOR_IDS).toList(String.class),
        containsInAnyOrder("sensor1", "sensor2")
      );
    assertThat(json.getInt(SensorConfig.COUNT), equalTo(2));
    assertThat(json.getBoolean(SensorConfig.ACTIVE), is(true));
  }

  @Test
  public void jsonToSensorConfig() {
    JsonObjectBuilder json = JsonBuilder.Object();
    SensorProto proto = new SensorProto(
        "model",
        Set.of("tag1", "tag2"),
        Set.of("matcher1", "matcher2"),
        new SensorStats(1.0, 2.0, 3.0, 4.0)
      );
    json.put(SensorConfig.PROTO, proto.toJson());
    JsonArrayBuilder jsonSensors = JsonBuilder.Array();
    jsonSensors.put("sensor1");
    jsonSensors.put("sensor2");
    json.put(SensorConfig.SENSOR_IDS, jsonSensors.toJsonArray());
    json.put(SensorConfig.COUNT, 2);
    json.put(SensorConfig.ACTIVE, true);
    SensorConfig config = new SensorConfig(json.toJson());
    assertThat(config.getProto(), is(proto));
    assertThat(config.getSensorIDs(), containsInAnyOrder("sensor1", "sensor2"));
    assertThat(config.getCount(), equalTo(2));
    assertThat(config.isActive(), is(true));
  }

  @Test
  public void sensorconfigDecode() {
    SensorProto proto = new SensorProto(
        "model",
        Set.of("tag1", "tag2"),
        Set.of("matcher1", "matcher2"),
        new SensorStats(1.0, 2.0, 3.0, 4.0)
      );
    SensorConfig config = new SensorConfig(proto, Set.of("sensor1", "sensor2"), true);
    assertThat(new SensorConfig(config.toJson()), is(config));
  }

  @Test
  public void sensorconfigPartialDecode() {
    SensorProto proto = new SensorProto(
        "model",
        Set.of("tag1", "tag2"),
        Set.of("matcher1", "matcher2"),
        new SensorStats(1.0, 2.0, 3.0, 4.0)
      );
    SensorConfig config = new SensorConfig(proto, 2, true);
    JsonObject json = config.toJson().getJsonObject();
    assertThat(new SensorProto(json.getJson(SensorConfig.PROTO)), is(proto));
    assertThat(
        json.getJsonArray(SensorConfig.SENSOR_IDS).toList(String.class),
        containsInAnyOrder("s:<model>:(0)", "s:<model>:(1)")
      );
    assertThat(json.getInt(SensorConfig.COUNT), equalTo(2));
    assertThat(json.getBoolean(SensorConfig.ACTIVE), is(true));
  }
}
