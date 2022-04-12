package com.seat.sim.common.remote.intent;

import com.seat.sim.common.json.JSONException;
import com.seat.sim.common.json.JSONObject;
import com.seat.sim.common.json.JSONObjectBuilder;
import com.seat.sim.common.json.JSONOption;
import com.seat.sim.common.math.Vector;

public class GoToIntention extends Intention {
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

    public GoToIntention() {
        this(null, GoToIntention.DEFAULT_VELOCITY, GoToIntention.DEFAULT_ACCELERATION, GoToIntention.DEFAULT_JERK);
    }

    public GoToIntention(double maxVelocity) {
        this(null, maxVelocity, GoToIntention.DEFAULT_ACCELERATION, GoToIntention.DEFAULT_JERK);
    }

    public GoToIntention(double maxVelocity, double maxAcceleration) {
        this(null, maxVelocity, maxAcceleration, GoToIntention.DEFAULT_JERK);
    }

    public GoToIntention(double maxVelocity, double maxAcceleration, double maxJerk) {
        this(null, maxVelocity, maxAcceleration, maxJerk);
    }

    public GoToIntention(Vector location) {
        this(location, GoToIntention.DEFAULT_VELOCITY, GoToIntention.DEFAULT_ACCELERATION, GoToIntention.DEFAULT_JERK);
    }

    public GoToIntention(Vector location, double maxVelocity) {
        this(location, maxVelocity, GoToIntention.DEFAULT_ACCELERATION, GoToIntention.DEFAULT_JERK);
    }

    public GoToIntention(Vector location, double maxVelocity, double maxAcceleration) {
        this(location, maxVelocity, maxAcceleration, GoToIntention.DEFAULT_JERK);
    }

    public GoToIntention(Vector location, double maxVelocity, double maxAcceleration, double maxJerk) {
        super(IntentionType.GOTO);
        this.location = location;
        this.maxVelocity = maxVelocity;
        this.maxAcceleration = maxAcceleration;
        this.maxJerk = maxJerk;
    }

    public GoToIntention(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        super.decode(json);
        this.location = (json.hasKey(GoToIntention.LOCATION)) ?
            new Vector(json.getJSONOption(GoToIntention.LOCATION)) :
            null;
        this.maxVelocity = (json.hasKey(GoToIntention.MAX_VELOCITY)) ?
            this.maxVelocity = json.getDouble(GoToIntention.MAX_VELOCITY) :
            Double.POSITIVE_INFINITY;
        this.maxAcceleration = (json.hasKey(GoToIntention.MAX_ACCELERATION)) ?
            this.maxAcceleration = json.getDouble(GoToIntention.MAX_ACCELERATION) :
            Double.POSITIVE_INFINITY;
        this.maxJerk = (json.hasKey(GoToIntention.MAX_JERK)) ?
            json.getDouble(GoToIntention.MAX_JERK) :
            Double.POSITIVE_INFINITY;
    }

    @Override
    protected JSONObjectBuilder getJSONBuilder() throws JSONException {
        JSONObjectBuilder json = super.getJSONBuilder();
        if (this.hasLocation()) {
            json.put(GoToIntention.LOCATION, this.location.toJSON());
        }
        if (this.hasMaxVelocity()) {
            json.put(GoToIntention.MAX_VELOCITY, this.maxVelocity);
        }
        if (this.hasMaxAcceleration()) {
            json.put(GoToIntention.MAX_ACCELERATION, this.maxAcceleration);
        }
        if (this.hasMaxJerk()) {
            json.put(GoToIntention.MAX_JERK, this.maxJerk);
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

    public boolean equals(GoToIntention intent) {
        if (intent == null) return false;
        return super.equals(intent) &&
            ((this.hasLocation() && this.location.equals(intent.location)) || this.location == intent.location) &&
            this.maxVelocity == intent.maxVelocity && this.maxAcceleration == intent.maxAcceleration &&
            this.maxJerk == intent.maxJerk;
    }

}
