package com.seat.rescuesim.simserver.sim;

import java.util.HashMap;
import java.util.Iterator;

import com.seat.rescuesim.common.Scenario;
import com.seat.rescuesim.common.victim.VictimConf;
import com.seat.rescuesim.common.victim.VictimSpec;

public class SimScenario {

    private Scenario scenarioConfig;
    private HashMap<String, SimVictim> remoteVictims;
    private HashMap<String, SimVictim> simVictims;

    public SimScenario(Scenario scenario) {
        this.scenarioConfig = scenario;
        this.initVictims();
    }

    private void initVictims() {
        this.simVictims = new HashMap<>();
        this.remoteVictims = new HashMap<>();
        for (VictimConf conf : this.scenarioConfig.getVictimConfiguration()) {
            VictimSpec spec = conf.getSpecification();
            Iterator<String> remotes = conf.getRemotes().iterator();
            for (int i = 0; i < conf.getCount(); i++) {
                String label = (i < conf.getRemotes().size()) ? remotes.next() : String.format("v<%d>", i);
                if (conf.isDynamic()) {
                    this.remoteVictims.put(label, new SimVictim(label, spec));
                } else {
                    this.simVictims.put(label, new SimVictim(label, spec));
                }
            }
        }
    }

    public String getScenarioID() {
        return this.scenarioConfig.getScenarioID();
    }

}
