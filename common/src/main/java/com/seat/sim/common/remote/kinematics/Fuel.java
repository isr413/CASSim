package com.seat.sim.common.remote.kinematics;

import com.seat.sim.common.json.*;

/** A struct class to store Fuel information for a Remote asset. */
public class Fuel extends Jsonable {
    public static final String INITIAL_FUEL = "initial_fuel";
    public static final String MAX_FUEL = "max_fuel";

    private double initialFuel;
    private double maxFuel;

    public Fuel(double initialFuel, double maxFuel) {
        this.initialFuel = initialFuel;
        this.maxFuel = maxFuel;
    }

    public Fuel(Json json) throws JsonException {
        super(json);
    }

    @Override
    protected void decode(JsonObject json) throws JsonException {
        this.initialFuel = (json.hasKey(Fuel.INITIAL_FUEL)) ?
            json.getDouble(Fuel.INITIAL_FUEL) :
            Double.POSITIVE_INFINITY;
        this.maxFuel = (json.hasKey(Fuel.MAX_FUEL)) ?
            json.getDouble(Fuel.MAX_FUEL) :
            Double.POSITIVE_INFINITY;
    }

    protected JsonObjectBuilder getJsonBuilder() throws JsonException {
        JsonObjectBuilder json = JsonBuilder.Object();
        if (this.hasInitialFuel()) {
            json.put(Fuel.INITIAL_FUEL, this.initialFuel);
        }
        if (this.hasMaxFuel()) {
            json.put(Fuel.MAX_FUEL, this.maxFuel);
        }
        return json;
    }

    public boolean equals(Fuel fuel) {
        if (fuel == null) return false;
        return this.initialFuel == fuel.initialFuel && this.maxFuel == fuel.maxFuel;
    }

    public double getInitialFuel() {
        return this.initialFuel;
    }

    public double getMaxFuel() {
        return this.maxFuel;
    }

    public boolean hasInitialFuel() {
        return this.initialFuel != Double.POSITIVE_INFINITY;
    }

    public boolean hasMaxFuel() {
        return this.maxFuel != Double.POSITIVE_INFINITY;
    }

    public Json toJson() throws JsonException {
        return this.getJsonBuilder().toJson();
    }
}
