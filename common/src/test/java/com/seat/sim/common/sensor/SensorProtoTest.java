package com.seat.sim.common.sensor;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.seat.sim.common.json.JsonArrayBuilder;
import com.seat.sim.common.json.JsonBuilder;
import com.seat.sim.common.json.JsonObject;
import com.seat.sim.common.json.JsonObjectBuilder;

public class SensorProtoTest {

  @Test
  public void sensorProtoToJson() {
    SensorStats stats = new SensorStats(1.0, 2.0, 3.0, 4.0);
    SensorProto proto = new SensorProto(
        "model",
        Set.of("tag1", "tag2"),
        Set.of("matcher1", "matcher2"),
        stats
      );
    JsonObject json = proto.toJson().getJsonObject();
    assertThat(json.getString(SensorProto.MODEL), equalTo("model"));
    assertThat(
        json.getJsonArray(SensorProto.TAGS).toList(String.class),
        containsInAnyOrder("tag1", "tag2")
      );
    assertThat(
        json.getJsonArray(SensorProto.MATCHERS).toList(String.class),
        containsInAnyOrder("matcher1", "matcher2")
      );
    assertThat(new SensorStats(json.getJson(SensorProto.STATS)), is(stats));
  }

  @Test
  public void jsonToSensorProto() {
    SensorStats stats = new SensorStats(1.0, 2.0, 3.0, 4.0);
    JsonArrayBuilder tags = JsonBuilder.Array();
    tags.put("tag1");
    tags.put("tag2");
    JsonArrayBuilder matchers = JsonBuilder.Array();
    matchers.put("matcher1");
    matchers.put("matcher2");
    JsonObjectBuilder json = JsonBuilder.Object();
    json.put(SensorProto.MODEL, "model");
    json.put(SensorProto.TAGS, tags.toJsonArray());
    json.put(SensorProto.MATCHERS, matchers.toJsonArray());
    json.put(SensorProto.STATS, stats.toJson());
    SensorProto proto = new SensorProto(json.toJson());
    assertThat(proto.getModel(), equalTo("model"));
    assertThat(List.copyOf(proto.getTags()), containsInAnyOrder("tag1", "tag2"));
    assertThat(
        List.copyOf(proto.getMatchers()),
        containsInAnyOrder("matcher1", "matcher2")
      );
    assertThat(proto.getStats(), is(stats));
  }

  @Test
  public void sensorProtoDecode() {
    SensorStats stats = new SensorStats(1.0, 2.0, 3.0, 4.0);
    SensorProto proto = new SensorProto(
        "model",
        Set.of("tag1", "tag2"),
        Set.of("matcher1", "matcher2"),
        stats
      );
    assertThat(new SensorProto(proto.toJson()), is(proto));
  }

  @Test
  public void sensorProtoPartialDecode() {
    SensorProto proto = new SensorProto(
        "model",
        Set.of("tag1", "tag2"),
        Set.of("matcher1", "matcher2")
      );
    JsonObject json = proto.toJson().getJsonObject();
    assertThat(json.getString(SensorProto.MODEL), equalTo("model"));
    assertThat(
        json.getJsonArray(SensorProto.TAGS).toList(String.class),
        containsInAnyOrder("tag1", "tag2")
      );
    assertThat(
        json.getJsonArray(SensorProto.MATCHERS).toList(String.class),
        containsInAnyOrder("matcher1", "matcher2")
      );
    assertThat(json.hasKey(SensorProto.STATS), is(false));
    proto = new SensorProto(json.toJson());
    assertThat(proto.getModel(), equalTo("model"));
    assertThat(List.copyOf(proto.getTags()), containsInAnyOrder("tag1", "tag2"));
    assertThat(
        List.copyOf(proto.getMatchers()),
        containsInAnyOrder("matcher1", "matcher2")
      );
    assertThat(proto.hasStats(), is(false));
  }
}
