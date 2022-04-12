package com.seat.sim.common.scenario;

import java.util.ArrayList;
import java.util.Collection;

import com.seat.sim.common.json.JSONException;
import com.seat.sim.common.json.JSONObject;
import com.seat.sim.common.json.JSONObjectBuilder;
import com.seat.sim.common.json.JSONOption;
import com.seat.sim.common.math.Grid;
import com.seat.sim.common.remote.RemoteConfig;
import com.seat.sim.common.remote.base.BaseRemoteConfig;
import com.seat.sim.common.remote.base.BaseRemoteProto;
import com.seat.sim.common.remote.mobile.aerial.DroneRemoteConfig;
import com.seat.sim.common.remote.mobile.aerial.DroneRemoteProto;
import com.seat.sim.common.remote.mobile.ground.VictimRemoteConfig;
import com.seat.sim.common.remote.mobile.ground.VictimRemoteProto;

public class SARConfig extends ScenarioConfig {
    public static final String DISASTER_SCALE = "disaster_scale";
    public static final String NUM_BASES = "num_bases";
    public static final String NUM_DRONES = "num_drones";
    public static final String NUM_VICTIMS = "num_victims";

    protected static final double DEFAULT_DISASTER_SCALE = 0.0;

    private double disasterScale;
    private ArrayList<BaseRemoteConfig> baseConfigs;
    private ArrayList<String> baseIDs;
    private ArrayList<DroneRemoteConfig> droneConfigs;
    private ArrayList<String> droneIDs;
    private ArrayList<VictimRemoteConfig> victimConfigs;
    private ArrayList<String> victimIDs;

    public SARConfig(Grid grid, int missionLength, double stepSize, Collection<RemoteConfig> remoteConfig) {
        this(ScenarioConfig.DEFAULT_ID, ScenarioConfig.DEFAULT_SEED, grid, SARConfig.DEFAULT_DISASTER_SCALE,
            missionLength, stepSize, remoteConfig);
    }

    public SARConfig(String scenarioID, Grid grid, int missionLength, double stepSize,
            Collection<RemoteConfig> remoteConfig) {
        this(scenarioID, ScenarioConfig.DEFAULT_SEED, grid, SARConfig.DEFAULT_DISASTER_SCALE, missionLength, stepSize,
            remoteConfig);
    }

    public SARConfig(long seed, Grid grid, int missionLength, double stepSize, Collection<RemoteConfig> remoteConfig) {
        this(ScenarioConfig.DEFAULT_ID, seed, grid, SARConfig.DEFAULT_DISASTER_SCALE, missionLength, stepSize,
            remoteConfig);
    }

    public SARConfig(String scenarioID, long seed, Grid grid, int missionLength, double stepSize,
            Collection<RemoteConfig> remoteConfig) {
        this(scenarioID, seed, grid, SARConfig.DEFAULT_DISASTER_SCALE, missionLength, stepSize, remoteConfig);
    }

    public SARConfig(Grid grid, double disasterScale, int missionLength, double stepSize,
            Collection<RemoteConfig> remoteConfig) {
        this(ScenarioConfig.DEFAULT_ID, ScenarioConfig.DEFAULT_SEED, grid, disasterScale, missionLength, stepSize,
            remoteConfig);
    }

    public SARConfig(String scenarioID, Grid grid, double disasterScale, int missionLength, double stepSize,
            Collection<RemoteConfig> remoteConfig) {
        this(scenarioID, ScenarioConfig.DEFAULT_SEED, grid, disasterScale, missionLength, stepSize, remoteConfig);
    }

    public SARConfig(long seed, Grid grid, double disasterScale, int missionLength, double stepSize,
            Collection<RemoteConfig> remoteConfig) {
        this(ScenarioConfig.DEFAULT_ID, seed, grid, disasterScale, missionLength, stepSize, remoteConfig);
    }

    public SARConfig(String scenarioID, long seed, Grid grid, double disasterScale, int missionLength,
            double stepSize, Collection<RemoteConfig> remoteConfig) {
        super(scenarioID, seed, grid, missionLength, stepSize, remoteConfig);
        this.disasterScale = disasterScale;
        this.init();
    }

    public SARConfig(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        super.decode(json);
        this.disasterScale = (json.hasKey(SARConfig.DISASTER_SCALE)) ?
            json.getDouble(SARConfig.DISASTER_SCALE) :
            SARConfig.DEFAULT_DISASTER_SCALE;
        this.init();
    }

    @Override
    protected JSONObjectBuilder getJSONBuilder() {
        JSONObjectBuilder json = super.getJSONBuilder();
        json.put(SARConfig.DISASTER_SCALE, this.disasterScale);
        json.put(SARConfig.NUM_BASES, this.getNumberOfBases());
        json.put(SARConfig.NUM_DRONES, this.getNumberOfDrones());
        json.put(SARConfig.NUM_VICTIMS, this.getNumberOfVictims());
        return json;
    }

    private void init() {
        this.baseConfigs = new ArrayList<>();
        this.baseIDs = new ArrayList<>();
        this.droneConfigs = new ArrayList<>();
        this.droneIDs = new ArrayList<>();
        this.victimConfigs = new ArrayList<>();
        this.victimIDs = new ArrayList<>();
        for (RemoteConfig config : this.getRemoteConfigs()) {
            if (BaseRemoteProto.class.isAssignableFrom(config.getProto().getClass())) {
                this.baseConfigs.add((BaseRemoteConfig) config);
                this.baseIDs.addAll(config.getRemoteIDs());
            } else if (DroneRemoteProto.class.isAssignableFrom(config.getProto().getClass())) {
                this.droneConfigs.add((DroneRemoteConfig) config);
                this.droneIDs.addAll(config.getRemoteIDs());
            } else if (VictimRemoteProto.class.isAssignableFrom(config.getProto().getClass())) {
                this.victimConfigs.add((VictimRemoteConfig) config);
                this.victimIDs.addAll(config.getRemoteIDs());
            }
        }
    }

    public Collection<BaseRemoteConfig> getBaseConfigs() {
        return this.baseConfigs;
    }

    public Collection<String> getBaseRemoteIDs() {
        return this.baseIDs;
    }

    public double getDisasterScale() {
        return this.disasterScale;
    }

    public Collection<DroneRemoteConfig> getDroneConfigs() {
        return this.droneConfigs;
    }

    public Collection<String> getDroneRemoteIDs() {
        return this.droneIDs;
    }

    public int getNumberOfBases() {
        return this.baseIDs.size();
    }

    public int getNumberOfDrones() {
        return this.droneIDs.size();
    }

    public int getNumberOfVictims() {
        return this.victimIDs.size();
    }

    public Collection<VictimRemoteConfig> getVictimConfigs() {
        return this.victimConfigs;
    }

    public Collection<String> getVictimRemoteIDs() {
        return this.victimIDs;
    }

    public boolean hasBases() {
        return !this.baseIDs.isEmpty();
    }

    public boolean hasDrones() {
        return !this.droneIDs.isEmpty();
    }

    public boolean hasVictims() {
        return !this.victimIDs.isEmpty();
    }

    public boolean equals(SARConfig config) {
        if (config == null) return false;
        return super.equals(config) && this.disasterScale == config.disasterScale;
    }

}
