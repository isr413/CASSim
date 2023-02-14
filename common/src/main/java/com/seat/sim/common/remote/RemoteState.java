package com.seat.sim.common.remote;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.seat.sim.common.core.TeamColor;
import com.seat.sim.common.json.*;
import com.seat.sim.common.math.Vector;
import com.seat.sim.common.sensor.SensorState;

/** A serializable snapshot of a Remote. */
public class RemoteState extends Jsonable {
  public static final String ACTIVE = "active";
  public static final String DONE = "done";
  public static final String FUEL = "fuel";
  public static final String LOCATION = "location";
  public static final String REMOTE_ID = "remote_id";
  public static final String TAGS = "tags";
  public static final String VELOCITY = "velocity";

  private boolean active;
  private boolean done;
  private Optional<Double> fuel;
  private Optional<Vector> location;
  private String remoteID;
  private Map<String, SensorState> sensorStates;
  private Set<String> tags;
  private TeamColor team;
  private Optional<Vector> velocity;

  public RemoteState(String remoteID, Set<String> tags, TeamColor team, Vector location, Vector velocity,
      Collection<SensorState> sensorStates, boolean active, boolean done) {
    this(remoteID, tags, team, location, velocity, null, sensorStates, active, done);
  }

  public RemoteState(String remoteID, Set<String> tags, TeamColor team, Vector location, Vector velocity,
      double fuel, Collection<SensorState> sensorStates, boolean active, boolean done) {
    this(remoteID, tags, team, location, velocity, Optional.of(fuel), sensorStates, active, done);
  }

  private RemoteState(String remoteID, Set<String> tags, TeamColor team, Vector location, Vector velocity,
      Optional<Double> fuel, Collection<SensorState> sensorStates, boolean active, boolean done) {
    this.remoteID = remoteID;
    this.tags = (tags != null) ? new HashSet<>(tags) : new HashSet<>();
    this.team = (team != null) ? team : TeamColor.NONE;
    this.location = (location != null) ? Optional.of(location) : Optional.empty();
    this.velocity = (velocity != null) ? Optional.of(velocity) : Optional.empty();
    this.fuel = (fuel != null) ? fuel : Optional.empty();
    this.sensorStates = (sensorStates != null)
        ? sensorStates.stream().collect(Collectors.toMap(SensorState::getSensorID, Function.identity()))
        : new HashMap<>();
    this.active = active;
    this.done = done;
    if (this.remoteID == null || this.remoteID.isBlank() || this.remoteID.isEmpty()) {
      throw new RuntimeException("cannot create RemoteState with empty ID");
    }
  }

  public RemoteState(Json json) throws JsonException {
    super(json);
  }

  @Override
  protected void decode(JsonObject json) throws JsonException {
    this.remoteID = json.getString(RemoteState.REMOTE_ID);
    this.tags = (json.hasKey(RemoteState.TAGS))
        ? new HashSet<>(json.getJsonArray(RemoteState.TAGS).toList(String.class))
        : new HashSet<>();
    this.team = (json.hasKey(TeamColor.TEAM)) ? TeamColor.decodeType(json) : TeamColor.NONE;
    this.location = (json.hasKey(RemoteState.LOCATION))
        ? Optional.of(new Vector(json.getJson(RemoteState.LOCATION)))
        : Optional.empty();
    this.velocity = (json.hasKey(RemoteState.VELOCITY))
        ? Optional.of(new Vector(json.getJson(RemoteState.VELOCITY)))
        : Optional.empty();
    this.fuel = (json.hasKey(RemoteState.FUEL)) ? Optional.of(json.getDouble(RemoteState.FUEL)) : Optional.empty();
    this.active = json.getBoolean(RemoteState.ACTIVE);
    this.done = json.getBoolean(RemoteState.DONE);
    this.sensorStates = (json.hasKey(RemoteProto.SENSORS))
        ? json.getJsonArray(RemoteProto.SENSORS).toList(Json.class).stream()
            .map(state -> new SensorState(state))
            .collect(Collectors.toMap(SensorState::getSensorID, Function.identity()))
        : new HashMap<>();
  }

  protected JsonObjectBuilder getJsonBuilder() throws JsonException {
    JsonObjectBuilder json = JsonBuilder.Object();
    json.put(RemoteState.REMOTE_ID, this.remoteID);
    if (this.hasTags()) {
      json.put(RemoteState.TAGS, JsonBuilder.toJsonArray(this.tags));
    }
    if (this.hasTeam()) {
      json.put(TeamColor.TEAM, this.team.getType());
    }
    if (this.hasLocation()) {
      json.put(RemoteState.LOCATION, this.getLocation().toJson());
    }
    if (this.isMobile()) {
      json.put(RemoteState.VELOCITY, this.getVelocity().toJson());
    }
    if (this.hasFuel()) {
      json.put(RemoteState.FUEL, this.getFuelAmount());
    }
    json.put(RemoteState.ACTIVE, this.active);
    json.put(RemoteState.DONE, this.done);
    if (this.hasSensors()) {
      json.put(
          RemoteProto.SENSORS,
          JsonBuilder.toJsonArray(
              this.sensorStates.values().stream()
                  .map(sensorState -> sensorState.toJson())
                  .collect(Collectors.toList())));
    }
    return json;
  }

  public Collection<SensorState> getAllSensorStatesWithModel(String sensorModel) {
    return this.getSensorStates()
        .stream()
        .filter(sensorState -> sensorState.hasSensorModel(sensorModel))
        .collect(Collectors.toList());
  }

  public Collection<SensorState> getAllSensorStatesWithTag(String tag) {
    return this.getSensorStates()
        .stream()
        .filter(sensorState -> sensorState.hasTag(tag))
        .collect(Collectors.toList());
  }

  public double getFuelAmount() {
    if (this.fuel.isEmpty()) {
      return 0.;
    }
    return this.fuel.get();
  }

  public String getLabel() {
    return String.format("%s:<remote>", this.remoteID);
  }

  public Vector getLocation() {
    return this.location.get();
  }

  public String getRemoteID() {
    return this.remoteID;
  }

  public Set<String> getSensorIDs() {
    return this.sensorStates.keySet();
  }

  public Collection<SensorState> getSensorStates() {
    return this.sensorStates.values();
  }

  public SensorState getSensorStateWithID(String sensorID) {
    return this.sensorStates.get(sensorID);
  }

  public Optional<SensorState> getSensorStateWithModel(String sensorModel) {
    for (SensorState state : this.getSensorStates()) {
      if (state.hasSensorModel(sensorModel)) {
        return Optional.of(state);
      }
    }
    return Optional.empty();
  }

  public Optional<SensorState> getSensorStateWithTag(String tag) {
    for (SensorState state : this.getSensorStates()) {
      if (state.hasTag(tag)) {
        return Optional.of(state);
      }
    }
    return Optional.empty();
  }

  public double getSpeed() {
    if (this.velocity.isEmpty()) {
      return 0.;
    }
    return this.getVelocity().getMagnitude();
  }

  public Set<String> getTags() {
    return this.tags;
  }

  public TeamColor getTeam() {
    return this.team;
  }

  public Vector getVelocity() {
    if (this.velocity.isEmpty()) {
      return Vector.ZERO;
    }
    return this.velocity.get();
  }

  public boolean hasFuel() {
    return this.fuel.isPresent();
  }

  public boolean hasLocation() {
    return this.location.isPresent();
  }

  public boolean hasMatch(Set<String> matchers) {
    return this.hasRemoteMatch(matchers) || this.hasSensorMatch(matchers);
  }

  public boolean hasRemoteID(String remoteID) {
    if (remoteID == null || this.remoteID == null) {
      return this.remoteID == remoteID;
    }
    return this.remoteID.equals(remoteID);
  }

  public boolean hasRemoteMatch(Set<String> matchers) {
    for (String matcher : matchers) {
      if (this.hasTag(matcher)) {
        return true;
      }
    }
    return false;
  }

  public boolean hasSensorMatch(Set<String> matchers) {
    for (SensorState state : this.getSensorStates()) {
      if (state.hasMatch(matchers)) {
        return true;
      }
    }
    return false;
  }

  public boolean hasSensors() {
    return !this.sensorStates.isEmpty();
  }

  public boolean hasSensorStateWithID(String sensorID) {
    return this.sensorStates.containsKey(sensorID);
  }

  public boolean hasSensorStateWithModel(String sensorModel) {
    return !this.getSensorStateWithModel(sensorModel).isEmpty();
  }

  public boolean hasSensorStateWithTag(String tag) {
    return !this.getSensorStateWithTag(tag).isEmpty();
  }

  public boolean hasSubjectWithID(String subjectID) {
    for (SensorState state : this.getSensorStates()) {
      if (state.hasSubjectWithID(subjectID)) {
        return true;
      }
    }
    return false;
  }

  public boolean hasTags() {
    return !this.tags.isEmpty();
  }

  public boolean hasTag(String tag) {
    return this.tags.contains(tag);
  }
  public boolean hasTeam() {
    return !this.team.equals(TeamColor.NONE);
  }

  public boolean isActive() {
    return this.active && this.isEnabled();
  }

  public boolean isDone() {
    return this.done;
  }

  public boolean isEnabled() {
    return !this.hasFuel() || this.getFuelAmount() > 0.;
  }

  public boolean isInMotion() {
    return this.isMobile() && this.getSpeed() > 0.;
  }

  public boolean isMobile() {
    return this.velocity.isPresent();
  }

  public Json toJson() throws JsonException {
    return this.getJsonBuilder().toJson();
  }
}
