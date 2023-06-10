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

public class SensorProtoTest {

  public static SensorProto MockSensorProto() {
    return SensorProtoTest.MockSensorProto(false);
  }

  public static SensorProto MockSensorProto(boolean usePartialConstructor) {
    return (usePartialConstructor)
        ? new SensorProto("model", Set.of(), Set.of())
        : new SensorProto(
              "model",
              Set.of("t1", "t2"),
              Set.of("m1", "m2"),
              SensorStatsTest.MockSensorStats()
            );
  }

  @Test
  public void sensorProtoToJson() {
    SensorProto proto = SensorProtoTest.MockSensorProto();
    JsonObject json = proto.toJson().getJsonObject();
    assertThat(json.getString(SensorProto.MODEL), equalTo(proto.getModel()));
    assertThat(
        json.getJsonArray(SensorProto.TAGS).toList(String.class),
        containsInAnyOrder(proto.getTags().toArray())
      );
    assertThat(
        json.getJsonArray(SensorProto.MATCHERS).toList(String.class),
        containsInAnyOrder(proto.getMatchers().toArray())
      );
    assertThat(new SensorStats(json.getJson(SensorProto.STATS)), is(proto.getStats()));
  }

  @Test
  public void jsonToSensorProto() {
    SensorProto proto = SensorProtoTest.MockSensorProto();
    JsonObjectBuilder json = JsonBuilder.Object();
    json.put(SensorProto.MODEL, proto.getModel());
    JsonArrayBuilder jsonTags = JsonBuilder.Array();
    proto.getTags().forEach(jsonTags::put);
    json.put(SensorProto.TAGS, jsonTags.toJsonArray());
    JsonArrayBuilder jsonMatchers = JsonBuilder.Array();
    proto.getMatchers().forEach(jsonMatchers::put);
    json.put(SensorProto.MATCHERS, jsonMatchers.toJsonArray());
    json.put(SensorProto.STATS, proto.getStats().toJson());
    assertThat(new SensorProto(json.toJson()), is(proto));
  }

  @Test
  public void sensorProtoDecode() {
    SensorProto proto = SensorProtoTest.MockSensorProto();
    assertThat(new SensorProto(proto.toJson()), is(proto));
  }

  @Test
  public void sensorProtoPartialDecode() {
    SensorProto proto = SensorProtoTest.MockSensorProto(true);
    JsonObject json = proto.toJson().getJsonObject();
    assertThat(json.getString(SensorProto.MODEL), equalTo(proto.getModel()));
    assertThat(json.hasKey(SensorProto.TAGS), is(false));
    assertThat(json.hasKey(SensorProto.MATCHERS), is(false));
    assertThat(json.hasKey(SensorProto.STATS), is(false));
    assertThat(new SensorProto(proto.toJson()), is(proto));
  }
}
