package com.seat.sim.common.remote.kinematics;

import java.util.Optional;

import com.seat.sim.common.json.*;
import com.seat.sim.common.math.Vector;

/** A struct class to store motion data for a Remote asset. */
public class MotionProto extends Jsonable {
  public static final String INITIAL_VELOCITY = "initial_velocity";
  public static final String MAX_ACCELERATION = "max_acceleration";
  public static final String MAX_VELOCITY = "max_velocity";

  private Vector initialVelocity;
  private Optional<Double> maxAcceleration;
  private Optional<Double> maxVelocity;

  public MotionProto() {
    this(Vector.ZERO);
  }

  public MotionProto(Vector initialVelocity) {
    this(initialVelocity, null, null);
  }

  public MotionProto(Vector initialVelocity, double maxVelocity) {
    this(initialVelocity, Optional.of(maxVelocity), null);
  }

  public MotionProto(Vector initialVelocity, double maxVelocity, double maxAcceleration) {
    this(initialVelocity, Optional.of(maxVelocity), Optional.of(maxAcceleration));
  }

  private MotionProto(Vector initialVelocity, Optional<Double> maxVelocity, Optional<Double> maxAcceleration) {
    this.initialVelocity = (initialVelocity != null) ? initialVelocity : Vector.ZERO;
    this.maxVelocity = (maxVelocity != null) ? maxVelocity : Optional.empty();
    this.maxAcceleration = (maxAcceleration != null) ? maxAcceleration : Optional.empty();
    if (!Double.isFinite(this.initialVelocity.getMagnitude())) {
      throw new RuntimeException(String.format("cannot create Motion with non-finite initial velocity `%f`",
          this.initialVelocity.getMagnitude()));
    }
  }

  public MotionProto(Json json) throws JsonException {
    super(json);
  }

  @Override
  protected void decode(JsonObject json) throws JsonException {
    this.initialVelocity = (json.hasKey(MotionProto.INITIAL_VELOCITY))
        ? new Vector(json.getJson(MotionProto.INITIAL_VELOCITY))
        : Vector.ZERO;
    this.maxVelocity = (json.hasKey(MotionProto.MAX_VELOCITY))
        ? Optional.of(json.getDouble(MotionProto.MAX_VELOCITY))
        : Optional.empty();
    this.maxAcceleration = (json.hasKey(MotionProto.MAX_ACCELERATION))
        ? Optional.of(json.getDouble(MotionProto.MAX_ACCELERATION))
        : Optional.empty();
  }

  protected JsonObjectBuilder getJsonBuilder() throws JsonException {
    JsonObjectBuilder json = JsonBuilder.Object();
    if (this.hasInitialVelocity()) {
      json.put(MotionProto.INITIAL_VELOCITY, this.initialVelocity.toJson());
    }
    if (this.hasMaxVelocity()) {
      json.put(MotionProto.MAX_VELOCITY, this.getMaxVelocity());
    }
    if (this.hasMaxAcceleration()) {
      json.put(MotionProto.MAX_ACCELERATION, this.getMaxAcceleration());
    }
    return json;
  }

  public double getInitialSpeed() {
    return this.getInitialVelocity().getMagnitude();
  }

  public Vector getInitialVelocity() {
    return this.initialVelocity;
  }

  public double getMaxAcceleration() {
    if (this.maxAcceleration.isEmpty()) {
      return Double.POSITIVE_INFINITY;
    }
    return this.maxAcceleration.get();
  }

  public double getMaxVelocity() {
    if (this.maxVelocity.isEmpty()) {
      return Double.POSITIVE_INFINITY;
    }
    return this.maxVelocity.get();
  }

  public boolean hasInitialVelocity() {
    return Double.isFinite(this.getInitialSpeed()) && this.getInitialSpeed() > 0.;
  }

  public boolean hasMaxAcceleration() {
    return this.maxAcceleration.isPresent() && Double.isFinite(this.getMaxAcceleration());
  }

  public boolean hasMaxVelocity() {
    return this.maxVelocity.isPresent() && Double.isFinite(this.getMaxVelocity());
  }

  public boolean isMobile() {
    return (!this.hasMaxVelocity() || this.getMaxVelocity() > 0.)
        && (this.hasInitialVelocity() || !this.hasMaxAcceleration() || this.getMaxAcceleration() > 0.);
  }

  public Json toJson() throws JsonException {
    return this.getJsonBuilder().toJson();
  }
}
