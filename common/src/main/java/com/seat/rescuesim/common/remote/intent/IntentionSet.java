package com.seat.rescuesim.common.remote.intent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import com.seat.rescuesim.common.core.CommonException;
import com.seat.rescuesim.common.json.JSONAble;
import com.seat.rescuesim.common.json.JSONArray;
import com.seat.rescuesim.common.json.JSONArrayBuilder;
import com.seat.rescuesim.common.json.JSONBuilder;
import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.JSONObjectBuilder;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.remote.RemoteState;
import com.seat.rescuesim.common.util.Debugger;

/** A serializable class to assign intentions to a Remote. */
public class IntentionSet extends JSONAble {
    public static final String INTENTIONS = "intentions";

    private HashMap<IntentionType, Intention> intentions;
    private String remoteID;

    public IntentionSet(String remoteID) {
        this.remoteID = remoteID;
        this.intentions = new HashMap<>();
    }

    public IntentionSet(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        this.remoteID = json.getString(RemoteState.REMOTE_ID);
        this.intentions = new HashMap<>();
        if (json.hasKey(IntentionSet.INTENTIONS)) {
            JSONArray jsonIntentions = json.getJSONArray(IntentionSet.INTENTIONS);
            for (int i = 0; i < jsonIntentions.length(); i++) {
                this.addIntention(IntentRegistry.Some(jsonIntentions.getJSONOption(i)));
            }
        }
    }

    public boolean addIntention(Intention intent) throws CommonException {
        if (this.hasIntention(intent)) {
            return true;
        }
        if (this.hasIntentionWithType(intent.getIntentionType())) {
            throw new CommonException(String.format("Remote %s already has intention %s", this.remoteID,
                intent.getLabel()));
        }
        this.intentions.put(intent.getIntentionType(), intent);
        return true;
    }

    public Collection<Intention> getIntentions() {
        return new ArrayList<Intention>(this.intentions.values());
    }

    public Collection<IntentionType> getIntentionTypes() {
        return new HashSet<IntentionType>(this.intentions.keySet());
    }

    public Intention getIntentionWithType(IntentionType intentionType) throws CommonException {
        if (!this.hasIntentionWithType(intentionType)) {
            throw new CommonException(String.format("Remote %s has no intention for %s", this.remoteID,
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

    public JSONOption toJSON() throws JSONException {
        JSONObjectBuilder json = JSONBuilder.Object();
        json.put(RemoteState.REMOTE_ID, this.remoteID);
        if (this.hasIntentions()) {
            JSONArrayBuilder jsonIntentions = JSONBuilder.Array();
            for (Intention intent : this.intentions.values()) {
                jsonIntentions.put(intent.toJSON());
            }
            json.put(IntentionSet.INTENTIONS, jsonIntentions.toJSON());
        }
        return json.toJSON();
    }

    public boolean equals(IntentionSet remote) {
        if (remote == null) return false;
        return this.remoteID.equals(remote.remoteID);
    }

}
