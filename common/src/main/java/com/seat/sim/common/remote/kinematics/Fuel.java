package com.seat.sim.common.remote.kinematics;

import com.seat.sim.common.json.JSONAble;
import com.seat.sim.common.json.JSONBuilder;
import com.seat.sim.common.json.JSONException;
import com.seat.sim.common.json.JSONObject;
import com.seat.sim.common.json.JSONObjectBuilder;
import com.seat.sim.common.json.JSONOptional;

/** A struct class to store Fuel information for a Remote asset. */
public class Fuel extends JSONAble {
    public static final String INITIAL_FUEL = "initial_fuel";
    public static final String MAX_FUEL = "max_fuel";

    private double initialFuel;
    private double maxFuel;

    public Fuel() {
        this(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    public Fuel(double maxFuel) {
        this(maxFuel, maxFuel);
    }

    public Fuel(double initialFuel, double maxFuel) {
        this.initialFuel = initialFuel;
        this.maxFuel = maxFuel;
    }

    public Fuel(JSONOptional optional) throws JSONException {
        super(optional);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        this.initialFuel = (json.hasKey(Fuel.INITIAL_FUEL)) ?
            json.getDouble(Fuel.INITIAL_FUEL) :
            Double.POSITIVE_INFINITY;
        this.maxFuel = (json.hasKey(Fuel.MAX_FUEL)) ?
            json.getDouble(Fuel.MAX_FUEL) :
            Double.POSITIVE_INFINITY;
    }

    protected JSONObjectBuilder getJSONBuilder() throws JSONException {
        JSONObjectBuilder json = JSONBuilder.Object();
        if (this.hasInitialFuel()) {
            json.put(Fuel.INITIAL_FUEL, this.initialFuel);
        }
        if (this.hasMaxFuel()) {
            json.put(Fuel.MAX_FUEL, this.maxFuel);
        }
        return json;
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

    public JSONOptional toJSON() throws JSONException {
        return this.getJSONBuilder().toJSON();
    }

    public boolean equals(Fuel fuel) {
        if (fuel == null) return false;
        return this.initialFuel == fuel.initialFuel && this.maxFuel == fuel.maxFuel;
    }

}
