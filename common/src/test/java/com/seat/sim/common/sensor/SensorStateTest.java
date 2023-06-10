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

public class SensorStateTest {

  public static SensorState MockSensorState() {
    return SensorStateTest.MockSensorState(false);
  }

  public static SensorState MockSensorState(boolean usePartialConstructor) {
    return (usePartialConstructor)
        ? new SensorState("sensor", "model", Set.of(), Set.of(), true)
        : new SensorState(
              "sensor",
              "model",
              Set.of("t1", "t2"),
              Set.of("m1", "m2"),
              true
            );
  }

  @Test
  public void sensorStateToJson() {
    SensorState state = SensorStateTest.MockSensorState();
    JsonObject json = state.toJson().getJsonObject();
    assertThat(json.getString(SensorState.SENSOR_ID), equalTo(state.getSensorID()));
    assertThat(json.getString(SensorState.SENSOR_MODEL), equalTo(state.getSensorModel()));
    assertThat(
        json.getJsonArray(SensorState.TAGS).toList(String.class),
        containsInAnyOrder(state.getTags().toArray())
      );
    assertThat(
        json.getJsonArray(SensorState.SUBJECTS).toList(String.class),
        containsInAnyOrder(state.getSubjects().toArray())
      );
    assertThat(json.getBoolean(SensorState.ACTIVE), is(state.isActive()));
  }

  @Test
  public void jsonToSensorState() {
    SensorState state = SensorStateTest.MockSensorState();
    JsonObjectBuilder json = JsonBuilder.Object();
    json.put(SensorState.SENSOR_ID, state.getSensorID());
    json.put(SensorState.SENSOR_MODEL, state.getSensorModel());
    JsonArrayBuilder jsonTags = JsonBuilder.Array();
    state.getTags().forEach(jsonTags::put);
    json.put(SensorState.TAGS, jsonTags.toJsonArray());
    JsonArrayBuilder jsonSubjects = JsonBuilder.Array();
    state.getSubjects().forEach(jsonSubjects::put);
    json.put(SensorState.SUBJECTS, jsonSubjects.toJsonArray());
    json.put(SensorState.ACTIVE, state.isActive());
    assertThat(new SensorState(json.toJson()), is(state));
  }

  @Test
  public void sensorStateDecode() {
    SensorState state = SensorStateTest.MockSensorState();
    assertThat(new SensorState(state.toJson()), is(state));
  }

  @Test
  public void sensorProtoPartialDecode() {
    SensorState state = SensorStateTest.MockSensorState(true);
    JsonObject json = state.toJson().getJsonObject();
    assertThat(json.getString(SensorState.SENSOR_ID), equalTo(state.getSensorID()));
    assertThat(json.getString(SensorState.SENSOR_MODEL), equalTo(state.getSensorModel()));
    assertThat(json.hasKey(SensorState.TAGS), is(false));
    assertThat(json.hasKey(SensorState.SUBJECTS), is(false));
    assertThat(json.getBoolean(SensorState.ACTIVE), is(state.isActive()));
    assertThat(new SensorState(json.toJson()), is(state));
  }
}
