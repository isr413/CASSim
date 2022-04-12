package com.seat.sim.common.remote.mobile;

import java.util.Collection;

import com.seat.sim.common.json.JSONException;
import com.seat.sim.common.json.JSONObject;
import com.seat.sim.common.json.JSONObjectBuilder;
import com.seat.sim.common.json.JSONOption;
import com.seat.sim.common.math.Vector;
import com.seat.sim.common.remote.RemoteProto;
import com.seat.sim.common.sensor.SensorConfig;

/** A serializable prototype of a Mobile Remote. */
public class MobileRemoteProto extends RemoteProto {
    public static final String MAX_ACCELERATION = "max_acceleration";
    public static final String MAX_JERK = "max_jerk";
    public static final String MAX_VELOCITY = "max_velocity";

    protected static final double DEFAULT_ACCELERATION = Double.POSITIVE_INFINITY;
    protected static final double DEFAULT_JERK = Double.POSITIVE_INFINITY;
    protected static final double DEFAULT_VELOCITY = Double.POSITIVE_INFINITY;

    private double maxAcceleration;
    private double maxJerk;
    private double maxVelocity;

    public MobileRemoteProto(double maxBatteryPower, double maxVelocity, double maxAcceleration, double maxJerk) {
        this(null, maxBatteryPower, null, maxVelocity, maxAcceleration, maxJerk);
    }

    public MobileRemoteProto(Vector location, double maxBatteryPower, double maxVelocity, double maxAcceleration,
            double maxJerk) {
        this(location, maxBatteryPower, null, maxVelocity, maxAcceleration, maxJerk);
    }

    public MobileRemoteProto(double maxBatteryPower, Collection<SensorConfig> sensors, double maxVelocity,
            double maxAcceleration, double maxJerk) {
        this(null, maxBatteryPower, sensors, maxVelocity, maxAcceleration, maxJerk);
    }

    public MobileRemoteProto(Vector location, double maxBatteryPower, Collection<SensorConfig> sensors,
            double maxVelocity, double maxAcceleration, double maxJerk) {
        super(location, maxBatteryPower, sensors);
        this.maxVelocity = maxVelocity;
        this.maxAcceleration = maxAcceleration;
        this.maxJerk = maxJerk;
    }

    public MobileRemoteProto(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        super.decode(json);
        this.maxVelocity = (json.hasKey(MobileRemoteProto.MAX_VELOCITY)) ?
            this.maxVelocity = json.getDouble(MobileRemoteProto.MAX_VELOCITY) :
            Double.POSITIVE_INFINITY;
        this.maxAcceleration = (json.hasKey(MobileRemoteProto.MAX_ACCELERATION)) ?
            this.maxAcceleration = json.getDouble(MobileRemoteProto.MAX_ACCELERATION) :
            Double.POSITIVE_INFINITY;
        this.maxJerk = (json.hasKey(MobileRemoteProto.MAX_JERK)) ?
            this.maxJerk = json.getDouble(MobileRemoteProto.MAX_JERK) :
            Double.POSITIVE_INFINITY;
    }

    @Override
    protected JSONObjectBuilder getJSONBuilder() throws JSONException {
        JSONObjectBuilder json = super.getJSONBuilder();
        if (this.hasMaxAcceleration()) {
            json.put(MobileRemoteProto.MAX_VELOCITY, this.maxVelocity);
        }
        if (this.hasMaxJerk()) {
            json.put(MobileRemoteProto.MAX_ACCELERATION, this.maxAcceleration);
        }
        if (this.hasMaxVelocity()) {
            json.put(MobileRemoteProto.MAX_JERK, this.maxJerk);
        }
        return json;
    }

    @Override
    public String getLabel() {
        return "r:<mobile>";
    }

    public double getMaxAcceleration() {
        return this.maxAcceleration;
    }

    public double getMaxJerk() {
        return this.maxJerk;
    }

    public double getMaxVelocity() {
        return this.maxVelocity;
    }

    public boolean hasAcceleration() {
        return this.maxAcceleration > 0;
    }

    public boolean hasJerk() {
        return this.maxJerk > 0;
    }

    public boolean hasVelocity() {
        return this.maxVelocity > 0;
    }

    public boolean hasMaxAcceleration() {
        return this.maxAcceleration != Double.POSITIVE_INFINITY;
    }

    public boolean hasMaxJerk() {
        return this.maxJerk != Double.POSITIVE_INFINITY;
    }

    public boolean hasMaxVelocity() {
        return this.maxVelocity != Double.POSITIVE_INFINITY;
    }

    public boolean isMobile() {
        return this.hasVelocity() && this.hasAcceleration() && this.hasJerk();
    }

    public boolean isStationary() {
        return !this.isMobile();
    }

    public boolean equals(MobileRemoteProto proto) {
        if (proto == null) return false;
        return super.equals(proto) && this.maxVelocity == proto.maxVelocity &&
            this.maxAcceleration == proto.maxAcceleration && this.maxJerk == proto.maxJerk;
    }

}