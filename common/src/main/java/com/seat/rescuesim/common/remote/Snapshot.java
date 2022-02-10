package com.seat.rescuesim.common.remote;

import java.util.ArrayList;
import java.util.HashSet;

import com.seat.rescuesim.common.json.*;
import com.seat.rescuesim.common.remote.snap.ScenarioState;
import com.seat.rescuesim.common.remote.snap.SnapStatus;

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
    private ScenarioState state;
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

    public Snapshot(String hash, String scenarioID, double time, double stepSize, HashSet<String> remotes,
            HashSet<String> activeRemotes, ScenarioState state) {
        this(hash, scenarioID, SnapStatus.START, time, stepSize, remotes, activeRemotes, state);
    }

    public Snapshot(String hash, String scenarioID, SnapStatus status, double time, double stepSize,
            HashSet<String> remotes, HashSet<String> activeRemotes, ScenarioState state) {
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
        this.state = new ScenarioState(json.getJSONArray(Snapshot.STATE));
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

    public String getScenarioID() {
        return this.scenarioID;
    }

    public ScenarioState getState() {
        return this.state;
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
        json.put(Snapshot.STATE, this.state.toJSON());
        return json.toJSON();
    }

    public boolean equals(Snapshot snap) {
        return this.hash.equals(snap.hash);
    }

}
