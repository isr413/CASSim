package com.seat.sim.common.core;

import java.util.Collection;
import java.util.Random;
import java.util.stream.Collectors;

import com.seat.sim.common.math.Grid;
import com.seat.sim.common.remote.RemoteConfig;
import com.seat.sim.common.remote.intent.IntentionSet;
import com.seat.sim.common.remote.mobile.MobileRemoteConfig;
import com.seat.sim.common.remote.mobile.aerial.AerialRemoteConfig;
import com.seat.sim.common.scenario.ScenarioConfig;
import com.seat.sim.common.scenario.Snapshot;

public interface Application {

    default Collection<AerialRemoteConfig> getAerialRemoteConfigs() {
        return this.getRemoteConfigs().stream()
            .filter(config -> config.getProto().isAerial())
            .map(config -> (AerialRemoteConfig) config)
            .collect(Collectors.toList());
    }

    Grid getGrid();

    int getMissionLength();

    default Collection<MobileRemoteConfig> getMobileRemoteConfigs() {
        return this.getRemoteConfigs().stream()
            .filter(config -> config.getProto().isMobile())
            .map(config -> (MobileRemoteConfig) config)
            .collect(Collectors.toList());
    }

    Collection<RemoteConfig> getRemoteConfigs();

    default ScenarioConfig getScenarioConfig() {
        return new ScenarioConfig(
            getScenarioID(),
            getSeed(),
            getGrid(),
            getMissionLength(),
            getStepSize(),
            getRemoteConfigs()
        );
    }

    String getScenarioID();

    default long getSeed() {
        return new Random().nextLong();
    }

    double getStepSize();

    Collection<IntentionSet> update(Snapshot snap);

}
