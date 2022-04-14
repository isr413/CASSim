package com.seat.sim.common.scenario;

import java.util.Collection;
import java.util.stream.Collectors;

import com.seat.sim.common.core.CommonException;
import com.seat.sim.common.json.JSONException;
import com.seat.sim.common.json.JSONObject;
import com.seat.sim.common.json.JSONObjectBuilder;
import com.seat.sim.common.json.JSONOptional;
import com.seat.sim.common.math.Grid;
import com.seat.sim.common.remote.RemoteConfig;
import com.seat.sim.common.remote.base.BaseRemoteConfig;
import com.seat.sim.common.remote.mobile.aerial.drone.DroneRemoteConfig;
import com.seat.sim.common.remote.mobile.victim.VictimRemoteConfig;

public class SARConfig extends ScenarioConfig {
    public static final String DISASTER_SCALE = "disaster_scale";
    public static final String NUM_BASES = "num_bases";
    public static final String NUM_DRONES = "num_drones";
    public static final String NUM_VICTIMS = "num_victims";

    private double disasterScale;

    public SARConfig(Grid grid, int missionLength, double stepSize, Collection<RemoteConfig> remoteConfigs,
            double disasterScale) {
        this(ScenarioConfig.DEFAULT_ID, ScenarioConfig.DEFAULT_SEED, grid, missionLength, stepSize, remoteConfigs,
            disasterScale);
    }

    public SARConfig(String scenarioID, Grid grid, int missionLength, double stepSize,
            Collection<RemoteConfig> remoteConfigs, double disasterScale) {
        this(scenarioID, ScenarioConfig.DEFAULT_SEED, grid, missionLength, stepSize, remoteConfigs, disasterScale);
    }

    public SARConfig(long seed, Grid grid, int missionLength, double stepSize,
            Collection<RemoteConfig> remoteConfigs, double disasterScale) {
        this(ScenarioConfig.DEFAULT_ID, seed, grid, missionLength, stepSize, remoteConfigs, disasterScale);
    }

    public SARConfig(String scenarioID, long seed, Grid grid, int missionLength, double stepSize,
            Collection<RemoteConfig> remoteConfigs, double disasterScale) {
        super(scenarioID, seed, grid, missionLength, stepSize, remoteConfigs);
        this.disasterScale = disasterScale;
    }

    public SARConfig(JSONOptional optional) throws JSONException {
        super(optional);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        super.decode(json);
        this.disasterScale = (json.hasKey(SARConfig.DISASTER_SCALE)) ?
            json.getDouble(SARConfig.DISASTER_SCALE) :
            0;
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
        return this.getRemoteConfigsWithType(BaseRemoteConfig.class);
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
        return this.getBaseRemoteConfigs().stream()
            .map(config -> config.getRemoteIDs())
            .flatMap(remoteIDs -> remoteIDs.stream())
            .collect(Collectors.toList());
    }

    public double getDisasterScale() {
        return this.disasterScale;
    }

    public Collection<DroneRemoteConfig> getDroneRemoteConfigs() {
        return this.getRemoteConfigsWithType(DroneRemoteConfig.class);
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
        return this.getDroneRemoteConfigs().stream()
            .map(config -> config.getRemoteIDs())
            .flatMap(remoteIDs -> remoteIDs.stream())
            .collect(Collectors.toList());
    }

    public int getNumberOfBases() {
        return this.getBaseRemoteIDs().size();
    }

    public int getNumberOfDrones() {
        return this.getDroneRemoteIDs().size();
    }

    public int getNumberOfVictims() {
        return this.getVictimRemoteIDs().size();
    }

    public Collection<VictimRemoteConfig> getVictimRemoteConfigs() {
        return this.getRemoteConfigsWithType(VictimRemoteConfig.class);
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
        return this.getVictimRemoteConfigs().stream()
            .map(config -> config.getRemoteIDs())
            .flatMap(remoteIDs -> remoteIDs.stream())
            .collect(Collectors.toList());
    }

    public boolean hasBaseRemotes() {
        return !this.getBaseRemoteConfigs().isEmpty();
    }

    public boolean hasBaseRemoteWithID(String remoteID) {
        return this.getBaseRemoteIDs().contains(remoteID);
    }

    public boolean hasDroneRemotes() {
        return !this.getDroneRemoteConfigs().isEmpty();
    }

    public boolean hasDroneRemoteWithID(String remoteID) {
        return this.getDroneRemoteIDs().contains(remoteID);
    }

    public boolean hasVictimRemotes() {
        return !this.getVictimRemoteConfigs().isEmpty();
    }

    public boolean hasVictimRemoteWithID(String remoteID) {
        return this.getVictimRemoteIDs().contains(remoteID);
    }

    public boolean equals(SARConfig config) {
        if (config == null) return false;
        return super.equals(config) && this.disasterScale == config.disasterScale;
    }

}
