package com.seat.rescuesim.common.remote;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import com.seat.rescuesim.common.json.JSONAble;
import com.seat.rescuesim.common.json.JSONArray;
import com.seat.rescuesim.common.json.JSONArrayBuilder;
import com.seat.rescuesim.common.json.JSONBuilder;
import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.JSONObjectBuilder;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.remote.intent.Intent;
import com.seat.rescuesim.common.remote.intent.Intention;
import com.seat.rescuesim.common.remote.intent.IntentionType;
import com.seat.rescuesim.common.util.CoreException;
import com.seat.rescuesim.common.util.Debugger;

/** A serializable class to assign intentions to a Remote. */
public class RemoteController extends JSONAble {

    protected HashMap<IntentionType, Intention> intentions;
    protected String remoteID;
    protected RemoteType type;

    public RemoteController(RemoteType type, String remoteID) {
        this.type = type;
        this.remoteID = remoteID;
        this.intentions = new HashMap<>();
    }

    public RemoteController(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        this.type = RemoteType.values()[json.getInt(RemoteConst.REMOTE_TYPE)];
        this.remoteID = json.getString(RemoteConst.REMOTE_ID);
        this.intentions = new HashMap<>();
        if (json.hasKey(RemoteConst.INTENTIONS)) {
            JSONArray jsonIntentions = json.getJSONArray(RemoteConst.INTENTIONS);
            for (int i = 0; i < jsonIntentions.length(); i++) {
                this.addIntention(Intent.Some(jsonIntentions.getJSONOption(i)));
            }
        }
    }

    public boolean addIntention(Intention intent) throws CoreException {
        if (this.hasIntention(intent)) {
            return true;
        }
        if (this.hasIntentionWithType(intent.getIntentionType())) {
            throw new CoreException(String.format("Remote %s already has intention %s", this.remoteID,
                intent.getLabel()));
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

    public Intention getIntentionWithType(IntentionType type) throws CoreException {
        if (!this.hasIntentionWithType(type)) {
            throw new CoreException(String.format("Remote %s has no intention for %s", this.remoteID, type.getLabel()));
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
        json.put(RemoteConst.REMOTE_TYPE, this.type.getType());
        json.put(RemoteConst.REMOTE_ID, this.remoteID);
        if (this.hasIntentions()) {
            JSONArrayBuilder jsonIntentions = JSONBuilder.Array();
            for (Intention intent : this.intentions.values()) {
                jsonIntentions.put(intent.toJSON());
            }
            json.put(RemoteConst.INTENTIONS, jsonIntentions.toJSON());
        }
        return json.toJSON();
    }

    public boolean equals(RemoteController remote) {
        return this.type.equals(remote.type) && this.remoteID.equals(remote.remoteID);
    }

}
