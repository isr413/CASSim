package com.seat.sim.common.remote.kinematics;

import com.seat.sim.common.json.*;
import com.seat.sim.common.math.Vector;

/** A class to model a Remote's fuel, fuel usage, location, and motion. */
public class Kinematics extends Jsonable {
    public static final String FUEL = "fuel";
    public static final String FUEL_USAGE = "fuel_usage";
    public static final String LOCATION = "location";
    public static final String MOTION = "motion";

    private Fuel fuel;
    private Vector fuelUsage;
    private Vector location;
    private Motion motion;

    public Kinematics(Vector location, Motion motion, Fuel fuel, Vector fuelUsage) {
        this.location = location;
        this.motion = (motion != null) ? motion : new Motion(0, 0, 0);
        this.fuel = (fuel != null) ? fuel : new Fuel(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        this.fuelUsage = (fuelUsage != null) ? fuelUsage : new Vector();
    }

    public Kinematics(Json json) throws JsonException {
        super(json);
    }

    @Override
    protected void decode(JsonObject json) throws JsonException {
        this.location = (json.hasKey(Kinematics.LOCATION)) ?
            new Vector(json.getJson(Kinematics.LOCATION)) :
            null;
        this.motion = (json.hasKey(Kinematics.MOTION)) ?
            new Motion(json.getJson(Kinematics.MOTION)) :
            new Motion(0, 0, 0);
        this.fuel = (json.hasKey(Kinematics.FUEL)) ?
            new Fuel(json.getJson(Kinematics.FUEL)) :
            new Fuel(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        this.fuelUsage = (json.hasKey(Kinematics.FUEL_USAGE)) ?
            new Vector(json.getJson(Kinematics.FUEL_USAGE)) :
            new Vector();
    }

    protected JsonObjectBuilder getJsonBuilder() throws JsonException {
        JsonObjectBuilder json = JsonBuilder.Object();
        if (this.hasLocation()) {
            json.put(Kinematics.LOCATION, this.location.toJson());
        }
        if (this.hasMotion()) {
            json.put(Kinematics.MOTION, this.motion.toJson());
        }
        if (this.hasFiniteFuel()) {
            json.put(Kinematics.FUEL, this.fuel.toJson());
        }
        if (this.hasFuelUsage()) {
            json.put(Kinematics.FUEL_USAGE, this.fuelUsage.toJson());
        }
        return json;
    }

    public boolean equals(Kinematics kinematics) {
        if (kinematics == null) return false;
        return ((this.hasLocation() && this.location.equals(kinematics.location))
                || this.location == kinematics.location) &&
            this.motion.equals(kinematics.motion) && this.fuel.equals(kinematics.fuel) &&
            this.fuelUsage.equals(kinematics.fuelUsage);
    }

    public double getInitialFuel() {
        return this.fuel.getInitialFuel();
    }

    public double getInitialVelocity() {
        return this.motion.getInitialVelocity();
    }

    public Fuel getFuel() {
        return this.fuel;
    }

    public Vector getFuelUsage() {
        return this.fuelUsage;
    }

    public Vector getLocation() {
        return this.location;
    }

    public double getMaxAcceleration() {
        return this.motion.getMaxAcceleration();
    }

    public double getMaxFuel() {
        return this.fuel.getMaxFuel();
    }

    public double getMaxVelocity() {
        return this.motion.getMaxVelocity();
    }

    public Motion getMotion() {
        return this.motion;
    }

    /** Override to change fuel usage formula. */
    public double getRemoteFuelUsage() {
        return this.fuelUsage.getX();
    }

    /** Override to change fuel usage formula. */
    public double getRemoteFuelUsage(Vector acceleration) {
        if (acceleration.getMagnitude() == 0) return this.getRemoteFuelUsage();
        return this.fuelUsage.getY() * acceleration.getProjectionXY().getMagnitude() +
            this.fuelUsage.getZ() * acceleration.getZ();
    }

    public boolean hasFiniteFuel() {
        return this.fuel.hasInitialFuel();
    }

    public boolean hasFuelUsage() {
        return this.fuelUsage.getMagnitude() > 0;
    }

    public boolean hasLocation() {
        return this.location != null;
    }

    public boolean hasMotion() {
        return this.motion.getMaxVelocity() > 0 && this.motion.getMaxAcceleration() > 0;
    }

    public boolean isEnabled() {
        return this.fuel.getInitialFuel() > 0;
    }

    public Json toJson() throws JsonException {
        return this.getJsonBuilder().toJson();
    }
}
