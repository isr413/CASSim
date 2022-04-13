package com.seat.sim.common.core;

import java.util.Collection;
import java.util.stream.Collectors;

import com.seat.sim.common.remote.base.BaseRemoteConfig;
import com.seat.sim.common.remote.mobile.aerial.drone.DroneRemoteConfig;
import com.seat.sim.common.remote.mobile.victim.VictimRemoteConfig;
import com.seat.sim.common.scenario.SARConfig;

public interface SARApplication extends Application {

    default Collection<BaseRemoteConfig> getBaseRemoteConfigs() {
        return this.getRemoteConfigs().stream()
            .filter(config -> BaseRemoteConfig.class.isAssignableFrom(config.getClass()))
            .map(config -> (BaseRemoteConfig) config)
            .collect(Collectors.toList());
    }

    double getDisasterScale();

    default Collection<DroneRemoteConfig> getDroneRemoteConfigs() {
        return this.getAerialRemoteConfigs().stream()
            .filter(config -> DroneRemoteConfig.class.isAssignableFrom(config.getClass()))
            .map(config -> (DroneRemoteConfig) config)
            .collect(Collectors.toList());
    }

    default SARConfig getScenarioConfig() {
        return new SARConfig(
            getScenarioID(),
            getSeed(),
            getGrid(),
            getMissionLength(),
            getStepSize(),
            getRemoteConfigs(),
            getDisasterScale()
        );
    }

    default Collection<VictimRemoteConfig> getVictimRemoteConfigs() {
        return this.getMobileRemoteConfigs().stream()
            .filter(config -> VictimRemoteConfig.class.isAssignableFrom(config.getClass()))
            .map(config -> (VictimRemoteConfig) config)
            .collect(Collectors.toList());
    }

}
