package com.seat.rescuesim.common;

import java.util.ArrayList;
import java.util.Random;

import com.seat.rescuesim.common.base.BaseConf;
import com.seat.rescuesim.common.drone.DroneConf;
import com.seat.rescuesim.common.map.Map;
import com.seat.rescuesim.common.remote.Remote;
import com.seat.rescuesim.common.victim.VictimConf;

public interface Application {

    ArrayList<BaseConf> getBaseConfigurations();

    double getDisasterScale();

    ArrayList<DroneConf> getDroneConfigurations();

    Map getMap();

    int getMissionLength();

    String getScenarioID();

    default long getSeed() {
        return new Random().nextLong();
    }

    default Scenario getScenario() {
        return new Scenario(
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

    ArrayList<VictimConf> getVictimConfigurations();

    void run();

    ArrayList<Remote> update(Snapshot snap);

}
