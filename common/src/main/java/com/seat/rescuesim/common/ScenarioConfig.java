package com.seat.rescuesim.common;

import org.json.JSONException;
import org.json.JSONObject;

public class ScenarioConfig {
    private static final String DISASTER_SCALE = "disaster_scale";
    private static final String MAP = "map";
    private static final String MISSION_LENGTH = "mission_length";
    private static final String NUM_VICTIMS = "num_victims";
    private static final String STEP_SIZE = "step_size";
    private static final String VICTIM_MOVE_SPEED_MEAN = "victim_move_speed_mean";
    private static final String VICTIM_MOVE_SPEED_STD_DEV = "victim_move_speed_std_dev";

    private double disasterScaleParam;
    private Map map;
    private int missionLength;
    private int numVictims;
    private double stepSize;
    private Tuple<Double, Double> victimMoveSpeedDistParams;

    public static ScenarioConfig decode(JSONObject json) throws JSONException {
        int numVictims = json.getInt(ScenarioConfig.NUM_VICTIMS);
        double victimMoveSpeedMean = json.getDouble(ScenarioConfig.VICTIM_MOVE_SPEED_MEAN);
        double victimMoveSpeedStdDev = json.getDouble(ScenarioConfig.VICTIM_MOVE_SPEED_STD_DEV);
        double disasterScale = json.getDouble(ScenarioConfig.DISASTER_SCALE);
        Map map = Map.decode(json.getJSONObject(ScenarioConfig.MAP));
        int missionLength = json.getInt(ScenarioConfig.MISSION_LENGTH);
        double stepSize = json.getDouble(ScenarioConfig.STEP_SIZE);
        return new ScenarioConfig(numVictims, victimMoveSpeedMean, victimMoveSpeedStdDev, disasterScale,
                map, missionLength, stepSize);
    }

    public static ScenarioConfig decode(String encoding) {
        return ScenarioConfig.decode(new JSONObject(encoding));
    }

    public ScenarioConfig(int numVictims, double victimMoveSpeedMean, double victimMoveSpeedStdDev,
            double disasterScale, Map map, int missionLength, double stepSize) {
        this.numVictims = numVictims;
        this.victimMoveSpeedDistParams = new Tuple<>(victimMoveSpeedMean, victimMoveSpeedStdDev);
        this.disasterScaleParam = disasterScale;
        this.map = map;
        this.missionLength = missionLength;
        this.stepSize = stepSize;
    }

    public double getDisasterScaleParameter() {
        return this.disasterScaleParam;
    }

    public Map getMap() {
        return this.map;
    }

    public int getMissionLength() {
        return this.missionLength;
    }

    public int getNumberOfVictims() {
        return this.numVictims;
    }

    public double getStepSize() {
        return this.stepSize;
    }

    public Tuple<Double, Double> getVictimMoveSpeedDistributionParameters() {
        return this.victimMoveSpeedDistParams;
    }

    public String encode() {
        return this.toJSON().toString();
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put(ScenarioConfig.NUM_VICTIMS, this.numVictims);
        json.put(ScenarioConfig.VICTIM_MOVE_SPEED_MEAN, this.victimMoveSpeedDistParams.getFirst());
        json.put(ScenarioConfig.VICTIM_MOVE_SPEED_STD_DEV, this.victimMoveSpeedDistParams.getSecond());
        json.put(ScenarioConfig.DISASTER_SCALE, this.disasterScaleParam);
        json.put(ScenarioConfig.MAP, this.map.toJSON());
        json.put(ScenarioConfig.MISSION_LENGTH, this.missionLength);
        json.put(ScenarioConfig.STEP_SIZE, this.stepSize);
        return json;
    }

    public String toString() {
        return this.encode();
    }

}
