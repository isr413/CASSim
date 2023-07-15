package com.seat.sim.common.remote;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.seat.sim.common.gui.TeamColor;
import com.seat.sim.common.json.*;
import com.seat.sim.common.math.PhysicsState;
import com.seat.sim.common.math.Vector;
import com.seat.sim.common.sensor.SensorState;

/** A serializable snapshot of a Remote. */
public class RemoteState extends Jsonable {
  public static final String ACTIVE = "active";
  public static final String DONE = "done";
  public static final String FUEL = "fuel";
  public static final String REMOTE_ID = "remote_id";
  public static final String SENSORS = "sensors";
  public static final String STATE = "state";
  public static final String TAGS = "tags";
  public static final String TEAM = "team";

  private boolean active;
  private boolean done;
  private Optional<Double> fuel;
  private String remoteID;
  private Map<String, SensorState> sensorStates;
  private Optional<PhysicsState> state;
  private Set<String> tags;
  private TeamColor team;

  public RemoteState(String remoteID, Set<String> tags, TeamColor team,
        Collection<SensorState> sensorStates, boolean active, boolean done) {
    this(remoteID, tags, team, Optional.empty(), Optional.empty(), sensorStates, active, done);
  }

  public RemoteState(String remoteID, Set<String> tags, TeamColor team, PhysicsState state,
        Collection<SensorState> sensorStates, boolean active, boolean done) {
    this(remoteID, tags, team, Optional.of(state), Optional.empty(), sensorStates, active, done);
  }

  public RemoteState(String remoteID, Set<String> tags, TeamColor team, double fuel,
        Collection<SensorState> sensorStates, boolean active, boolean done) {
    this(remoteID, tags, team, Optional.empty(), Optional.of(fuel), sensorStates, active, done);
  }

  public RemoteState(String remoteID, Set<String> tags, TeamColor team, PhysicsState state,
      double fuel, Collection<SensorState> sensorStates, boolean active, boolean done) {
    this(remoteID, tags, team, Optional.of(state), Optional.of(fuel), sensorStates, active, done);
  }

  private RemoteState(String remoteID, Set<String> tags, TeamColor team,
        Optional<PhysicsState> state, Optional<Double> fuel, Collection<SensorState> sensorStates,
        boolean active, boolean done) {
    this.remoteID = remoteID;
    this.tags = (tags != null) ? new HashSet<>(tags) : new HashSet<>();
    this.team = (team != null) ? team : TeamColor.NONE;
    this.state = (state != null) ? state : Optional.empty();
    this.fuel = (fuel != null) ? fuel : Optional.empty();
    this.sensorStates = (sensorStates != null)
        ? sensorStates
            .stream()
            .collect(Collectors.toMap(SensorState::getSensorID, Function.identity()))
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
    this.team = (json.hasKey(RemoteState.TEAM)) ? TeamColor.decodeType(json) : TeamColor.NONE;
    this.state = (json.hasKey(RemoteState.STATE))
        ? Optional.of(new PhysicsState(json.getJson(RemoteState.STATE)))
        : Optional.empty();
    this.fuel = (json.hasKey(RemoteState.FUEL))
        ? Optional.of(json.getDouble(RemoteState.FUEL))
        : Optional.empty();
    this.active = json.getBoolean(RemoteState.ACTIVE);
    this.done = json.getBoolean(RemoteState.DONE);
    this.sensorStates = (json.hasKey(RemoteState.SENSORS))
        ? json.getJsonArray(RemoteState.SENSORS).toList(Json.class).stream()
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
      json.put(RemoteState.TEAM, this.team.getType());
    }
    if (this.hasLocation()) {
      json.put(RemoteState.STATE, this.getPhysicsState().toJson());
    }
    if (this.hasFuel()) {
      json.put(RemoteState.FUEL, this.getFuelAmount());
    }
    json.put(RemoteState.ACTIVE, this.active);
    json.put(RemoteState.DONE, this.done);
    if (this.hasSensors()) {
      json.put(
          RemoteState.SENSORS,
          JsonBuilder.toJsonArray(
              this.sensorStates.values().stream()
                  .map(sensorState -> sensorState.toJson())
                  .collect(Collectors.toList())));
    }
    return json;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || this.getClass() != o.getClass()) {
      return this == o;
    }
    return this.equals((RemoteState) o);
  }

  public boolean equals(RemoteState state) {
    return this.remoteID.equals(state.remoteID) && this.tags.equals(state.tags) &&
        this.team.equals(state.team) && this.hasPhysicsState() == state.hasPhysicsState() &&
        (!this.hasPhysicsState() || this.getPhysicsState().equals(state.getPhysicsState())) &&
        this.hasFuel() == state.hasFuel() &&
        (!this.hasFuel() || Vector.near(this.getFuelAmount(), state.getFuelAmount())) &&
        this.sensorStates.equals(state.sensorStates) && this.active == state.active &&
        this.done == state.done;
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
    return this.getPhysicsState().getLocation();
  }

  public PhysicsState getPhysicsState() {
    return this.state.get();
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
    if (this.state.isEmpty()) {
      return 0.;
    }
    return this.getPhysicsState().getSpeed();
  }

  public Set<String> getSubjects() {
    return this.getSensorStates()
      .stream()
      .flatMap(state -> state.getSubjects().stream())
      .collect(Collectors.toSet());
  }

  public Set<String> getTags() {
    return this.tags;
  }

  public TeamColor getTeam() {
    return this.team;
  }

  public Vector getVelocity() {
    if (this.state.isEmpty()) {
      return Vector.ZERO;
    }
    return this.getPhysicsState().getVelocity();
  }

  public boolean hasFuel() {
    return this.fuel.isPresent();
  }

  public boolean hasLocation() {
    return this.state.isPresent() && this.state.get().hasLocation();
  }

  public boolean hasMatch(Set<String> matchers) {
    return this.hasRemoteMatch(matchers) || this.hasSensorMatch(matchers);
  }

  public boolean hasPhysicsState() {
    return this.state.isPresent();
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
    return this.hasLocation() && this.getPhysicsState().isMobile();
  }

  public Json toJson() throws JsonException {
    return this.getJsonBuilder().toJson();
  }
}
