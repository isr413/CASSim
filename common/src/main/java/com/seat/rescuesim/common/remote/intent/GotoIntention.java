package com.seat.rescuesim.common.remote.intent;

import com.seat.rescuesim.common.json.*;
import com.seat.rescuesim.common.math.Vector;

public class GotoIntention extends Intention {
    private static final String LOCATION = "location";
    private static final String MAX_ACCELERATION = "max_acceleration";
    private static final String MAX_VELOCITY = "max_velocity";

    private Vector location;
    private double maxAcceleration;
    private double maxVelocity;

    public GotoIntention(JSONObject json) {
        super(json);
    }

    public GotoIntention(JSONOption option) {
        super(option);
    }

    public GotoIntention(String encoding) {
        super(encoding);
    }

    public GotoIntention(Vector location) {
        this(location, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    public GotoIntention(Vector location, double maxAcceleration) {
        this(location, maxAcceleration, Double.POSITIVE_INFINITY);
    }

    public GotoIntention(Vector location, double maxAcceleration, double maxVelocity) {
        super(IntentionType.MOVE);
        this.location = location;
        this.maxAcceleration = maxAcceleration;
        this.maxVelocity = maxVelocity;
    }

    @Override
    protected void decode(JSONObject json) {
        super.decode(json);
        this.location = new Vector(json.getJSONArray(GotoIntention.LOCATION));
        if (json.hasKey(GotoIntention.MAX_ACCELERATION)) {
            this.maxAcceleration = json.getDouble(GotoIntention.MAX_ACCELERATION);
        } else {
            this.maxAcceleration = Double.POSITIVE_INFINITY;
        }
        if (json.hasKey(GotoIntention.MAX_VELOCITY)) {
            this.maxVelocity = json.getDouble(GotoIntention.MAX_VELOCITY);
        } else {
            this.maxVelocity = Double.POSITIVE_INFINITY;
        }
    }

    public Vector getLocation() {
        return this.location;
    }

    public double getMaxAcceleration() {
        return this.maxAcceleration;
    }

    public double getMaxVelocity() {
        return this.maxVelocity;
    }

    public boolean hasMaxAcceleration() {
        return this.maxAcceleration != Double.POSITIVE_INFINITY;
    }

    public boolean hasMaxVelocity() {
        return this.maxVelocity != Double.POSITIVE_INFINITY;
    }

    @Override
    public JSONOption toJSON() {
        JSONObjectBuilder json = super.getJSONBuilder();
        json.put(GotoIntention.LOCATION, this.location.toJSON());
        if (this.hasMaxAcceleration()) {
            json.put(GotoIntention.MAX_ACCELERATION, this.maxAcceleration);
        }
        if (this.hasMaxVelocity()) {
            json.put(GotoIntention.MAX_VELOCITY, this.maxVelocity);
        }
        return json.toJSON();
    }

    public boolean equals(GotoIntention intent) {
        return this.type == intent.type && this.maxAcceleration == intent.maxAcceleration &&
            this.maxVelocity == intent.maxVelocity;
    }

}
