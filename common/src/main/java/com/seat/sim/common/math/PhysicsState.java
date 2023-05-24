package com.seat.sim.common.math;

import com.seat.sim.common.json.Json;
import com.seat.sim.common.json.JsonBuilder;
import com.seat.sim.common.json.JsonException;
import com.seat.sim.common.json.JsonObject;
import com.seat.sim.common.json.JsonObjectBuilder;
import com.seat.sim.common.json.Jsonable;

public class PhysicsState extends Jsonable {
  public static final String LOCATION = "location";
  public static final String MAX_ACCELERATION = "max_acceleration";
  public static final String MAX_VELOCITY = "max_velocity";
  public static final String VELOCITY = "velocity";

  private Vector location;
  private double maxAcceleration;
  private double maxVelocity;
  private Vector velocity;

  public PhysicsState(Vector location) {
  this(location, Vector.ZERO, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
  }

  public PhysicsState(Vector location, double maxAcceleration) {
  this(location, Vector.ZERO, Double.POSITIVE_INFINITY, maxAcceleration);
  }

  public PhysicsState(Vector location, Vector velocity) {
  this(location, velocity, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
  }

  public PhysicsState(Vector location, Vector velocity, double maxAcceleration) {
  this(location, velocity, Double.POSITIVE_INFINITY, maxAcceleration);
  }

  public PhysicsState(Vector location, Vector velocity, double maxVelocity, double maxAcceleration) {
  this.location = location;
  this.velocity = velocity;
  this.maxVelocity = maxVelocity;
  this.maxAcceleration = maxAcceleration;
  }

  public PhysicsState(Json json) throws JsonException {
  super(json);
  }

  @Override
  protected void decode(JsonObject json) throws JsonException {
  this.location = new Vector(json.getJson(PhysicsState.LOCATION));
  this.velocity = new Vector(json.getJson(PhysicsState.VELOCITY));
  this.maxVelocity = (json.hasKey(PhysicsState.MAX_VELOCITY))
    ? json.getDouble(PhysicsState.MAX_VELOCITY)
    : Double.POSITIVE_INFINITY;
  this.maxAcceleration = (json.hasKey(PhysicsState.MAX_ACCELERATION))
    ? json.getDouble(PhysicsState.MAX_ACCELERATION)
    : Double.POSITIVE_INFINITY;
  }

  protected JsonObjectBuilder getJsonBuilder() throws JsonException {
  JsonObjectBuilder json = JsonBuilder.Object();
  json.put(PhysicsState.LOCATION, this.location.toJson());
  json.put(PhysicsState.VELOCITY, this.velocity.toJson());
  if (this.hasMaxVelocity()) {
    json.put(PhysicsState.MAX_VELOCITY, this.maxVelocity);
  }
  if (this.hasMaxAcceleration()) {
    json.put(PhysicsState.MAX_ACCELERATION, this.maxAcceleration);
  }
  return json;
  }

  public double getMaxAcceleration() {
  return this.maxAcceleration;
  }

  public double getMaxVelocity() {
  return this.maxVelocity;
  }

  public Vector getLocation() {
  return this.location;
  }

  public double getSpeed() {
  return this.velocity.getMagnitude();
  }

  public Vector getVelocity() {
  return this.velocity;
  }

  public boolean hasMaxAcceleration() {
  return this.maxAcceleration != Double.POSITIVE_INFINITY;
  }

  public boolean hasMaxVelocity() {
  return this.maxVelocity != Double.POSITIVE_INFINITY;
  }

  public boolean isMobile() {
    return this.maxVelocity > 0. && (this.maxAcceleration > 0. || this.getSpeed() > 0.);
  }

  public Json toJson() throws JsonException {
  return this.getJsonBuilder().toJson();
  }
}
