package com.seat.sim.common.remote;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;

import com.seat.sim.common.json.Json;
import com.seat.sim.common.json.JsonArrayBuilder;
import com.seat.sim.common.json.JsonBuilder;
import com.seat.sim.common.json.JsonObject;
import com.seat.sim.common.json.JsonObjectBuilder;
import com.seat.sim.common.math.Vector;
import com.seat.sim.common.remote.kinematics.FuelProto;
import com.seat.sim.common.remote.kinematics.FuelProtoTest;
import com.seat.sim.common.remote.kinematics.KinematicsProto;
import com.seat.sim.common.remote.kinematics.KinematicsProtoTest;
import com.seat.sim.common.remote.kinematics.MotionProto;
import com.seat.sim.common.remote.kinematics.MotionProtoTest;
import com.seat.sim.common.sensor.SensorConfig;
import com.seat.sim.common.sensor.SensorConfigTest;

public class RemoteProtoTest {

  public static RemoteProto MockRemoteProto() {
    return RemoteProtoTest.MockRemoteProto(false);
  }

  public static RemoteProto MockRemoteProto(boolean usePartialConstructor) {
    return (usePartialConstructor)
        ? new RemoteProto(Set.of(), List.of())
        : new RemoteProto(
            Set.of("t1", "t2"),
            List.of(SensorConfigTest.MockSensorConfig(), SensorConfigTest.MockSensorConfig()),
            KinematicsProtoTest.MockKinematicsProto()
          );
  }

  @Test
  public void remoteProtoToJson() {
    RemoteProto proto = RemoteProtoTest.MockRemoteProto();
    JsonObject json = proto.toJson().getJsonObject();
    assertThat(
        json.getJsonArray(RemoteProto.TAGS).toList(String.class),
        containsInAnyOrder(proto.getTags().toArray())
      );
    json
      .getJsonArray(RemoteProto.SENSORS)
      .toList(Json.class)
      .forEach(config -> assertThat(new SensorConfig(config), is(SensorConfigTest.MockSensorConfig())));
    assertThat(json.getInt(RemoteProto.SENSOR_COUNT), equalTo(proto.getSensorCount()));
    assertThat(new KinematicsProto(json.getJson(RemoteProto.KINEMATICS_PROTO)), is(proto.getKinematicsProto()));
  }

  @Test
  public void jsonToRemoteProto() {
    RemoteProto proto = RemoteProtoTest.MockRemoteProto();
    JsonObjectBuilder json = JsonBuilder.Object();
    JsonArrayBuilder jsonTags = JsonBuilder.Array();
    proto.getTags().forEach(jsonTags::put);
    json.put(RemoteProto.TAGS, jsonTags.toJson());
    JsonArrayBuilder jsonSensors = JsonBuilder.Array();
    proto.getSensorConfigs().forEach(config -> jsonSensors.put(config.toJson()));
    json.put(RemoteProto.SENSORS, jsonSensors.toJson());
    json.put(RemoteProto.SENSOR_COUNT, proto.getSensorCount());
    json.put(RemoteProto.KINEMATICS_PROTO, proto.getKinematicsProto().toJson());
    assertThat(new RemoteProto(json.toJson()), is(proto));
  }

  @Test
  public void remoteProtoDecode() {
    RemoteProto proto = RemoteProtoTest.MockRemoteProto();
    assertThat(new RemoteProto(proto.toJson()), is(proto));
  }

  @Test
  public void remoteProtoPartialDecode() {
    RemoteProto proto = RemoteProtoTest.MockRemoteProto(true);
    JsonObject json = proto.toJson().getJsonObject();
    assertThat(json.hasKey(RemoteProto.TAGS), is(false));
    assertThat(json.hasKey(RemoteProto.SENSORS), is(false));
    assertThat(json.getInt(RemoteProto.SENSOR_COUNT), equalTo(0));
    assertThat(json.hasKey(RemoteProto.KINEMATICS_PROTO), is(false));
    assertThat(new RemoteProto(json.toJson()), is(proto));
  }

  @Test
  public void hasKinematicsProto() {
    assertThat(RemoteProtoTest.MockRemoteProto().hasKinematicsProto(), is(true));
    assertThat(RemoteProtoTest.MockRemoteProto(true).hasKinematicsProto(), is(false));
  }

  @Test
  public void hasTag() {
    RemoteProto proto = RemoteProtoTest.MockRemoteProto();
    assertThat(proto.hasTags(), is(true));
    proto
      .getTags()
      .stream()
      .map(tag -> String.valueOf(tag))
      .forEach(tag -> {
          assertThat(proto.hasTag(tag), is(true));
          assertThat(proto.hasRemoteMatch(Set.of(tag)), is(true));
          assertThat(proto.hasRemoteMatch(Set.of(tag, "nt1")), is(true));
        });
    assertThat(
        proto
          .hasRemoteMatch(
            proto
              .getTags()
              .stream()
              .map(tag -> String.valueOf(tag))
              .collect(Collectors.toSet())
          ),
        is(true)
      );
    assertThat(proto.hasRemoteMatch(Set.of("nt1", "nt2")), is(false));
    assertThat(proto.hasRemoteMatch(Set.of()), is(false));
    assertThat(proto.hasSensors(), is(true));
    proto
        .getSensorConfigs()
        .stream()
        .flatMap(config -> config.getProto().getTags().stream())
        .map(tag -> String.valueOf(tag))
        .forEach(tag -> {
          assertThat(proto.hasSensorMatch(Set.of(tag)), is(true));
          assertThat(proto.hasSensorMatch(Set.of(tag, "nt1")), is(true));
        });
  }

  @Test
  public void hasTags() {
    assertThat(RemoteProtoTest.MockRemoteProto().hasTags(), is(true));
    assertThat(RemoteProtoTest.MockRemoteProto(true).hasTags(), is(false));
  }

  @Test
  public void hasSensors() {
    assertThat(RemoteProtoTest.MockRemoteProto().hasSensors(), is(true));
    assertThat(RemoteProtoTest.MockRemoteProto(true).hasSensors(), is(false));
  }

  @Test
  public void isEnabled() {
    assertThat(RemoteProtoTest.MockRemoteProto().isEnabled(), is(true));
    assertThat(RemoteProtoTest.MockRemoteProto(true).isEnabled(), is(true));
    assertThat(
        new RemoteProto(
            Set.of(),
            List.of(),
            new KinematicsProto(
                Vector.ZERO,
                new FuelProto(0., Vector.ZERO),
                MotionProtoTest.MockMotionProto()
              )
          )
          .isEnabled(),
        is(false)
      );
  }

  @Test
  public void isMobile() {
    assertThat(RemoteProtoTest.MockRemoteProto().isMobile(), is(true));
    assertThat(RemoteProtoTest.MockRemoteProto(true).isMobile(), is(false));
    assertThat(
        new RemoteProto(
            Set.of(),
            List.of(),
            new KinematicsProto(
                Vector.ZERO,
                FuelProtoTest.MockFuelProto(),
                new MotionProto(Vector.ZERO, 0., 0.)
              )
          )
          .isMobile(),
        is(false)
      );
  }
}
