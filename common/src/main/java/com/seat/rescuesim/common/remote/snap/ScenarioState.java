package com.seat.rescuesim.common.remote.snap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;

import com.seat.rescuesim.common.json.*;
import com.seat.rescuesim.common.util.Debugger;

public class ScenarioState extends JSONAble {

    private HashMap<String, RemoteState> remotes;

    public ScenarioState(JSONArray json) {
        super(json);
    }

    public ScenarioState(JSONOption option) {
        super(option);
    }

    public ScenarioState(String encoding) {
        super(encoding);
    }

    @Override
    public void decode(JSONArray json) {
        this.remotes = new HashMap<>();
        for (int i = 0; i < json.length(); i++) {
            RemoteState state = new RemoteState(json.getJSONObject(i));
            this.remotes.put(state.getRemoteLabel(), state);
        }
    }

    public HashSet<String> getRemotes() {
        return new HashSet<String>(this.remotes.keySet());
    }

    public RemoteState getStateOfRemote(String remote) {
        if (!this.hasRemote(remote)) {
            Debugger.logger.err(String.format("No state for remote %s", remote));
            return null;
        }
        return this.remotes.get(remote);
    }

    public ArrayList<RemoteState> getStates() {
        return new ArrayList<RemoteState>(this.remotes.values());
    }

    public boolean hasRemote(String remote) {
        return this.remotes.containsKey(remote);
    }

    public boolean hasRemotes() {
        return !this.remotes.isEmpty();
    }

    public boolean hasStates() {
        return this.hasRemotes();
    }

    public JSONOption toJSON() {
        JSONArrayBuilder json = JSONBuilder.Array();
        for (RemoteState state : this.remotes.values()) {
            json.put(state.toJSON());
        }
        return json.toJSON();
    }

    public boolean equals(ScenarioState state) {
        return this.remotes.equals(state.remotes);
    }

}
