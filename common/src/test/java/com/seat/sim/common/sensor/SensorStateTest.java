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

  @Test
  public void sensorStateToJson() {
    SensorState state = new SensorState(
        "sensor",
        "model",
        Set.of("tag1", "tag2"),
        Set.of("subject1", "subject2"),
        true
      );
    JsonObject json = state.toJson().getJsonObject();
    assertThat(json.getString(SensorState.SENSOR_ID), equalTo("sensor"));
    assertThat(json.getString(SensorState.SENSOR_MODEL), equalTo("model"));
    assertThat(
        json.getJsonArray(SensorState.TAGS).toList(String.class),
        containsInAnyOrder("tag1", "tag2")
      );
    assertThat(
        json.getJsonArray(SensorState.SUBJECTS).toList(String.class),
        containsInAnyOrder("subject1", "subject2")
      );
    assertThat(json.getBoolean(SensorState.ACTIVE), is(true));
  }

  @Test
  public void jsonToSensorState() {
    JsonObjectBuilder json = JsonBuilder.Object();
    json.put(SensorState.SENSOR_ID, "sensor");
    json.put(SensorState.SENSOR_MODEL, "model");
    JsonArrayBuilder jsonTags = JsonBuilder.Array();
    jsonTags.put("tag1");
    jsonTags.put("tag2");
    json.put(SensorState.TAGS, jsonTags.toJsonArray());
    JsonArrayBuilder jsonSubjects = JsonBuilder.Array();
    jsonSubjects.put("subject1");
    jsonSubjects.put("subject2");
    json.put(SensorState.SUBJECTS, jsonSubjects.toJsonArray());
    json.put(SensorState.ACTIVE, true);
    SensorState state = new SensorState(json.toJson());
    assertThat(state.getSensorID(), equalTo("sensor"));
    assertThat(state.getSensorModel(), equalTo("model"));
    assertThat(state.getTags(), containsInAnyOrder("tag1", "tag2"));
    assertThat(state.getSubjects(), containsInAnyOrder("subject1", "subject2"));
    assertThat(state.isActive(), is(true));
  }

  @Test
  public void sensorStateDecode() {
    SensorState state = new SensorState(
        "sensor",
        "model",
        Set.of("tag1", "tag2"),
        Set.of("subject1", "subject2"),
        true
      );
    assertThat(new SensorState(state.toJson()), is(state));
  }
}
