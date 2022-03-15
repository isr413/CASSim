package com.seat.rescuesim.common;

import java.util.ArrayList;
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
    private static final String NUM_BASES = "num_bases";
    private static final String NUM_DRONES = "num_drones";
    private static final String NUM_VICTIMS = "num_victims";
    private static final String SCENARIO_ID = "scenario_id";
    private static final String SEED = "seed";
    private static final String STEP_SIZE = "step_size";
    private static final String VICTIM_CONF = "victim_conf";

    private ArrayList<BaseConf> baseConf;
    private double disasterScale;
    private ArrayList<DroneConf> droneConf;
    private Map map;
    private int missionLength;
    private int numBases;
    private int numDrones;
    private int numVictims;
    private String scenarioID;
    private long seed;
    private double stepSize;
    private ArrayList<VictimConf> victimConf;

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
        this(Scenario.DEFAULT_ID, map, 0, missionLength, stepSize, new ArrayList<VictimConf>(),
            new ArrayList<BaseConf>(), new ArrayList<DroneConf>());
    }

    public Scenario(String scenarioID, Map map, int missionLength, double stepSize) {
        this(scenarioID, map, 0, missionLength, stepSize, new ArrayList<VictimConf>(),
            new ArrayList<BaseConf>(), new ArrayList<DroneConf>());
    }

    public Scenario(long seed, Map map, int missionLength, double stepSize) {
        this(Scenario.DEFAULT_ID, seed, map, 0, missionLength, stepSize, new ArrayList<VictimConf>(),
            new ArrayList<BaseConf>(), new ArrayList<DroneConf>());
    }

    public Scenario(String scenarioID, long seed, Map map, int missionLength, double stepSize) {
        this(scenarioID, seed, map, 0, missionLength, stepSize, new ArrayList<VictimConf>(),
            new ArrayList<BaseConf>(), new ArrayList<DroneConf>());
    }

    //-- Constructors for scenarios with victims and no drones --//
    public Scenario(Map map, int missionLength, double stepSize, ArrayList<VictimConf> victimConf) {
        this(Scenario.DEFAULT_ID, map, 0, missionLength, stepSize, victimConf,
            new ArrayList<BaseConf>(), new ArrayList<DroneConf>());
    }

    public Scenario(String scenarioID, Map map, int missionLength, double stepSize, ArrayList<VictimConf> victimConf) {
        this(scenarioID, map, 0, missionLength, stepSize, victimConf,
            new ArrayList<BaseConf>(), new ArrayList<DroneConf>());
    }

    public Scenario(long seed, Map map, int missionLength, double stepSize, ArrayList<VictimConf> victimConf) {
        this(Scenario.DEFAULT_ID, seed, map, 0, missionLength, stepSize, victimConf,
            new ArrayList<BaseConf>(), new ArrayList<DroneConf>());
    }

    public Scenario(String scenarioID, long seed, Map map, int missionLength, double stepSize,
            ArrayList<VictimConf> victimConf) {
        this(scenarioID, seed, map, 0, missionLength, stepSize, victimConf,
            new ArrayList<BaseConf>(), new ArrayList<DroneConf>());
    }

    //-- Constructors for scenarios with victims and drones but no base --//
    public Scenario(Map map, int missionLength, double stepSize, ArrayList<VictimConf> victimConf,
            ArrayList<DroneConf> droneConf) {
        this(Scenario.DEFAULT_ID, map, 0, missionLength, stepSize, victimConf, new ArrayList<BaseConf>(), droneConf);
    }

    public Scenario(String scenarioID, Map map, int missionLength, double stepSize,
            ArrayList<VictimConf> victimConf, ArrayList<DroneConf> droneConf) {
        this(scenarioID, map, 0, missionLength, stepSize, victimConf, new ArrayList<BaseConf>(), droneConf);
    }

    public Scenario(long seed, Map map, int missionLength, double stepSize, ArrayList<VictimConf> victimConf,
            ArrayList<DroneConf> droneConf) {
        this(Scenario.DEFAULT_ID, seed, map, 0, missionLength, stepSize, victimConf, new ArrayList<BaseConf>(),
            droneConf);
    }

    public Scenario(String scenarioID, long seed, Map map, int missionLength, double stepSize,
            ArrayList<VictimConf> victimConf, ArrayList<DroneConf> droneConf) {
        this(scenarioID, seed, map, 0, missionLength, stepSize, victimConf, new ArrayList<BaseConf>(), droneConf);
    }

    //-- Constructors for scenarios with victims, drones, and a base --//
    public Scenario(Map map, double disasterScale, int missionLength, double stepSize,
            ArrayList<VictimConf> victimConf, ArrayList<BaseConf> baseConf, ArrayList<DroneConf> droneConf) {
        this(Scenario.DEFAULT_ID, map, disasterScale, missionLength, stepSize, victimConf, baseConf, droneConf);
    }

    public Scenario(String scenarioID, Map map, double disasterScale, int missionLength, double stepSize,
            ArrayList<VictimConf> victimConf, ArrayList<BaseConf> baseConf, ArrayList<DroneConf> droneConf) {
        this(scenarioID, new Random().nextLong(), map, disasterScale, missionLength, stepSize, victimConf,
            baseConf, droneConf);
    }

    public Scenario(long seed, Map map, double disasterScale, int missionLength, double stepSize,
            ArrayList<VictimConf> victimConf, ArrayList<BaseConf> baseConf, ArrayList<DroneConf> droneConf) {
        this(Scenario.DEFAULT_ID, seed, map, disasterScale, missionLength, stepSize, victimConf, baseConf, droneConf);
    }

    public Scenario(String scenarioID, long seed, Map map, double disasterScale, int missionLength,
            double stepSize, ArrayList<VictimConf> victimConf, ArrayList<BaseConf> baseConf,
            ArrayList<DroneConf> droneConf) {
        this.scenarioID = scenarioID;
        this.seed = seed;
        this.map = map;
        this.disasterScale = disasterScale;
        this.missionLength = missionLength;
        this.stepSize = stepSize;
        this.victimConf = victimConf;
        this.baseConf = baseConf;
        this.droneConf = droneConf;
        this.countBases();
        this.countDrones();
        this.countVictims();
    }

    private void countBases() {
        this.numBases = 0;
        for (BaseConf conf : baseConf) {
            this.numBases += conf.getCount();
        }
    }

    private void countDrones() {
        this.numDrones = 0;
        for (DroneConf conf : droneConf) {
            this.numDrones += conf.getCount();
        }
    }

    private void countVictims() {
        this.numVictims = 0;
        for (VictimConf conf : victimConf) {
            this.numVictims += conf.getCount();
        }
    }

    @Override
    protected void decode(JSONObject json) {
        this.scenarioID = json.getString(Scenario.SCENARIO_ID);
        this.seed = json.getLong(Scenario.SEED);
        this.map = new Map(json.getJSONObject(Scenario.MAP));
        this.disasterScale = json.getDouble(Scenario.DISASTER_SCALE);
        this.missionLength = json.getInt(Scenario.MISSION_LENGTH);
        this.stepSize = json.getDouble(Scenario.STEP_SIZE);
        this.victimConf = new ArrayList<>();
        if (json.hasKey(Scenario.VICTIM_CONF)) {
            JSONArray jsonVictims = json.getJSONArray(Scenario.VICTIM_CONF);
            for (int i = 0; i < jsonVictims.length(); i++) {
                this.victimConf.add(new VictimConf(jsonVictims.getJSONObject(i)));
            }
        }
        this.baseConf = new ArrayList<>();
        if (json.hasKey(Scenario.BASE_CONF)) {
            JSONArray jsonBases = json.getJSONArray(Scenario.BASE_CONF);
            for (int i = 0; i < jsonBases.length(); i++) {
                this.baseConf.add(new BaseConf(jsonBases.getJSONObject(i)));
            }
        }
        this.droneConf = new ArrayList<>();
        if (json.hasKey(Scenario.DRONE_CONF)) {
            JSONArray jsonDrones = json.getJSONArray(Scenario.DRONE_CONF);
            for (int i = 0; i < jsonDrones.length(); i++) {
                this.droneConf.add(new DroneConf(jsonDrones.getJSONObject(i)));
            }
        }
        this.countBases();
        this.countDrones();
        this.countVictims();
    }

    public ArrayList<BaseConf> getBaseConf() {
        return this.baseConf;
    }

    public double getDisasterScale() {
        return this.disasterScale;
    }

    public ArrayList<DroneConf> getDroneConf() {
        return this.droneConf;
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

    public ArrayList<VictimConf> getVictimConf() {
        return this.victimConf;
    }

    public boolean hasBaseConf() {
        return !this.baseConf.isEmpty();
    }

    public boolean hasDroneConf() {
        return !this.droneConf.isEmpty();
    }

    public boolean hasVictimConf() {
        return !this.victimConf.isEmpty();
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
        if (this.hasVictimConf()) {
            JSONArrayBuilder jsonVictims = JSONBuilder.Array();
            for (VictimConf conf : this.victimConf) {
                jsonVictims.put(conf.toJSON());
            }
            json.put(Scenario.VICTIM_CONF, jsonVictims.toJSON());
        }
        json.put(Scenario.NUM_BASES, this.numBases);
        if (this.hasBaseConf()) {
            JSONArrayBuilder jsonBases = JSONBuilder.Array();
            for (BaseConf conf : this.baseConf) {
                jsonBases.put(conf.toJSON());
            }
            json.put(Scenario.BASE_CONF, jsonBases.toJSON());
        }
        json.put(Scenario.NUM_DRONES, this.numDrones);
        if (this.hasDroneConf()) {
            JSONArrayBuilder jsonDrones = JSONBuilder.Array();
            for (DroneConf conf : this.droneConf) {
                jsonDrones.put(conf.toJSON());
            }
            json.put(Scenario.DRONE_CONF, jsonDrones.toJSON());
        }
        return json.toJSON();
    }

    public boolean equals(Scenario config) {
        return this.map.equals(config.map) && this.disasterScale == config.disasterScale &&
            this.missionLength == config.missionLength && this.victimConf.equals(config.victimConf) &
            this.baseConf.equals(config.baseConf) && this.droneConf.equals(config.droneConf);
    }

}
