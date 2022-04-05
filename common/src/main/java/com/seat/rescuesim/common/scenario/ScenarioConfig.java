package com.seat.rescuesim.common.scenario;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import com.seat.rescuesim.common.json.JSONAble;
import com.seat.rescuesim.common.json.JSONArray;
import com.seat.rescuesim.common.json.JSONArrayBuilder;
import com.seat.rescuesim.common.json.JSONBuilder;
import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.JSONObjectBuilder;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.map.Map;
import com.seat.rescuesim.common.remote.RemoteConfig;
import com.seat.rescuesim.common.remote.RemoteFactory;

/** A serializable class to store scenario configurations. */
public class ScenarioConfig extends JSONAble {
    public static final String MAP = "map";
    public static final String MISSION_LENGTH = "mission_length";
    public static final String REMOTE_CONFIG = "remote_config";
    public static final String SCENARIO_ID = "scenario_id";
    public static final String SEED = "seed";
    public static final String STEP_SIZE = "step_size";

    protected static final String DEFAULT_ID = "default";
    protected static final long DEFAULT_SEED = new Random().nextLong();

    private Map map;
    private int missionLength;
    private ArrayList<RemoteConfig> remoteConfig;
    private String scenarioID;
    private long seed;
    private double stepSize;

    public ScenarioConfig(Map map, int missionLength, double stepSize) {
        this(ScenarioConfig.DEFAULT_ID, ScenarioConfig.DEFAULT_SEED, map, missionLength, stepSize, null);
    }

    public ScenarioConfig(String scenarioID, Map map, int missionLength, double stepSize) {
        this(scenarioID, ScenarioConfig.DEFAULT_SEED, map, missionLength, stepSize, null);
    }

    public ScenarioConfig(long seed, Map map, int missionLength, double stepSize) {
        this(ScenarioConfig.DEFAULT_ID, seed, map, missionLength, stepSize, null);
    }

    public ScenarioConfig(String scenarioID, long seed, Map map, int missionLength, double stepSize) {
        this(scenarioID, seed, map, missionLength, stepSize, null);
    }

    public ScenarioConfig(Map map, int missionLength, double stepSize, Collection<RemoteConfig> remoteConfig) {
        this(ScenarioConfig.DEFAULT_ID, ScenarioConfig.DEFAULT_SEED, map, missionLength, stepSize, remoteConfig);
    }

    public ScenarioConfig(String scenarioID, Map map, int missionLength, double stepSize,
            Collection<RemoteConfig> remoteConfig) {
        this(scenarioID, ScenarioConfig.DEFAULT_SEED, map, missionLength, stepSize, remoteConfig);
    }

    public ScenarioConfig(long seed, Map map, int missionLength, double stepSize,
            Collection<RemoteConfig> remoteConfig) {
        this(ScenarioConfig.DEFAULT_ID, seed, map, missionLength, stepSize, remoteConfig);
    }

    public ScenarioConfig(String scenarioID, long seed, Map map, int missionLength, double stepSize,
            Collection<RemoteConfig> remoteConfig) {
        this.scenarioID = scenarioID;
        this.seed = seed;
        this.map = map;
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
        this.map = new Map(json.getJSONOption(ScenarioConfig.MAP));
        this.missionLength = json.getInt(ScenarioConfig.MISSION_LENGTH);
        this.stepSize = json.getDouble(ScenarioConfig.STEP_SIZE);
        this.remoteConfig = new ArrayList<>();
        if (json.hasKey(ScenarioConfig.REMOTE_CONFIG)) {
            JSONArray jsonDrones = json.getJSONArray(ScenarioConfig.REMOTE_CONFIG);
            for (int i = 0; i < jsonDrones.length(); i++) {
                this.remoteConfig.add(RemoteFactory.decodeRemoteConfig(jsonDrones.getJSONOption(i)));
            }
        }
    }

    protected JSONObjectBuilder getJSONBuilder() {
        JSONObjectBuilder json = JSONBuilder.Object();
        json.put(ScenarioConfig.SCENARIO_ID, this.scenarioID);
        json.put(ScenarioConfig.SEED, this.seed);
        json.put(ScenarioConfig.MAP, this.map.toJSON());
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

    public Map getMap() {
        return this.map;
    }

    public int getMapHeight() {
        return this.map.getHeight();
    }

    public int getMapWidth() {
        return this.map.getWidth();
    }

    public int getMissionLength() {
        return this.missionLength;
    }

    public ArrayList<RemoteConfig> getRemotes() {
        return this.remoteConfig;
    }

    public String getScenarioID() {
        return this.scenarioID;
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
        return this.map.equals(config.map) && this.missionLength == config.missionLength &&
            this.remoteConfig.equals(config.remoteConfig);
    }

}
