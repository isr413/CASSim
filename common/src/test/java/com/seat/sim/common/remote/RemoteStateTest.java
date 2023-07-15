package com.seat.sim.common.remote;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;

import com.seat.sim.common.gui.TeamColor;
import com.seat.sim.common.json.Json;
import com.seat.sim.common.json.JsonArrayBuilder;
import com.seat.sim.common.json.JsonBuilder;
import com.seat.sim.common.json.JsonObject;
import com.seat.sim.common.json.JsonObjectBuilder;
import com.seat.sim.common.math.PhysicsState;
import com.seat.sim.common.math.PhysicsStateTest;
import com.seat.sim.common.sensor.SensorState;
import com.seat.sim.common.sensor.SensorStateTest;

public class RemoteStateTest {

  public static RemoteState MockRemoteState() {
    return RemoteStateTest.MockRemoteState(false);
  }

  public static RemoteState MockRemoteState(boolean usePartialConstructor) {
    return (usePartialConstructor)
        ? new RemoteState(
              "remote",
              Set.of(),
              TeamColor.NONE,
              List.of(),
              true,
              true
            )
        : new RemoteState(
              "remote",
              Set.of("t1", "t2"),
              TeamColor.BLUE,
              PhysicsStateTest.MockPhysicsState(),
              1.,
              List.of(SensorStateTest.MockSensorState()),
              true,
              false
            );
  }

  @Test
  public void remoteStateToJson() {
    RemoteState state = RemoteStateTest.MockRemoteState();
    JsonObject json = state.toJson().getJsonObject();
    assertThat(json.getString(RemoteState.REMOTE_ID), equalTo(state.getRemoteID()));
    assertThat(
        json.getJsonArray(RemoteState.TAGS).toList(String.class),
        containsInAnyOrder(state.getTags().toArray())
      );
    assertThat(TeamColor.decodeType(json), equalTo(state.getTeam()));
    assertThat(new PhysicsState(json.getJson(RemoteState.STATE)), is(state.getPhysicsState()));
    assertThat(json.getDouble(RemoteState.FUEL), equalTo(state.getFuelAmount()));
    assertThat(
        json.getJsonArray(RemoteState.SENSORS)
          .toList(Json.class)
          .stream()
          .map(SensorState::new)
          .collect(Collectors.toList()),
        containsInAnyOrder(state.getSensorStates().toArray())
      );
    assertThat(json.getBoolean(RemoteState.ACTIVE), is(state.isActive()));
    assertThat(json.getBoolean(RemoteState.DONE), is(state.isDone()));
  }

  @Test
  public void jsonToRemoteState() {
    RemoteState state = RemoteStateTest.MockRemoteState();
    JsonObjectBuilder json = JsonBuilder.Object();
    json.put(RemoteState.REMOTE_ID, state.getRemoteID());
    JsonArrayBuilder jsonTags = JsonBuilder.Array();
    state.getTags().forEach(jsonTags::put);
    json.put(RemoteState.TAGS, jsonTags.toJson());
    json.put(RemoteState.TEAM, state.getTeam().getType());
    json.put(RemoteState.STATE, state.getPhysicsState().toJson());
    json.put(RemoteState.FUEL, state.getFuelAmount());
    JsonArrayBuilder jsonSensors = JsonBuilder.Array();
    state.getSensorStates().stream().map(sensor -> sensor.toJson()).forEach(jsonSensors::put);
    json.put(RemoteState.SENSORS, jsonSensors.toJson());
    json.put(RemoteState.ACTIVE, state.isActive());
    json.put(RemoteState.DONE, state.isDone());
    assertThat(new RemoteState(json.toJson()), is(state));
  }

  @Test
  public void remoteStateDecode() {
    RemoteState config = RemoteStateTest.MockRemoteState();
    assertThat(new RemoteState(config.toJson()), is(config));
  }

  @Test
  public void remoteStatePartialDecode() {
    RemoteState state = RemoteStateTest.MockRemoteState(true);
    JsonObject json = state.toJson().getJsonObject();
    assertThat(json.getString(RemoteState.REMOTE_ID), equalTo(state.getRemoteID()));
    assertThat(json.hasKey(RemoteState.TAGS), is(false));
    assertThat(json.hasKey(RemoteState.TEAM), is(false));
    assertThat(json.hasKey(RemoteState.STATE), is(false));
    assertThat(json.hasKey(RemoteState.FUEL), is(false));
    assertThat(json.hasKey(RemoteState.SENSORS), is(false));
    assertThat(json.getBoolean(RemoteState.ACTIVE), is(state.isActive()));
    assertThat(json.getBoolean(RemoteState.DONE), is(state.isDone()));
    assertThat(new RemoteState(json.toJson()), is(state));
  }

  @Test
  public void getSensorStates() {
    RemoteState state = RemoteStateTest.MockRemoteState();
    assertThat(state.hasSensors(), is(true));
    SensorState sensor = SensorStateTest.MockSensorState();
    assertThat(state.hasSensorStateWithID(sensor.getSensorID()), is(true));
    assertThat(state.getSensorStateWithID(sensor.getSensorID()), is(sensor));
    assertThat(state.hasSensorStateWithModel(sensor.getSensorModel()), is(true));
    state
      .getAllSensorStatesWithModel(sensor.getSensorModel())
      .forEach(sensorState -> assertThat(sensorState, is(sensor)));
    assertThat(state.hasSensorStateWithTag(sensor.getTags().iterator().next()), is(true));
    state
      .getAllSensorStatesWithTag(sensor.getTags().iterator().next())
      .forEach(sensorState -> assertThat(sensorState, is(sensor)));
  }

  @Test
  public void hasTeam() {
    assertThat(RemoteStateTest.MockRemoteState().hasTeam(), is(true));
    assertThat(RemoteStateTest.MockRemoteState(true).hasTeam(), is(false));
  }
}
