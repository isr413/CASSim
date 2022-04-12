package com.seat.sim.common.core;

import java.util.Collection;
import java.util.Random;

import com.seat.sim.common.math.Grid;
import com.seat.sim.common.remote.RemoteConfig;
import com.seat.sim.common.remote.intent.IntentionSet;
import com.seat.sim.common.scenario.ScenarioConfig;
import com.seat.sim.common.scenario.Snapshot;

public interface Application {

    Grid getGrid();

    int getMissionLength();

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
