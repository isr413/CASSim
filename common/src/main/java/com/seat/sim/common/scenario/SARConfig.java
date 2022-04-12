package com.seat.sim.common.scenario;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import com.seat.sim.common.core.CommonException;
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

    private HashSet<String> baseIDs;
    private double disasterScale;
    private HashSet<String> droneIDs;
    private HashSet<String> victimIDs;

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

    private void init() {
        this.baseIDs = new HashSet<>();
        this.droneIDs = new HashSet<>();
        this.victimIDs = new HashSet<>();
        for (RemoteConfig config : this.getRemoteConfigs()) {
            if (BaseRemoteProto.class.isAssignableFrom(config.getProto().getClass())) {
                this.baseIDs.addAll(config.getRemoteIDs());
            } else if (DroneRemoteProto.class.isAssignableFrom(config.getProto().getClass())) {
                this.droneIDs.addAll(config.getRemoteIDs());
            } else if (VictimRemoteProto.class.isAssignableFrom(config.getProto().getClass())) {
                this.victimIDs.addAll(config.getRemoteIDs());
            }
        }
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

    public Collection<BaseRemoteConfig> getBaseRemoteConfigs() {
        ArrayList<BaseRemoteConfig> baseConfigs = new ArrayList<>();
        for (String remoteID : this.baseIDs) {
            baseConfigs.add(this.getBaseRemoteConfigWithID(remoteID));
        }
        return baseConfigs;
    }

    public BaseRemoteConfig getBaseRemoteConfigWithID(String remoteID) throws CommonException {
        if (!this.hasRemoteWithID(remoteID)) {
            throw new CommonException(String.format("Scenario %s has no remote %s", this.getScenarioID(), remoteID));
        }
        if (!this.hasBaseRemoteWithID(remoteID)) {
            throw new CommonException(String.format("Scenario %s has no base remote %s", this.getScenarioID(),
                remoteID));
        }
        return (BaseRemoteConfig) this.getRemoteConfigWithID(remoteID);
    }

    public Collection<String> getBaseRemoteIDs() {
        return this.baseIDs;
    }

    public double getDisasterScale() {
        return this.disasterScale;
    }

    public Collection<DroneRemoteConfig> getDroneRemoteConfigs() {
        ArrayList<DroneRemoteConfig> droneConfigs = new ArrayList<>();
        for (String remoteID : this.droneIDs) {
            droneConfigs.add(this.getDroneRemoteConfigWithID(remoteID));
        }
        return droneConfigs;
    }

    public DroneRemoteConfig getDroneRemoteConfigWithID(String remoteID) throws CommonException {
        if (!this.hasRemoteWithID(remoteID)) {
            throw new CommonException(String.format("Scenario %s has no remote %s", this.getScenarioID(), remoteID));
        }
        if (!this.hasDroneRemoteWithID(remoteID)) {
            throw new CommonException(String.format("Scenario %s has no drone remote %s", this.getScenarioID(),
                remoteID));
        }
        return (DroneRemoteConfig) this.getRemoteConfigWithID(remoteID);
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

    public Collection<VictimRemoteConfig> getVictimRemoteConfigs() {
        ArrayList<VictimRemoteConfig> victimConfigs = new ArrayList<>();
        for (String remoteID : this.victimIDs) {
            victimConfigs.add(this.getVictimRemoteConfigWithID(remoteID));
        }
        return victimConfigs;
    }

    public VictimRemoteConfig getVictimRemoteConfigWithID(String remoteID) throws CommonException {
        if (!this.hasRemoteWithID(remoteID)) {
            throw new CommonException(String.format("Scenario %s has no remote %s", this.getScenarioID(), remoteID));
        }
        if (!this.hasVictimRemoteWithID(remoteID)) {
            throw new CommonException(String.format("Scenario %s has no victim remote %s", this.getScenarioID(),
                remoteID));
        }
        return (VictimRemoteConfig) this.getRemoteConfigWithID(remoteID);
    }

    public Collection<String> getVictimRemoteIDs() {
        return this.victimIDs;
    }

    public boolean hasBaseRemotes() {
        return !this.baseIDs.isEmpty();
    }

    public boolean hasBaseRemoteWithID(String remoteID) {
        return this.baseIDs.contains(remoteID);
    }

    public boolean hasDroneRemotes() {
        return !this.droneIDs.isEmpty();
    }

    public boolean hasDroneRemoteWithID(String remoteID) {
        return this.droneIDs.contains(remoteID);
    }

    public boolean hasVictimRemotes() {
        return !this.victimIDs.isEmpty();
    }

    public boolean hasVictimRemoteWithID(String remoteID) {
        return this.victimIDs.contains(remoteID);
    }

    public boolean equals(SARConfig config) {
        if (config == null) return false;
        return super.equals(config) && this.disasterScale == config.disasterScale;
    }

}
