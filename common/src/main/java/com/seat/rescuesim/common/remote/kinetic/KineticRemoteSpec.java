package com.seat.rescuesim.common.remote.kinetic;

import java.util.Collection;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.JSONObjectBuilder;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.remote.RemoteSpec;
import com.seat.rescuesim.common.remote.RemoteType;
import com.seat.rescuesim.common.sensor.SensorConfig;

/** A serializable specification of a kinetic Remote. */
public class KineticRemoteSpec extends RemoteSpec {
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

    public KineticRemoteSpec() {
        this(KineticRemoteSpec.DEFAULT_SPEC_TYPE, null, RemoteSpec.DEFAULT_BATTERY_POWER, null,
            KineticRemoteSpec.DEFAULT_VELOCITY, KineticRemoteSpec.DEFAULT_ACCELERATION, KineticRemoteSpec.DEFAULT_JERK);
    }

    public KineticRemoteSpec(double maxVelocity, double maxAcceleration, double maxJerk) {
        this(KineticRemoteSpec.DEFAULT_SPEC_TYPE, null, RemoteSpec.DEFAULT_BATTERY_POWER, null, maxVelocity,
            maxAcceleration, maxJerk);
    }

    public KineticRemoteSpec(double maxBatteryPower) {
        this(KineticRemoteSpec.DEFAULT_SPEC_TYPE, null, maxBatteryPower, null, KineticRemoteSpec.DEFAULT_VELOCITY,
            KineticRemoteSpec.DEFAULT_ACCELERATION, KineticRemoteSpec.DEFAULT_JERK);
    }

    public KineticRemoteSpec(double maxBatteryPower, double maxVelocity, double maxAcceleration, double maxJerk) {
        this(KineticRemoteSpec.DEFAULT_SPEC_TYPE, null, maxBatteryPower, null, maxVelocity, maxAcceleration, maxJerk);
    }

    public KineticRemoteSpec(Vector location) {
        this(KineticRemoteSpec.DEFAULT_SPEC_TYPE, location, RemoteSpec.DEFAULT_BATTERY_POWER, null,
            KineticRemoteSpec.DEFAULT_VELOCITY, KineticRemoteSpec.DEFAULT_ACCELERATION, KineticRemoteSpec.DEFAULT_JERK);
    }

    public KineticRemoteSpec(Vector location, double maxVelocity, double maxAcceleration, double maxJerk) {
        this(KineticRemoteSpec.DEFAULT_SPEC_TYPE, location, RemoteSpec.DEFAULT_BATTERY_POWER, null, maxVelocity,
            maxAcceleration, maxJerk);
    }

    public KineticRemoteSpec(Vector location, double maxBatteryPower) {
        this(KineticRemoteSpec.DEFAULT_SPEC_TYPE, location, maxBatteryPower, null, KineticRemoteSpec.DEFAULT_VELOCITY,
            KineticRemoteSpec.DEFAULT_ACCELERATION, KineticRemoteSpec.DEFAULT_JERK);
    }

    public KineticRemoteSpec(Vector location, double maxBatteryPower, double maxVelocity, double maxAcceleration,
            double maxJerk) {
        this(KineticRemoteSpec.DEFAULT_SPEC_TYPE, location, maxBatteryPower, null, KineticRemoteSpec.DEFAULT_VELOCITY,
            KineticRemoteSpec.DEFAULT_ACCELERATION, KineticRemoteSpec.DEFAULT_JERK);
    }

    public KineticRemoteSpec(double maxBatteryPower, Collection<SensorConfig> sensors) {
        this(KineticRemoteSpec.DEFAULT_SPEC_TYPE, null, maxBatteryPower, sensors, KineticRemoteSpec.DEFAULT_VELOCITY,
            KineticRemoteSpec.DEFAULT_ACCELERATION, KineticRemoteSpec.DEFAULT_JERK);
    }

    public KineticRemoteSpec(double maxBatteryPower, Collection<SensorConfig> sensors, double maxVelocity,
            double maxAcceleration, double maxJerk) {
        this(KineticRemoteSpec.DEFAULT_SPEC_TYPE, null, maxBatteryPower, sensors, maxVelocity, maxAcceleration,
            maxJerk);
    }

    public KineticRemoteSpec(Vector location, double maxBatteryPower, Collection<SensorConfig> sensors) {
        this(KineticRemoteSpec.DEFAULT_SPEC_TYPE, location, maxBatteryPower, sensors,
            KineticRemoteSpec.DEFAULT_VELOCITY, KineticRemoteSpec.DEFAULT_ACCELERATION, KineticRemoteSpec.DEFAULT_JERK);
    }

    public KineticRemoteSpec(Vector location, double maxBatteryPower, Collection<SensorConfig> sensors,
            double maxVelocity, double maxAcceleration, double maxJerk) {
        this(KineticRemoteSpec.DEFAULT_SPEC_TYPE, location, maxBatteryPower, sensors, maxVelocity, maxAcceleration,
            maxJerk);
    }

    public KineticRemoteSpec(KineticRemoteType specType) {
        this(specType, null, RemoteSpec.DEFAULT_BATTERY_POWER, null, KineticRemoteSpec.DEFAULT_VELOCITY,
            KineticRemoteSpec.DEFAULT_ACCELERATION, KineticRemoteSpec.DEFAULT_JERK);
    }

    public KineticRemoteSpec(KineticRemoteType specType, double maxVelocity, double maxAcceleration, double maxJerk) {
        this(specType, null, RemoteSpec.DEFAULT_BATTERY_POWER, null, maxVelocity, maxAcceleration, maxJerk);
    }

    public KineticRemoteSpec(KineticRemoteType specType, double maxBatteryPower) {
        this(specType, null, maxBatteryPower, null, KineticRemoteSpec.DEFAULT_VELOCITY,
            KineticRemoteSpec.DEFAULT_ACCELERATION, KineticRemoteSpec.DEFAULT_JERK);
    }

    public KineticRemoteSpec(KineticRemoteType specType, double maxBatteryPower, double maxVelocity,
            double maxAcceleration, double maxJerk) {
        this(specType, null, maxBatteryPower, null, maxVelocity, maxAcceleration, maxJerk);
    }

    public KineticRemoteSpec(KineticRemoteType specType, Vector location) {
        this(specType, location, RemoteSpec.DEFAULT_BATTERY_POWER, null, KineticRemoteSpec.DEFAULT_VELOCITY,
            KineticRemoteSpec.DEFAULT_ACCELERATION, KineticRemoteSpec.DEFAULT_JERK);
    }

    public KineticRemoteSpec(KineticRemoteType specType, Vector location, double maxVelocity, double maxAcceleration,
            double maxJerk) {
        this(specType, location, RemoteSpec.DEFAULT_BATTERY_POWER, null, maxVelocity, maxAcceleration, maxJerk);
    }

    public KineticRemoteSpec(KineticRemoteType specType, Vector location, double maxBatteryPower) {
        this(specType, location, maxBatteryPower, null, KineticRemoteSpec.DEFAULT_VELOCITY,
            KineticRemoteSpec.DEFAULT_ACCELERATION, KineticRemoteSpec.DEFAULT_JERK);
    }

    public KineticRemoteSpec(KineticRemoteType specType, Vector location, double maxBatteryPower, double maxVelocity,
            double maxAcceleration, double maxJerk) {
        this(specType, location, maxBatteryPower, null, maxVelocity, maxAcceleration, maxJerk);
    }

    public KineticRemoteSpec(KineticRemoteType specType, double maxBatteryPower, Collection<SensorConfig> sensors) {
        this(specType, null, maxBatteryPower, sensors, KineticRemoteSpec.DEFAULT_VELOCITY,
            KineticRemoteSpec.DEFAULT_ACCELERATION, KineticRemoteSpec.DEFAULT_JERK);
    }

    public KineticRemoteSpec(KineticRemoteType specType, double maxBatteryPower, Collection<SensorConfig> sensors,
            double maxVelocity, double maxAcceleration, double maxJerk) {
        this(specType, null, maxBatteryPower, sensors, maxVelocity, maxAcceleration, maxJerk);
    }

    public KineticRemoteSpec(KineticRemoteType specType, Vector location, double maxBatteryPower,
            Collection<SensorConfig> sensors) {
        this(specType, location, maxBatteryPower, sensors, KineticRemoteSpec.DEFAULT_VELOCITY,
            KineticRemoteSpec.DEFAULT_ACCELERATION, KineticRemoteSpec.DEFAULT_JERK);
    }

    public KineticRemoteSpec(KineticRemoteType specType, Vector location, double maxBatteryPower,
            Collection<SensorConfig> sensors, double maxVelocity, double maxAcceleration, double maxJerk) {
        super(RemoteType.KINETIC, location, maxBatteryPower, sensors);
        this.specType = specType;
        this.maxVelocity = maxVelocity;
        this.maxAcceleration = maxAcceleration;
        this.maxJerk = maxJerk;
    }

    public KineticRemoteSpec(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        super.decode(json);
        this.specType = KineticRemoteType.decodeType(json);
        if (json.hasKey(KineticRemoteSpec.MAX_VELOCITY)) {
            this.maxVelocity = json.getDouble(KineticRemoteSpec.MAX_VELOCITY);
        } else {
            this.maxVelocity = Double.POSITIVE_INFINITY;
        }
        if (json.hasKey(KineticRemoteSpec.MAX_ACCELERATION)) {
            this.maxAcceleration = json.getDouble(KineticRemoteSpec.MAX_ACCELERATION);
        } else {
            this.maxAcceleration = Double.POSITIVE_INFINITY;
        }
        if (json.hasKey(KineticRemoteSpec.MAX_JERK)) {
            this.maxJerk = json.getDouble(KineticRemoteSpec.MAX_JERK);
        } else {
            this.maxJerk = Double.POSITIVE_INFINITY;
        }
    }

    @Override
    protected JSONObjectBuilder getJSONBuilder() {
        JSONObjectBuilder json = super.getJSONBuilder();
        json.put(KineticRemoteType.KINETIC_REMOTE_TYPE, this.specType.getType());
        if (this.hasMaxAcceleration()) {
            json.put(KineticRemoteSpec.MAX_VELOCITY, this.maxVelocity);
        }
        if (this.hasMaxJerk()) {
            json.put(KineticRemoteSpec.MAX_ACCELERATION, this.maxAcceleration);
        }
        if (this.hasMaxVelocity()) {
            json.put(KineticRemoteSpec.MAX_JERK, this.maxJerk);
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

    public boolean equals(KineticRemoteSpec spec) {
        return super.equals(spec) && this.specType.equals(spec.specType) && this.maxVelocity == spec.maxVelocity &&
            this.maxAcceleration == spec.maxAcceleration && this.maxJerk == spec.maxJerk;
    }

}
