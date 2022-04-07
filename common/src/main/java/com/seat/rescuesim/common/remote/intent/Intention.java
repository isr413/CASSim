package com.seat.rescuesim.common.remote.intent;

import com.seat.rescuesim.common.json.JSONAble;
import com.seat.rescuesim.common.json.JSONBuilder;
import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.JSONObjectBuilder;
import com.seat.rescuesim.common.json.JSONOption;

public abstract class Intention extends JSONAble {

    private IntentionType intentType;

    public Intention(IntentionType intentType) {
        this.intentType = intentType;
    }

    public Intention(JSONOption option) throws JSONException {
        super(option);
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

    public JSONOption toJSON() throws JSONException {
        return this.getJSONBuilder().toJSON();
    }

    public boolean equals(Intention intent) {
        if (intent == null) return false;
        return this.intentType.equals(intent.intentType);
    }

}
