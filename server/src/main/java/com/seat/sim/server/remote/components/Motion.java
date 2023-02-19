package com.seat.sim.server.remote.components;

import com.seat.sim.common.math.Vector;
import com.seat.sim.common.remote.kinematics.MotionProto;
import com.seat.sim.server.core.SimException;

public class Motion {

  private MotionProto proto;
  private Vector velocity;

  public Motion(MotionProto proto) {
    this.proto = (proto != null) ? proto : new MotionProto();
    this.velocity = (proto.hasInitialVelocity()) ? proto.getInitialVelocity() : Vector.ZERO;
  }

  public MotionProto getProto() {
    return this.proto;
  }

  public double getMaxAcceleration() {
    if (!this.hasMaxAcceleration()) {
      return Double.POSITIVE_INFINITY;
    }
    return this.proto.getMaxAcceleration();
  }

  public double getMaxVelocity() {
    if (!this.hasMaxVelocity()) {
      return Double.POSITIVE_INFINITY;
    }
    return this.proto.getMaxVelocity();
  }

  public double getSpeed() {
    return this.getVelocity().getMagnitude();
  }

  public Vector getVelocity() {
    return this.velocity;
  }

  public boolean hasMaxAcceleration() {
    return this.proto.hasMaxAcceleration();
  }

  public boolean hasMaxVelocity() {
    return this.proto.hasMaxVelocity();
  }

  public boolean isInMotion() {
    return this.getSpeed() > 0.;
  }

  public boolean isMobile() {
    return this.proto.isMobile() || this.getSpeed() > 0.;
  }

  public void setVelocityTo(Vector velocity) {
    this.velocity = (velocity != null) ? velocity : Vector.ZERO;
    if (this.hasMaxVelocity()) {
      this.velocity = this.velocity.squeeze(this.getMaxVelocity());
    }
  }

  public Vector shiftVelocityTo(Vector target) {
    if (!this.isMobile()) {
      return Vector.ZERO;
    }
    if (this.hasMaxVelocity()) {
      target = target.squeeze(this.getMaxVelocity());
    }
    if (this.hasMaxAcceleration()) {
      return Vector.sub(target, this.getVelocity()).squeeze(this.getMaxAcceleration());
    }
    return Vector.sub(target, this.getVelocity());
  }

  public void updateVelocityBy(Vector force, double stepSize) throws SimException {
    if (this.hasMaxAcceleration()) {
      force = force.squeeze(this.getMaxAcceleration());
    }
    this.velocity = Vector.add(this.velocity, force.scale(stepSize));
    if (this.hasMaxVelocity()) {
      this.velocity = this.velocity.squeeze(this.getMaxVelocity());
    }
  }

  public void updateVelocityTo(Vector target, double stepSize) {
    this.updateVelocityTo(target, Double.POSITIVE_INFINITY, stepSize);
  }

  public void updateVelocityTo(Vector targetVelocity, double maxAcceleration, double stepSize) {
    if (this.hasMaxVelocity()) {
      targetVelocity = targetVelocity.squeeze(this.getMaxVelocity());
    }
    Vector delta = Vector.sub(targetVelocity, this.getVelocity());
    if (Double.isFinite(maxAcceleration)) {
      delta = delta.squeeze(maxAcceleration);
    }
    if (this.hasMaxAcceleration()) {
      delta = delta.squeeze(this.getMaxAcceleration());
    }
    this.updateVelocityBy(delta, stepSize);
  }
}
