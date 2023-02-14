package com.seat.sim.common.scenario;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.seat.sim.common.core.CommonException;
import com.seat.sim.common.json.*;
import com.seat.sim.common.remote.RemoteState;

/**
 * A serializable class to represent a single snapshot of the current sim state.
 */
public class Snapshot extends Jsonable {
  public static final String ACTIVE_REMOTES = "active_remote_ids";
  public static final String DYNAMIC_REMOTES = "dynamic_remote_ids";
  public static final String HASH = "hash";
  public static final String SCENARIO_ID = "scenario_id";
  public static final String STATE = "state";
  public static final String STEP_SIZE = "step_size";
  public static final String TIME = "time";

  private Set<String> activeRemoteIDs;
  private Set<String> dynamicRemoteIDs;
  private String hash;
  private Map<String, RemoteState> remoteStates;
  private String scenarioID;
  private ScenarioStatus status;
  private double stepSize;
  private double time;

  public Snapshot(String hash, String scenarioID, ScenarioStatus status, double time, double stepSize,
      Collection<String> activeRemoteIDs, Collection<String> dynamicRemoteIDs,
      Map<String, RemoteState> remoteStates) {
    this.hash = hash;
    this.scenarioID = scenarioID;
    this.status = status;
    this.time = time;
    this.stepSize = stepSize;
    this.activeRemoteIDs = (activeRemoteIDs != null) ? new HashSet<>(activeRemoteIDs) : new HashSet<>();
    this.dynamicRemoteIDs = (dynamicRemoteIDs != null) ? new HashSet<>(dynamicRemoteIDs) : new HashSet<>();
    this.remoteStates = (remoteStates != null) ? new HashMap<>(remoteStates) : new HashMap<>();
    if (this.hash == null || this.hash.isBlank() || this.hash.isEmpty()) {
      throw new RuntimeException("cannot create Snapshot with empty hash");
    }
    if (this.scenarioID == null || this.scenarioID.isBlank() || this.scenarioID.isEmpty()) {
      throw new RuntimeException("cannot create Snapshot with empty scenarioID");
    }
  }

  public Snapshot(Json json) throws JsonException {
    super(json);
  }

  @Override
  protected void decode(JsonObject json) throws JsonException {
    this.hash = json.getString(Snapshot.HASH);
    this.scenarioID = json.getString(Snapshot.SCENARIO_ID);
    this.status = ScenarioStatus.Value(json.getInt(ScenarioStatus.STATUS));
    this.time = json.getDouble(Snapshot.TIME);
    this.stepSize = json.getDouble(Snapshot.STEP_SIZE);
    this.activeRemoteIDs = (json.hasKey(Snapshot.ACTIVE_REMOTES))
        ? new HashSet<>(json.getJsonArray(Snapshot.ACTIVE_REMOTES).toList(String.class))
        : new HashSet<>();
    this.dynamicRemoteIDs = (json.hasKey(Snapshot.DYNAMIC_REMOTES))
        ? new HashSet<>(json.getJsonArray(Snapshot.DYNAMIC_REMOTES).toList(String.class))
        : new HashSet<>();
    this.remoteStates = (json.hasKey(Snapshot.STATE))
        ? json.getJsonArray(Snapshot.STATE).toList(Json.class).stream()
            .map(state -> new RemoteState(state))
            .collect(Collectors.toMap(RemoteState::getRemoteID, Function.identity()))
        : new HashMap<>();
  }

  protected JsonObjectBuilder getJsonBuilder() throws JsonException {
    JsonObjectBuilder json = JsonBuilder.Object();
    json.put(Snapshot.HASH, this.hash);
    json.put(Snapshot.SCENARIO_ID, this.scenarioID);
    json.put(ScenarioStatus.STATUS, this.status.getType());
    json.put(Snapshot.TIME, this.time);
    json.put(Snapshot.STEP_SIZE, this.stepSize);
    if (this.hasActiveRemotes()) {
      json.put(Snapshot.ACTIVE_REMOTES, JsonBuilder.toJsonArray(this.activeRemoteIDs));
    }
    if (this.hasDynamicRemotes()) {
      json.put(Snapshot.DYNAMIC_REMOTES, JsonBuilder.toJsonArray(this.dynamicRemoteIDs));
    }
    if (this.hasRemotes()) {
      json.put(
          Snapshot.STATE,
          JsonBuilder.toJsonArray(
              this.remoteStates.values().stream()
                  .map(remoteState -> remoteState.toJson())
                  .collect(Collectors.toList())));
    }
    return json;
  }

  public Set<String> getActiveRemoteIDs() {
    return this.activeRemoteIDs;
  }

  public Collection<RemoteState> getActiveRemoteStates() {
    return this.getActiveDynamicRemoteIDs()
        .stream()
        .map(remoteID -> this.getRemoteStateWithID(remoteID))
        .collect(Collectors.toList());
  }

  public Set<String> getActiveDynamicRemoteIDs() {
    return this.getActiveRemoteIDs()
        .stream()
        .filter(remoteID -> this.hasDynamicRemoteWithID(remoteID))
        .collect(Collectors.toSet());
  }

  public Collection<RemoteState> getActiveDynamicRemoteStates() {
    return this.getActiveDynamicRemoteIDs()
        .stream()
        .map(remoteID -> this.getRemoteStateWithID(remoteID))
        .collect(Collectors.toList());
  }

  public Collection<RemoteState> getAllRemoteStatesWithMatch(Set<String> matchers) {
    return this.getRemoteStates()
        .stream()
        .filter(state -> state.hasMatch(matchers))
        .collect(Collectors.toList());
  }

  public Collection<RemoteState> getAllRemoteStatesWithTag(String tag) {
    return this.getRemoteStates()
        .stream()
        .filter(state -> state.hasTag(tag))
        .collect(Collectors.toList());
  }

  public Set<String> getDynamicRemoteIDs() {
    return this.dynamicRemoteIDs;
  }

  public Collection<RemoteState> getDynamicRemoteStates() {
    return this.getDynamicRemoteIDs()
        .stream()
        .map(remoteID -> this.getRemoteStateWithID(remoteID))
        .collect(Collectors.toList());
  }

  public String getHash() {
    return this.hash;
  }

  public Set<String> getRemoteIDs() {
    return this.remoteStates.keySet();
  }

  public Collection<RemoteState> getRemoteStates() {
    return this.remoteStates.values();
  }

  public Optional<RemoteState> getRemoteStateWithMatch(Set<String> matchers) {
    for (RemoteState state : this.getRemoteStates()) {
      if (state.hasMatch(matchers)) {
        return Optional.of(state);
      }
    }
    return Optional.empty();
  }

  public RemoteState getRemoteStateWithID(String remoteID) throws CommonException {
    return this.remoteStates.get(remoteID);
  }

  public Optional<RemoteState> getRemoteStateWithTag(String tag) {
    for (RemoteState state : this.getRemoteStates()) {
      if (state.hasTag(tag)) {
        return Optional.of(state);
      }
    }
    return Optional.empty();
  }

  public String getScenarioID() {
    return this.scenarioID;
  }

  public ScenarioStatus getStatus() {
    return this.status;
  }

  public double getStepSize() {
    return this.stepSize;
  }

  public double getTime() {
    return this.time;
  }

  public boolean hasActiveRemotes() {
    return !this.activeRemoteIDs.isEmpty();
  }

  public boolean hasActiveRemoteWithID(String remoteID) {
    return this.activeRemoteIDs.contains(remoteID);
  }

  public boolean hasDynamicRemotes() {
    return !this.dynamicRemoteIDs.isEmpty();
  }

  public boolean hasDynamicRemoteWithID(String remoteID) {
    return this.dynamicRemoteIDs.contains(remoteID);
  }

  public boolean hasError() {
    return this.status.equals(ScenarioStatus.ERROR);
  }

  public boolean hasHash(String hash) {
    if (hash == null || this.hash == null) {
      return this.hash == null;
    }
    return this.hash.equals(hash);
  }

  public boolean hasRemotes() {
    return !this.remoteStates.isEmpty();
  }

  public boolean hasRemoteMatch(Set<String> matchers) {
    for (RemoteState state : this.getRemoteStates()) {
      if (state.hasMatch(matchers)) {
        return true;
      }
    }
    return false;
  }

  public boolean hasRemoteStateWithID(String remoteID) {
    return this.remoteStates.containsKey(remoteID);
  }

  public boolean hasRemoteStateWithTag(String tag) {
    for (RemoteState state : this.getRemoteStates()) {
      if (state.hasTag(tag)) {
        return true;
      }
    }
    return false;
  }

  public boolean hasScenarioID(String scenarioID) {
    if (scenarioID == null || this.scenarioID == null) {
      return this.scenarioID == scenarioID;
    }
    return this.scenarioID.equals(scenarioID);
  }

  public boolean hasScenarioStatus(ScenarioStatus status) {
    return this.status.equals(status);
  }

  public boolean isDone() {
    return this.status.equals(ScenarioStatus.DONE);
  }

  public boolean isInProgress() {
    return this.status.equals(ScenarioStatus.START) || this.status.equals(ScenarioStatus.IN_PROGRESS);
  }

  public Json toJson() throws JsonException {
    return this.getJsonBuilder().toJson();
  }
}
