package com.seat.sim.common.remote.kinematics;

import java.util.Optional;

import com.seat.sim.common.json.*;
import com.seat.sim.common.math.Vector;

/** A class to model a Remote's fuel, fuel usage, location, and motion. */
public class Kinematics extends Jsonable {
    public static final String FUEL = "fuel";
    public static final String LOCATION = "location";
    public static final String MOTION = "motion";

    private Optional<Fuel> fuel;
    private Optional<Vector> location;
    private Optional<Motion> motion;

    public Kinematics(Vector location) {
        this(location, null, null);
    }

    public Kinematics(Vector location, Motion motion) {
        this(location, motion, null);
    }

    public Kinematics(Fuel fuel) {
        this(null, null, fuel);
    }

    public Kinematics(Vector location, Fuel fuel) {
        this(location, null, fuel);
    }

    public Kinematics(Vector location, Motion motion, Fuel fuel) {
        this.location = (location != null) ? Optional.of(location) : Optional.empty();
        this.motion = (motion != null) ? Optional.of(motion) : Optional.empty();
        this.fuel = (fuel != null) ? Optional.of(fuel) : Optional.empty();
    }

    public Kinematics(Json json) throws JsonException {
        super(json);
    }

    @Override
    protected void decode(JsonObject json) throws JsonException {
        this.location = (json.hasKey(Kinematics.LOCATION)) ? Optional.of(new Vector(json.getJson(Kinematics.LOCATION)))
                : Optional.empty();
        this.motion = (json.hasKey(Kinematics.MOTION)) ? Optional.of(new Motion(json.getJson(Kinematics.MOTION)))
                : Optional.empty();
        this.fuel = (json.hasKey(Kinematics.FUEL)) ? Optional.of(new Fuel(json.getJson(Kinematics.FUEL)))
                : Optional.empty();
    }

    protected JsonObjectBuilder getJsonBuilder() throws JsonException {
        JsonObjectBuilder json = JsonBuilder.Object();
        if (this.hasLocation()) {
            json.put(Kinematics.LOCATION, this.getLocation().toJson());
        }
        if (this.isMobile()) {
            json.put(Kinematics.MOTION, this.getMotion().toJson());
        }
        if (this.hasFuel()) {
            json.put(Kinematics.FUEL, this.getFuel().toJson());
        }
        return json;
    }

    public boolean equals(Kinematics kinematics) {
        if (kinematics == null)
            return false;
        return this.location.equals(kinematics.location) && this.motion.equals(kinematics.motion)
                && this.fuel.equals(kinematics.fuel);
    }

    public double getInitialFuel() {
        return this.getFuel().getInitialFuel();
    }

    public double getInitialVelocity() {
        return this.getMotion().getInitialVelocity();
    }

    public Fuel getFuel() {
        return this.fuel.get();
    }

    public Vector getFuelUsage() {
        return this.getFuel().getFuelUsage();
    }

    public Vector getLocation() {
        return this.location.get();
    }

    public double getMaxAcceleration() {
        return this.getMotion().getMaxAcceleration();
    }

    public double getMaxFuel() {
        return this.getFuel().getMaxFuel();
    }

    public double getMaxVelocity() {
        return this.getMotion().getMaxVelocity();
    }

    public Motion getMotion() {
        return this.motion.get();
    }

    /** Override to change fuel usage formula. */
    public double getRemoteFuelUsage() {
        return this.getFuelUsage().getX();
    }

    /** Override to change fuel usage formula. */
    public double getRemoteFuelUsage(Vector acceleration) {
        if (!this.hasFuel() || !this.hasFuelUsage())
            return 0;
        if (acceleration.getMagnitude() == 0)
            return this.getRemoteFuelUsage();
        return this.getFuelUsage().getY() * acceleration.getProjectionXY().getMagnitude() +
                this.getFuelUsage().getZ() * acceleration.getZ();
    }

    public boolean hasFuel() {
        return this.fuel.isPresent() && (!this.getFuel().hasMaxFuel() || this.getMaxFuel() > 0);
    }

    public boolean hasFuelUsage() {
        return this.hasFuel() && this.getFuel().hasFuelUsage();
    }

    public boolean hasLocation() {
        return this.location.isPresent();
    }

    public boolean isEnabled() {
        return !this.hasFuel() || this.getInitialFuel() > 0;
    }

    public boolean isMobile() {
        return this.motion.isPresent() && this.getMotion().isMobile();
    }

    public Json toJson() throws JsonException {
        return this.getJsonBuilder().toJson();
    }
}
