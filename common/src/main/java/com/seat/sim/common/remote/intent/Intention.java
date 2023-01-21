package com.seat.sim.common.remote.intent;

import com.seat.sim.common.json.*;

public abstract class Intention extends Jsonable {

    private IntentionType intentType;

    public Intention(IntentionType intentType) {
        this.intentType = intentType;
    }

    public Intention(Json json) throws JsonException {
        super(json);
    }

    @Override
    protected void decode(JsonObject json) throws JsonException {
        this.intentType = IntentionType.decode(json);
    }

    protected JsonObjectBuilder getJsonBuilder() throws JsonException {
        JsonObjectBuilder json = JsonBuilder.Object();
        json.put(IntentionType.INTENTION_TYPE, this.intentType.getType());
        return json;
    }

    public boolean equals(Intention intent) {
        if (intent == null) return false;
        return this.intentType.equals(intent.intentType);
    }

    public IntentionType getIntentionType() {
        return this.intentType;
    }

    public String getLabel() {
        return String.format("i:%s", this.intentType.getLabel());
    }

    public Json toJson() throws JsonException {
        return this.getJsonBuilder().toJson();
    }
}
