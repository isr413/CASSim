package com.seat.rescuesim.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import com.seat.rescuesim.common.json.*;
import com.seat.rescuesim.common.remote.RemoteState;
import com.seat.rescuesim.common.util.Debugger;
import com.seat.rescuesim.common.util.RemoteFactory;

/** A serializable class to represent a single snapshot of the current sim state. */
public class Snapshot extends JSONAble {
    private static final String ACTIVE_REMOTES = "active_remotes";
    private static final String DYNAMIC_REMOTES = "dynamic_remotes";
    private static final String HASH = "hash";
    private static final String REMOTE_IDS = "remote_ids";
    private static final String SCENARIO_ID = "scenario_id";
    private static final String STATE = "state";
    private static final String STATUS = "status";
    private static final String STEP_SIZE = "step_size";
    private static final String TIME = "time";

    private HashSet<String> activeRemotes;
    private HashSet<String> dynamicRemotes;
    private String hash;
    private HashSet<String> remoteIDs;
    private String scenarioID;
    private HashMap<String, RemoteState> state;
    private SnapStatus status;
    private double stepSize;
    private double time;

    public Snapshot(JSONObject json) throws JSONException {
        super(json);
    }

    public Snapshot(JSONOption option) throws JSONException {
        super(option);
    }

    public Snapshot(String encoding) throws JSONException {
        super(encoding);
    }

    public Snapshot(String hash, String scenarioID, double stepSize, HashSet<String> remoteIDs,
            HashMap<String, RemoteState> state) {
        this(hash, scenarioID, SnapStatus.START, 0, stepSize, remoteIDs, new HashSet<String>(remoteIDs),
            new HashSet<String>(remoteIDs), state);
    }

    public Snapshot(String hash, String scenarioID, SnapStatus status, double time, double stepSize,
            HashSet<String> remoteIDs, HashSet<String> activeRemotes, HashSet<String> dynamicRemotes,
            HashMap<String, RemoteState> state) {
        this.hash = hash;
        this.scenarioID = scenarioID;
        this.status = status;
        this.time = time;
        this.stepSize = stepSize;
        this.remoteIDs = remoteIDs;
        this.activeRemotes = activeRemotes;
        this.dynamicRemotes = dynamicRemotes;
        this.state = state;
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        this.hash = json.getString(Snapshot.HASH);
        this.scenarioID = json.getString(Snapshot.SCENARIO_ID);
        this.status = SnapStatus.values()[json.getInt(Snapshot.STATUS)];
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
            RemoteState state = RemoteFactory.decodeRemoteState(jsonState.getJSONObject(i));
            this.state.put(state.getRemoteID(), state);
        }
    }

    public HashSet<String> getActiveRemotes() {
        return this.activeRemotes;
    }

    public HashSet<String> getDynamicRemotes() {
        return this.dynamicRemotes;
    }

    public String getHash() {
        return this.hash;
    }

    public HashSet<String> getRemoteIDs() {
        return this.remoteIDs;
    }

    public RemoteState getRemoteState(String remoteID) {
        if (!this.hasStateWithID(remoteID)) {
            Debugger.logger.err(String.format("No state for remote %s", remoteID));
            return null;
        }
        return this.state.get(remoteID);
    }

    public String getScenarioID() {
        return this.scenarioID;
    }

    public ArrayList<RemoteState> getState() {
        return new ArrayList<RemoteState>(this.state.values());
    }

    public SnapStatus getStatus() {
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

    public JSONOption toJSON() {
        JSONObjectBuilder json = JSONBuilder.Object();
        json.put(Snapshot.HASH, this.hash);
        json.put(Snapshot.SCENARIO_ID, this.scenarioID);
        json.put(Snapshot.STATUS, this.status.getType());
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
        return this.hash.equals(snap.hash);
    }

}
