package com.seat.sim.common.remote.kinematics;

import java.util.Optional;

import com.seat.sim.common.json.*;
import com.seat.sim.common.math.Vector;

/** A struct class to store Fuel information for a Remote asset. */
public class Fuel extends Jsonable {
    public static final String FUEL_USAGE = "fuel_usage";
    public static final String INITIAL_FUEL = "initial_fuel";
    public static final String MAX_FUEL = "max_fuel";

    private Optional<Vector> fuelUsage;
    private double initialFuel;
    private double maxFuel;

    public Fuel(double maxFuel) {
        this(maxFuel, maxFuel);
    }

    public Fuel(double initialFuel, double maxFuel) {
        this(initialFuel, maxFuel, null);
    }

    public Fuel(double initialFuel, double maxFuel, Vector fuelUsage) {
        this.initialFuel = initialFuel;
        this.maxFuel = maxFuel;
        this.fuelUsage = (fuelUsage != null) ? Optional.of(fuelUsage) : Optional.empty();
    }

    public Fuel(Json json) throws JsonException {
        super(json);
    }

    @Override
    protected void decode(JsonObject json) throws JsonException {
        this.initialFuel = (json.hasKey(Fuel.INITIAL_FUEL)) ? json.getDouble(Fuel.INITIAL_FUEL)
                : Double.POSITIVE_INFINITY;
        this.maxFuel = (json.hasKey(Fuel.MAX_FUEL)) ? json.getDouble(Fuel.MAX_FUEL) : Double.POSITIVE_INFINITY;
        this.fuelUsage = (json.hasKey(Fuel.FUEL_USAGE)) ? Optional.of(new Vector(json.getJson(Fuel.FUEL_USAGE)))
                : Optional.empty();
    }

    protected JsonObjectBuilder getJsonBuilder() throws JsonException {
        JsonObjectBuilder json = JsonBuilder.Object();
        if (this.hasInitialFuel()) {
            json.put(Fuel.INITIAL_FUEL, this.initialFuel);
        }
        if (this.hasMaxFuel()) {
            json.put(Fuel.MAX_FUEL, this.maxFuel);
        }
        if (this.hasFuelUsage()) {
            json.put(Fuel.FUEL_USAGE, this.getFuelUsage().toJson());
        }
        return json;
    }

    public boolean equals(Fuel fuel) {
        if (fuel == null)
            return false;
        return this.initialFuel == fuel.initialFuel && this.maxFuel == fuel.maxFuel;
    }

    public Vector getFuelUsage() {
        return this.fuelUsage.get();
    }

    public double getInitialFuel() {
        return this.initialFuel;
    }

    public double getMaxFuel() {
        return this.maxFuel;
    }

    public boolean hasFuelUsage() {
        return this.fuelUsage.isPresent() && this.getFuelUsage().getMagnitude() > 0;
    }

    public boolean hasInitialFuel() {
        return Double.isFinite(this.initialFuel);
    }

    public boolean hasMaxFuel() {
        return Double.isFinite(this.maxFuel);
    }

    public Json toJson() throws JsonException {
        return this.getJsonBuilder().toJson();
    }
}
