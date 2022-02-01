package com.seat.rescuesim;

import org.json.JSONObject;

public class ScenarioConfig {
    private static final String NUM_VICTIMS = "num_victims";
    private static final String VICTIM_MOVE_SPEED_MEAN = "victim_move_speed_mean";
    private static final String VICTIM_MOVE_SPEED_STD_DEV = "victim_move_speed_std_dev";

    private int numVictims;
    private Tuple<Double, Double> victimMoveSpeedDistParams;

    public ScenarioConfig(int numVictims, double victimMoveSpeedMean, double victimMoveSpeedStdDev) {
        this.numVictims = numVictims;
        this.victimMoveSpeedDistParams = new Tuple<>(victimMoveSpeedMean, victimMoveSpeedStdDev);
    }

    public ScenarioConfig(String encoding) {
        this.decode(encoding);
    }

    private void decode(String encoding) {
        JSONObject json = new JSONObject(encoding);
        this.numVictims = json.getInt(ScenarioConfig.NUM_VICTIMS);
        this.victimMoveSpeedDistParams = new Tuple<>(
            json.getDouble(ScenarioConfig.VICTIM_MOVE_SPEED_MEAN),
            json.getDouble(ScenarioConfig.VICTIM_MOVE_SPEED_STD_DEV)
        );
    }

    public int getNumberOfVictims() {
        return this.numVictims;
    }

    public Tuple<Double, Double> getVictimMoveSpeedDistributionParameters() {
        return this.victimMoveSpeedDistParams;
    }

    public String encode() {
        JSONObject json = new JSONObject();
        json.put(ScenarioConfig.NUM_VICTIMS, this.numVictims);
        json.put(ScenarioConfig.VICTIM_MOVE_SPEED_MEAN, this.victimMoveSpeedDistParams.getFirst());
        json.put(ScenarioConfig.VICTIM_MOVE_SPEED_STD_DEV, this.victimMoveSpeedDistParams.getSecond());
        return json.toString();
    }

}
