package com.seat.sim.common.remote.intent;

import java.util.Optional;

import com.seat.sim.common.json.*;
import com.seat.sim.common.math.Vector;

public class GoToIntention extends Intention {
    public static final String LOCATION = "location";
    public static final String MAX_ACCELERATION = "max_acceleration";
    public static final String MAX_VELOCITY = "max_velocity";

    private Vector location;
    private Optional<Double> maxAcceleration;
    private Optional<Double> maxVelocity;

    public GoToIntention(Vector location) {
        this(location, null, null);
    }

    public GoToIntention(Vector location, double maxVelocity) {
        this(location, Optional.of(maxVelocity), null);
    }

    public GoToIntention(Vector location, double maxVelocity, double maxAcceleration) {
        this(location, Optional.of(maxVelocity), Optional.of(maxAcceleration));
    }

    private GoToIntention(Vector location, Optional<Double> maxVelocity, Optional<Double> maxAcceleration) {
        super(IntentionType.GOTO);
        this.location = location;
        this.maxVelocity = (maxVelocity != null) ? maxVelocity : Optional.empty();
        this.maxAcceleration = (maxAcceleration != null) ? maxAcceleration : Optional.empty();
    }

    public GoToIntention(Json json) throws JsonException {
        super(json);
    }

    @Override
    protected void decode(JsonObject json) throws JsonException {
        super.decode(json);
        this.location = (json.hasKey(GoToIntention.LOCATION)) ? new Vector(json.getJson(GoToIntention.LOCATION)) : null;
        this.maxVelocity = (json.hasKey(GoToIntention.MAX_VELOCITY))
                ? this.maxVelocity = Optional.of(json.getDouble(GoToIntention.MAX_VELOCITY))
                : Optional.empty();
        this.maxAcceleration = (json.hasKey(GoToIntention.MAX_ACCELERATION))
                ? this.maxAcceleration = Optional.of(json.getDouble(GoToIntention.MAX_ACCELERATION))
                : Optional.empty();
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
        if (intent == null)
            return false;
        return super.equals(intent) &&
                ((this.hasLocation() && this.location.equals(intent.location)) || this.location == intent.location) &&
                this.maxVelocity.equals(intent.maxVelocity) && this.maxAcceleration.equals(intent.maxAcceleration);
    }

    @Override
    public String getLabel() {
        return "<GOTO>";
    }

    public Vector getLocation() {
        return this.location;
    }

    public double getMaxAcceleration() {
        return this.maxAcceleration.get();
    }

    public double getMaxVelocity() {
        return this.maxVelocity.get();
    }

    public boolean hasLocation() {
        return this.location != null;
    }

    public boolean hasMaxAcceleration() {
        return this.maxAcceleration.isPresent();
    }

    public boolean hasMaxVelocity() {
        return this.maxVelocity.isPresent();
    }
}
