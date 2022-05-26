package com.seat.sim.common.remote.kinematics;

import com.seat.sim.common.json.JSONAble;
import com.seat.sim.common.json.JSONBuilder;
import com.seat.sim.common.json.JSONException;
import com.seat.sim.common.json.JSONObject;
import com.seat.sim.common.json.JSONObjectBuilder;
import com.seat.sim.common.json.JSONOptional;

/** A struct class to store motion data for a Remote asset. */
public class Motion extends JSONAble {
    public static final String INITIAL_VELOCITY = "initial_velocity";
    public static final String MAX_ACCELERATION = "max_acceleration";
    public static final String MAX_VELOCITY = "max_velocity";

    private double initialVelocity;
    private double maxAcceleration;
    private double maxVelocity;

    public Motion() {
        this(0, 0, 0);
    }

    public Motion(double maxVelocity, double maxAcceleration) {
        this(0, maxVelocity, maxAcceleration);
    }

    public Motion(double initialVelocity, double maxVelocity, double maxAcceleration) {
        this.initialVelocity = initialVelocity;
        this.maxVelocity = maxVelocity;
        this.maxAcceleration = maxAcceleration;
    }

    public Motion(JSONOptional optional) throws JSONException {
        super(optional);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        this.initialVelocity = (json.hasKey(Motion.INITIAL_VELOCITY)) ?
            json.getDouble(Motion.INITIAL_VELOCITY) :
            Double.POSITIVE_INFINITY;
        this.maxVelocity = (json.hasKey(Motion.MAX_VELOCITY)) ?
            json.getDouble(Motion.MAX_VELOCITY) :
            Double.POSITIVE_INFINITY;
        this.maxAcceleration = (json.hasKey(Motion.MAX_ACCELERATION)) ?
            json.getDouble(Motion.MAX_ACCELERATION) :
            Double.POSITIVE_INFINITY;
    }

    protected JSONObjectBuilder getJSONBuilder() throws JSONException {
        JSONObjectBuilder json = JSONBuilder.Object();
        if (this.hasInitialVelocity()) {
            json.put(Motion.INITIAL_VELOCITY, this.initialVelocity);
        }
        if (this.hasMaxVelocity()) {
            json.put(Motion.MAX_VELOCITY, this.maxVelocity);
        }
        if (this.hasMaxAcceleration()) {
            json.put(Motion.MAX_ACCELERATION, this.maxAcceleration);
        }
        return json;
    }

    public double getInitialVelocity() {
        return this.initialVelocity;
    }

    public double getMaxAcceleration() {
        return this.maxAcceleration;
    }

    public double getMaxVelocity() {
        return this.maxVelocity;
    }

    public boolean hasInitialVelocity() {
        return this.initialVelocity != Double.POSITIVE_INFINITY;
    }

    public boolean hasMaxAcceleration() {
        return this.maxAcceleration != Double.POSITIVE_INFINITY;
    }

    public boolean hasMaxVelocity() {
        return this.maxVelocity != Double.POSITIVE_INFINITY;
    }

    public JSONOptional toJSON() throws JSONException {
        return this.getJSONBuilder().toJSON();
    }

    public boolean equals(Motion motion) {
        if (motion == null) return false;
        return this.initialVelocity == motion.initialVelocity && this.maxVelocity == motion.maxVelocity &&
            this.maxAcceleration == motion.maxAcceleration;
    }

}
