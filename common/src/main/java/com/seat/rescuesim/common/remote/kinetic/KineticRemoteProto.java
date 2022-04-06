package com.seat.rescuesim.common.remote.kinetic;

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
public class KineticRemoteProto extends RemoteProto {
    public static final String MAX_ACCELERATION = "max_acceleration";
    public static final String MAX_JERK = "max_jerk";
    public static final String MAX_VELOCITY = "max_velocity";

    protected static final double DEFAULT_ACCELERATION = Double.POSITIVE_INFINITY;
    protected static final double DEFAULT_JERK = Double.POSITIVE_INFINITY;
    protected static final KineticRemoteType DEFAULT_SPEC_TYPE = KineticRemoteType.GENERIC;
    protected static final double DEFAULT_VELOCITY = Double.POSITIVE_INFINITY;

    private double maxAcceleration;
    private double maxJerk;
    private double maxVelocity;
    private KineticRemoteType specType;

    public KineticRemoteProto() {
        this(KineticRemoteProto.DEFAULT_SPEC_TYPE, null, RemoteProto.DEFAULT_BATTERY_POWER, null,
            KineticRemoteProto.DEFAULT_VELOCITY, KineticRemoteProto.DEFAULT_ACCELERATION,
            KineticRemoteProto.DEFAULT_JERK);
    }

    public KineticRemoteProto(double maxVelocity, double maxAcceleration, double maxJerk) {
        this(KineticRemoteProto.DEFAULT_SPEC_TYPE, null, RemoteProto.DEFAULT_BATTERY_POWER, null, maxVelocity,
            maxAcceleration, maxJerk);
    }

    public KineticRemoteProto(double maxBatteryPower) {
        this(KineticRemoteProto.DEFAULT_SPEC_TYPE, null, maxBatteryPower, null, KineticRemoteProto.DEFAULT_VELOCITY,
            KineticRemoteProto.DEFAULT_ACCELERATION, KineticRemoteProto.DEFAULT_JERK);
    }

    public KineticRemoteProto(double maxBatteryPower, double maxVelocity, double maxAcceleration, double maxJerk) {
        this(KineticRemoteProto.DEFAULT_SPEC_TYPE, null, maxBatteryPower, null, maxVelocity, maxAcceleration, maxJerk);
    }

    public KineticRemoteProto(Vector location) {
        this(KineticRemoteProto.DEFAULT_SPEC_TYPE, location, RemoteProto.DEFAULT_BATTERY_POWER, null,
            KineticRemoteProto.DEFAULT_VELOCITY, KineticRemoteProto.DEFAULT_ACCELERATION,
            KineticRemoteProto.DEFAULT_JERK);
    }

    public KineticRemoteProto(Vector location, double maxVelocity, double maxAcceleration, double maxJerk) {
        this(KineticRemoteProto.DEFAULT_SPEC_TYPE, location, RemoteProto.DEFAULT_BATTERY_POWER, null, maxVelocity,
            maxAcceleration, maxJerk);
    }

    public KineticRemoteProto(Vector location, double maxBatteryPower) {
        this(KineticRemoteProto.DEFAULT_SPEC_TYPE, location, maxBatteryPower, null, KineticRemoteProto.DEFAULT_VELOCITY,
            KineticRemoteProto.DEFAULT_ACCELERATION, KineticRemoteProto.DEFAULT_JERK);
    }

    public KineticRemoteProto(Vector location, double maxBatteryPower, double maxVelocity, double maxAcceleration,
            double maxJerk) {
        this(KineticRemoteProto.DEFAULT_SPEC_TYPE, location, maxBatteryPower, null, KineticRemoteProto.DEFAULT_VELOCITY,
            KineticRemoteProto.DEFAULT_ACCELERATION, KineticRemoteProto.DEFAULT_JERK);
    }

    public KineticRemoteProto(double maxBatteryPower, Collection<SensorConfig> sensors) {
        this(KineticRemoteProto.DEFAULT_SPEC_TYPE, null, maxBatteryPower, sensors, KineticRemoteProto.DEFAULT_VELOCITY,
            KineticRemoteProto.DEFAULT_ACCELERATION, KineticRemoteProto.DEFAULT_JERK);
    }

    public KineticRemoteProto(double maxBatteryPower, Collection<SensorConfig> sensors, double maxVelocity,
            double maxAcceleration, double maxJerk) {
        this(KineticRemoteProto.DEFAULT_SPEC_TYPE, null, maxBatteryPower, sensors, maxVelocity, maxAcceleration,
            maxJerk);
    }

    public KineticRemoteProto(Vector location, double maxBatteryPower, Collection<SensorConfig> sensors) {
        this(KineticRemoteProto.DEFAULT_SPEC_TYPE, location, maxBatteryPower, sensors,
            KineticRemoteProto.DEFAULT_VELOCITY, KineticRemoteProto.DEFAULT_ACCELERATION,
            KineticRemoteProto.DEFAULT_JERK);
    }

    public KineticRemoteProto(Vector location, double maxBatteryPower, Collection<SensorConfig> sensors,
            double maxVelocity, double maxAcceleration, double maxJerk) {
        this(KineticRemoteProto.DEFAULT_SPEC_TYPE, location, maxBatteryPower, sensors, maxVelocity, maxAcceleration,
            maxJerk);
    }

    public KineticRemoteProto(KineticRemoteType specType) {
        this(specType, null, RemoteProto.DEFAULT_BATTERY_POWER, null, KineticRemoteProto.DEFAULT_VELOCITY,
            KineticRemoteProto.DEFAULT_ACCELERATION, KineticRemoteProto.DEFAULT_JERK);
    }

    public KineticRemoteProto(KineticRemoteType specType, double maxVelocity, double maxAcceleration, double maxJerk) {
        this(specType, null, RemoteProto.DEFAULT_BATTERY_POWER, null, maxVelocity, maxAcceleration, maxJerk);
    }

    public KineticRemoteProto(KineticRemoteType specType, double maxBatteryPower) {
        this(specType, null, maxBatteryPower, null, KineticRemoteProto.DEFAULT_VELOCITY,
            KineticRemoteProto.DEFAULT_ACCELERATION, KineticRemoteProto.DEFAULT_JERK);
    }

    public KineticRemoteProto(KineticRemoteType specType, double maxBatteryPower, double maxVelocity,
            double maxAcceleration, double maxJerk) {
        this(specType, null, maxBatteryPower, null, maxVelocity, maxAcceleration, maxJerk);
    }

    public KineticRemoteProto(KineticRemoteType specType, Vector location) {
        this(specType, location, RemoteProto.DEFAULT_BATTERY_POWER, null, KineticRemoteProto.DEFAULT_VELOCITY,
            KineticRemoteProto.DEFAULT_ACCELERATION, KineticRemoteProto.DEFAULT_JERK);
    }

    public KineticRemoteProto(KineticRemoteType specType, Vector location, double maxVelocity, double maxAcceleration,
            double maxJerk) {
        this(specType, location, RemoteProto.DEFAULT_BATTERY_POWER, null, maxVelocity, maxAcceleration, maxJerk);
    }

    public KineticRemoteProto(KineticRemoteType specType, Vector location, double maxBatteryPower) {
        this(specType, location, maxBatteryPower, null, KineticRemoteProto.DEFAULT_VELOCITY,
            KineticRemoteProto.DEFAULT_ACCELERATION, KineticRemoteProto.DEFAULT_JERK);
    }

    public KineticRemoteProto(KineticRemoteType specType, Vector location, double maxBatteryPower, double maxVelocity,
            double maxAcceleration, double maxJerk) {
        this(specType, location, maxBatteryPower, null, maxVelocity, maxAcceleration, maxJerk);
    }

    public KineticRemoteProto(KineticRemoteType specType, double maxBatteryPower, Collection<SensorConfig> sensors) {
        this(specType, null, maxBatteryPower, sensors, KineticRemoteProto.DEFAULT_VELOCITY,
            KineticRemoteProto.DEFAULT_ACCELERATION, KineticRemoteProto.DEFAULT_JERK);
    }

    public KineticRemoteProto(KineticRemoteType specType, double maxBatteryPower, Collection<SensorConfig> sensors,
            double maxVelocity, double maxAcceleration, double maxJerk) {
        this(specType, null, maxBatteryPower, sensors, maxVelocity, maxAcceleration, maxJerk);
    }

    public KineticRemoteProto(KineticRemoteType specType, Vector location, double maxBatteryPower,
            Collection<SensorConfig> sensors) {
        this(specType, location, maxBatteryPower, sensors, KineticRemoteProto.DEFAULT_VELOCITY,
            KineticRemoteProto.DEFAULT_ACCELERATION, KineticRemoteProto.DEFAULT_JERK);
    }

    public KineticRemoteProto(KineticRemoteType specType, Vector location, double maxBatteryPower,
            Collection<SensorConfig> sensors, double maxVelocity, double maxAcceleration, double maxJerk) {
        super(RemoteType.KINETIC, location, maxBatteryPower, sensors);
        this.specType = specType;
        this.maxVelocity = maxVelocity;
        this.maxAcceleration = maxAcceleration;
        this.maxJerk = maxJerk;
    }

    public KineticRemoteProto(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        super.decode(json);
        this.specType = KineticRemoteType.decodeType(json);
        this.maxVelocity = (json.hasKey(KineticRemoteProto.MAX_VELOCITY)) ?
            this.maxVelocity = json.getDouble(KineticRemoteProto.MAX_VELOCITY) :
            Double.POSITIVE_INFINITY;
        this.maxAcceleration = (json.hasKey(KineticRemoteProto.MAX_ACCELERATION)) ?
            this.maxAcceleration = json.getDouble(KineticRemoteProto.MAX_ACCELERATION) :
            Double.POSITIVE_INFINITY;
        this.maxJerk = (json.hasKey(KineticRemoteProto.MAX_JERK)) ?
            this.maxJerk = json.getDouble(KineticRemoteProto.MAX_JERK) :
            Double.POSITIVE_INFINITY;
    }

    @Override
    protected JSONObjectBuilder getJSONBuilder() {
        JSONObjectBuilder json = super.getJSONBuilder();
        json.put(KineticRemoteType.KINETIC_REMOTE_TYPE, this.specType.getType());
        if (this.hasMaxAcceleration()) {
            json.put(KineticRemoteProto.MAX_VELOCITY, this.maxVelocity);
        }
        if (this.hasMaxJerk()) {
            json.put(KineticRemoteProto.MAX_ACCELERATION, this.maxAcceleration);
        }
        if (this.hasMaxVelocity()) {
            json.put(KineticRemoteProto.MAX_JERK, this.maxJerk);
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

    @Override
    public KineticRemoteType getSpecType() {
        return this.specType;
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

    public boolean equals(KineticRemoteProto proto) {
        if (proto == null) return false;
        return super.equals(proto) && this.specType.equals(proto.specType) && this.maxVelocity == proto.maxVelocity &&
            this.maxAcceleration == proto.maxAcceleration && this.maxJerk == proto.maxJerk;
    }

}
