package com.seat.rescuesim.common.remote;

import java.util.ArrayList;

import com.seat.rescuesim.common.json.*;
import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.sensor.SensorConfig;
import com.seat.rescuesim.common.util.SerializableEnum;

/** A serializable specification of a kinetic Remote. */
public abstract class KineticRemoteSpec extends RemoteSpec {

    protected double maxAcceleration;
    protected double maxJerk;
    protected double maxVelocity;

    public KineticRemoteSpec(JSONObject json) throws JSONException {
        super(json);
    }

    public KineticRemoteSpec(JSONOption option) throws JSONException {
        super(option);
    }

    public KineticRemoteSpec(String encoding) throws JSONException {
        super(encoding);
    }

    public KineticRemoteSpec(RemoteType type, Vector location, double maxBatteryPower, double maxVelocity,
            double maxAcceleration, double maxJerk) {
        this(type, location, maxBatteryPower, new ArrayList<SensorConfig>(), maxVelocity, maxAcceleration, maxJerk);
    }

    public KineticRemoteSpec(RemoteType type, Vector location, double maxBatteryPower,
            ArrayList<SensorConfig> sensors, double maxVelocity, double maxAcceleration, double maxJerk) {
        super(type, location, maxBatteryPower, sensors);
        this.maxVelocity = maxVelocity;
        this.maxAcceleration = maxAcceleration;
        this.maxJerk = maxJerk;
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        super.decode(json);
        if (json.hasKey(RemoteConst.MAX_VELOCITY)) {
            this.maxVelocity = json.getDouble(RemoteConst.MAX_VELOCITY);
        } else {
            this.maxVelocity = Double.POSITIVE_INFINITY;
        }
        if (json.hasKey(RemoteConst.MAX_ACCELERATION)) {
            this.maxAcceleration = json.getDouble(RemoteConst.MAX_ACCELERATION);
        } else {
            this.maxAcceleration = Double.POSITIVE_INFINITY;
        }
        if (json.hasKey(RemoteConst.MAX_JERK)) {
            this.maxJerk = json.getDouble(RemoteConst.MAX_JERK);
        } else {
            this.maxJerk = Double.POSITIVE_INFINITY;
        }
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

    @Override
    public boolean isKinetic() {
        return this.hasVelocity() && this.hasAcceleration() && this.hasJerk();
    }

    @Override
    public boolean isStatic() {
        return !this.isKinetic();
    }

    public abstract SerializableEnum getSpecType();

    @Override
    protected JSONObjectBuilder getJSONBuilder() {
        JSONObjectBuilder json = super.getJSONBuilder();
        if (this.hasMaxAcceleration()) {
            json.put(RemoteConst.MAX_VELOCITY, this.maxVelocity);
        }
        if (this.hasMaxJerk()) {
            json.put(RemoteConst.MAX_ACCELERATION, this.maxAcceleration);
        }
        if (this.hasMaxVelocity()) {
            json.put(RemoteConst.MAX_JERK, this.maxJerk);
        }
        return json;
    }

    public abstract JSONOption toJSON();

    public boolean equals(KineticRemoteSpec spec) {
        return super.equals(spec) && this.maxVelocity == spec.maxVelocity &&
            this.maxAcceleration == spec.maxAcceleration && this.maxJerk == spec.maxJerk;
    }

}
