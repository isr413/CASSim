package com.seat.rescuesim.common.scenario;

import java.util.ArrayList;
import java.util.Collection;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.JSONObjectBuilder;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.map.Map;
import com.seat.rescuesim.common.remote.RemoteConfig;
import com.seat.rescuesim.common.remote.RemoteType;
import com.seat.rescuesim.common.remote.base.BaseConfig;
import com.seat.rescuesim.common.remote.kinetic.KineticRemoteType;
import com.seat.rescuesim.common.remote.kinetic.drone.DroneConfig;
import com.seat.rescuesim.common.remote.kinetic.victim.VictimConfig;

public class SARConfig extends ScenarioConfig {
    public static final String DISASTER_SCALE = "disaster_scale";
    public static final String NUM_BASES = "num_bases";
    public static final String NUM_DRONES = "num_drones";
    public static final String NUM_VICTIMS = "num_victims";

    protected static final double DEFAULT_DISASTER_SCALE = 0.0;

    private double disasterScale;
    private int numBases;
    private int numDrones;
    private int numVictims;

    public SARConfig(Map map, int missionLength, double stepSize, Collection<RemoteConfig> remoteConfig) {
        this(ScenarioConfig.DEFAULT_ID, ScenarioConfig.DEFAULT_SEED, map, SARConfig.DEFAULT_DISASTER_SCALE,
            missionLength, stepSize, remoteConfig);
    }

    public SARConfig(String scenarioID, Map map, int missionLength, double stepSize,
            Collection<RemoteConfig> remoteConfig) {
        this(scenarioID, ScenarioConfig.DEFAULT_SEED, map, SARConfig.DEFAULT_DISASTER_SCALE, missionLength, stepSize,
            remoteConfig);
    }

    public SARConfig(long seed, Map map, int missionLength, double stepSize, Collection<RemoteConfig> remoteConfig) {
        this(ScenarioConfig.DEFAULT_ID, seed, map, SARConfig.DEFAULT_DISASTER_SCALE, missionLength, stepSize,
            remoteConfig);
    }

    public SARConfig(String scenarioID, long seed, Map map, int missionLength, double stepSize,
            Collection<RemoteConfig> remoteConfig) {
        this(scenarioID, seed, map, SARConfig.DEFAULT_DISASTER_SCALE, missionLength, stepSize, remoteConfig);
    }

    public SARConfig(Map map, double disasterScale, int missionLength, double stepSize,
            Collection<RemoteConfig> remoteConfig) {
        this(ScenarioConfig.DEFAULT_ID, ScenarioConfig.DEFAULT_SEED, map, disasterScale, missionLength, stepSize,
            remoteConfig);
    }

    public SARConfig(String scenarioID, Map map, double disasterScale, int missionLength, double stepSize,
            Collection<RemoteConfig> remoteConfig) {
        this(scenarioID, ScenarioConfig.DEFAULT_SEED, map, disasterScale, missionLength, stepSize, remoteConfig);
    }

    public SARConfig(long seed, Map map, double disasterScale, int missionLength, double stepSize,
            Collection<RemoteConfig> remoteConfig) {
        this(ScenarioConfig.DEFAULT_ID, seed, map, disasterScale, missionLength, stepSize, remoteConfig);
    }

    public SARConfig(String scenarioID, long seed, Map map, double disasterScale, int missionLength,
            double stepSize, Collection<RemoteConfig> remoteConfig) {
        super(scenarioID, seed, map, missionLength, stepSize, remoteConfig);
        this.disasterScale = disasterScale;
        this.init();
    }

    public SARConfig(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        super.decode(json);
        this.disasterScale = json.getDouble(SARConfig.DISASTER_SCALE);
        this.init();
    }

    @Override
    protected JSONObjectBuilder getJSONBuilder() {
        JSONObjectBuilder json = super.getJSONBuilder();
        json.put(SARConfig.DISASTER_SCALE, this.disasterScale);
        json.put(SARConfig.NUM_VICTIMS, this.numVictims);
        json.put(SARConfig.NUM_BASES, this.numBases);
        json.put(SARConfig.NUM_DRONES, this.numDrones);
        return json;
    }

    private void countBases() {
        this.numBases = 0;
        for (RemoteConfig config : this.getRemotes()) {
            if (config.getSpecType().equals(RemoteType.BASE)) {
                this.numBases += config.getCount();
            }
        }
    }

    private void countDrones() {
        this.numDrones = 0;
        for (RemoteConfig config : this.getRemotes()) {
            if (config.getSpecType().equals(KineticRemoteType.DRONE)) {
                this.numDrones += config.getCount();
            }
        }
    }

    private void countVictims() {
        this.numVictims = 0;
        for (RemoteConfig config : this.getRemotes()) {
            if (config.getSpecType().equals(KineticRemoteType.VICTIM)) {
                this.numVictims += config.getCount();
            }
        }
    }

    private void init() {
        this.countBases();
        this.countDrones();
        this.countVictims();
    }

    public ArrayList<BaseConfig> getBases() {
        ArrayList<BaseConfig> baseConfig = new ArrayList<>();
        for (RemoteConfig config : this.getRemotes()) {
            if (config.getSpecType().equals(RemoteType.BASE)) {
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
        for (RemoteConfig config : this.getRemotes()) {
            if (config.getSpecType().equals(KineticRemoteType.DRONE)) {
                droneConfig.add((DroneConfig) config);
            }
        }
        return droneConfig;
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

    public ArrayList<VictimConfig> getVictims() {
        ArrayList<VictimConfig> victimConfig = new ArrayList<>();
        for (RemoteConfig config : this.getRemotes()) {
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

    public boolean hasVictims() {
        return this.numVictims > 0;
    }

    public boolean equals(SARConfig config) {
        return super.equals(config) && this.disasterScale == config.disasterScale;
    }

}
