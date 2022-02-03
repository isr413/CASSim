package com.seat.rescuesim.common;

import org.json.JSONException;
import org.json.JSONObject;

public class ScenarioConfig {
    private static final String BASE = "base";
    private static final String DISASTER_SCALE = "disaster_scale";
    private static final String DRONE_SPEC = "drone_spec";
    private static final String MAP = "map";
    private static final String MISSION_LENGTH = "mission_length";
    private static final String NUM_DRONES = "num_drones";
    private static final String NUM_VICTIMS = "num_victims";
    private static final String STEP_SIZE = "step_size";
    private static final String VICTIM_SPEC = "victim_spec";

    private Base base;
    private double disasterScaleParam;
    private DroneSpecification droneSpec;
    private Map map;
    private int missionLength;
    private int numDrones;
    private int numVictims;
    private double stepSize;
    private VictimSpecification victimSpec;

    public static ScenarioConfig decode(JSONObject json) throws JSONException {
        int numDrones = json.getInt(ScenarioConfig.NUM_DRONES);
        DroneSpecification droneSpec = DroneSpecification.decode(json.getJSONObject(ScenarioConfig.DRONE_SPEC));
        int numVictims = json.getInt(ScenarioConfig.NUM_VICTIMS);
        VictimSpecification victimSpec = VictimSpecification.decode(json.getJSONObject(ScenarioConfig.VICTIM_SPEC));
        Map map = Map.decode(json.getJSONObject(ScenarioConfig.MAP));
        Base base = Base.decode(json.getJSONObject(ScenarioConfig.BASE));
        double disasterScale = json.getDouble(ScenarioConfig.DISASTER_SCALE);
        int missionLength = json.getInt(ScenarioConfig.MISSION_LENGTH);
        double stepSize = json.getDouble(ScenarioConfig.STEP_SIZE);
        return new ScenarioConfig(numDrones, droneSpec, numVictims, victimSpec, map, base,
                disasterScale, missionLength, stepSize);
    }

    public static ScenarioConfig decode(String encoding) {
        return ScenarioConfig.decode(new JSONObject(encoding));
    }

    public ScenarioConfig(int numDrones, DroneSpecification droneSpec,
            int numVictims, VictimSpecification victimSpec,
            Map map, Base base,
            double disasterScale, int missionLength, double stepSize) {
        this.numDrones = numDrones;
        this.droneSpec = droneSpec;
        this.numVictims = numVictims;
        this.victimSpec = victimSpec;
        this.map = map;
        this.base = base;
        this.disasterScaleParam = disasterScale;
        this.missionLength = missionLength;
        this.stepSize = stepSize;
    }

    public Base getBase() {
        return this.base;
    }

    public double getDisasterScaleParameter() {
        return this.disasterScaleParam;
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

    public double getStepSize() {
        return this.stepSize;
    }

    public VictimSpecification getVictimSpecification() {
        return this.victimSpec;
    }

    public String encode() {
        return this.toJSON().toString();
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put(ScenarioConfig.NUM_DRONES, this.numDrones);
        json.put(ScenarioConfig.DRONE_SPEC, this.droneSpec.toJSON());
        json.put(ScenarioConfig.NUM_VICTIMS, this.numVictims);
        json.put(ScenarioConfig.VICTIM_SPEC, this.victimSpec.toJSON());
        json.put(ScenarioConfig.MAP, this.map.toJSON());
        json.put(ScenarioConfig.BASE, this.base.toJSON());
        json.put(ScenarioConfig.DISASTER_SCALE, this.disasterScaleParam);
        json.put(ScenarioConfig.MISSION_LENGTH, this.missionLength);
        json.put(ScenarioConfig.STEP_SIZE, this.stepSize);
        return json;
    }

    public String toString() {
        return this.encode();
    }

    public boolean equals(ScenarioConfig config) {
        // scenario config should be a singleton
        return this == config;
    }

}
