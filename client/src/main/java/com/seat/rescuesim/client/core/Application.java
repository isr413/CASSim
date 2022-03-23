package com.seat.rescuesim.client.core;

import java.util.ArrayList;
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

    ArrayList<RemoteConfig> getRemoteConfigs();

    double getStepSize();

    void run();

    ArrayList<RemoteController> update(Snapshot snap);

}
