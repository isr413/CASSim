package com.seat.rescuesim.common.remote.intent;

import com.seat.rescuesim.common.json.*;
import com.seat.rescuesim.common.math.Vector;

public class GotoIntention extends Intention {
    private static final String LOCATION = "location";
    private static final String MAX_ACCELERATION = "max_acceleration";
    private static final String MAX_JERK = "max_jerk";
    private static final String MAX_VELOCITY = "max_velocity";

    private Vector location;
    private double maxAcceleration;
    private double maxJerk;
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
        this(location, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    public GotoIntention(Vector location, double maxVelocity) {
        this(location, maxVelocity, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    public GotoIntention(Vector location, double maxVelocity, double maxAcceleration) {
        this(location, maxVelocity, maxAcceleration, Double.POSITIVE_INFINITY);
    }

    public GotoIntention(Vector location, double maxVelocity, double maxAcceleration, double maxJerk) {
        super(IntentionType.GOTO);
        this.location = location;
        this.maxVelocity = maxVelocity;
        this.maxAcceleration = maxAcceleration;
        this.maxJerk = maxJerk;
    }

    @Override
    protected void decode(JSONObject json) {
        super.decode(json);
        this.location = new Vector(json.getJSONArray(GotoIntention.LOCATION));
        if (json.hasKey(GotoIntention.MAX_VELOCITY)) {
            this.maxVelocity = json.getDouble(GotoIntention.MAX_VELOCITY);
        } else {
            this.maxVelocity = Double.POSITIVE_INFINITY;
        }
        if (json.hasKey(GotoIntention.MAX_ACCELERATION)) {
            this.maxAcceleration = json.getDouble(GotoIntention.MAX_ACCELERATION);
        } else {
            this.maxAcceleration = Double.POSITIVE_INFINITY;
        }
        if (json.hasKey(GotoIntention.MAX_JERK)) {
            this.maxJerk = json.getDouble(GotoIntention.MAX_JERK);
        } else {
            this.maxJerk = Double.POSITIVE_INFINITY;
        }
    }

    @Override
    public String getLabel() {
        return "<GOTO>";
    }

    public Vector getLocation() {
        return this.location;
    }

    public double getMaxAcceleration() {
        return this.maxAcceleration;
    }

    public double getMaxJerk() {
        return this.maxJerk;
    }

    public double getMaxVelocity() {
        return this.maxVelocity;
    }

    public boolean hasMaxAcceleration() {
        return this.maxAcceleration != Double.POSITIVE_INFINITY;
    }

    public boolean hasMaxJerk() {
        return this.maxJerk != Double.POSITIVE_INFINITY;
    }

    public boolean hasMaxVelocity() {
        return this.maxVelocity != Double.POSITIVE_INFINITY;
    }

    @Override
    public JSONOption toJSON() {
        JSONObjectBuilder json = super.getJSONBuilder();
        json.put(GotoIntention.LOCATION, this.location.toJSON());
        if (this.hasMaxVelocity()) {
            json.put(GotoIntention.MAX_VELOCITY, this.maxVelocity);
        }
        if (this.hasMaxAcceleration()) {
            json.put(GotoIntention.MAX_ACCELERATION, this.maxAcceleration);
        }
        if (this.hasMaxJerk()) {
            json.put(GotoIntention.MAX_JERK, this.maxJerk);
        }
        return json.toJSON();
    }

    public boolean equals(GotoIntention intent) {
        return this.type == intent.type && this.maxVelocity == intent.maxVelocity &&
            this.maxAcceleration == intent.maxAcceleration && this.maxJerk == intent.maxJerk;
    }

}
