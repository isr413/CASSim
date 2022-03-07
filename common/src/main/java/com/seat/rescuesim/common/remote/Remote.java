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
    private static final String REMOTE_ID = "remote_id";
    private static final String REMOTE_TYPE = "remote_type";

    private HashMap<IntentionType, Intention> intentions;
    private String remoteID;
    private RemoteType type;

    public static Remote Drone(String remoteID) {
        return new Remote(RemoteType.DRONE, remoteID);
    }

    public static Remote None(String remoteID) {
        return new Remote(RemoteType.NONE, remoteID);
    }

    public static Remote Sensor(String remoteID) {
        return new Remote(RemoteType.SENSOR, remoteID);
    }

    public static Remote Victim(String remoteID) {
        return new Remote(RemoteType.VICTIM, remoteID);
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

    public Remote(RemoteType type, String remoteID) {
        this.type = type;
        this.remoteID = remoteID;
        this.intentions = new HashMap<>();
    }

    @Override
    protected void decode(JSONObject json) {
        this.type = RemoteType.values()[json.getInt(Remote.REMOTE_ID)];
        this.remoteID = json.getString(Remote.REMOTE_ID);
        this.intentions = new HashMap<>();
        JSONArray jsonIntentions = json.getJSONArray(Remote.INTENTIONS);
        for (int i = 0; i < jsonIntentions.length(); i++) {
            this.addIntention(Intent.Some(jsonIntentions.getJSONObject(i)));
        }
    }

    public boolean addIntention(Intention intent) {
        if (this.hasIntention(intent)) {
            Debugger.logger.err(String.format("Remote %s already has intention %s", this.remoteID, intent.getLabel()));
            return false;
        }
        this.intentions.put(intent.getIntentionType(), intent);
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
            Debugger.logger.err(String.format("Remote %s has no intention for %s", this.remoteID, type.getLabel()));
            return null;
        }
        return this.intentions.get(type);
    }

    public String getLabel() {
        return String.format("<%d>", this.remoteID);
    }

    public String getRemoteID() {
        return this.remoteID;
    }

    public RemoteType getRemoteType() {
        return this.type;
    }

    public boolean hasIntention(Intention intent) {
        return this.hasIntentionWithType(intent.getIntentionType());
    }

    public boolean hasIntentions() {
        return !this.intentions.isEmpty();
    }

    public boolean hasIntentionWithType(IntentionType type) {
        return this.intentions.containsKey(type);
    }

    public boolean removeIntention(Intention intent) {
        return this.removeIntentionWithType(intent.getIntentionType());
    }

    public boolean removeIntentionWithType(IntentionType type) {
        if (!this.hasIntentionWithType(type)) {
            Debugger.logger.warn(String.format("Remote %s has no intention for %s", this.remoteID, type.getLabel()));
            return true;
        }
        this.intentions.remove(type);
        return true;
    }

    public JSONOption toJSON() {
        JSONObjectBuilder json = JSONBuilder.Object();
        json.put(Remote.REMOTE_TYPE, this.type.getType());
        json.put(Remote.REMOTE_ID, this.remoteID);
        JSONArrayBuilder jsonIntentions = JSONBuilder.Array();
        for (Intention intent : this.intentions.values()) {
            jsonIntentions.put(intent.toJSON());
        }
        json.put(Remote.INTENTIONS, jsonIntentions.toJSON());
        return json.toJSON();
    }

    public boolean equals(Remote remote) {
        return this.type.equals(remote.type) && this.remoteID.equals(remote.remoteID);
    }

}
