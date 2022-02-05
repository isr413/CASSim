package com.seat.rescuesim.simserver;

import java.util.ArrayList;
import java.util.HashMap;

import com.seat.rescuesim.common.Debugger;
import com.seat.rescuesim.common.ScenarioConfig;
import com.seat.rescuesim.common.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Scenario {
    private static final String SCENARIO_CONFIG = "config";
    private static final String SCENARIO_REMOTES = "remotes";
    private static final String SCENARIO_TIME = "time";
    private static final String SCENARIO_VICTIMS = "victims";

    private ScenarioConfig config;
    private double missionTime;
    private HashMap<Integer, DroneRemote> remotes;
    private HashMap<Integer, Victim> victims;

    public static Scenario decode(JSONObject json) throws JSONException {
        ScenarioConfig config = ScenarioConfig.decode(json.getJSONObject(Scenario.SCENARIO_CONFIG));
        double missionTime = json.getDouble(Scenario.SCENARIO_TIME);
        JSONArray jsonRemotes = json.getJSONArray(Scenario.SCENARIO_REMOTES);
        ArrayList<DroneRemote> remotes = new ArrayList<>();
        for (int i = 0; i < jsonRemotes.length(); i++) {
            remotes.add(DroneRemote.decode(config.getDroneSpecification(), jsonRemotes.getJSONObject(i)));
        }
        JSONArray jsonVictims = json.getJSONArray(Scenario.SCENARIO_VICTIMS);
        ArrayList<Victim> victims = new ArrayList<>();
        for (int i = 0; i < jsonVictims.length(); i++) {
            victims.add(Victim.decode(config.getVictimSpecification(), jsonVictims.getJSONObject(i)));
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
        this(config, 0, new HashMap<Integer, DroneRemote>(), new HashMap<Integer, Victim>());
        this.initRemotes();
        this.initVictims();
    }

    public Scenario(ScenarioConfig config, double missionTime, ArrayList<DroneRemote> remotes,
            ArrayList<Victim> victims) {
        this(config, missionTime, new HashMap<Integer, DroneRemote>(), new HashMap<Integer, Victim>());
        for (DroneRemote remote : remotes) {
            if (!this.hasRemoteWithID(remote.getRemoteID())) {
                this.remotes.put(remote.getRemoteID(), remote);
            } else {
                Debugger.logger.err(String.format("Scenario should not include duplicate remotes with label %s",
                    remote.getLabel()));
            }
        }
        for (Victim victim : victims) {
            if (!this.hasVictimWithID(victim.getVictimID())) {
                this.victims.put(victim.getVictimID(), victim);
            } else {
                Debugger.logger.err(String.format("Scenario should not include duplicate remotes with label %s",
                    victim.getLabel()));
            }
        }
    }

    public Scenario(ScenarioConfig config, double missionTime, HashMap<Integer, DroneRemote> remotes,
            HashMap<Integer, Victim> victims) {
        this.config = config;
        this.missionTime = missionTime;
        this.remotes = remotes;
        this.victims = victims;
    }

    private void initRemotes() {
        for (int i = 0; i < this.config.getNumberOfDrones(); i++) {
            DroneRemote remote = new DroneRemote(this.config.getDroneSpecification());
            this.remotes.put(remote.getRemoteID(), remote);
        }
    }

    private void initVictims() {
        for (int i = 0; i < this.config.getNumberOfVictims(); i++) {
            Vector randomLocation = this.config.getBase().getLocation();
            while (this.config.getMap().getZoneAtLocation(randomLocation).equals(
                    this.config.getMap().getZoneAtLocation(this.config.getBase().getLocation()))) {
                randomLocation = new Vector(
                    Math.random() * this.config.getMap().getWidth(),
                    Math.random() * this.config.getMap().getHeight(),
                    0
                );
            }
            Victim victim = new Victim(this.config.getVictimSpecification(), randomLocation);
            this.victims.put(victim.getVictimID(), victim);
        }
    }

    public ScenarioConfig getConfig() {
        return this.config;
    }

    public double getMissionTime() {
        return this.missionTime;
    }

    public ArrayList<DroneRemote> getRemotes() {
        return new ArrayList<DroneRemote>(this.remotes.values());
    }

    public DroneRemote getRemoteWithID(int remoteID) {
        if (!this.hasRemoteWithID(remoteID)) {
            Debugger.logger.err(String.format("Scenario does not include a remote with id %d", remoteID));
            return null;
        }
        return this.remotes.get(remoteID);
    }

    public ArrayList<Victim> getVictims() {
        return new ArrayList<Victim>(this.victims.values());
    }

    public Victim getVictimWithID(int victimID) {
        if (!this.hasVictimWithID(victimID)) {
            Debugger.logger.err(String.format("Scenario does not include a victim with id %d", victimID));
            return null;
        }
        return this.victims.get(victimID);
    }

    public boolean hasRemoteWithID(int remoteID) {
        return this.remotes.containsKey(remoteID);
    }

    public boolean hasVictimWithID(int victimID) {
        return this.victims.containsKey(victimID);
    }

    public String encode() {
        return this.toJSON().toString();
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put(Scenario.SCENARIO_CONFIG, this.config.toJSON());
        json.put(Scenario.SCENARIO_TIME, this.missionTime);
        JSONArray jsonRemotes = new JSONArray();
        for (DroneRemote remote : this.remotes.values()) {
            jsonRemotes.put(remote.toJSON());
        }
        json.put(Scenario.SCENARIO_REMOTES, jsonRemotes);
        JSONArray jsonVictims = new JSONArray();
        for (Victim victim : this.victims.values()) {
            jsonVictims.put(victim.toJSON());
        }
        json.put(Scenario.SCENARIO_VICTIMS, jsonVictims);
        return json;
    }

    public String toString() {
        return this.encode();
    }

}
