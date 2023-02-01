package com.seat.sim.common.remote.kinematics;

import java.util.Optional;

import com.seat.sim.common.json.*;

/** A struct class to store motion data for a Remote asset. */
public class Motion extends Jsonable {
    public static final String INITIAL_VELOCITY = "initial_velocity";
    public static final String MAX_ACCELERATION = "max_acceleration";
    public static final String MAX_VELOCITY = "max_velocity";

    private double initialVelocity;
    private Optional<Double> maxAcceleration;
    private Optional<Double> maxVelocity;

    public Motion() {
        this(0);
    }

    public Motion(double initialVelocity) {
        this(initialVelocity, null, null);
    }

    public Motion(double initialVelocity, double maxVelocity) {
        this(initialVelocity, Optional.of(maxVelocity), null);
    }

    public Motion(double initialVelocity, double maxVelocity, double maxAcceleration) {
        this(initialVelocity, Optional.of(maxVelocity), Optional.of(maxAcceleration));
    }

    private Motion(double initialVelocity, Optional<Double> maxVelocity, Optional<Double> maxAcceleration) {
        this.initialVelocity = initialVelocity;
        this.maxVelocity = (maxVelocity != null) ? maxVelocity : Optional.empty();
        this.maxAcceleration = (maxAcceleration != null) ? maxAcceleration : Optional.empty();
        if (!Double.isFinite(this.initialVelocity)) {
            throw new RuntimeException(
                    String.format("cannot create Motion with non-finite initial velocity `%f`", this.initialVelocity));
        }
    }

    public Motion(Json json) throws JsonException {
        super(json);
    }

    @Override
    protected void decode(JsonObject json) throws JsonException {
        this.initialVelocity = (json.hasKey(Motion.INITIAL_VELOCITY)) ? json.getDouble(Motion.INITIAL_VELOCITY) : 0;
        this.maxVelocity = (json.hasKey(Motion.MAX_VELOCITY)) ? Optional.of(json.getDouble(Motion.MAX_VELOCITY))
                : Optional.empty();
        this.maxAcceleration = (json.hasKey(Motion.MAX_ACCELERATION))
                ? Optional.of(json.getDouble(Motion.MAX_ACCELERATION))
                : Optional.empty();
    }

    protected JsonObjectBuilder getJsonBuilder() throws JsonException {
        JsonObjectBuilder json = JsonBuilder.Object();
        if (this.hasInitialVelocity()) {
            json.put(Motion.INITIAL_VELOCITY, this.initialVelocity);
        }
        if (this.hasMaxVelocity()) {
            json.put(Motion.MAX_VELOCITY, this.maxVelocity);
        }
        if (this.hasMaxAcceleration()) {
            json.put(Motion.MAX_ACCELERATION, this.maxAcceleration);
        }
        return json;
    }

    public boolean equals(Motion motion) {
        if (motion == null)
            return false;
        return this.initialVelocity == motion.initialVelocity && this.maxVelocity.equals(motion.maxVelocity)
                && this.maxAcceleration.equals(motion.maxAcceleration);
    }

    public double getInitialVelocity() {
        return this.initialVelocity;
    }

    public double getMaxAcceleration() {
        return this.maxAcceleration.get();
    }

    public double getMaxVelocity() {
        return this.maxVelocity.get();
    }

    public boolean hasInitialVelocity() {
        return Double.isFinite(this.initialVelocity) && this.initialVelocity > 0;
    }

    public boolean hasMaxAcceleration() {
        return this.maxAcceleration.isPresent() && Double.isFinite(this.getMaxAcceleration());
    }

    public boolean hasMaxVelocity() {
        return this.maxVelocity.isPresent() && Double.isFinite(this.getMaxVelocity());
    }

    public boolean isMobile() {
        return (!this.hasMaxVelocity() || this.getMaxVelocity() > 0)
                && (this.hasInitialVelocity() || !this.hasMaxAcceleration() || this.getMaxAcceleration() > 0);
    }

    public Json toJson() throws JsonException {
        return this.getJsonBuilder().toJson();
    }
}
