package com.seat.rescuesim.common.scenario;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import com.seat.rescuesim.common.core.CommonException;
import com.seat.rescuesim.common.json.JSONAble;
import com.seat.rescuesim.common.json.JSONArray;
import com.seat.rescuesim.common.json.JSONArrayBuilder;
import com.seat.rescuesim.common.json.JSONBuilder;
import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.JSONObjectBuilder;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.remote.RemoteRegistry;
import com.seat.rescuesim.common.remote.RemoteState;

/** A serializable class to represent a single snapshot of the current sim state. */
public class Snapshot extends JSONAble {
    public static final String ACTIVE_REMOTES = "active_remotes";
    public static final String DYNAMIC_REMOTES = "dynamic_remotes";
    public static final String HASH = "hash";
    public static final String REMOTE_IDS = "remote_ids";
    public static final String SCENARIO_ID = "scenario_id";
    public static final String STATE = "state";
    public static final String STEP_SIZE = "step_size";
    public static final String TIME = "time";

    private HashSet<String> activeRemotes;
    private HashSet<String> dynamicRemotes;
    private String hash;
    private HashSet<String> remoteIDs;
    private String scenarioID;
    private ScenarioType scenarioType;
    private HashMap<String, RemoteState> state;
    private ScenarioStatus status;
    private double stepSize;
    private double time;

    public Snapshot(String hash, ScenarioType scenarioType, String scenarioID, double stepSize,
            Collection<String> remoteIDs, HashMap<String, RemoteState> state) {
        this(hash, scenarioType, scenarioID, ScenarioStatus.START, 0, stepSize, remoteIDs, remoteIDs, remoteIDs, state);
    }

    public Snapshot(String hash, ScenarioType scenarioType, String scenarioID, ScenarioStatus status, double time,
            double stepSize, Collection<String> remoteIDs, Collection<String> activeRemotes,
            Collection<String> dynamicRemotes, HashMap<String, RemoteState> state) {
        this.hash = hash;
        this.scenarioType = scenarioType;
        this.scenarioID = scenarioID;
        this.status = status;
        this.time = time;
        this.stepSize = stepSize;
        this.remoteIDs = (remoteIDs != null) ? new HashSet<>(remoteIDs) : new HashSet<>();
        this.activeRemotes = (activeRemotes != null) ? new HashSet<>(activeRemotes) : new HashSet<>();
        this.dynamicRemotes = (dynamicRemotes != null) ? new HashSet<>(dynamicRemotes) : new HashSet<>();
        this.state = state;
    }

    public Snapshot(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        this.hash = json.getString(Snapshot.HASH);
        this.scenarioType = ScenarioType.decodeType(json);
        this.scenarioID = json.getString(Snapshot.SCENARIO_ID);
        this.status = ScenarioStatus.values()[json.getInt(ScenarioStatus.STATUS)];
        this.time = json.getDouble(Snapshot.TIME);
        this.stepSize = json.getDouble(Snapshot.STEP_SIZE);
        this.remoteIDs = new HashSet<>();
        if (json.hasKey(Snapshot.REMOTE_IDS)) {
            JSONArray jsonRemotes = json.getJSONArray(Snapshot.REMOTE_IDS);
            for (int i = 0; i < jsonRemotes.length(); i++) {
                this.remoteIDs.add(jsonRemotes.getString(i));
            }
        }
        this.activeRemotes = new HashSet<>();
        if (json.hasKey(Snapshot.ACTIVE_REMOTES)) {
            JSONArray jsonActiveRemotes = json.getJSONArray(Snapshot.ACTIVE_REMOTES);
            for (int i = 0; i < jsonActiveRemotes.length(); i++) {
                this.activeRemotes.add(jsonActiveRemotes.getString(i));
            }
        }
        this.dynamicRemotes = new HashSet<>();
        if (json.hasKey(Snapshot.DYNAMIC_REMOTES)) {
            JSONArray jsonDynamicRemotes = json.getJSONArray(Snapshot.DYNAMIC_REMOTES);
            for (int i = 0; i < jsonDynamicRemotes.length(); i++) {
                this.dynamicRemotes.add(jsonDynamicRemotes.getString(i));
            }
        }
        this.state = new HashMap<>();
        JSONArray jsonState = json.getJSONArray(Snapshot.STATE);
        for (int i = 0; i < jsonState.length(); i++) {
            RemoteState state = RemoteRegistry.decodeTo(jsonState.getJSONOption(i), RemoteState.class);
            this.state.put(state.getRemoteID(), state);
        }
    }

    public Collection<String> getActiveRemotes() {
        return this.activeRemotes;
    }

    public Collection<String> getDynamicRemotes() {
        return this.dynamicRemotes;
    }

    public String getHash() {
        return this.hash;
    }

    public Collection<String> getRemoteIDs() {
        return this.remoteIDs;
    }

    public RemoteState getRemoteState(String remoteID) throws CommonException {
        if (!this.hasStateWithID(remoteID)) {
            throw new CommonException(String.format("No state for remote %s", remoteID));
        }
        return this.state.get(remoteID);
    }

    public String getScenarioID() {
        return this.scenarioID;
    }

    public ScenarioType getScenarioType() {
        return this.scenarioType;
    }

    public Collection<RemoteState> getState() {
        return new ArrayList<RemoteState>(this.state.values());
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

    public boolean hasActiveRemoteWithID(String remoteID) {
        return this.activeRemotes.contains(remoteID);
    }

    public boolean hasActiveRemotes() {
        return !this.activeRemotes.isEmpty();
    }

    public boolean hasDynamicRemoteWithID(String remoteID) {
        return this.dynamicRemotes.contains(remoteID);
    }

    public boolean hasDynamicRemotes() {
        return !this.dynamicRemotes.isEmpty();
    }

    public boolean hasError() {
        return this.status.equals(ScenarioStatus.ERROR);
    }

    public boolean hasRemoteWithID(String remoteID) {
        return this.remoteIDs.contains(remoteID);
    }

    public boolean hasRemotes() {
        return !this.remoteIDs.isEmpty();
    }

    public boolean hasStateWithID(String remoteID) {
        return this.state.containsKey(remoteID);
    }

    public boolean hasState() {
        return !this.state.isEmpty();
    }

    public boolean isDone() {
        return this.status.equals(ScenarioStatus.DONE);
    }

    public boolean isInProgress() {
        return this.status.equals(ScenarioStatus.START) || this.status.equals(ScenarioStatus.IN_PROGRESS);
    }

    public JSONOption toJSON() throws JSONException {
        JSONObjectBuilder json = JSONBuilder.Object();
        json.put(Snapshot.HASH, this.hash);
        json.put(ScenarioType.SCENARIO_TYPE, this.scenarioType.getType());
        json.put(Snapshot.SCENARIO_ID, this.scenarioID);
        json.put(ScenarioStatus.STATUS, this.status.getType());
        json.put(Snapshot.TIME, this.time);
        json.put(Snapshot.STEP_SIZE, this.stepSize);
        if (this.hasRemotes()) {
            JSONArrayBuilder jsonRemotes = JSONBuilder.Array();
            for (String remote : this.remoteIDs) {
                jsonRemotes.put(remote);
            }
            json.put(Snapshot.REMOTE_IDS, jsonRemotes.toJSON());
        }
        if (this.hasActiveRemotes()) {
            JSONArrayBuilder jsonActiveRemotes = JSONBuilder.Array();
            for (String remote : this.activeRemotes) {
                jsonActiveRemotes.put(remote);
            }
            json.put(Snapshot.ACTIVE_REMOTES, jsonActiveRemotes.toJSON());
        }
        if (this.hasDynamicRemotes()) {
            JSONArrayBuilder jsonDynamicRemotes = JSONBuilder.Array();
            for (String remote : this.dynamicRemotes) {
                jsonDynamicRemotes.put(remote);
            }
            json.put(Snapshot.DYNAMIC_REMOTES, jsonDynamicRemotes.toJSON());
        }
        JSONArrayBuilder jsonState = JSONBuilder.Array();
        for (RemoteState state : this.state.values()) {
            jsonState.put(state.toJSON());
        }
        json.put(Snapshot.STATE, jsonState.toJSON());
        return json.toJSON();
    }

    public boolean equals(Snapshot snap) {
        if (snap == null) return false;
        return this.hash.equals(snap.hash);
    }

}
