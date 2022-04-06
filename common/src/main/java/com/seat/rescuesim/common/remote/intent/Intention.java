package com.seat.rescuesim.common.remote.intent;

import com.seat.rescuesim.common.json.JSONAble;
import com.seat.rescuesim.common.json.JSONBuilder;
import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.JSONObjectBuilder;
import com.seat.rescuesim.common.json.JSONOption;

public abstract class Intention extends JSONAble {

    private IntentionType intentType;

    public Intention(IntentionType intentType) {
        this.intentType = intentType;
    }

    public Intention(JSONOption option) {
        super(option);
    }

    @Override
    protected void decode(JSONObject json) {
        this.intentType = IntentionType.decodeType(json);
    }

    public IntentionType getIntentionType() {
        return this.intentType;
    }

    public String getLabel() {
        return String.format("i:%s", this.intentType.getLabel());
    }

    protected JSONObjectBuilder getJSONBuilder() {
        JSONObjectBuilder json = JSONBuilder.Object();
        json.put(IntentionType.INTENTION_TYPE, this.intentType.getType());
        return json;
    }

    public JSONOption toJSON() {
        return this.getJSONBuilder().toJSON();
    }

    public boolean equals(Intention intent) {
        return this.intentType.equals(intent.intentType);
    }

}
