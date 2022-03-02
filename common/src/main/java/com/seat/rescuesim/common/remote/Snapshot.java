package com.seat.rescuesim.common.remote;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import com.seat.rescuesim.common.json.*;
import com.seat.rescuesim.common.util.Debugger;

/** A serializable class to represent a single snapshot of the current sim state. */
public class Snapshot extends JSONAble {
    private static final String ACTIVE_REMOTES = "active_remotes";
    private static final String HASH = "hash";
    private static final String REMOTES = "remotes";
    private static final String SCENARIO_ID = "scenario_id";
    private static final String STATE = "state";
    private static final String STATUS = "status";
    private static final String STEP_SIZE = "step_size";
    private static final String TIME = "time";

    private HashSet<String> activeRemotes;
    private String hash;
    private HashSet<String> remotes;
    private String scenarioID;
    private HashMap<String, RemoteState> state;
    private SnapStatus status;
    private double stepSize;
    private double time;

    public Snapshot(JSONObject json) {
        super(json);
    }

    public Snapshot(JSONOption option) {
        super(option);
    }

    public Snapshot(String encoding) {
        super(encoding);
    }

    public Snapshot(String hash, String scenarioID, double stepSize, HashSet<String> remotes,
            HashMap<String, RemoteState> state) {
        this(hash, scenarioID, SnapStatus.START, 0, stepSize, remotes, remotes, state);
    }

    public Snapshot(String hash, String scenarioID, SnapStatus status, double time, double stepSize,
            HashSet<String> remotes, HashSet<String> activeRemotes, HashMap<String, RemoteState> state) {
        this.hash = hash;
        this.scenarioID = scenarioID;
        this.status = status;
        this.time = time;
        this.stepSize = stepSize;
        this.remotes = remotes;
        this.activeRemotes = activeRemotes;
        this.state = state;
    }

    @Override
    protected void decode(JSONObject json) {
        this.hash = json.getString(Snapshot.HASH);
        this.scenarioID = json.getString(Snapshot.SCENARIO_ID);
        this.status = SnapStatus.values()[json.getInt(Snapshot.STATUS)];
        this.time = json.getDouble(Snapshot.TIME);
        this.stepSize = json.getDouble(Snapshot.STEP_SIZE);
        this.remotes = new HashSet<>();
        JSONArray jsonRemotes = json.getJSONArray(Snapshot.REMOTES);
        for (int i = 0; i < jsonRemotes.length(); i++) {
            this.remotes.add(jsonRemotes.getString(i));
        }
        this.activeRemotes = new HashSet<>();
        JSONArray jsonActiveRemotes = json.getJSONArray(Snapshot.ACTIVE_REMOTES);
        for (int i = 0; i < jsonActiveRemotes.length(); i++) {
            this.activeRemotes.add(jsonActiveRemotes.getString(i));
        }
        JSONArray jsonState = json.getJSONArray(Snapshot.STATE);
        this.state = new HashMap<>();
        for (int i = 0; i < jsonState.length(); i++) {
            RemoteState state = new RemoteState(jsonState.getJSONObject(i));
            this.state.put(state.getRemoteLabel(), state);
        }
    }

    public ArrayList<String> getActiveRemotes() {
        return new ArrayList<String>(this.activeRemotes);
    }

    public String getHash() {
        return this.hash;
    }

    public ArrayList<String> getRemotes() {
        return new ArrayList<String>(this.remotes);
    }

    public RemoteState getRemoteState(String remote) {
        if (!this.hasRemoteState(remote)) {
            Debugger.logger.err(String.format("No state for remote %s", remote));
            return null;
        }
        return this.state.get(remote);
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

    public boolean hasActiveRemote(String remote) {
        return this.activeRemotes.contains(remote);
    }

    public boolean hasActiveRemotes() {
        return !this.activeRemotes.isEmpty();
    }

    public boolean hasRemote(String remote) {
        return this.remotes.contains(remote);
    }

    public boolean hasRemotes() {
        return !this.remotes.isEmpty();
    }

    public boolean hasRemoteState(String remote) {
        return this.state.containsKey(remote);
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
        JSONArrayBuilder jsonRemotes = JSONBuilder.Array();
        for (String remote : this.remotes) {
            jsonRemotes.put(remote);
        }
        json.put(Snapshot.REMOTES, jsonRemotes.toJSON());
        JSONArrayBuilder jsonActiveRemotes = JSONBuilder.Array();
        for (String remote : this.activeRemotes) {
            jsonActiveRemotes.put(remote);
        }
        json.put(Snapshot.ACTIVE_REMOTES, jsonActiveRemotes.toJSON());
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
