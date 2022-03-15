package com.seat.rescuesim.common.remote;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import com.seat.rescuesim.common.json.*;
import com.seat.rescuesim.common.remote.intent.Intent;
import com.seat.rescuesim.common.remote.intent.Intention;
import com.seat.rescuesim.common.remote.intent.IntentionType;
import com.seat.rescuesim.common.util.Debugger;

/** A serializable class to assign intentions to a Remote. */
public abstract class RemoteController extends JSONAble {
    private static final String INTENTIONS = "intentions";
    private static final String REMOTE_ID = "remote_id";
    private static final String REMOTE_TYPE = "remote_type";

    protected HashMap<IntentionType, Intention> intentions;
    protected String remoteID;
    protected RemoteType type;

    public RemoteController(JSONObject json) {
        super(json);
    }

    public RemoteController(JSONOption option) {
        super(option);
    }

    public RemoteController(String encoding) {
        super(encoding);
    }

    public RemoteController(RemoteType type, String remoteID) {
        this(type, remoteID, new HashMap<IntentionType, Intention>());
    }

    public RemoteController(RemoteType type, String remoteID, ArrayList<Intention> intentions) {
        this(type, remoteID, new HashMap<IntentionType, Intention>());
        for (Intention intent : intentions) {
            this.addIntention(intent);
        }
    }

    public RemoteController(RemoteType type, String remoteID, HashMap<IntentionType, Intention> intentions) {
        this.type = type;
        this.remoteID = remoteID;
        this.intentions = intentions;
    }

    @Override
    protected void decode(JSONObject json) {
        this.type = RemoteType.values()[json.getInt(RemoteController.REMOTE_TYPE)];
        this.remoteID = json.getString(RemoteController.REMOTE_ID);
        this.intentions = new HashMap<>();
        if (json.hasKey(RemoteController.INTENTIONS)) {
            JSONArray jsonIntentions = json.getJSONArray(RemoteController.INTENTIONS);
            for (int i = 0; i < jsonIntentions.length(); i++) {
                this.addIntention(Intent.Some(jsonIntentions.getJSONObject(i)));
            }
        }
    }

    public boolean addIntention(Intention intent) {
        if (this.hasIntentionWithType(intent.getIntentionType())) {
            Debugger.logger.err(String.format("Remote %s already has intention %s", this.remoteID, intent.getLabel()));
            return this.hasIntention(intent);
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
        return String.format("%s%s", this.remoteID, this.type.getLabel());
    }

    public String getRemoteID() {
        return this.remoteID;
    }

    public RemoteType getRemoteType() {
        return this.type;
    }

    public boolean hasIntention(Intention intent) {
        return this.hasIntentionWithType(intent.getIntentionType()) &&
            this.intentions.get(intent.getIntentionType()).equals(intent);
    }

    public boolean hasIntentions() {
        return !this.intentions.isEmpty();
    }

    public boolean hasIntentionWithType(IntentionType type) {
        return this.intentions.containsKey(type);
    }

    public boolean removeIntention(Intention intent) {
        if (!this.hasIntention(intent)) {
            Debugger.logger.warn(String.format("Remote %s has no intention for %s", this.remoteID, type.getLabel()));
            return true;
        }
        this.intentions.remove(intent.getIntentionType());
        return true;
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
        json.put(RemoteController.REMOTE_TYPE, this.type.getType());
        json.put(RemoteController.REMOTE_ID, this.remoteID);
        if (this.hasIntentions()) {
            JSONArrayBuilder jsonIntentions = JSONBuilder.Array();
            for (Intention intent : this.intentions.values()) {
                jsonIntentions.put(intent.toJSON());
            }
            json.put(RemoteController.INTENTIONS, jsonIntentions.toJSON());
        }
        return json.toJSON();
    }

    public boolean equals(RemoteController remote) {
        return this.type.equals(remote.type) && this.remoteID.equals(remote.remoteID);
    }

}
