package com.seat.rescuesim.client;

import java.util.ArrayList;
import java.util.Random;

import com.seat.rescuesim.common.ScenarioConfig;
import com.seat.rescuesim.common.Snapshot;
import com.seat.rescuesim.common.base.BaseConfig;
import com.seat.rescuesim.common.drone.DroneConfig;
import com.seat.rescuesim.common.map.Map;
import com.seat.rescuesim.common.remote.RemoteController;
import com.seat.rescuesim.common.victim.VictimConfig;

public interface Application {

    ArrayList<BaseConfig> getBaseConfigurations();

    double getDisasterScale();

    ArrayList<DroneConfig> getDroneConfigurations();

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
            getVictimConfigurations(),
            getBaseConfigurations(),
            getDroneConfigurations()
        );
    }

    double getStepSize();

    ArrayList<VictimConfig> getVictimConfigurations();

    void run();

    ArrayList<RemoteController> update(Snapshot snap);

}
