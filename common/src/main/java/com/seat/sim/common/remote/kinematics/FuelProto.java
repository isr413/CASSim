package com.seat.sim.common.remote.kinematics;

import java.util.Optional;

import com.seat.sim.common.json.*;
import com.seat.sim.common.math.Vector;

/** A struct class to store Fuel information for a Remote asset. */
public class FuelProto extends Jsonable {
  public static final String FUEL_USAGE = "fuel_usage";
  public static final String INITIAL_FUEL = "initial_fuel";
  public static final String MAX_FUEL = "max_fuel";

  private Optional<Vector> fuelUsage;
  private double initialFuel;
  private Optional<Double> maxFuel;

  public FuelProto() {
    this(0, null, null);
  }

  public FuelProto(double initialFuel, Vector fuelUsage) {
    this(initialFuel, null, fuelUsage);
  }

  public FuelProto(double initialFuel, double maxFuel, Vector fuelUsage) {
    this(initialFuel, Optional.of(maxFuel), fuelUsage);
  }

  private FuelProto(double initialFuel, Optional<Double> maxFuel, Vector fuelUsage) {
    this.initialFuel = initialFuel;
    this.maxFuel = (maxFuel != null) ? maxFuel : Optional.empty();
    this.fuelUsage = (fuelUsage != null) ? Optional.of(fuelUsage) : Optional.empty();
    if (!Double.isFinite(this.initialFuel)) {
      throw new RuntimeException(
          String.format("cannot create Fuel with non-finite initial fuel `%f`", this.initialFuel));
    }
  }

  public FuelProto(Json json) throws JsonException {
    super(json);
  }

  @Override
  protected void decode(JsonObject json) throws JsonException {
    this.initialFuel = (json.hasKey(FuelProto.INITIAL_FUEL)) ? json.getDouble(FuelProto.INITIAL_FUEL) : 0.;
    this.maxFuel = (json.hasKey(FuelProto.MAX_FUEL)) ? Optional.of(json.getDouble(FuelProto.MAX_FUEL))
        : Optional.empty();
    this.fuelUsage = (json.hasKey(FuelProto.FUEL_USAGE)) ? Optional.of(new Vector(json.getJson(FuelProto.FUEL_USAGE)))
        : Optional.empty();
  }

  protected JsonObjectBuilder getJsonBuilder() throws JsonException {
    JsonObjectBuilder json = JsonBuilder.Object();
    if (this.hasInitialFuel()) {
      json.put(FuelProto.INITIAL_FUEL, this.initialFuel);
    }
    if (this.hasMaxFuel()) {
      json.put(FuelProto.MAX_FUEL, this.getMaxFuel());
    }
    if (this.hasFuelUsage()) {
      json.put(FuelProto.FUEL_USAGE, this.getFuelUsage().toJson());
    }
    return json;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || this.getClass() != o.getClass()) {
      return this == o;
    }
    return this.equals((FuelProto) o);
  }

  public boolean equals(FuelProto proto) {
    return Vector.near(this.initialFuel, proto.initialFuel) && Vector.near(this.getMaxFuel(), proto.getMaxFuel()) &&
        this.getFuelUsage().equals(proto.getFuelUsage());
  }

  public Vector getFuelUsage() {
    if (this.fuelUsage.isEmpty()) {
      return Vector.ZERO;
    }
    return this.fuelUsage.get();
  }

  public double getInitialFuel() {
    return this.initialFuel;
  }

  public double getMaxFuel() {
    if (this.maxFuel.isEmpty()) {
      return Double.POSITIVE_INFINITY;
    }
    return this.maxFuel.get();
  }

  public boolean hasFuelUsage() {
    return this.fuelUsage.isPresent() && this.getFuelUsage().getMagnitude() > 0.;
  }

  public boolean hasInitialFuel() {
    return Double.isFinite(this.initialFuel) && this.initialFuel > 0.;
  }

  public boolean hasMaxFuel() {
    return this.maxFuel.isPresent() && Double.isFinite(this.getMaxFuel());
  }

  public Json toJson() throws JsonException {
    return this.getJsonBuilder().toJson();
  }
}
