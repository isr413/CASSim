package com.seat.sim.common.core;

import com.seat.sim.common.scenario.SARConfig;

public interface SARApplication extends Application {

    double getDisasterScale();

    default SARConfig getScenarioConfig() {
        return new SARConfig(
            getScenarioID(),
            getSeed(),
            getGrid(),
            getDisasterScale(),
            getMissionLength(),
            getStepSize(),
            getRemoteConfigs()
        );
    }

}
