package com.seat.rescuesim.common.remote.intent;

import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.JSONObjectBuilder;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.math.Vector;

public class MoveIntention extends Intention {
    private static final String JERK = "jerk";

    private Vector jerk;

    public MoveIntention() {
        this(new Vector());
    }

    public MoveIntention(Vector jerk) {
        super(IntentionType.MOVE);
        this.jerk = jerk;
    }

    public MoveIntention(JSONOption option) {
        super(option);
    }

    @Override
    protected void decode(JSONObject json) {
        super.decode(json);
        this.jerk = (json.hasKey(MoveIntention.JERK)) ?
            new Vector(json.getJSONOption(MoveIntention.JERK)) :
            new Vector();
    }

    public Vector getJerk() {
        return this.jerk;
    }

    @Override
    public String getLabel() {
        return "<MOVE>";
    }

    public boolean hasJerk() {
        return this.jerk.getMagnitude() > 0;
    }

    @Override
    public JSONOption toJSON() {
        JSONObjectBuilder json = super.getJSONBuilder();
        json.put(MoveIntention.JERK, this.jerk.toJSON());
        return json.toJSON();
    }

    public boolean equals(MoveIntention intent) {
        if (intent == null) return false;
        return super.equals(intent) && this.jerk.equals(intent.jerk);
    }

}
