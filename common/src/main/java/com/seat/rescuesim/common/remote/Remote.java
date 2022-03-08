package com.seat.rescuesim.common.remote;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import com.seat.rescuesim.common.json.*;
import com.seat.rescuesim.common.remote.intent.Intent;
import com.seat.rescuesim.common.remote.intent.Intention;
import com.seat.rescuesim.common.remote.intent.IntentionType;
import com.seat.rescuesim.common.util.Debugger;

public class Remote extends JSONAble {
    private static final String INTENTIONS = "intentions";
    private static final String REMOTE_ID = "remote";
    private static final String REMOTE_TYPE = "remote_type";

    private HashMap<IntentionType, Intention> intentions;
    private String remote;
    private RemoteType type;

    public static Remote Base(String remote) {
        return new Remote(RemoteType.BASE, remote);
    }

    public static Remote Drone(String remote) {
        return new Remote(RemoteType.DRONE, remote);
    }

    public static Remote None(String remote) {
        return new Remote(RemoteType.NONE, remote);
    }

    public static Remote Sensor(String remote) {
        return new Remote(RemoteType.SENSOR, remote);
    }

    public static Remote Victim(String remote) {
        return new Remote(RemoteType.VICTIM, remote);
    }

    public Remote(JSONObject json) {
        super(json);
    }

    public Remote(JSONOption option) {
        super(option);
    }

    public Remote(String encoding) {
        super(encoding);
    }

    public Remote(RemoteType type, String remote) {
        this.type = type;
        this.remote = remote;
        this.intentions = new HashMap<>();
    }

    @Override
    protected void decode(JSONObject json) {
        this.type = RemoteType.values()[json.getInt(Remote.REMOTE_ID)];
        this.remote = json.getString(Remote.REMOTE_ID);
        this.intentions = new HashMap<>();
        JSONArray jsonIntentions = json.getJSONArray(Remote.INTENTIONS);
        for (int i = 0; i < jsonIntentions.length(); i++) {
            this.addIntention(Intent.Some(jsonIntentions.getJSONObject(i)));
        }
    }

    public boolean addIntention(Intention intent) {
        if (this.hasIntention(intent)) {
            Debugger.logger.err(String.format("Remote %s already has intention %s", this.remote, intent.getLabel()));
            return false;
        }
        this.intentions.put(intent.getType(), intent);
        return true;
    }

    public ArrayList<Intention> getIntentions() {
        return new ArrayList<Intention>(this.intentions.values());
    }

    public HashSet<IntentionType> getIntentionTypes() {
        return new HashSet<IntentionType>(this.intentions.keySet());
    }

    public Intention getIntentionWithType(IntentionType type) {
        if (!this.hasIntentionWithType(type)) {
            Debugger.logger.err(String.format("Remote %s has no intention for %s", this.remote, type.getLabel()));
            return null;
        }
        return this.intentions.get(type);
    }

    public String getLabel() {
        return String.format("%s%s", this.remote, this.type.getLabel());
    }

    public String getRemoteID() {
        return this.remote;
    }

    public RemoteType getType() {
        return this.type;
    }

    public boolean hasIntention(Intention intent) {
        return this.hasIntentionWithType(intent.getType());
    }

    public boolean hasIntentions() {
        return !this.intentions.isEmpty();
    }

    public boolean hasIntentionWithType(IntentionType type) {
        return this.intentions.containsKey(type);
    }

    public boolean removeIntention(Intention intent) {
        return this.removeIntentionWithType(intent.getType());
    }

    public boolean removeIntentionWithType(IntentionType type) {
        if (!this.hasIntentionWithType(type)) {
            Debugger.logger.warn(String.format("Remote %s has no intention for %s", this.remote, type.getLabel()));
            return true;
        }
        this.intentions.remove(type);
        return true;
    }

    public JSONOption toJSON() {
        JSONObjectBuilder json = JSONBuilder.Object();
        json.put(Remote.REMOTE_TYPE, this.type.getType());
        json.put(Remote.REMOTE_ID, this.remote);
        JSONArrayBuilder jsonIntentions = JSONBuilder.Array();
        for (Intention intent : this.intentions.values()) {
            jsonIntentions.put(intent.toJSON());
        }
        json.put(Remote.INTENTIONS, jsonIntentions.toJSON());
        return json.toJSON();
    }

    public boolean equals(Remote remote) {
        return this.type.equals(remote.type) && this.remote.equals(remote.remote);
    }

}
