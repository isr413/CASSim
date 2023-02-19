package com.seat.sim.server.remote.components;

import java.util.Optional;

import com.seat.sim.common.math.Vector;
import com.seat.sim.common.remote.kinematics.KinematicsProto;
import com.seat.sim.server.core.SimException;

public class Kinematics {

  public static double brakeDistance(double velocity, double acceleration) {
    return (velocity > 0 && Double.isFinite(acceleration)) ? 0.5 * velocity * velocity / acceleration : 0;
  }

  private Optional<Fuel> fuel;
  private Optional<Vector> location;
  private Optional<Motion> motion;
  private KinematicsProto proto;

  public Kinematics(KinematicsProto proto) {
    this.proto = (proto != null) ? proto : new KinematicsProto();
    this.location = (proto.hasLocation()) ? Optional.of(proto.getLocation()) : Optional.empty();
    this.motion = (proto.isMobile()) ? Optional.of(new Motion(proto.getMotionProto())) : Optional.empty();
    this.fuel = (proto.hasFuelProto()) ? Optional.of(new Fuel(proto.getFuelProto())) : Optional.empty();
  }

  public double getBrakeDistance() {
    if (!this.isMobile()) {
      return 0;
    }
    return Kinematics.brakeDistance(this.getSpeed(), this.getMaxAcceleration());
  }

  public Fuel getFuel() {
    return this.fuel.get();
  }

  public double getFuelAmount() {
    if (!this.hasFuel()) {
      return 0;
    }
    return this.getFuel().getFuelAmount();
  }

  public Vector getHomeLocation() {
    return this.proto.getLocation();
  }

  public Vector getLocation() {
    return this.location.get();
  }

  public double getMaxAcceleration() {
    if (!this.isMobile()) {
      return 0.;
    }
    return this.getMotion().getMaxAcceleration();
  }

  public double getMaxVelocity() {
    if (!this.isMobile()) {
      return 0.;
    }
    return this.getMotion().getMaxVelocity();
  }

  public Motion getMotion() {
    return this.motion.get();
  }

  public Optional<Vector> getNextLocation(double stepSize) {
    if (!this.hasLocation()) {
      return Optional.empty();
    }
    if (!this.isMobile()) {
      return this.location;
    }
    return this.getNextLocation(this.getVelocity(), stepSize);
  }

  public Optional<Vector> getNextLocation(Vector velocity, double stepSize) {
    if (!this.hasLocation()) {
      return Optional.empty();
    }
    if (!this.isMobile() || velocity == null || !Double.isFinite(velocity.getMagnitude())) {
      return this.location;
    }
    if (this.hasMaxVelocity()) {
      velocity = velocity.squeeze(this.getMaxVelocity());
    }
    return Optional.of(Vector.add(this.getLocation(), velocity.scale(stepSize)));
  }

  public KinematicsProto getProto() {
    return this.proto;
  }

  public double getRemoteFuelUsage() {
    return this.proto.getRemoteFuelUsage();
  }

  public double getRemoteFuelUsage(double dist) {
    return this.proto.getRemoteFuelUsage(dist);
  }

  public double getRemoteFuelUsage(Vector acceleration) {
    return this.proto.getRemoteFuelUsage(acceleration);
  }

  public double getSpeed() {
    if (!this.isMobile()) {
      return 0.;
    }
    return this.getMotion().getSpeed();
  }

  public Vector getVelocity() {
    if (!this.isMobile()) {
      return Vector.ZERO;
    }
    return this.getMotion().getVelocity();
  }

  public boolean hasFuel() {
    return this.fuel.isPresent();
  }

  public boolean hasLocation() {
    return this.location.isPresent();
  }

  public boolean hasMaxAcceleration() {
    return this.isMobile() && this.getMotion().hasMaxAcceleration();
  }

  public boolean hasMaxVelocity() {
    return this.isMobile() && this.getMotion().hasMaxVelocity();
  }

  public boolean isFuelEmpty() {
    return this.getFuelAmount() == 0;
  }

  public boolean isInMotion() {
    return this.isMobile() && this.getMotion().isInMotion();
  }

  public boolean isMobile() {
    return this.motion.isPresent() && this.getMotion().isMobile();
  }

  public void setLocationTo(Vector location) {
    this.location = (location != null) ? Optional.of(location) : Optional.empty();
  }

  public void setVelocityTo(Vector velocity) {
    if (!this.isMobile()) {
      return;
    }
    this.getMotion().setVelocityTo(velocity);
  }

  public Vector shiftLocationTo(Vector dest) {
    if (!this.hasLocation() || !this.isMobile()) {
      return Vector.ZERO;
    }
    return this.shiftLocationTo(dest, this.getMaxVelocity(), this.getMaxAcceleration());
  }

  public Vector shiftLocationTo(Vector dest, double maxVelocity) {
    if (!this.hasLocation() || !this.isMobile()) {
      return Vector.ZERO;
    }
    return this.shiftLocationTo(dest, maxVelocity, this.getMaxAcceleration());
  }

  public Vector shiftLocationTo(Vector dest, double maxVelocity, double maxAcceleration) {
    if (!this.hasLocation() || !this.isMobile()) {
      return Vector.ZERO;
    }
    maxVelocity = Math.min(maxVelocity, this.getMaxVelocity());
    maxAcceleration = Math.min(maxAcceleration, this.getMaxAcceleration());
    if (this.getLocation().near(dest) && this.getSpeed() < maxAcceleration) {
      return this.getVelocity().invert();
    }
    Vector deltaS = Vector.sub(dest, this.getLocation());
    if (this.getSpeed() > 0 && Double.isFinite(maxAcceleration)) {
      double brakeDistance = Kinematics.brakeDistance(this.getSpeed(), maxAcceleration);
      if (deltaS.getMagnitude() <= brakeDistance) {
        if (this.getSpeed() < maxAcceleration) {
          return this.getVelocity().invert();
        } else {
          return this.getVelocity().getUnitVector().scale(-maxAcceleration);
        }
      }
    }
    if (Double.isFinite(maxVelocity)) {
      deltaS = deltaS.squeeze(maxVelocity);
    }
    Vector deltaV = Vector.sub(deltaS, this.getVelocity());
    if (Double.isFinite(maxAcceleration)) {
      deltaV = deltaV.squeeze(maxAcceleration);
    }
    Vector nextV = Vector.add(this.getVelocity(), deltaV);
    if (Double.isFinite(maxVelocity)) {
      nextV = nextV.squeeze(maxVelocity);
    }
    if (this.getSpeed() > 0 && Double.isFinite(maxAcceleration)) {
      double brakeDistance = Kinematics.brakeDistance(nextV.getMagnitude(), maxAcceleration);
      Vector nextS = Vector.add(this.getLocation(), nextV);
      if (Vector.sub(dest, nextS).getMagnitude() < brakeDistance) {
        double targetMag = Math.sqrt(maxAcceleration) *
            Math.sqrt(maxAcceleration + 2 * deltaV.getMagnitude()) - maxAcceleration;
        return Vector.sub(nextV.squeeze(targetMag), this.getVelocity()).squeeze(maxAcceleration);
      }
    }
    return deltaV;
  }

  public Vector shiftVelocityTo(Vector target) {
    if (!this.isMobile()) {
      return Vector.ZERO;
    }
    return this.getMotion().shiftVelocityTo(target);
  }

  public void update(double stepSize) throws SimException {
    this.update(Vector.ZERO, stepSize);
  }

  public void update(Vector acceleration, double stepSize) throws SimException {
    if (acceleration == null || !Double.isFinite(acceleration.getMagnitude())) {
      acceleration = Vector.ZERO;
    }
    if (this.isMobile() && this.hasMaxAcceleration()) {
      acceleration = acceleration.squeeze(this.getMaxAcceleration());
    }
    if (this.isMobile()) {
      this.getMotion().updateVelocityBy(acceleration, stepSize);
      if (this.getMotion().getVelocity().near(Vector.ZERO)) {
        this.getMotion().setVelocityTo(Vector.ZERO);
      }
    }
    if (this.hasLocation()) {
      this.updateLocation(stepSize);
    }
    if (this.hasFuel()) {
      if (this.isMobile()) {
        this.getFuel().updateFuelBy(-this.getRemoteFuelUsage(acceleration), stepSize);
      } else {
        this.getFuel().updateFuelBy(-this.getRemoteFuelUsage(), stepSize);
      }
    }
  }

  public void updateFuelBy(double fuelUsage, double stepSize) throws SimException {
    if (!this.hasFuel()) {
      return;
    }
    this.getFuel().updateFuelBy(fuelUsage, stepSize);
  }

  public void updateLocation(double stepSize) throws SimException {
    if (!this.hasLocation() || !this.isMobile()) {
      return;
    }
    this.updateLocationBy(this.getVelocity(), stepSize);
  }

  public void updateLocationBy(Vector velocity, double stepSize) throws SimException {
    if (!this.hasLocation() || velocity == null || !Double.isFinite(velocity.getMagnitude())) {
      return;
    }
    this.location = Optional.of(Vector.add(this.getLocation(), velocity.scale(stepSize)));
  }

  public void updateVelocityBy(Vector force, double stepSize) throws SimException {
    if (!this.isMobile()) {
      return;
    }
    this.getMotion().updateVelocityBy(force, stepSize);
  }
}
