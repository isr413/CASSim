package com.seat.rescuesim.common.remote.intent;

import com.seat.rescuesim.common.json.*;
import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.remote.Intention;
import com.seat.rescuesim.common.remote.IntentionType;

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

    public boolean hasNonZeroJerk() {
        return this.jerk.getX() != 0 || this.jerk.getY() != 0 || this.jerk.getZ() != 0;
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
