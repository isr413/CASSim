package com.seat.rescuesim.common.core;

import com.seat.rescuesim.common.scenario.SARConfig;
import com.seat.rescuesim.common.scenario.ScenarioType;

public interface SARApplication extends Application {

    double getDisasterScale();

    default SARConfig getScenarioConfig() {
        return new SARConfig(
            getScenarioID(),
            getSeed(),
            getMap(),
            getDisasterScale(),
            getMissionLength(),
            getStepSize(),
            getRemoteConfigs()
        );
    }

    default ScenarioType getScenarioType() {
        return ScenarioType.SAR;
    }

}
