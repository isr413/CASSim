package com.seat.sim.common.remote.intent;

import com.seat.sim.common.json.JSONException;
import com.seat.sim.common.json.JSONObject;
import com.seat.sim.common.json.JSONObjectBuilder;
import com.seat.sim.common.json.JSONOptional;
import com.seat.sim.common.math.Vector;

public class MoveIntention extends Intention {
    private static final String ACCELERATION = "acceleration";

    private Vector acceleration;

    public MoveIntention() {
        this(new Vector());
    }

    public MoveIntention(Vector acceleration) {
        super(IntentionType.MOVE);
        this.acceleration = acceleration;
    }

    public MoveIntention(JSONOptional optional) throws JSONException {
        super(optional);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        super.decode(json);
        this.acceleration = (json.hasKey(MoveIntention.ACCELERATION)) ?
            new Vector(json.getJSONOptional(MoveIntention.ACCELERATION)) :
            new Vector();
    }

    @Override
    protected JSONObjectBuilder getJSONBuilder() throws JSONException {
        JSONObjectBuilder json = super.getJSONBuilder();
        json.put(MoveIntention.ACCELERATION, this.acceleration.toJSON());
        return json;
    }

    public Vector getAcceleration() {
        return this.acceleration;
    }

    @Override
    public String getLabel() {
        return "<MOVE>";
    }

    public boolean hasAcceleration() {
        return this.acceleration.getMagnitude() > 0;
    }

    public boolean equals(MoveIntention intent) {
        if (intent == null) return false;
        return super.equals(intent) && this.acceleration.equals(intent.acceleration);
    }

}
