package com.seat.sim.common.remote.kinematics;

import com.seat.sim.common.json.JSONAble;
import com.seat.sim.common.json.JSONBuilder;
import com.seat.sim.common.json.JSONException;
import com.seat.sim.common.json.JSONObject;
import com.seat.sim.common.json.JSONObjectBuilder;
import com.seat.sim.common.json.JSONOptional;
import com.seat.sim.common.math.Vector;

/** A class to model a Remote's fuel, fuel usage, location, and motion. */
public class Kinematics extends JSONAble {
    public static final String FUEL = "fuel";
    public static final String FUEL_USAGE = "fuel_usage";
    public static final String LOCATION = "location";
    public static final String MOTION = "motion";

    private Fuel fuel;
    private Vector fuelUsage;
    private Vector location;
    private Motion motion;

    public Kinematics() {
        this(null, null, null, null);
    }

    public Kinematics(Vector location, Motion motion, Fuel fuel, Vector fuelUsage) {
        this.location = location;
        this.motion = (motion != null) ? motion : new Motion();
        this.fuel = (fuel != null) ? fuel : new Fuel();
        this.fuelUsage = (fuelUsage != null) ? fuelUsage : new Vector();
    }

    public Kinematics(JSONOptional optional) throws JSONException {
        super(optional);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        this.location = (json.hasKey(Kinematics.LOCATION)) ?
            new Vector(json.getJSONOptional(Kinematics.LOCATION)) :
            null;
        this.motion = (json.hasKey(Kinematics.MOTION)) ?
            new Motion(json.getJSONOptional(Kinematics.MOTION)) :
            new Motion();
        this.fuel = (json.hasKey(Kinematics.FUEL)) ?
            new Fuel(json.getJSONOptional(Kinematics.FUEL)) :
            new Fuel();
        this.fuelUsage = (json.hasKey(Kinematics.FUEL_USAGE)) ?
            new Vector(json.getJSONOptional(Kinematics.FUEL_USAGE)) :
            new Vector();
    }

    protected JSONObjectBuilder getJSONBuilder() throws JSONException {
        JSONObjectBuilder json = JSONBuilder.Object();
        if (this.hasLocation()) {
            json.put(Kinematics.LOCATION, this.location.toJSON());
        }
        if (this.hasMotion()) {
            json.put(Kinematics.MOTION, this.motion.toJSON());
        }
        if (this.hasFiniteFuel()) {
            json.put(Kinematics.FUEL, this.fuel.toJSON());
        }
        if (this.hasFuelUsage()) {
            json.put(Kinematics.FUEL_USAGE, this.fuelUsage.toJSON());
        }
        return json;
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

    public JSONOptional toJSON() throws JSONException {
        return this.getJSONBuilder().toJSON();
    }

    public boolean equals(Kinematics kinematics) {
        if (kinematics == null) return false;
        return ((this.hasLocation() && this.location.equals(kinematics.location))
                || this.location == kinematics.location) &&
            this.motion.equals(kinematics.motion) && this.fuel.equals(kinematics.fuel) &&
            this.fuelUsage.equals(kinematics.fuelUsage);
    }

}
