package com.seat.sim.common.remote.intent;

import com.seat.sim.common.json.JSONException;
import com.seat.sim.common.json.JSONObject;
import com.seat.sim.common.json.JSONObjectBuilder;
import com.seat.sim.common.json.JSONOptional;
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

    public SteerIntention(JSONOptional optional) throws JSONException {
        super(optional);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        super.decode(json);
        this.direction = (json.hasKey(SteerIntention.DIRECTION)) ?
            new Vector(json.getJSONOption(SteerIntention.DIRECTION)) :
            null;
    }

    @Override
    protected JSONObjectBuilder getJSONBuilder() throws JSONException {
        JSONObjectBuilder json = super.getJSONBuilder();
        if (this.hasDirection()) {
            json.put(SteerIntention.DIRECTION, this.direction.toJSON());
        }
        return json;
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

    public boolean equals(SteerIntention intent) {
        if (intent == null) return false;
        return super.equals(intent) &&
            ((this.hasDirection() && this.direction.equals(intent.direction)) || this.direction == intent.direction);
    }

}
