package com.seat.rescuesim.common;

import java.util.Random;

import com.seat.rescuesim.common.json.*;

public class ScenarioConfig extends JSONAble {
    private static final String BASE = "base";
    private static final String DEFAULT_ID = "default";
    private static final String DISASTER_SCALE = "disaster_scale";
    private static final String DRONE_SPEC = "drone_spec";
    private static final String MAP = "map";
    private static final String MISSION_LENGTH = "mission_length";
    private static final String NUM_DRONES = "num_drones";
    private static final String NUM_VICTIMS = "num_victims";
    private static final String SCENARIO_ID = "scenario_id";
    private static final String SEED = "seed";
    private static final String STEP_SIZE = "step_size";
    private static final String VICTIM_SPEC = "victim_spec";

    private Base base;
    private double disasterScale;
    private DroneSpecification droneSpec;
    private Map map;
    private int missionLength;
    private int numDrones;
    private int numVictims;
    private String scenarioID;
    private long seed;
    private double stepSize;
    private VictimSpecification victimSpec;

    public ScenarioConfig(JSONObject json) {
        super(json);
    }

    public ScenarioConfig(JSONOption option) {
        super(option);
    }

    public ScenarioConfig(String encoding) {
        super(encoding);
    }

    public ScenarioConfig(Map map, double disasterScale, int missionLength,
            double stepSize, int numVictims, VictimSpecification victimSpec,
            Base base, int numDrones, DroneSpecification droneSpec) {
        this(ScenarioConfig.DEFAULT_ID, map, disasterScale, missionLength, stepSize, numVictims, victimSpec,
            base, numDrones, droneSpec);
    }

    public ScenarioConfig(long seed, Map map, double disasterScale, int missionLength,
            double stepSize, int numVictims, VictimSpecification victimSpec,
            Base base, int numDrones, DroneSpecification droneSpec) {
        this(ScenarioConfig.DEFAULT_ID, seed, map, disasterScale, missionLength, stepSize, numVictims, victimSpec,
            base, numDrones, droneSpec);
    }

    public ScenarioConfig(String scenarioID, Map map, double disasterScale, int missionLength,
            double stepSize, int numVictims, VictimSpecification victimSpec,
            Base base, int numDrones, DroneSpecification droneSpec) {
        this(scenarioID, new Random().nextLong(), map, disasterScale, missionLength, stepSize, numVictims, victimSpec,
            base, numDrones, droneSpec);
    }

    public ScenarioConfig(String scenarioID, long seed, Map map, double disasterScale, int missionLength,
            double stepSize, int numVictims, VictimSpecification victimSpec,
            Base base, int numDrones, DroneSpecification droneSpec) {
        this.scenarioID = scenarioID;
        this.seed = seed;
        this.map = map;
        this.disasterScale = disasterScale;
        this.missionLength = missionLength;
        this.stepSize = stepSize;
        this.numVictims = numVictims;
        this.victimSpec = victimSpec;
        this.base = base;
        this.numDrones = numDrones;
        this.droneSpec = droneSpec;
    }

    @Override
    protected void decode(JSONObject json) {
        this.scenarioID = json.getString(ScenarioConfig.SCENARIO_ID);
        this.seed = json.getLong(ScenarioConfig.SEED);
        this.map = new Map(json.getJSONObject(ScenarioConfig.MAP));
        this.disasterScale = json.getDouble(ScenarioConfig.DISASTER_SCALE);
        this.missionLength = json.getInt(ScenarioConfig.MISSION_LENGTH);
        this.stepSize = json.getDouble(ScenarioConfig.STEP_SIZE);
        this.numVictims = json.getInt(ScenarioConfig.NUM_VICTIMS);
        this.victimSpec = new VictimSpecification(json.getJSONObject(ScenarioConfig.VICTIM_SPEC));
        this.base = new Base(json.getJSONObject(ScenarioConfig.BASE));
        this.numDrones = json.getInt(ScenarioConfig.NUM_DRONES);
        this.droneSpec = new DroneSpecification(json.getJSONObject(ScenarioConfig.DRONE_SPEC));
    }

    public Base getBase() {
        return this.base;
    }

    public double getDisasterScale() {
        return this.disasterScale;
    }

    public DroneSpecification getDroneSpecification() {
        return this.droneSpec;
    }

    public Map getMap() {
        return this.map;
    }

    public int getMissionLength() {
        return this.missionLength;
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

    public VictimSpecification getVictimSpecification() {
        return this.victimSpec;
    }

    public String encode() {
        return this.toJSON().toString();
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
        json.put(ScenarioConfig.VICTIM_SPEC, this.victimSpec.toJSON());
        json.put(ScenarioConfig.BASE, this.base.toJSON());
        json.put(ScenarioConfig.NUM_DRONES, this.numDrones);
        json.put(ScenarioConfig.DRONE_SPEC, this.droneSpec.toJSON());
        return json.toJSON();
    }

    public boolean equals(ScenarioConfig config) {
        return this.map.equals(config.map) && this.disasterScale == config.disasterScale &&
            this.missionLength == config.missionLength && this.numVictims == config.numVictims &&
            this.victimSpec.equals(config.victimSpec) && this.base.equals(config.base) &&
            this.numDrones == config.numDrones && this.droneSpec.equals(config.droneSpec);
    }

}
