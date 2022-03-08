package com.seat.rescuesim.common;

import java.util.Random;

import com.seat.rescuesim.common.base.BaseConf;
import com.seat.rescuesim.common.drone.DroneConf;
import com.seat.rescuesim.common.json.*;
import com.seat.rescuesim.common.map.Map;
import com.seat.rescuesim.common.victim.VictimConf;

/** A serializable class to store scenario configurations. */
public class Scenario extends JSONAble {
    private static final String BASE_CONF = "base_conf";
    private static final String DEFAULT_ID = "default";
    private static final String DISASTER_SCALE = "disaster_scale";
    private static final String DRONE_CONF = "drone_conf";
    private static final String MAP = "map";
    private static final String MISSION_LENGTH = "mission_length";
    private static final String NUM_DRONES = "num_drones";
    private static final String NUM_VICTIMS = "num_victims";
    private static final String SCENARIO_ID = "scenario_id";
    private static final String SEED = "seed";
    private static final String STEP_SIZE = "step_size";
    private static final String VICTIM_CONF = "victim_conf";

    private BaseConf baseConf;
    private double disasterScale;
    private DroneConf droneConf;
    private Map map;
    private int missionLength;
    private int numDrones;
    private int numVictims;
    private String scenarioID;
    private long seed;
    private double stepSize;
    private VictimConf victimConf;

    public Scenario(JSONObject json) {
        super(json);
    }

    public Scenario(JSONOption option) {
        super(option);
    }

    public Scenario(String encoding) {
        super(encoding);
    }

    //-- Constructors for scenarios with no victims and no drones --//
    public Scenario(Map map, int missionLength, double stepSize) {
        this(Scenario.DEFAULT_ID, map, 0, missionLength, stepSize, 0, VictimConf.None(),
            BaseConf.None(), 0, DroneConf.None());
    }

    public Scenario(String scenarioID, Map map, int missionLength, double stepSize) {
        this(scenarioID, map, 0, missionLength, stepSize, 0, VictimConf.None(),
            BaseConf.None(), 0, DroneConf.None());
    }

    public Scenario(long seed, Map map, int missionLength, double stepSize) {
        this(Scenario.DEFAULT_ID, seed, map, 0, missionLength, stepSize, 0, VictimConf.None(),
            BaseConf.None(), 0, DroneConf.None());
    }

    public Scenario(String scenarioID, long seed, Map map, int missionLength, double stepSize) {
        this(scenarioID, seed, map, 0, missionLength, stepSize, 0, VictimConf.None(),
            BaseConf.None(), 0, DroneConf.None());
    }

    //-- Constructors for scenarios with victims and no drones --//
    public Scenario(Map map, int missionLength, double stepSize, int numVictims, VictimConf victimConf) {
        this(Scenario.DEFAULT_ID, map, 0, missionLength, stepSize, numVictims, victimConf,
            BaseConf.None(), 0, DroneConf.None());
    }

    public Scenario(String scenarioID, Map map, int missionLength, double stepSize, int numVictims,
            VictimConf victimConf) {
        this(scenarioID, map, 0, missionLength, stepSize, numVictims, victimConf,
            BaseConf.None(), 0, DroneConf.None());
    }

    public Scenario(long seed, Map map, int missionLength, double stepSize, int numVictims,
            VictimConf victimConf) {
        this(Scenario.DEFAULT_ID, seed, map, 0, missionLength, stepSize, numVictims, victimConf,
            BaseConf.None(), 0, DroneConf.None());
    }

    public Scenario(String scenarioID, long seed, Map map, int missionLength, double stepSize, int numVictims,
            VictimConf victimConf) {
        this(scenarioID, seed, map, 0, missionLength, stepSize, numVictims, victimConf,
            BaseConf.None(), 0, DroneConf.None());
    }

    //-- Constructors for scenarios with victims and drones but no base --//
    public Scenario(Map map, int missionLength, double stepSize, int numVictims, VictimConf victimConf,
            int numDrones, DroneConf droneConf) {
        this(Scenario.DEFAULT_ID, map, 0, missionLength, stepSize, numVictims, victimConf,
            BaseConf.None(), numDrones, droneConf);
    }

    public Scenario(String scenarioID, Map map, int missionLength, double stepSize, int numVictims,
            VictimConf victimConf, int numDrones, DroneConf droneConf) {
        this(scenarioID, map, 0, missionLength, stepSize, numVictims, victimConf,
            BaseConf.None(), numDrones, droneConf);
    }

    public Scenario(long seed, Map map, int missionLength, double stepSize, int numVictims,
            VictimConf victimConf, int numDrones, DroneConf droneConf) {
        this(Scenario.DEFAULT_ID, seed, map, 0, missionLength, stepSize, numVictims, victimConf,
            BaseConf.None(), numDrones, droneConf);
    }

    public Scenario(String scenarioID, long seed, Map map, int missionLength, double stepSize, int numVictims,
            VictimConf victimConf, int numDrones, DroneConf droneConf) {
        this(scenarioID, seed, map, 0, missionLength, stepSize, numVictims, victimConf,
            BaseConf.None(), numDrones, droneConf);
    }

    //-- Constructors for scenarios with victims, drones, and a base --//
    public Scenario(Map map, double disasterScale, int missionLength,
            double stepSize, int numVictims, VictimConf victimConf,
            BaseConf baseConf, int numDrones, DroneConf droneConf) {
        this(Scenario.DEFAULT_ID, map, disasterScale, missionLength, stepSize, numVictims, victimConf,
            baseConf, numDrones, droneConf);
    }

    public Scenario(String scenarioID, Map map, double disasterScale, int missionLength,
            double stepSize, int numVictims, VictimConf victimConf,
            BaseConf baseConf, int numDrones, DroneConf droneConf) {
        this(scenarioID, new Random().nextLong(), map, disasterScale, missionLength, stepSize, numVictims, victimConf,
            baseConf, numDrones, droneConf);
    }

    public Scenario(long seed, Map map, double disasterScale, int missionLength,
            double stepSize, int numVictims, VictimConf victimConf,
            BaseConf baseConf, int numDrones, DroneConf droneConf) {
        this(Scenario.DEFAULT_ID, seed, map, disasterScale, missionLength, stepSize, numVictims, victimConf,
            baseConf, numDrones, droneConf);
    }

    public Scenario(String scenarioID, long seed, Map map, double disasterScale, int missionLength,
            double stepSize, int numVictims, VictimConf victimConf,
            BaseConf baseConf, int numDrones, DroneConf droneConf) {
        this.scenarioID = scenarioID;
        this.seed = seed;
        this.map = map;
        this.disasterScale = disasterScale;
        this.missionLength = missionLength;
        this.stepSize = stepSize;
        this.numVictims = numVictims;
        this.victimConf = victimConf;
        this.baseConf = baseConf;
        this.numDrones = numDrones;
        this.droneConf = droneConf;
    }

    @Override
    protected void decode(JSONObject json) {
        this.scenarioID = json.getString(Scenario.SCENARIO_ID);
        this.seed = json.getLong(Scenario.SEED);
        this.map = new Map(json.getJSONObject(Scenario.MAP));
        this.disasterScale = json.getDouble(Scenario.DISASTER_SCALE);
        this.missionLength = json.getInt(Scenario.MISSION_LENGTH);
        this.stepSize = json.getDouble(Scenario.STEP_SIZE);
        this.numVictims = json.getInt(Scenario.NUM_VICTIMS);
        this.victimConf = new VictimConf(json.getJSONObject(Scenario.VICTIM_CONF));
        this.baseConf = new BaseConf(json.getJSONObject(Scenario.BASE_CONF));
        this.numDrones = json.getInt(Scenario.NUM_DRONES);
        this.droneConf = new DroneConf(json.getJSONObject(Scenario.DRONE_CONF));
    }

    public BaseConf getBaseConfiguration() {
        return this.baseConf;
    }

    public double getDisasterScale() {
        return this.disasterScale;
    }

    public DroneConf getDroneConfiguration() {
        return this.droneConf;
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

    public VictimConf getVictimConfiguration() {
        return this.victimConf;
    }

    public JSONOption toJSON() {
        JSONObjectBuilder json = JSONBuilder.Object();
        json.put(Scenario.SCENARIO_ID, this.scenarioID);
        json.put(Scenario.SEED, this.seed);
        json.put(Scenario.MAP, this.map.toJSON());
        json.put(Scenario.DISASTER_SCALE, this.disasterScale);
        json.put(Scenario.MISSION_LENGTH, this.missionLength);
        json.put(Scenario.STEP_SIZE, this.stepSize);
        json.put(Scenario.NUM_VICTIMS, this.numVictims);
        json.put(Scenario.VICTIM_CONF, this.victimConf.toJSON());
        json.put(Scenario.BASE_CONF, this.baseConf.toJSON());
        json.put(Scenario.NUM_DRONES, this.numDrones);
        json.put(Scenario.DRONE_CONF, this.droneConf.toJSON());
        return json.toJSON();
    }

    public boolean equals(Scenario config) {
        return this.map.equals(config.map) && this.disasterScale == config.disasterScale &&
            this.missionLength == config.missionLength && this.numVictims == config.numVictims &&
            this.victimConf.equals(config.victimConf) && this.baseConf.equals(config.baseConf) &&
            this.numDrones == config.numDrones && this.droneConf.equals(config.droneConf);
    }

}
