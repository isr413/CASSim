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

    protected double maxAcceleration;
    protected double maxJerk;
    protected double maxVelocity;
    protected KineticRemoteType specType;

    public KineticRemoteSpec() {
        this(KineticRemoteType.GENERIC);
    }

    public KineticRemoteSpec(double maxVelocity, double maxAcceleration, double maxJerk) {
        this(KineticRemoteType.GENERIC, maxVelocity, maxAcceleration, maxJerk);
    }

    public KineticRemoteSpec(double maxBatteryPower) {
        this(KineticRemoteType.GENERIC, maxBatteryPower);
    }

    public KineticRemoteSpec(double maxBatteryPower, double maxVelocity, double maxAcceleration, double maxJerk) {
        this(KineticRemoteType.GENERIC, maxBatteryPower, maxVelocity, maxAcceleration, maxJerk);
    }

    public KineticRemoteSpec(Vector location) {
        this(KineticRemoteType.GENERIC, location);
    }

    public KineticRemoteSpec(Vector location, double maxVelocity, double maxAcceleration, double maxJerk) {
        this(KineticRemoteType.GENERIC, location, maxVelocity, maxAcceleration, maxJerk);
    }

    public KineticRemoteSpec(Vector location, double maxBatteryPower) {
        this(KineticRemoteType.GENERIC, location, maxBatteryPower);
    }

    public KineticRemoteSpec(Vector location, double maxBatteryPower, double maxVelocity, double maxAcceleration,
            double maxJerk) {
        this(KineticRemoteType.GENERIC, location, maxBatteryPower, maxVelocity, maxAcceleration, maxJerk);
    }

    public KineticRemoteSpec(double maxBatteryPower, Collection<SensorConfig> sensors) {
        this(KineticRemoteType.GENERIC, maxBatteryPower, sensors);
    }

    public KineticRemoteSpec(double maxBatteryPower, Collection<SensorConfig> sensors, double maxVelocity,
            double maxAcceleration, double maxJerk) {
        this(KineticRemoteType.GENERIC, maxBatteryPower, sensors, maxVelocity, maxAcceleration, maxJerk);
    }

    public KineticRemoteSpec(Vector location, double maxBatteryPower, Collection<SensorConfig> sensors) {
        this(KineticRemoteType.GENERIC, location, maxBatteryPower, sensors);
    }

    public KineticRemoteSpec(Vector location, double maxBatteryPower, Collection<SensorConfig> sensors,
            double maxVelocity, double maxAcceleration, double maxJerk) {
        this(KineticRemoteType.GENERIC, location, maxBatteryPower, sensors, maxVelocity, maxAcceleration, maxJerk);
    }

    public KineticRemoteSpec(KineticRemoteType specType) {
        this(specType, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    public KineticRemoteSpec(KineticRemoteType specType, double maxVelocity, double maxAcceleration,
            double maxJerk) {
        super(RemoteType.KINETIC);
        this.specType = specType;
        this.maxVelocity = maxVelocity;
        this.maxAcceleration = maxAcceleration;
        this.maxJerk = maxJerk;
    }

    public KineticRemoteSpec(KineticRemoteType specType, double maxBatteryPower) {
        this(specType, maxBatteryPower, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    public KineticRemoteSpec(KineticRemoteType specType, double maxBatteryPower, double maxVelocity,
            double maxAcceleration, double maxJerk) {
        super(RemoteType.KINETIC, maxBatteryPower);
        this.specType = specType;
        this.maxVelocity = maxVelocity;
        this.maxAcceleration = maxAcceleration;
        this.maxJerk = maxJerk;
    }

    public KineticRemoteSpec(KineticRemoteType specType, Vector location) {
        this(specType, location, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    public KineticRemoteSpec(KineticRemoteType specType, Vector location, double maxVelocity, double maxAcceleration,
            double maxJerk) {
        super(RemoteType.KINETIC, location);
        this.specType = specType;
        this.maxVelocity = maxVelocity;
        this.maxAcceleration = maxAcceleration;
        this.maxJerk = maxJerk;
    }

    public KineticRemoteSpec(KineticRemoteType specType, Vector location, double maxBatteryPower) {
        this(specType, location, maxBatteryPower, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY,
            Double.POSITIVE_INFINITY);
    }

    public KineticRemoteSpec(KineticRemoteType specType, Vector location, double maxBatteryPower, double maxVelocity,
            double maxAcceleration, double maxJerk) {
        super(RemoteType.KINETIC, location, maxBatteryPower);
        this.specType = specType;
        this.maxVelocity = maxVelocity;
        this.maxAcceleration = maxAcceleration;
        this.maxJerk = maxJerk;
    }

    public KineticRemoteSpec(KineticRemoteType specType, double maxBatteryPower, Collection<SensorConfig> sensors) {
        this(specType, maxBatteryPower, sensors, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY,
            Double.POSITIVE_INFINITY);
    }

    public KineticRemoteSpec(KineticRemoteType specType, double maxBatteryPower, Collection<SensorConfig> sensors,
            double maxVelocity, double maxAcceleration, double maxJerk) {
        super(RemoteType.KINETIC, maxBatteryPower, sensors);
        this.specType = specType;
        this.maxVelocity = maxVelocity;
        this.maxAcceleration = maxAcceleration;
        this.maxJerk = maxJerk;
    }

    public KineticRemoteSpec(KineticRemoteType specType, Vector location, double maxBatteryPower,
            Collection<SensorConfig> sensors) {
        this(specType, location, maxBatteryPower, sensors, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY,
            Double.POSITIVE_INFINITY);
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
        if (json.hasKey(KineticRemoteConst.MAX_VELOCITY)) {
            this.maxVelocity = json.getDouble(KineticRemoteConst.MAX_VELOCITY);
        } else {
            this.maxVelocity = Double.POSITIVE_INFINITY;
        }
        if (json.hasKey(KineticRemoteConst.MAX_ACCELERATION)) {
            this.maxAcceleration = json.getDouble(KineticRemoteConst.MAX_ACCELERATION);
        } else {
            this.maxAcceleration = Double.POSITIVE_INFINITY;
        }
        if (json.hasKey(KineticRemoteConst.MAX_JERK)) {
            this.maxJerk = json.getDouble(KineticRemoteConst.MAX_JERK);
        } else {
            this.maxJerk = Double.POSITIVE_INFINITY;
        }
    }

    @Override
    protected JSONObjectBuilder getJSONBuilder() {
        JSONObjectBuilder json = super.getJSONBuilder();
        json.put(KineticRemoteConst.KINETIC_REMOTE_TYPE, this.specType.getType());
        if (this.hasMaxAcceleration()) {
            json.put(KineticRemoteConst.MAX_VELOCITY, this.maxVelocity);
        }
        if (this.hasMaxJerk()) {
            json.put(KineticRemoteConst.MAX_ACCELERATION, this.maxAcceleration);
        }
        if (this.hasMaxVelocity()) {
            json.put(KineticRemoteConst.MAX_JERK, this.maxJerk);
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
