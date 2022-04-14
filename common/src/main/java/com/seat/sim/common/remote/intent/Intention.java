package com.seat.sim.common.remote.intent;

import com.seat.sim.common.json.JSONAble;
import com.seat.sim.common.json.JSONBuilder;
import com.seat.sim.common.json.JSONException;
import com.seat.sim.common.json.JSONObject;
import com.seat.sim.common.json.JSONObjectBuilder;
import com.seat.sim.common.json.JSONOptional;

public abstract class Intention extends JSONAble {

    private IntentionType intentType;

    public Intention(IntentionType intentType) {
        this.intentType = intentType;
    }

    public Intention(JSONOptional optional) throws JSONException {
        super(optional);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        this.intentType = IntentionType.decodeType(json);
    }

    protected JSONObjectBuilder getJSONBuilder() throws JSONException {
        JSONObjectBuilder json = JSONBuilder.Object();
        json.put(IntentionType.INTENTION_TYPE, this.intentType.getType());
        return json;
    }

    public IntentionType getIntentionType() {
        return this.intentType;
    }

    public String getLabel() {
        return String.format("i:%s", this.intentType.getLabel());
    }

    public JSONOptional toJSON() throws JSONException {
        return this.getJSONBuilder().toJSON();
    }

    public boolean equals(Intention intent) {
        if (intent == null) return false;
        return this.intentType.equals(intent.intentType);
    }

}
