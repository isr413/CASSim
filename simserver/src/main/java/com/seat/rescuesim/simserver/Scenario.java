package com.seat.rescuesim.simserver;

import com.seat.rescuesim.common.ScenarioConfig;

public class Scenario {

    private ScenarioConfig config;

    public Scenario(ScenarioConfig config) {
        this.config = config;
    }

    public ScenarioConfig getConfig() {
        return this.config;
    }

}
