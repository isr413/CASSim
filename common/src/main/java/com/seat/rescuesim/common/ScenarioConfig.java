package com.seat.rescuesim.common;

import java.util.ArrayList;
import java.util.Random;

import com.seat.rescuesim.common.base.BaseConfig;
import com.seat.rescuesim.common.drone.DroneConfig;
import com.seat.rescuesim.common.json.*;
import com.seat.rescuesim.common.map.Map;
import com.seat.rescuesim.common.victim.VictimConfig;

/** A serializable class to store scenario configurations. */
public class ScenarioConfig extends JSONAble {
    private static final String BASE_CONFIG = "base_config";
    private static final String DEFAULT_ID = "default";
    private static final String DISASTER_SCALE = "disaster_scale";
    private static final String DRONE_CONFIG = "drone_config";
    private static final String MAP = "map";
    private static final String MISSION_LENGTH = "mission_length";
    private static final String NUM_BASES = "num_bases";
    private static final String NUM_DRONES = "num_drones";
    private static final String NUM_VICTIMS = "num_victims";
    private static final String SCENARIO_ID = "scenario_id";
    private static final String SEED = "seed";
    private static final String STEP_SIZE = "step_size";
    private static final String VICTIM_CONFIG = "victim_config";

    private ArrayList<BaseConfig> baseConfig;
    private double disasterScale;
    private ArrayList<DroneConfig> droneConfig;
    private Map map;
    private int missionLength;
    private int numBases;
    private int numDrones;
    private int numVictims;
    private String scenarioID;
    private long seed;
    private double stepSize;
    private ArrayList<VictimConfig> victimConfig;

    public ScenarioConfig(JSONObject json) {
        super(json);
    }

    public ScenarioConfig(JSONOption option) {
        super(option);
    }

    public ScenarioConfig(String encoding) {
        super(encoding);
    }

    //-- Constructors for scenarios with no victims and no drones --//
    public ScenarioConfig(Map map, int missionLength, double stepSize) {
        this(ScenarioConfig.DEFAULT_ID, map, 0, missionLength, stepSize, new ArrayList<VictimConfig>(),
            new ArrayList<BaseConfig>(), new ArrayList<DroneConfig>());
    }

    public ScenarioConfig(String scenarioID, Map map, int missionLength, double stepSize) {
        this(scenarioID, map, 0, missionLength, stepSize, new ArrayList<VictimConfig>(),
            new ArrayList<BaseConfig>(), new ArrayList<DroneConfig>());
    }

    public ScenarioConfig(long seed, Map map, int missionLength, double stepSize) {
        this(ScenarioConfig.DEFAULT_ID, seed, map, 0, missionLength, stepSize, new ArrayList<VictimConfig>(),
            new ArrayList<BaseConfig>(), new ArrayList<DroneConfig>());
    }

    public ScenarioConfig(String scenarioID, long seed, Map map, int missionLength, double stepSize) {
        this(scenarioID, seed, map, 0, missionLength, stepSize, new ArrayList<VictimConfig>(),
            new ArrayList<BaseConfig>(), new ArrayList<DroneConfig>());
    }

    //-- Constructors for scenarios with victims and no drones --//
    public ScenarioConfig(Map map, int missionLength, double stepSize, ArrayList<VictimConfig> victimConfig) {
        this(ScenarioConfig.DEFAULT_ID, map, 0, missionLength, stepSize, victimConfig,
            new ArrayList<BaseConfig>(), new ArrayList<DroneConfig>());
    }

    public ScenarioConfig(String scenarioID, Map map, int missionLength, double stepSize, ArrayList<VictimConfig> victimConfig) {
        this(scenarioID, map, 0, missionLength, stepSize, victimConfig,
            new ArrayList<BaseConfig>(), new ArrayList<DroneConfig>());
    }

    public ScenarioConfig(long seed, Map map, int missionLength, double stepSize, ArrayList<VictimConfig> victimConfig) {
        this(ScenarioConfig.DEFAULT_ID, seed, map, 0, missionLength, stepSize, victimConfig,
            new ArrayList<BaseConfig>(), new ArrayList<DroneConfig>());
    }

    public ScenarioConfig(String scenarioID, long seed, Map map, int missionLength, double stepSize,
            ArrayList<VictimConfig> victimConfig) {
        this(scenarioID, seed, map, 0, missionLength, stepSize, victimConfig,
            new ArrayList<BaseConfig>(), new ArrayList<DroneConfig>());
    }

    //-- Constructors for scenarios with victims and drones but no base --//
    public ScenarioConfig(Map map, int missionLength, double stepSize, ArrayList<VictimConfig> victimConfig,
            ArrayList<DroneConfig> droneConfig) {
        this(ScenarioConfig.DEFAULT_ID, map, 0, missionLength, stepSize, victimConfig, new ArrayList<BaseConfig>(), droneConfig);
    }

    public ScenarioConfig(String scenarioID, Map map, int missionLength, double stepSize,
            ArrayList<VictimConfig> victimConfig, ArrayList<DroneConfig> droneConfig) {
        this(scenarioID, map, 0, missionLength, stepSize, victimConfig, new ArrayList<BaseConfig>(), droneConfig);
    }

    public ScenarioConfig(long seed, Map map, int missionLength, double stepSize, ArrayList<VictimConfig> victimConfig,
            ArrayList<DroneConfig> droneConfig) {
        this(ScenarioConfig.DEFAULT_ID, seed, map, 0, missionLength, stepSize, victimConfig, new ArrayList<BaseConfig>(),
            droneConfig);
    }

    public ScenarioConfig(String scenarioID, long seed, Map map, int missionLength, double stepSize,
            ArrayList<VictimConfig> victimConfig, ArrayList<DroneConfig> droneConfig) {
        this(scenarioID, seed, map, 0, missionLength, stepSize, victimConfig, new ArrayList<BaseConfig>(), droneConfig);
    }

    //-- Constructors for scenarios with victims, drones, and a base --//
    public ScenarioConfig(Map map, double disasterScale, int missionLength, double stepSize,
            ArrayList<VictimConfig> victimConfig, ArrayList<BaseConfig> baseConfig, ArrayList<DroneConfig> droneConfig) {
        this(ScenarioConfig.DEFAULT_ID, map, disasterScale, missionLength, stepSize, victimConfig, baseConfig, droneConfig);
    }

    public ScenarioConfig(String scenarioID, Map map, double disasterScale, int missionLength, double stepSize,
            ArrayList<VictimConfig> victimConfig, ArrayList<BaseConfig> baseConfig, ArrayList<DroneConfig> droneConfig) {
        this(scenarioID, new Random().nextLong(), map, disasterScale, missionLength, stepSize, victimConfig,
            baseConfig, droneConfig);
    }

    public ScenarioConfig(long seed, Map map, double disasterScale, int missionLength, double stepSize,
            ArrayList<VictimConfig> victimConfig, ArrayList<BaseConfig> baseConfig, ArrayList<DroneConfig> droneConfig) {
        this(ScenarioConfig.DEFAULT_ID, seed, map, disasterScale, missionLength, stepSize, victimConfig, baseConfig, droneConfig);
    }

    public ScenarioConfig(String scenarioID, long seed, Map map, double disasterScale, int missionLength,
            double stepSize, ArrayList<VictimConfig> victimConfig, ArrayList<BaseConfig> baseConfig,
            ArrayList<DroneConfig> droneConfig) {
        this.scenarioID = scenarioID;
        this.seed = seed;
        this.map = map;
        this.disasterScale = disasterScale;
        this.missionLength = missionLength;
        this.stepSize = stepSize;
        this.victimConfig = victimConfig;
        this.baseConfig = baseConfig;
        this.droneConfig = droneConfig;
        this.countBases();
        this.countDrones();
        this.countVictims();
    }

    private void countBases() {
        this.numBases = 0;
        for (BaseConfig conf : baseConfig) {
            this.numBases += conf.getCount();
        }
    }

    private void countDrones() {
        this.numDrones = 0;
        for (DroneConfig conf : droneConfig) {
            this.numDrones += conf.getCount();
        }
    }

    private void countVictims() {
        this.numVictims = 0;
        for (VictimConfig conf : victimConfig) {
            this.numVictims += conf.getCount();
        }
    }

    @Override
    protected void decode(JSONObject json) {
        this.scenarioID = json.getString(ScenarioConfig.SCENARIO_ID);
        this.seed = json.getLong(ScenarioConfig.SEED);
        this.map = new Map(json.getJSONObject(ScenarioConfig.MAP));
        this.disasterScale = json.getDouble(ScenarioConfig.DISASTER_SCALE);
        this.missionLength = json.getInt(ScenarioConfig.MISSION_LENGTH);
        this.stepSize = json.getDouble(ScenarioConfig.STEP_SIZE);
        this.victimConfig = new ArrayList<>();
        if (json.hasKey(ScenarioConfig.VICTIM_CONFIG)) {
            JSONArray jsonVictims = json.getJSONArray(ScenarioConfig.VICTIM_CONFIG);
            for (int i = 0; i < jsonVictims.length(); i++) {
                this.victimConfig.add(new VictimConfig(jsonVictims.getJSONObject(i)));
            }
        }
        this.baseConfig = new ArrayList<>();
        if (json.hasKey(ScenarioConfig.BASE_CONFIG)) {
            JSONArray jsonBases = json.getJSONArray(ScenarioConfig.BASE_CONFIG);
            for (int i = 0; i < jsonBases.length(); i++) {
                this.baseConfig.add(new BaseConfig(jsonBases.getJSONObject(i)));
            }
        }
        this.droneConfig = new ArrayList<>();
        if (json.hasKey(ScenarioConfig.DRONE_CONFIG)) {
            JSONArray jsonDrones = json.getJSONArray(ScenarioConfig.DRONE_CONFIG);
            for (int i = 0; i < jsonDrones.length(); i++) {
                this.droneConfig.add(new DroneConfig(jsonDrones.getJSONObject(i)));
            }
        }
        this.countBases();
        this.countDrones();
        this.countVictims();
    }

    public ArrayList<BaseConfig> getBaseConfig() {
        return this.baseConfig;
    }

    public double getDisasterScale() {
        return this.disasterScale;
    }

    public ArrayList<DroneConfig> getDroneConfig() {
        return this.droneConfig;
    }

    public Map getMap() {
        return this.map;
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

    public String getScenarioID() {
        return this.scenarioID;
    }

    public long getSeed() {
        return this.seed;
    }

    public double getStepSize() {
        return this.stepSize;
    }

    public ArrayList<VictimConfig> getVictimConfig() {
        return this.victimConfig;
    }

    public boolean hasBaseConfig() {
        return !this.baseConfig.isEmpty();
    }

    public boolean hasDroneConfig() {
        return !this.droneConfig.isEmpty();
    }

    public boolean hasVictimConfig() {
        return !this.victimConfig.isEmpty();
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
        if (this.hasVictimConfig()) {
            JSONArrayBuilder jsonVictims = JSONBuilder.Array();
            for (VictimConfig conf : this.victimConfig) {
                jsonVictims.put(conf.toJSON());
            }
            json.put(ScenarioConfig.VICTIM_CONFIG, jsonVictims.toJSON());
        }
        json.put(ScenarioConfig.NUM_BASES, this.numBases);
        if (this.hasBaseConfig()) {
            JSONArrayBuilder jsonBases = JSONBuilder.Array();
            for (BaseConfig conf : this.baseConfig) {
                jsonBases.put(conf.toJSON());
            }
            json.put(ScenarioConfig.BASE_CONFIG, jsonBases.toJSON());
        }
        json.put(ScenarioConfig.NUM_DRONES, this.numDrones);
        if (this.hasDroneConfig()) {
            JSONArrayBuilder jsonDrones = JSONBuilder.Array();
            for (DroneConfig conf : this.droneConfig) {
                jsonDrones.put(conf.toJSON());
            }
            json.put(ScenarioConfig.DRONE_CONFIG, jsonDrones.toJSON());
        }
        return json.toJSON();
    }

    public boolean equals(ScenarioConfig config) {
        return this.map.equals(config.map) && this.disasterScale == config.disasterScale &&
            this.missionLength == config.missionLength && this.victimConfig.equals(config.victimConfig) &
            this.baseConfig.equals(config.baseConfig) && this.droneConfig.equals(config.droneConfig);
    }

}
