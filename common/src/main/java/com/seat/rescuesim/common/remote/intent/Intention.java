package com.seat.rescuesim.common.remote.intent;

import com.seat.rescuesim.common.json.JSONAble;
import com.seat.rescuesim.common.json.JSONBuilder;
import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.JSONObjectBuilder;
import com.seat.rescuesim.common.json.JSONOption;

public abstract class Intention extends JSONAble {
    static final String INTENTION_TYPE = "intention_type";

    protected IntentionType type;

    public Intention(IntentionType type) {
        this.type = type;
    }

    public Intention(JSONOption option) {
        super(option);
    }

    @Override
    protected void decode(JSONObject json) {
        this.type = IntentionType.values()[json.getInt(Intention.INTENTION_TYPE)];
    }

    public IntentionType getIntentionType() {
        return this.type;
    }

    public String getLabel() {
        return String.format("i%s", this.type.getLabel());
    }

    protected JSONObjectBuilder getJSONBuilder() {
        JSONObjectBuilder json = JSONBuilder.Object();
        json.put(Intention.INTENTION_TYPE, this.type.getType());
        return json;
    }

    public JSONOption toJSON() {
        return this.getJSONBuilder().toJSON();
    }

    public boolean equals(Intention intent) {
        return this.type.equals(intent.type);
    }

}
