package com.seat.sim.server.remote.components;

import java.util.Optional;

import com.seat.sim.common.math.Vector;

public class Destination {

  private Vector location;
  private Optional<Double> maxAcceleration;
  private Optional<Double> maxVelocity;

  public Destination(Vector location) {
    this(location, null, null);
  }

  public Destination(Vector location, double maxVelocity, double maxAcceleration) {
    this(location, Optional.of(maxVelocity), Optional.of(maxAcceleration));
  }

  private Destination(Vector location, Optional<Double> maxVelocity, Optional<Double> maxAcceleration) {
    this.location = location;
    this.maxVelocity = (maxVelocity != null) ? maxVelocity : Optional.empty();
    this.maxAcceleration = (maxAcceleration != null) ? maxAcceleration : Optional.empty();
  }

  public Vector getLocation() {
    return this.location;
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

  public boolean hasMaxAcceleration() {
    return this.maxAcceleration.isPresent() && Double.isFinite(this.maxAcceleration.get());
  }

  public boolean hasMaxVelocity() {
    return this.maxVelocity.isPresent() && Double.isFinite(this.maxVelocity.get());
  }
}
