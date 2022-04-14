package com.seat.sim.common.remote.intent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Collectors;

import com.seat.sim.common.core.CommonException;
import com.seat.sim.common.json.JSONAble;
import com.seat.sim.common.json.JSONBuilder;
import com.seat.sim.common.json.JSONException;
import com.seat.sim.common.json.JSONObject;
import com.seat.sim.common.json.JSONObjectBuilder;
import com.seat.sim.common.json.JSONOptional;
import com.seat.sim.common.remote.RemoteState;
import com.seat.sim.common.util.Debugger;

/** A serializable class to assign intentions to a Remote. */
public class IntentionSet extends JSONAble {
    public static final String INTENTIONS = "intentions";

    private HashMap<IntentionType, Intention> intentions;
    private String remoteID;

    public IntentionSet(String remoteID) {
        this.remoteID = remoteID;
        this.intentions = new HashMap<>();
    }

    public IntentionSet(JSONOptional optional) throws JSONException {
        super(optional);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        this.remoteID = json.getString(RemoteState.REMOTE_ID);
        this.intentions = new HashMap<>();
        if (json.hasKey(IntentionSet.INTENTIONS)) {
            json.getJSONArray(IntentionSet.INTENTIONS).toList(JSONOptional.class).stream()
                .map(optional -> IntentRegistry.From(optional))
                .forEach(intent -> this.addIntention(intent));
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

    public JSONOptional toJSON() throws JSONException {
        JSONObjectBuilder json = JSONBuilder.Object();
        json.put(RemoteState.REMOTE_ID, this.remoteID);
        if (this.hasIntentions()) {
            json.put(
                IntentionSet.INTENTIONS,
                JSONBuilder.Array(
                    this.intentions.values().stream()
                        .map(intent -> intent.toJSON())
                        .collect(Collectors.toList())
                ).toJSON()
            );
        }
        return json.toJSON();
    }

    public boolean equals(IntentionSet remote) {
        if (remote == null) return false;
        return this.remoteID.equals(remote.remoteID);
    }

}
