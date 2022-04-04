package com.seat.rescuesim.common.app;

import java.util.Collection;
import java.util.Random;

import com.seat.rescuesim.common.ScenarioConfig;
import com.seat.rescuesim.common.Snapshot;
import com.seat.rescuesim.common.map.Map;
import com.seat.rescuesim.common.remote.RemoteConfig;
import com.seat.rescuesim.common.remote.RemoteController;

public interface Application {

    double getDisasterScale();

    Map getMap();

    int getMissionLength();

    String getScenarioID();

    default long getSeed() {
        return new Random().nextLong();
    }

    default ScenarioConfig getScenarioConfig() {
        return new ScenarioConfig(
            getScenarioID(),
            getSeed(),
            getMap(),
            getDisasterScale(),
            getMissionLength(),
            getStepSize(),
            getRemoteConfigs()
        );
    }

    Collection<RemoteConfig> getRemoteConfigs();

    double getStepSize();

    Collection<RemoteController> update(Snapshot snap);

}
