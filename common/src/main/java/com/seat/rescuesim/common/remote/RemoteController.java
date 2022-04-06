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
    public static final String INTENTIONS = "intentions";

    private HashMap<IntentionType, Intention> intentions;
    private String remoteID;

    public RemoteController(String remoteID) {
        this.remoteID = remoteID;
        this.intentions = new HashMap<>();
    }

    public RemoteController(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        this.remoteID = json.getString(RemoteState.REMOTE_ID);
        this.intentions = new HashMap<>();
        if (json.hasKey(RemoteController.INTENTIONS)) {
            JSONArray jsonIntentions = json.getJSONArray(RemoteController.INTENTIONS);
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

    public Intention getIntentionWithType(IntentionType intentionType) throws CoreException {
        if (!this.hasIntentionWithType(intentionType)) {
            throw new CoreException(String.format("Remote %s has no intention for %s", this.remoteID,
                intentionType.getLabel()));
        }
        return this.intentions.get(intentionType);
    }

    public String getLabel() {
        return String.format("[%s]", this.remoteID);
    }

    public String getRemoteID() {
        return this.remoteID;
    }

    public boolean hasIntention(Intention intent) {
        return this.hasIntentionWithType(intent.getIntentionType()) &&
            this.intentions.get(intent.getIntentionType()).equals(intent);
    }

    public boolean hasIntentions() {
        return !this.intentions.isEmpty();
    }

    public boolean hasIntentionWithType(IntentionType intentionType) {
        return this.intentions.containsKey(intentionType);
    }

    public boolean removeIntention(Intention intent) {
        if (!this.hasIntention(intent)) {
            Debugger.logger.warn(String.format("Remote %s has no intention for %s", this.remoteID, intent.getLabel()));
            return true;
        }
        this.intentions.remove(intent.getIntentionType());
        return true;
    }

    public boolean removeIntentionWithType(IntentionType intentionType) {
        if (!this.hasIntentionWithType(intentionType)) {
            Debugger.logger.warn(String.format("Remote %s has no intention for %s", this.remoteID,
                intentionType.getLabel()));
            return true;
        }
        this.intentions.remove(intentionType);
        return true;
    }

    public JSONOption toJSON() {
        JSONObjectBuilder json = JSONBuilder.Object();
        json.put(RemoteState.REMOTE_ID, this.remoteID);
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
        return this.remoteID.equals(remote.remoteID);
    }

}
