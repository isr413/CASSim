package com.seat.rescuesim.common;

import java.util.ArrayList;
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
import com.seat.rescuesim.common.remote.kinetic.KineticRemoteType;
import com.seat.rescuesim.common.remote.kinetic.drone.DroneConfig;
import com.seat.rescuesim.common.remote.kinetic.victim.VictimConfig;
import com.seat.rescuesim.common.remote.stat.StaticRemoteType;
import com.seat.rescuesim.common.remote.stat.base.BaseConfig;
import com.seat.rescuesim.common.util.RemoteFactory;

/** A serializable class to store scenario configurations. */
public class ScenarioConfig extends JSONAble {
    private static final String DEFAULT_ID = "default";
    private static final String DISASTER_SCALE = "disaster_scale";
    private static final String MAP = "map";
    private static final String MISSION_LENGTH = "mission_length";
    private static final String NUM_BASES = "num_bases";
    private static final String NUM_DRONES = "num_drones";
    private static final String NUM_VICTIMS = "num_victims";
    private static final String REMOTE_CONFIG = "remote_config";
    private static final String SCENARIO_ID = "scenario_id";
    private static final String SEED = "seed";
    private static final String STEP_SIZE = "step_size";

    private double disasterScale;
    private Map map;
    private int missionLength;
    private int numBases;
    private int numDrones;
    private int numVictims;
    private ArrayList<RemoteConfig> remoteConfig;
    private String scenarioID;
    private long seed;
    private double stepSize;

    //-- Constructors for scenarios with no victims and no drones --//
    public ScenarioConfig(Map map, int missionLength, double stepSize) {
        this(ScenarioConfig.DEFAULT_ID, map, 0, missionLength, stepSize, new ArrayList<RemoteConfig>());
    }

    public ScenarioConfig(String scenarioID, Map map, int missionLength, double stepSize) {
        this(scenarioID, map, 0, missionLength, stepSize, new ArrayList<RemoteConfig>());
    }

    public ScenarioConfig(long seed, Map map, int missionLength, double stepSize) {
        this(ScenarioConfig.DEFAULT_ID, seed, map, 0, missionLength, stepSize, new ArrayList<RemoteConfig>());
    }

    public ScenarioConfig(String scenarioID, long seed, Map map, int missionLength, double stepSize) {
        this(scenarioID, seed, map, 0, missionLength, stepSize, new ArrayList<RemoteConfig>());
    }

    //-- Constructors for scenarios with victims, drones, and/or a base --//
    public ScenarioConfig(Map map, double disasterScale, int missionLength, double stepSize,
            ArrayList<RemoteConfig> remoteConfig) {
        this(ScenarioConfig.DEFAULT_ID, map, disasterScale, missionLength, stepSize, remoteConfig);
    }

    public ScenarioConfig(String scenarioID, Map map, double disasterScale, int missionLength, double stepSize,
            ArrayList<RemoteConfig> remoteConfig) {
        this(scenarioID, new Random().nextLong(), map, disasterScale, missionLength, stepSize, remoteConfig);
    }

    public ScenarioConfig(long seed, Map map, double disasterScale, int missionLength, double stepSize,
            ArrayList<RemoteConfig> remoteConfig) {
        this(ScenarioConfig.DEFAULT_ID, seed, map, disasterScale, missionLength, stepSize, remoteConfig);
    }

    public ScenarioConfig(String scenarioID, long seed, Map map, double disasterScale, int missionLength,
            double stepSize, ArrayList<RemoteConfig> remoteConfig) {
        this.scenarioID = scenarioID;
        this.seed = seed;
        this.map = map;
        this.disasterScale = disasterScale;
        this.missionLength = missionLength;
        this.stepSize = stepSize;
        this.remoteConfig = remoteConfig;
        this.countBases();
        this.countDrones();
        this.countVictims();
    }

    private void countBases() {
        this.numBases = 0;
        for (RemoteConfig config : this.remoteConfig) {
            if (config.getSpecType().equals(StaticRemoteType.BASE)) {
                this.numBases += config.getCount();
            }
        }
    }

    private void countDrones() {
        this.numDrones = 0;
        for (RemoteConfig config : this.remoteConfig) {
            if (config.getSpecType().equals(KineticRemoteType.DRONE)) {
                this.numDrones += config.getCount();
            }
        }
    }

    private void countVictims() {
        this.numVictims = 0;
        for (RemoteConfig config : this.remoteConfig) {
            if (config.getSpecType().equals(KineticRemoteType.VICTIM)) {
                this.numVictims += config.getCount();
            }
        }
    }

    public ScenarioConfig(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        this.scenarioID = json.getString(ScenarioConfig.SCENARIO_ID);
        this.seed = json.getLong(ScenarioConfig.SEED);
        this.map = new Map(json.getJSONOption(ScenarioConfig.MAP));
        this.disasterScale = json.getDouble(ScenarioConfig.DISASTER_SCALE);
        this.missionLength = json.getInt(ScenarioConfig.MISSION_LENGTH);
        this.stepSize = json.getDouble(ScenarioConfig.STEP_SIZE);
        this.remoteConfig = new ArrayList<>();
        if (json.hasKey(ScenarioConfig.REMOTE_CONFIG)) {
            JSONArray jsonDrones = json.getJSONArray(ScenarioConfig.REMOTE_CONFIG);
            for (int i = 0; i < jsonDrones.length(); i++) {
                this.remoteConfig.add(RemoteFactory.decodeRemoteConfig(jsonDrones.getJSONOption(i)));
            }
        }
        this.countBases();
        this.countDrones();
        this.countVictims();
    }

    public ArrayList<BaseConfig> getBases() {
        ArrayList<BaseConfig> baseConfig = new ArrayList<>();
        for (RemoteConfig config : this.remoteConfig) {
            if (config.getSpecType().equals(StaticRemoteType.BASE)) {
                baseConfig.add((BaseConfig) config);
            }
        }
        return baseConfig;
    }

    public double getDisasterScale() {
        return this.disasterScale;
    }

    public ArrayList<DroneConfig> getDrones() {
        ArrayList<DroneConfig> droneConfig = new ArrayList<>();
        for (RemoteConfig config : this.remoteConfig) {
            if (config.getSpecType().equals(KineticRemoteType.DRONE)) {
                droneConfig.add((DroneConfig) config);
            }
        }
        return droneConfig;
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

    public int getNumberOfBases() {
        return this.numBases;
    }

    public int getNumberOfDrones() {
        return this.numDrones;
    }

    public int getNumberOfVictims() {
        return this.numVictims;
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

    public ArrayList<VictimConfig> getVictims() {
        ArrayList<VictimConfig> victimConfig = new ArrayList<>();
        for (RemoteConfig config : this.remoteConfig) {
            if (config.getSpecType().equals(KineticRemoteType.VICTIM)) {
                victimConfig.add((VictimConfig) config);
            }
        }
        return victimConfig;
    }

    public boolean hasBases() {
        return this.numBases > 0;
    }

    public boolean hasDrones() {
        return this.numDrones > 0;
    }

    public boolean hasRemotes() {
        return !this.remoteConfig.isEmpty();
    }

    public boolean hasVictims() {
        return this.numVictims > 0;
    }

    public JSONOption toJSON() {
        JSONObjectBuilder json = JSONBuilder.Object();
        json.put(ScenarioConfig.SCENARIO_ID, this.scenarioID);
        json.put(ScenarioConfig.SEED, this.seed);
        json.put(ScenarioConfig.MAP, this.map.toJSON());
        json.put(ScenarioConfig.DISASTER_SCALE, this.disasterScale);
        json.put(ScenarioConfig.MISSION_LENGTH, this.missionLength);
        json.put(ScenarioConfig.STEP_SIZE, this.stepSize);
        json.put(ScenarioConfig.NUM_VICTIMS, this.numVictims);
        json.put(ScenarioConfig.NUM_BASES, this.numBases);
        json.put(ScenarioConfig.NUM_DRONES, this.numDrones);
        if (this.hasRemotes()) {
            JSONArrayBuilder jsonRemotes = JSONBuilder.Array();
            for (RemoteConfig config : this.remoteConfig) {
                jsonRemotes.put(config.toJSON());
            }
            json.put(ScenarioConfig.REMOTE_CONFIG, jsonRemotes.toJSON());
        }
        return json.toJSON();
    }

    public boolean equals(ScenarioConfig config) {
        return this.map.equals(config.map) && this.disasterScale == config.disasterScale &&
            this.missionLength == config.missionLength && this.remoteConfig.equals(config.remoteConfig);
    }

}
