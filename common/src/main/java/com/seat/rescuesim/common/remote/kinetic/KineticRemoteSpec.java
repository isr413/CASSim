package com.seat.rescuesim.common.remote.kinetic;

import java.util.ArrayList;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.JSONObjectBuilder;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.remote.RemoteSpec;
import com.seat.rescuesim.common.remote.RemoteType;
import com.seat.rescuesim.common.sensor.SensorConfig;

/** A serializable specification of a kinetic Remote. */
public abstract class KineticRemoteSpec extends RemoteSpec {

    protected double maxAcceleration;
    protected double maxJerk;
    protected double maxVelocity;
    protected KineticRemoteType type;

    public KineticRemoteSpec(KineticRemoteType type) {
        this(type, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    public KineticRemoteSpec(KineticRemoteType type, double maxVelocity, double maxAcceleration, double maxJerk) {
        super(RemoteType.KINETIC);
        this.type = type;
        this.maxVelocity = maxVelocity;
        this.maxAcceleration = maxAcceleration;
        this.maxJerk = maxJerk;
    }

    public KineticRemoteSpec(KineticRemoteType type, Vector location) {
        this(type, location, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    public KineticRemoteSpec(KineticRemoteType type, Vector location, double maxVelocity, double maxAcceleration,
            double maxJerk) {
        super(RemoteType.KINETIC, location);
        this.type = type;
        this.maxVelocity = maxVelocity;
        this.maxAcceleration = maxAcceleration;
        this.maxJerk = maxJerk;
    }

    public KineticRemoteSpec(KineticRemoteType type, double maxBatteryPower, ArrayList<SensorConfig> sensors) {
        this(type, maxBatteryPower, sensors, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY,
            Double.POSITIVE_INFINITY);
    }

    public KineticRemoteSpec(KineticRemoteType type, double maxBatteryPower, ArrayList<SensorConfig> sensors,
            double maxVelocity, double maxAcceleration, double maxJerk) {
        super(RemoteType.KINETIC, maxBatteryPower, sensors);
        this.type = type;
        this.maxVelocity = maxVelocity;
        this.maxAcceleration = maxAcceleration;
        this.maxJerk = maxJerk;
    }

    public KineticRemoteSpec(KineticRemoteType type, Vector location, double maxBatteryPower,
            ArrayList<SensorConfig> sensors) {
        this(type, location, maxBatteryPower, sensors, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY,
            Double.POSITIVE_INFINITY);
    }

    public KineticRemoteSpec(KineticRemoteType type, Vector location, double maxBatteryPower,
            ArrayList<SensorConfig> sensors, double maxVelocity, double maxAcceleration, double maxJerk) {
        super(RemoteType.KINETIC, location, maxBatteryPower, sensors);
        this.type = type;
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
        this.type = KineticRemoteType.decodeType(json);
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

    public abstract String getLabel();

    public double getMaxAcceleration() {
        return this.maxAcceleration;
    }

    public double getMaxJerk() {
        return this.maxJerk;
    }

    public double getMaxVelocity() {
        return this.maxVelocity;
    }

    public KineticRemoteType getSpecType() {
        return this.type;
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

    @Override
    protected JSONObjectBuilder getJSONBuilder() {
        JSONObjectBuilder json = super.getJSONBuilder();
        json.put(KineticRemoteConst.KINETIC_REMOTE_TYPE, this.type.getType());
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

    public boolean equals(KineticRemoteSpec spec) {
        return super.equals(spec) && this.type.equals(spec.type) && this.maxVelocity == spec.maxVelocity &&
            this.maxAcceleration == spec.maxAcceleration && this.maxJerk == spec.maxJerk;
    }

}
