package com.seat.rescuesim.common.remote.intent;

import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.JSONObjectBuilder;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.math.Vector;

public class GotoIntention extends Intention {
    public static final String LOCATION = "location";
    public static final String MAX_ACCELERATION = "max_acceleration";
    public static final String MAX_JERK = "max_jerk";
    public static final String MAX_VELOCITY = "max_velocity";

    protected static final double DEFAULT_ACCELERATION = Double.POSITIVE_INFINITY;
    protected static final double DEFAULT_JERK = Double.POSITIVE_INFINITY;
    protected static final double DEFAULT_VELOCITY = Double.POSITIVE_INFINITY;

    private Vector location;
    private double maxAcceleration;
    private double maxJerk;
    private double maxVelocity;

    public GotoIntention() {
        this(null, GotoIntention.DEFAULT_VELOCITY, GotoIntention.DEFAULT_ACCELERATION, GotoIntention.DEFAULT_JERK);
    }

    public GotoIntention(double maxVelocity) {
        this(null, maxVelocity, GotoIntention.DEFAULT_ACCELERATION, GotoIntention.DEFAULT_JERK);
    }

    public GotoIntention(double maxVelocity, double maxAcceleration) {
        this(null, maxVelocity, maxAcceleration, GotoIntention.DEFAULT_JERK);
    }

    public GotoIntention(double maxVelocity, double maxAcceleration, double maxJerk) {
        this(null, maxVelocity, maxAcceleration, maxJerk);
    }

    public GotoIntention(Vector location) {
        this(location, GotoIntention.DEFAULT_VELOCITY, GotoIntention.DEFAULT_ACCELERATION, GotoIntention.DEFAULT_JERK);
    }

    public GotoIntention(Vector location, double maxVelocity) {
        this(location, maxVelocity, GotoIntention.DEFAULT_ACCELERATION, GotoIntention.DEFAULT_JERK);
    }

    public GotoIntention(Vector location, double maxVelocity, double maxAcceleration) {
        this(location, maxVelocity, maxAcceleration, GotoIntention.DEFAULT_JERK);
    }

    public GotoIntention(Vector location, double maxVelocity, double maxAcceleration, double maxJerk) {
        super(IntentionType.GOTO);
        this.location = location;
        this.maxVelocity = maxVelocity;
        this.maxAcceleration = maxAcceleration;
        this.maxJerk = maxJerk;
    }

    public GotoIntention(JSONOption option) {
        super(option);
    }

    @Override
    protected void decode(JSONObject json) {
        super.decode(json);
        this.location = (json.hasKey(GotoIntention.LOCATION)) ?
            new Vector(json.getJSONOption(GotoIntention.LOCATION)) :
            null;
        this.maxVelocity = (json.hasKey(GotoIntention.MAX_VELOCITY)) ?
            this.maxVelocity = json.getDouble(GotoIntention.MAX_VELOCITY) :
            Double.POSITIVE_INFINITY;
        this.maxAcceleration = (json.hasKey(GotoIntention.MAX_ACCELERATION)) ?
            this.maxAcceleration = json.getDouble(GotoIntention.MAX_ACCELERATION) :
            Double.POSITIVE_INFINITY;
        this.maxJerk = (json.hasKey(GotoIntention.MAX_JERK)) ?
            json.getDouble(GotoIntention.MAX_JERK) :
            Double.POSITIVE_INFINITY;
    }

    @Override
    protected JSONObjectBuilder getJSONBuilder() {
        JSONObjectBuilder json = super.getJSONBuilder();
        if (this.hasLocation()) {
            json.put(GotoIntention.LOCATION, this.location.toJSON());
        }
        if (this.hasMaxVelocity()) {
            json.put(GotoIntention.MAX_VELOCITY, this.maxVelocity);
        }
        if (this.hasMaxAcceleration()) {
            json.put(GotoIntention.MAX_ACCELERATION, this.maxAcceleration);
        }
        if (this.hasMaxJerk()) {
            json.put(GotoIntention.MAX_JERK, this.maxJerk);
        }
        return json;
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

    public boolean hasLocation() {
        return this.location != null;
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

    public boolean equals(GotoIntention intent) {
        if (intent == null) return false;
        return super.equals(intent) &&
            ((this.hasLocation() && this.location.equals(intent.location)) || this.location == intent.location) &&
            this.maxVelocity == intent.maxVelocity && this.maxAcceleration == intent.maxAcceleration &&
            this.maxJerk == intent.maxJerk;
    }

}
