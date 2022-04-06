package com.seat.rescuesim.common.app;

import java.util.Collection;
import java.util.Random;

import com.seat.rescuesim.common.map.Map;
import com.seat.rescuesim.common.remote.RemoteConfig;
import com.seat.rescuesim.common.remote.intent.IntentionSet;
import com.seat.rescuesim.common.scenario.ScenarioConfig;
import com.seat.rescuesim.common.scenario.ScenarioType;
import com.seat.rescuesim.common.scenario.Snapshot;

public interface Application {

    Map getMap();

    int getMissionLength();

    String getScenarioID();

    default long getSeed() {
        return new Random().nextLong();
    }

    default ScenarioConfig getScenarioConfig() {
        return new ScenarioConfig(
            getScenarioType(),
            getScenarioID(),
            getSeed(),
            getMap(),
            getMissionLength(),
            getStepSize(),
            getRemoteConfigs()
        );
    }

    Collection<RemoteConfig> getRemoteConfigs();

    default ScenarioType getScenarioType() {
        return ScenarioType.GENERIC;
    }

    double getStepSize();

    Collection<IntentionSet> update(Snapshot snap);

}
