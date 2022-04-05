package com.seat.rescuesim.common.app;

import com.seat.rescuesim.common.scenario.SARConfig;

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

}
