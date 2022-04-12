package com.seat.sim.common.scenario;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import com.seat.sim.common.json.JSONAble;
import com.seat.sim.common.json.JSONArray;
import com.seat.sim.common.json.JSONArrayBuilder;
import com.seat.sim.common.json.JSONBuilder;
import com.seat.sim.common.json.JSONException;
import com.seat.sim.common.json.JSONObject;
import com.seat.sim.common.json.JSONObjectBuilder;
import com.seat.sim.common.json.JSONOption;
import com.seat.sim.common.math.Grid;
import com.seat.sim.common.remote.RemoteConfig;
import com.seat.sim.common.remote.RemoteRegistry;

/** A serializable class to store scenario configurations. */
public class ScenarioConfig extends JSONAble {
    public static final String GRID = "grid";
    public static final String MISSION_LENGTH = "mission_length";
    public static final String REMOTE_CONFIG = "remote_config";
    public static final String SCENARIO_ID = "scenario_id";
    public static final String SEED = "seed";
    public static final String STEP_SIZE = "step_size";

    protected static final String DEFAULT_ID = "default";
    protected static final long DEFAULT_SEED = new Random().nextLong();

    private Grid grid;
    private int missionLength;
    private ArrayList<RemoteConfig> remoteConfig;
    private String scenarioID;
    private long seed;
    private double stepSize;

    public ScenarioConfig(Grid grid, int missionLength, double stepSize) {
        this(ScenarioConfig.DEFAULT_ID, ScenarioConfig.DEFAULT_SEED, grid,
            missionLength, stepSize, null);
    }

    public ScenarioConfig(String scenarioID, Grid grid, int missionLength, double stepSize) {
        this(scenarioID, ScenarioConfig.DEFAULT_SEED, grid, missionLength,
            stepSize, null);
    }

    public ScenarioConfig(long seed, Grid grid, int missionLength, double stepSize) {
        this(ScenarioConfig.DEFAULT_ID, seed, grid, missionLength, stepSize, null);
    }

    public ScenarioConfig(String scenarioID, long seed, Grid grid, int missionLength, double stepSize) {
        this(scenarioID, seed, grid, missionLength, stepSize, null);
    }

    public ScenarioConfig(Grid grid, int missionLength, double stepSize, Collection<RemoteConfig> remoteConfig) {
        this(ScenarioConfig.DEFAULT_ID, ScenarioConfig.DEFAULT_SEED, grid, missionLength, stepSize, remoteConfig);
    }

    public ScenarioConfig(String scenarioID, Grid grid, int missionLength, double stepSize,
            Collection<RemoteConfig> remoteConfig) {
        this(scenarioID, ScenarioConfig.DEFAULT_SEED, grid, missionLength, stepSize, remoteConfig);
    }

    public ScenarioConfig(long seed, Grid grid, int missionLength, double stepSize,
            Collection<RemoteConfig> remoteConfig) {
        this(ScenarioConfig.DEFAULT_ID, seed, grid, missionLength, stepSize, remoteConfig);
    }

    public ScenarioConfig(String scenarioID, long seed, Grid grid, int missionLength, double stepSize,
            Collection<RemoteConfig> remoteConfig) {
        this.scenarioID = scenarioID;
        this.seed = seed;
        this.grid = grid;
        this.missionLength = missionLength;
        this.stepSize = stepSize;
        this.remoteConfig = (remoteConfig != null ) ? new ArrayList<>(remoteConfig) : new ArrayList<>();
    }

    public ScenarioConfig(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        this.scenarioID = json.getString(ScenarioConfig.SCENARIO_ID);
        this.seed = json.getLong(ScenarioConfig.SEED);
        this.grid = new Grid(json.getJSONOption(ScenarioConfig.GRID));
        this.missionLength = json.getInt(ScenarioConfig.MISSION_LENGTH);
        this.stepSize = json.getDouble(ScenarioConfig.STEP_SIZE);
        this.remoteConfig = new ArrayList<>();
        if (json.hasKey(ScenarioConfig.REMOTE_CONFIG)) {
            JSONArray jsonRemotes = json.getJSONArray(ScenarioConfig.REMOTE_CONFIG);
            for (int i = 0; i < jsonRemotes.length(); i++) {
                this.remoteConfig.add(RemoteRegistry.decodeTo(jsonRemotes.getJSONOption(i), RemoteConfig.class));
            }
        }
    }

    protected JSONObjectBuilder getJSONBuilder() throws JSONException {
        JSONObjectBuilder json = JSONBuilder.Object();
        json.put(ScenarioConfig.SCENARIO_ID, this.scenarioID);
        json.put(ScenarioConfig.SEED, this.seed);
        json.put(ScenarioConfig.GRID, this.grid.toJSON());
        json.put(ScenarioConfig.MISSION_LENGTH, this.missionLength);
        json.put(ScenarioConfig.STEP_SIZE, this.stepSize);
        if (this.hasRemotes()) {
            JSONArrayBuilder jsonRemotes = JSONBuilder.Array();
            for (RemoteConfig config : this.remoteConfig) {
                jsonRemotes.put(config.toJSON());
            }
            json.put(ScenarioConfig.REMOTE_CONFIG, jsonRemotes.toJSON());
        }
        return json;
    }

    public Grid getGrid() {
        return this.grid;
    }

    public int getGridHeight() {
        return this.grid.getHeight();
    }

    public int getGridWidth() {
        return this.grid.getWidth();
    }

    public int getMissionLength() {
        return this.missionLength;
    }

    public Collection<RemoteConfig> getRemotes() {
        return this.remoteConfig;
    }

    public Collection<String> getRemoteIDs() {
        ArrayList<String> remoteIDs = new ArrayList<>();
        for (RemoteConfig config : this.remoteConfig) {
            remoteIDs.addAll(config.getRemoteIDs());
        }
        return remoteIDs;
    }

    public String getScenarioID() {
        return this.scenarioID;
    }

    public String getScenarioType() {
        return this.getClass().getName();
    }

    public long getSeed() {
        return this.seed;
    }

    public double getStepSize() {
        return this.stepSize;
    }

    public boolean hasRemotes() {
        return !this.remoteConfig.isEmpty();
    }

    public JSONOption toJSON() {
        return this.getJSONBuilder().toJSON();
    }

    public boolean equals(ScenarioConfig config) {
        if (config == null) return false;
        return this.getScenarioType().equals(config.getScenarioType()) && this.scenarioID.equals(config.scenarioID) &&
            this.seed == config.seed && this.grid.equals(config.grid) && this.missionLength == config.missionLength &&
            this.stepSize == config.stepSize && this.remoteConfig.equals(config.remoteConfig);
    }

}
