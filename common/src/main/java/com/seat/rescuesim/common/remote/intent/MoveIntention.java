package com.seat.rescuesim.common.remote.intent;

import com.seat.rescuesim.common.json.*;
import com.seat.rescuesim.common.math.Vector;

public class MoveIntention extends Intention {
    private static final String JERK = "jerk";

    private Vector jerk;

    public MoveIntention(JSONObject json) {
        super(json);
    }

    public MoveIntention(JSONOption option) {
        super(option);
    }

    public MoveIntention(String encoding) {
        super(encoding);
    }

    public MoveIntention() {
        this(new Vector());
    }

    public MoveIntention(Vector jerk) {
        super(IntentionType.MOVE);
        this.jerk = jerk;
    }

    @Override
    protected void decode(JSONObject json) {
        super.decode(json);
        this.jerk = new Vector(json.getJSONArray(MoveIntention.JERK));
    }

    public Vector getJerk() {
        return this.jerk;
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
        return this.type == intent.type && this.jerk.equals(intent.jerk);
    }

}
