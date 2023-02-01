package com.seat.sim.common.remote.intent;

import com.seat.sim.common.json.*;
import com.seat.sim.common.math.Vector;

public class MoveIntention extends Intention {
    private static final String ACCELERATION = "acceleration";

    private Vector acceleration;

    public MoveIntention() {
        this(new Vector());
    }

    public MoveIntention(Vector acceleration) {
        super(IntentionType.MOVE);
        this.acceleration = (acceleration != null) ? acceleration : new Vector();
        if (!Double.isFinite(this.acceleration.getMagnitude())) {
            throw new RuntimeException(
                    String.format("cannot Move remote with a non-finite force `%s`", this.acceleration.toString()));
        }
    }

    public MoveIntention(Json json) throws JsonException {
        super(json);
    }

    @Override
    protected void decode(JsonObject json) throws JsonException {
        super.decode(json);
        this.acceleration = (json.hasKey(MoveIntention.ACCELERATION))
                ? new Vector(json.getJson(MoveIntention.ACCELERATION))
                : new Vector();
    }

    @Override
    protected JsonObjectBuilder getJsonBuilder() throws JsonException {
        JsonObjectBuilder json = super.getJsonBuilder();
        if (this.hasAcceleration()) {
            json.put(MoveIntention.ACCELERATION, this.acceleration.toJson());
        }
        return json;
    }

    public boolean equals(MoveIntention intent) {
        if (intent == null)
            return false;
        return super.equals(intent) && this.acceleration.equals(intent.acceleration);
    }

    public Vector getAcceleration() {
        return this.acceleration;
    }

    @Override
    public String getLabel() {
        return "<MOVE>";
    }

    public boolean hasAcceleration() {
        return Double.isFinite(this.acceleration.getMagnitude()) && this.acceleration.getMagnitude() > 0;
    }
}
