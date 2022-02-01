package com.seat.rescuesim;

import org.json.JSONException;
import org.json.JSONObject;

public class ScenarioConfig {
    private static final String DISASTER_SCALE = "disaster_scale";
    private static final String MAP = "map";
    private static final String NUM_VICTIMS = "num_victims";
    private static final String VICTIM_MOVE_SPEED_MEAN = "victim_move_speed_mean";
    private static final String VICTIM_MOVE_SPEED_STD_DEV = "victim_move_speed_std_dev";

    private int numVictims;
    private Tuple<Double, Double> victimMoveSpeedDistParams;
    private double disasterScaleParam;
    private Map map;

    public static ScenarioConfig decode(JSONObject json) throws JSONException {
        int numVictims = json.getInt(ScenarioConfig.NUM_VICTIMS);
        double victimMoveSpeedMean = json.getDouble(ScenarioConfig.VICTIM_MOVE_SPEED_MEAN);
        double victimMoveSpeedStdDev = json.getDouble(ScenarioConfig.VICTIM_MOVE_SPEED_STD_DEV);
        double disasterScale = json.getDouble(ScenarioConfig.DISASTER_SCALE);
        Map map = Map.decode(json.getJSONObject(ScenarioConfig.MAP));
        return new ScenarioConfig(numVictims, victimMoveSpeedMean, victimMoveSpeedStdDev, disasterScale, map);
    }

    public static ScenarioConfig decode(String encoding) {
        return ScenarioConfig.decode(new JSONObject(encoding));
    }

    public ScenarioConfig(int numVictims, double victimMoveSpeedMean, double victimMoveSpeedStdDev,
            double disasterScale, Map map) {
        this.numVictims = numVictims;
        this.victimMoveSpeedDistParams = new Tuple<>(victimMoveSpeedMean, victimMoveSpeedStdDev);
        this.disasterScaleParam = disasterScale;
        this.map = map;
    }

    public int getNumberOfVictims() {
        return this.numVictims;
    }

    public Tuple<Double, Double> getVictimMoveSpeedDistributionParameters() {
        return this.victimMoveSpeedDistParams;
    }

    public double getDisasterScaleParameter() {
        return this.disasterScaleParam;
    }

    public Map getMap() {
        return this.map;
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
        return json;
    }

    public String toString() {
        return this.encode();
    }

}
