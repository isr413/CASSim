package com.seat.sim.common.remote.kinematics;

import java.util.Optional;

import com.seat.sim.common.json.*;
import com.seat.sim.common.math.Vector;

/** A class to model a Remote's fuel, fuel usage, location, and motion. */
public class KinematicsProto extends Jsonable {
  public static final String FUEL_PROTO = "fuel_proto";
  public static final String LOCATION = "location";
  public static final String MOTION_PROTO = "motion_proto";

  private Optional<FuelProto> fuelProto;
  private Optional<Vector> location;
  private Optional<MotionProto> motionProto;

  public KinematicsProto() {
    this(null, null, null);
  }

  public KinematicsProto(Vector location) {
    this(location, null, null);
  }

  public KinematicsProto(Vector location, FuelProto fuelProto, MotionProto motionProto) {
    this.location = (location != null) ? Optional.of(location) : Optional.empty();
    this.motionProto = (motionProto != null) ? Optional.of(motionProto) : Optional.empty();
    this.fuelProto = (fuelProto != null) ? Optional.of(fuelProto) : Optional.empty();
  }

  public KinematicsProto(Json json) throws JsonException {
    super(json);
  }

  @Override
  protected void decode(JsonObject json) throws JsonException {
    this.location = (json.hasKey(KinematicsProto.LOCATION))
        ? Optional.of(new Vector(json.getJson(KinematicsProto.LOCATION)))
        : Optional.empty();
    this.fuelProto = (json.hasKey(KinematicsProto.FUEL_PROTO))
        ? Optional.of(new FuelProto(json.getJson(KinematicsProto.FUEL_PROTO)))
        : Optional.empty();
    this.motionProto = (json.hasKey(KinematicsProto.MOTION_PROTO))
        ? Optional.of(new MotionProto(json.getJson(KinematicsProto.MOTION_PROTO)))
        : Optional.empty();
  }

  protected JsonObjectBuilder getJsonBuilder() throws JsonException {
    JsonObjectBuilder json = JsonBuilder.Object();
    if (this.hasLocation()) {
      json.put(KinematicsProto.LOCATION, this.getLocation().toJson());
    }
    if (this.hasFuelProto()) {
      json.put(KinematicsProto.FUEL_PROTO, this.getFuelProto().toJson());
    }
    if (this.isMobile()) {
      json.put(KinematicsProto.MOTION_PROTO, this.getMotionProto().toJson());
    }
    return json;
  }

  public FuelProto getFuelProto() {
    return this.fuelProto.get();
  }

  public Vector getLocation() {
    return this.location.get();
  }

  public MotionProto getMotionProto() {
    return this.motionProto.get();
  }

  /** Override to change fuel usage formula. */
  public double getRemoteFuelUsage() {
    return this.getRemoteFuelUsage(null);
  }

  /** Override to change fuel usage formula. */
  public double getRemoteFuelUsage(Vector acceleration) {
    if (!this.hasFuelProto() || !this.getFuelProto().hasFuelUsage()) {
      return 0;
    }
    if (acceleration == null || acceleration.getMagnitude() == 0) {
      return this.getFuelProto().getFuelUsage().getX();
    }
    return this.getFuelProto().getFuelUsage().getX()
        + this.getFuelProto().getFuelUsage().getY() * acceleration.getProjectionXY().getMagnitude()
        + this.getFuelProto().getFuelUsage().getZ() * acceleration.getZ();
  }

  public boolean hasFuelProto() {
    return this.fuelProto.isPresent();
  }

  public boolean hasLocation() {
    return this.location.isPresent();
  }

  public boolean isEnabled() {
    return !this.hasFuelProto() || this.getFuelProto().hasInitialFuel();
  }

  public boolean isMobile() {
    return this.motionProto.isPresent() && this.getMotionProto().isMobile();
  }

  public Json toJson() throws JsonException {
    return this.getJsonBuilder().toJson();
  }
}
