package com.seat.rescuesim.simserver;

import com.seat.rescuesim.common.ScenarioConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Scenario {
    private static final String SCENARIO_CONFIG = "scenario_config";
    private static final String SCENARIO_REMOTES = "remotes";
    private static final String SCENARIO_TIME = "time";
    private static final String SCENARIO_VICTIMS = "victims";

    private ScenarioConfig config;
    private Log history;
    private double missionTime;
    private DroneRemote[] remotes;
    private Victim[] victims;

    public static Scenario decode(JSONObject json) throws JSONException {
        ScenarioConfig config = ScenarioConfig.decode(json.getJSONObject(Scenario.SCENARIO_CONFIG));
        double missionTime = json.getDouble(Scenario.SCENARIO_TIME);
        JSONArray jsonRemotes = json.getJSONArray(Scenario.SCENARIO_REMOTES);
        DroneRemote[] remotes = new DroneRemote[jsonRemotes.length()];
        for (int i = 0; i < remotes.length; i++) {
            remotes[i] = DroneRemote.decode(jsonRemotes.getJSONObject(i), config.getDroneSpecification());
        }
        JSONArray jsonVictims = json.getJSONArray(Scenario.SCENARIO_VICTIMS);
        Victim[] victims = new Victim[jsonVictims.length()];
        for (int i = 0; i < victims.length; i++) {
            victims[i] = Victim.decode(jsonVictims.getJSONObject(i), config.getVictimSpecification());
        }
        return new Scenario(config, missionTime, remotes, victims);
    }

    public static Scenario decode(String encoding) throws JSONException {
        return Scenario.decode(new JSONObject(encoding));
    }

    public static Scenario decodeConfig(JSONObject json) throws JSONException {
        return new Scenario(ScenarioConfig.decode(json));
    }

    public static Scenario decodeConfig(String encoding) throws JSONException {
        return Scenario.decodeConfig(new JSONObject(encoding));
    }

    public Scenario(ScenarioConfig config) {
        this.history = new Log();
        this.config = config;
        this.missionTime = 0.0;
        this.remotes = new DroneRemote[this.config.getNumberOfDrones()];
        this.victims = new Victim[this.config.getNumberOfVictims()];
        this.initRemotes();
        this.initVictims();
    }

    public Scenario(ScenarioConfig config, double missionTime, DroneRemote[] remotes, Victim[] victims) {
        this.history = new Log();
        this.config = config;
        this.missionTime = missionTime;
        this.remotes = remotes;
        this.victims = victims;
    }

    private void initRemotes() {
        for (int i = 0; i < this.remotes.length; i++) {
            this.remotes[i] = new DroneRemote(this.config.getDroneSpecification());
        }
    }

    private void initVictims() {
        for (int i = 0; i < this.victims.length; i++) {
            this.victims[i] = new Victim(this.config.getVictimSpecification());
        }
    }

    public ScenarioConfig getConfig() {
        return this.config;
    }

    public Log getHistory() {
        return this.history;
    }

    public double getMissionTime() {
        return this.missionTime;
    }

    public DroneRemote[] getRemotes() {
        return this.remotes;
    }

    public DroneRemote getRemote(int idx) {
        if (idx < 0 || this.remotes.length <= idx) {
            return null;
        }
        return this.remotes[idx];
    }

    public Victim[] getVictims() {
        return this.victims;
    }

    public Victim getVictim(int idx) {
        if (idx < 0 || this.remotes.length <= idx) {
            return null;
        }
        return this.victims[idx];
    }

    public String encode() {
        return this.toJSON().toString();
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put(Scenario.SCENARIO_CONFIG, this.config.toJSON());
        json.put(Scenario.SCENARIO_TIME, this.missionTime);
        JSONArray remotes = new JSONArray();
        for (int i = 0; i < this.remotes.length; i++) {
            remotes.put(this.remotes[i].toJSON());
        }
        json.put(Scenario.SCENARIO_REMOTES, remotes);
        JSONArray victims = new JSONArray();
        for (int i = 0; i < this.victims.length; i++) {
            victims.put(this.victims[i].toJSON());
        }
        return json;
    }

    public String toString() {
        return this.encode();
    }

}
