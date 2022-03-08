package com.seat.rescuesim.common.remote.intent;

import com.seat.rescuesim.common.json.*;
import com.seat.rescuesim.common.util.Debugger;

public abstract class Intention extends JSONAble {
    private static final String INTENTION_TYPE = "intent_type";

    public static IntentionType decodeType(JSONObject json) {
        return IntentionType.values()[json.getInt(Intention.INTENTION_TYPE)];
    }

    public static IntentionType decodeType(JSONOption option) {
        if (option.isSomeObject()) {
            return Intention.decodeType(option.someObject());
        }
        Debugger.logger.err(String.format("Cannot decode intention type of %s", option.toString()));
        return null;
    }

    public static IntentionType decodeType(String encoding) {
        return Intention.decodeType(JSONOption.String(encoding));
    }

    protected IntentionType type;

    public Intention(JSONObject json) {
        super(json);
    }

    public Intention(JSONOption option) {
        super(option);
    }

    public Intention(String encoding) {
        super(encoding);
    }

    public Intention(IntentionType type) {
        this.type = type;
    }

    @Override
    protected void decode(JSONObject json) {
        this.type = IntentionType.values()[json.getInt(Intention.INTENTION_TYPE)];
    }

    public String getLabel() {
        return this.type.getLabel();
    }

    public IntentionType getType() {
        return this.type;
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
