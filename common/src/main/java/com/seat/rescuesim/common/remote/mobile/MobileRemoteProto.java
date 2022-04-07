package com.seat.rescuesim.common.remote.mobile;

import java.util.Collection;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.JSONObjectBuilder;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.remote.RemoteProto;
import com.seat.rescuesim.common.remote.RemoteType;
import com.seat.rescuesim.common.sensor.SensorConfig;

/** A serializable prototype of a kinetic Remote. */
public class MobileRemoteProto extends RemoteProto {
    public static final RemoteType DEFAULT_MOBILE_REMOTE_TYPE = RemoteType.MOBILE;
    public static final String MAX_ACCELERATION = "max_acceleration";
    public static final String MAX_JERK = "max_jerk";
    public static final String MAX_VELOCITY = "max_velocity";

    protected static final double DEFAULT_ACCELERATION = Double.POSITIVE_INFINITY;
    protected static final double DEFAULT_JERK = Double.POSITIVE_INFINITY;
    protected static final double DEFAULT_VELOCITY = Double.POSITIVE_INFINITY;

    private double maxAcceleration;
    private double maxJerk;
    private double maxVelocity;

    public MobileRemoteProto() {
        this(MobileRemoteProto.DEFAULT_MOBILE_REMOTE_TYPE, null, RemoteProto.DEFAULT_BATTERY_POWER, null,
            MobileRemoteProto.DEFAULT_VELOCITY, MobileRemoteProto.DEFAULT_ACCELERATION,
            MobileRemoteProto.DEFAULT_JERK);
    }

    public MobileRemoteProto(double maxVelocity, double maxAcceleration, double maxJerk) {
        this(MobileRemoteProto.DEFAULT_MOBILE_REMOTE_TYPE, null, RemoteProto.DEFAULT_BATTERY_POWER, null, maxVelocity,
            maxAcceleration, maxJerk);
    }

    public MobileRemoteProto(double maxBatteryPower) {
        this(MobileRemoteProto.DEFAULT_MOBILE_REMOTE_TYPE, null, maxBatteryPower, null,
            MobileRemoteProto.DEFAULT_VELOCITY, MobileRemoteProto.DEFAULT_ACCELERATION, MobileRemoteProto.DEFAULT_JERK);
    }

    public MobileRemoteProto(double maxBatteryPower, double maxVelocity, double maxAcceleration, double maxJerk) {
        this(MobileRemoteProto.DEFAULT_MOBILE_REMOTE_TYPE, null, maxBatteryPower, null, maxVelocity, maxAcceleration,
            maxJerk);
    }

    public MobileRemoteProto(Vector location) {
        this(MobileRemoteProto.DEFAULT_MOBILE_REMOTE_TYPE, location, RemoteProto.DEFAULT_BATTERY_POWER, null,
            MobileRemoteProto.DEFAULT_VELOCITY, MobileRemoteProto.DEFAULT_ACCELERATION,
            MobileRemoteProto.DEFAULT_JERK);
    }

    public MobileRemoteProto(Vector location, double maxVelocity, double maxAcceleration, double maxJerk) {
        this(MobileRemoteProto.DEFAULT_MOBILE_REMOTE_TYPE, location, RemoteProto.DEFAULT_BATTERY_POWER, null,
            maxVelocity, maxAcceleration, maxJerk);
    }

    public MobileRemoteProto(Vector location, double maxBatteryPower) {
        this(MobileRemoteProto.DEFAULT_MOBILE_REMOTE_TYPE, location, maxBatteryPower, null,
            MobileRemoteProto.DEFAULT_VELOCITY, MobileRemoteProto.DEFAULT_ACCELERATION, MobileRemoteProto.DEFAULT_JERK);
    }

    public MobileRemoteProto(Vector location, double maxBatteryPower, double maxVelocity, double maxAcceleration,
            double maxJerk) {
        this(MobileRemoteProto.DEFAULT_MOBILE_REMOTE_TYPE, location, maxBatteryPower, null,
            MobileRemoteProto.DEFAULT_VELOCITY, MobileRemoteProto.DEFAULT_ACCELERATION, MobileRemoteProto.DEFAULT_JERK);
    }

    public MobileRemoteProto(double maxBatteryPower, Collection<SensorConfig> sensors) {
        this(MobileRemoteProto.DEFAULT_MOBILE_REMOTE_TYPE, null, maxBatteryPower, sensors,
            MobileRemoteProto.DEFAULT_VELOCITY, MobileRemoteProto.DEFAULT_ACCELERATION, MobileRemoteProto.DEFAULT_JERK);
    }

    public MobileRemoteProto(double maxBatteryPower, Collection<SensorConfig> sensors, double maxVelocity,
            double maxAcceleration, double maxJerk) {
        this(MobileRemoteProto.DEFAULT_MOBILE_REMOTE_TYPE, null, maxBatteryPower, sensors, maxVelocity, maxAcceleration,
            maxJerk);
    }

    public MobileRemoteProto(Vector location, double maxBatteryPower, Collection<SensorConfig> sensors) {
        this(MobileRemoteProto.DEFAULT_MOBILE_REMOTE_TYPE, location, maxBatteryPower, sensors,
            MobileRemoteProto.DEFAULT_VELOCITY, MobileRemoteProto.DEFAULT_ACCELERATION,
            MobileRemoteProto.DEFAULT_JERK);
    }

    public MobileRemoteProto(Vector location, double maxBatteryPower, Collection<SensorConfig> sensors,
            double maxVelocity, double maxAcceleration, double maxJerk) {
        this(MobileRemoteProto.DEFAULT_MOBILE_REMOTE_TYPE, location, maxBatteryPower, sensors, maxVelocity,
            maxAcceleration, maxJerk);
    }

    public MobileRemoteProto(RemoteType remoteType) {
        this(remoteType, null, RemoteProto.DEFAULT_BATTERY_POWER, null, MobileRemoteProto.DEFAULT_VELOCITY,
            MobileRemoteProto.DEFAULT_ACCELERATION, MobileRemoteProto.DEFAULT_JERK);
    }

    public MobileRemoteProto(RemoteType remoteType, double maxVelocity, double maxAcceleration, double maxJerk) {
        this(remoteType, null, RemoteProto.DEFAULT_BATTERY_POWER, null, maxVelocity, maxAcceleration, maxJerk);
    }

    public MobileRemoteProto(RemoteType remoteType, double maxBatteryPower) {
        this(remoteType, null, maxBatteryPower, null, MobileRemoteProto.DEFAULT_VELOCITY,
            MobileRemoteProto.DEFAULT_ACCELERATION, MobileRemoteProto.DEFAULT_JERK);
    }

    public MobileRemoteProto(RemoteType remoteType, double maxBatteryPower, double maxVelocity, double maxAcceleration,
            double maxJerk) {
        this(remoteType, null, maxBatteryPower, null, maxVelocity, maxAcceleration, maxJerk);
    }

    public MobileRemoteProto(RemoteType remoteType, Vector location) {
        this(remoteType, location, RemoteProto.DEFAULT_BATTERY_POWER, null, MobileRemoteProto.DEFAULT_VELOCITY,
            MobileRemoteProto.DEFAULT_ACCELERATION, MobileRemoteProto.DEFAULT_JERK);
    }

    public MobileRemoteProto(RemoteType remoteType, Vector location, double maxVelocity, double maxAcceleration,
            double maxJerk) {
        this(remoteType, location, RemoteProto.DEFAULT_BATTERY_POWER, null, maxVelocity, maxAcceleration, maxJerk);
    }

    public MobileRemoteProto(RemoteType remoteType, Vector location, double maxBatteryPower) {
        this(remoteType, location, maxBatteryPower, null, MobileRemoteProto.DEFAULT_VELOCITY,
            MobileRemoteProto.DEFAULT_ACCELERATION, MobileRemoteProto.DEFAULT_JERK);
    }

    public MobileRemoteProto(RemoteType remoteType, Vector location, double maxBatteryPower, double maxVelocity,
            double maxAcceleration, double maxJerk) {
        this(remoteType, location, maxBatteryPower, null, maxVelocity, maxAcceleration, maxJerk);
    }

    public MobileRemoteProto(RemoteType remoteType, double maxBatteryPower, Collection<SensorConfig> sensors) {
        this(remoteType, null, maxBatteryPower, sensors, MobileRemoteProto.DEFAULT_VELOCITY,
            MobileRemoteProto.DEFAULT_ACCELERATION, MobileRemoteProto.DEFAULT_JERK);
    }

    public MobileRemoteProto(RemoteType remoteType, double maxBatteryPower, Collection<SensorConfig> sensors,
            double maxVelocity, double maxAcceleration, double maxJerk) {
        this(remoteType, null, maxBatteryPower, sensors, maxVelocity, maxAcceleration, maxJerk);
    }

    public MobileRemoteProto(RemoteType remoteType, Vector location, double maxBatteryPower,
            Collection<SensorConfig> sensors) {
        this(remoteType, location, maxBatteryPower, sensors, MobileRemoteProto.DEFAULT_VELOCITY,
            MobileRemoteProto.DEFAULT_ACCELERATION, MobileRemoteProto.DEFAULT_JERK);
    }

    public MobileRemoteProto(RemoteType remoteType, Vector location, double maxBatteryPower,
            Collection<SensorConfig> sensors, double maxVelocity, double maxAcceleration, double maxJerk) {
        super(remoteType, location, maxBatteryPower, sensors);
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
    protected JSONObjectBuilder getJSONBuilder() {
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

    public boolean isKinetic() {
        return this.hasVelocity() && this.hasAcceleration() && this.hasJerk();
    }

    public boolean isStatic() {
        return !this.isKinetic();
    }

    public boolean equals(MobileRemoteProto proto) {
        if (proto == null) return false;
        return super.equals(proto) && this.maxVelocity == proto.maxVelocity &&
            this.maxAcceleration == proto.maxAcceleration && this.maxJerk == proto.maxJerk;
    }

}
