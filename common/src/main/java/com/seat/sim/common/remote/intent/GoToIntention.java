package com.seat.sim.common.remote.intent;

import com.seat.sim.common.json.*;
import com.seat.sim.common.math.Vector;

public class GoToIntention extends Intention {
    public static final String LOCATION = "location";
    public static final String MAX_ACCELERATION = "max_acceleration";
    public static final String MAX_VELOCITY = "max_velocity";

    private Vector location;
    private double maxAcceleration;
    private double maxVelocity;

    public GoToIntention() {
        this(null, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    public GoToIntention(double maxVelocity) {
        this(null, maxVelocity, Double.POSITIVE_INFINITY);
    }

    public GoToIntention(double maxVelocity, double maxAcceleration) {
        this(null, maxVelocity, maxAcceleration);
    }

    public GoToIntention(Vector location) {
        this(location, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    public GoToIntention(Vector location, double maxVelocity) {
        this(location, maxVelocity, Double.POSITIVE_INFINITY);
    }

    public GoToIntention(Vector location, double maxVelocity, double maxAcceleration) {
        super(IntentionType.GOTO);
        this.location = location;
        this.maxVelocity = maxVelocity;
        this.maxAcceleration = maxAcceleration;
    }

    public GoToIntention(Json json) throws JsonException {
        super(json);
    }

    @Override
    protected void decode(JsonObject json) throws JsonException {
        super.decode(json);
        this.location = (json.hasKey(GoToIntention.LOCATION)) ?
            new Vector(json.getJson(GoToIntention.LOCATION)) :
            null;
        this.maxVelocity = (json.hasKey(GoToIntention.MAX_VELOCITY)) ?
            this.maxVelocity = json.getDouble(GoToIntention.MAX_VELOCITY) :
            Double.POSITIVE_INFINITY;
        this.maxAcceleration = (json.hasKey(GoToIntention.MAX_ACCELERATION)) ?
            this.maxAcceleration = json.getDouble(GoToIntention.MAX_ACCELERATION) :
            Double.POSITIVE_INFINITY;
    }

    @Override
    protected JsonObjectBuilder getJsonBuilder() throws JsonException {
        JsonObjectBuilder json = super.getJsonBuilder();
        if (this.hasLocation()) {
            json.put(GoToIntention.LOCATION, this.location.toJson());
        }
        if (this.hasMaxVelocity()) {
            json.put(GoToIntention.MAX_VELOCITY, this.maxVelocity);
        }
        if (this.hasMaxAcceleration()) {
            json.put(GoToIntention.MAX_ACCELERATION, this.maxAcceleration);
        }
        return json;
    }

    public boolean equals(GoToIntention intent) {
        if (intent == null) return false;
        return super.equals(intent) &&
            ((this.hasLocation() && this.location.equals(intent.location)) || this.location == intent.location) &&
            this.maxVelocity == intent.maxVelocity && this.maxAcceleration == intent.maxAcceleration;
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

    public double getMaxVelocity() {
        return this.maxVelocity;
    }

    public boolean hasLocation() {
        return this.location != null;
    }

    public boolean hasMaxAcceleration() {
        return this.maxAcceleration != Double.POSITIVE_INFINITY;
    }

    public boolean hasMaxVelocity() {
        return this.maxVelocity != Double.POSITIVE_INFINITY;
    }
}
