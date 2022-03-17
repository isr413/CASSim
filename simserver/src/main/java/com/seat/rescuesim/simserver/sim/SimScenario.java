package com.seat.rescuesim.simserver.sim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.seat.rescuesim.common.ScenarioConfig;
import com.seat.rescuesim.common.map.Map;
import com.seat.rescuesim.common.remote.intent.Intention;
import com.seat.rescuesim.common.util.Debugger;
import com.seat.rescuesim.common.victim.VictimConfig;
import com.seat.rescuesim.common.victim.VictimSpec;
import com.seat.rescuesim.simserver.util.Random;

public class SimScenario {

    private ScenarioConfig config;
    private HashMap<String, SimVictim> dynamicVictims;
    private HashMap<String, SimVictim> passiveVictims;
    private Random rng;

    public SimScenario(ScenarioConfig config) {
        this(config, new Random(config.getSeed()));
    }

    public SimScenario(ScenarioConfig config, Random rng) {
        this.config = config;
        this.rng = rng;
        this.initVictims();
    }

    private void initVictims() {
        this.dynamicVictims = new HashMap<>();
        this.passiveVictims = new HashMap<>();
        for (VictimConfig victimConfig : this.config.getVictimConfig()) {
            VictimSpec victimSpec = victimConfig.getSpec();
            Iterator<String> remoteIDs = victimConfig.getRemoteIDs().iterator();
            for (int i = 0; i < victimConfig.getCount(); i++) {
                String label = (i < victimConfig.getRemoteIDs().size()) ? remoteIDs.next() : String.format("v<%d>", i);
                if (victimConfig.isDynamic()) {
                    this.dynamicVictims.put(label, new SimVictim(label, victimSpec,
                        this.rng.getRandomLocation2D(this.getMapWidth(), this.getMapHeight()),
                        this.rng.getRandomSpeed2D(victimSpec.getSpeedMean(), victimSpec.getSpeedStddev())));
                } else {
                    this.passiveVictims.put(label, new SimVictim(label, victimSpec,
                        this.rng.getRandomLocation2D(this.getMapWidth(), this.getMapHeight()),
                        this.rng.getRandomSpeed2D(victimSpec.getSpeedMean(), victimSpec.getSpeedStddev())));
                }
            }
        }
    }

    public Map getMap() {
        return this.config.getMap();
    }

    public int getMapHeight() {
        return this.getMap().getHeight() * this.getMap().getZoneSize();
    }

    public int getMapWidth() {
        return this.getMap().getWidth() * this.getMap().getZoneSize();
    }

    public String getScenarioID() {
        return this.config.getScenarioID();
    }

    public void update() {
        this.update(null, this.config.getStepSize());
    }

    public void update(double stepSize) {
        this.update(null, stepSize);
    }

    public void update(HashMap<String, ArrayList<Intention>> intentions, double stepSize) {
        if (intentions == null || intentions.isEmpty()) {
            Debugger.logger.info(String.format("Updating dynamic remotes %s", this.dynamicVictims.keySet().toString()));
            for (SimVictim victim : this.dynamicVictims.values()) {
                victim.update(stepSize);
            }
        } else {
            Debugger.logger.info(String.format("Updating dynamic remotes %s", this.dynamicVictims.keySet().toString()));
            for (SimVictim victim : this.dynamicVictims.values()) {
                if (intentions.containsKey(victim.getVictimID())) {
                    victim.update(intentions.get(victim.getVictimID()), stepSize);
                } else {
                    victim.update(stepSize);
                }
            }
        }
        Debugger.logger.info(String.format("Updating passive remotes %s", this.passiveVictims.keySet().toString()));
        for (SimVictim victim : this.passiveVictims.values()) {
            victim.update(stepSize);
        }
    }

}
