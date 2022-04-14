package com.seat.sim.common.scenario;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.seat.sim.common.core.CommonException;
import com.seat.sim.common.json.JSONAble;
import com.seat.sim.common.json.JSONArray;
import com.seat.sim.common.json.JSONArrayBuilder;
import com.seat.sim.common.json.JSONBuilder;
import com.seat.sim.common.json.JSONException;
import com.seat.sim.common.json.JSONObject;
import com.seat.sim.common.json.JSONObjectBuilder;
import com.seat.sim.common.json.JSONOptional;
import com.seat.sim.common.remote.RemoteRegistry;
import com.seat.sim.common.remote.RemoteState;

/** A serializable class to represent a single snapshot of the current sim state. */
public class Snapshot extends JSONAble {
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
        this.remoteStates = new HashMap<>(remoteStates);
    }

    public Snapshot(JSONOptional optional) throws JSONException {
        super(optional);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        this.hash = json.getString(Snapshot.HASH);
        this.scenarioID = json.getString(Snapshot.SCENARIO_ID);
        this.status = ScenarioStatus.values()[json.getInt(ScenarioStatus.STATUS)];
        this.time = json.getDouble(Snapshot.TIME);
        this.stepSize = json.getDouble(Snapshot.STEP_SIZE);
        this.activeRemoteIDs = new HashSet<>();
        if (json.hasKey(Snapshot.ACTIVE_REMOTES)) {
            JSONArray jsonActiveRemoteIDs = json.getJSONArray(Snapshot.ACTIVE_REMOTES);
            for (int i = 0; i < jsonActiveRemoteIDs.length(); i++) {
                this.activeRemoteIDs.add(jsonActiveRemoteIDs.getString(i));
            }
        }
        this.dynamicRemoteIDs = new HashSet<>();
        if (json.hasKey(Snapshot.DYNAMIC_REMOTES)) {
            JSONArray jsonDynamicRemoteIDs = json.getJSONArray(Snapshot.DYNAMIC_REMOTES);
            for (int i = 0; i < jsonDynamicRemoteIDs.length(); i++) {
                this.dynamicRemoteIDs.add(jsonDynamicRemoteIDs.getString(i));
            }
        }
        this.remoteStates = new HashMap<>();
        JSONArray jsonRemoteStates = json.getJSONArray(Snapshot.STATE);
        for (int i = 0; i < jsonRemoteStates.length(); i++) {
            RemoteState remoteState = RemoteRegistry.decodeTo(jsonRemoteStates.getJSONOptional(i), RemoteState.class);
            this.remoteStates.put(remoteState.getRemoteID(), remoteState);
        }
    }

    public Collection<String> getActiveRemoteIDs() {
        return this.activeRemoteIDs;
    }

    public Collection<RemoteState> getActiveRemoteStates() {
        return this.getActiveDynamicRemoteIDs().stream()
            .map(remoteID -> this.getRemoteStateWithID(remoteID))
            .collect(Collectors.toList());
    }

    public Collection<String> getActiveDynamicRemoteIDs() {
        return this.getActiveRemoteIDs().stream()
            .filter(remoteID -> this.hasDynamicRemoteWithID(remoteID))
            .collect(Collectors.toList());
    }

    public Collection<RemoteState> getActiveDynamicRemoteStates() {
        return this.getActiveDynamicRemoteIDs().stream()
            .map(remoteID -> this.getRemoteStateWithID(remoteID))
            .collect(Collectors.toList());
    }

    public Collection<String> getDynamicRemoteIDs() {
        return this.dynamicRemoteIDs;
    }

    public Collection<RemoteState> getDynamicRemoteStates() {
        return this.getDynamicRemoteIDs().stream()
            .map(remoteID -> this.getRemoteStateWithID(remoteID))
            .collect(Collectors.toList());
    }

    public String getHash() {
        return this.hash;
    }

    public Collection<String> getRemoteIDs() {
        return this.remoteStates.keySet();
    }

    public Collection<RemoteState> getRemoteStates() {
        return this.remoteStates.values();
    }

    public RemoteState getRemoteStateWithID(String remoteID) throws CommonException {
        if (!this.hasRemoteWithID(remoteID)) {
            throw new CommonException(String.format("No state for remote %s", remoteID));
        }
        return this.remoteStates.get(remoteID);
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

    public boolean hasRemotes() {
        return !this.remoteStates.isEmpty();
    }

    public boolean hasRemoteWithID(String remoteID) {
        return this.remoteStates.containsKey(remoteID);
    }

    public boolean isDone() {
        return this.status.equals(ScenarioStatus.DONE);
    }

    public boolean isInProgress() {
        return this.status.equals(ScenarioStatus.START) || this.status.equals(ScenarioStatus.IN_PROGRESS);
    }

    public JSONOptional toJSON() throws JSONException {
        JSONObjectBuilder json = JSONBuilder.Object();
        json.put(Snapshot.HASH, this.hash);
        json.put(Snapshot.SCENARIO_ID, this.scenarioID);
        json.put(ScenarioStatus.STATUS, this.status.getType());
        json.put(Snapshot.TIME, this.time);
        json.put(Snapshot.STEP_SIZE, this.stepSize);
        if (this.hasActiveRemotes()) {
            JSONArrayBuilder jsonActiveRemoteIDs = JSONBuilder.Array();
            for (String remoteID : this.activeRemoteIDs) {
                jsonActiveRemoteIDs.put(remoteID);
            }
            json.put(Snapshot.ACTIVE_REMOTES, jsonActiveRemoteIDs.toJSON());
        }
        if (this.hasDynamicRemotes()) {
            JSONArrayBuilder jsonDynamicRemoteIDs = JSONBuilder.Array();
            for (String remoteID : this.dynamicRemoteIDs) {
                jsonDynamicRemoteIDs.put(remoteID);
            }
            json.put(Snapshot.DYNAMIC_REMOTES, jsonDynamicRemoteIDs.toJSON());
        }
        JSONArrayBuilder jsonRemoteStates = JSONBuilder.Array();
        for (RemoteState remoteState : this.remoteStates.values()) {
            jsonRemoteStates.put(remoteState.toJSON());
        }
        json.put(Snapshot.STATE, jsonRemoteStates.toJSON());
        return json.toJSON();
    }

    public boolean equals(Snapshot snap) {
        if (snap == null) return false;
        return this.hash.equals(snap.hash);
    }

}
