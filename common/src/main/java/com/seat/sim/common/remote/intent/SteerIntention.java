package com.seat.sim.common.remote.intent;

import com.seat.sim.common.json.*;
import com.seat.sim.common.math.Vector;

public class SteerIntention extends Intention {
    private static final String DIRECTION = "direction";

    private Vector direction;

    public SteerIntention() {
        super(IntentionType.STEER);
        this.direction = null;
    }

    public SteerIntention(Vector direction) {
        super(IntentionType.STEER);
        this.direction = direction;
    }

    public SteerIntention(Json json) throws JsonException {
        super(json);
    }

    @Override
    protected void decode(JsonObject json) throws JsonException {
        super.decode(json);
        this.direction = (json.hasKey(SteerIntention.DIRECTION)) ?
            new Vector(json.getJson(SteerIntention.DIRECTION)) :
            null;
    }

    @Override
    protected JsonObjectBuilder getJsonBuilder() throws JsonException {
        JsonObjectBuilder json = super.getJsonBuilder();
        if (this.hasDirection()) {
            json.put(SteerIntention.DIRECTION, this.direction.toJson());
        }
        return json;
    }

    public boolean equals(SteerIntention intent) {
        if (intent == null) return false;
        return super.equals(intent) &&
            ((this.hasDirection() && this.direction.equals(intent.direction)) || this.direction == intent.direction);
    }

    public Vector getDirection() {
        return this.direction;
    }

    @Override
    public String getLabel() {
        return "<STEER>";
    }

    public boolean hasDirection() {
        return this.direction != null;
    }
}
