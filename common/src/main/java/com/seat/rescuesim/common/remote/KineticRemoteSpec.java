package com.seat.rescuesim.common.remote;

import java.util.ArrayList;

import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.JSONObjectBuilder;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.sensor.SensorConfig;
import com.seat.rescuesim.common.util.SerializableEnum;

/** A serializable specification of a kinetic Remote. */
public abstract class KineticRemoteSpec extends RemoteSpec {
    private static final String MAX_ACCELERATION = "max_acceleration";
    private static final String MAX_JERK = "max_jerk";
    private static final String MAX_VELOCITY = "max_velocity";

    protected double maxAcceleration;
    protected double maxJerk;
    protected double maxVelocity;

    public KineticRemoteSpec(JSONObject json) {
        super(json);
    }

    public KineticRemoteSpec(JSONOption option) {
        super(option);
    }

    public KineticRemoteSpec(String encoding) {
        super(encoding);
    }

    public KineticRemoteSpec(RemoteType type, Vector location, double maxBatteryPower,
            ArrayList<SensorConfig> sensors, double maxVelocity, double maxAcceleration, double maxJerk) {
        super(type, location, maxBatteryPower, sensors);
        this.maxVelocity = maxVelocity;
        this.maxAcceleration = maxAcceleration;
        this.maxJerk = maxJerk;
    }

    @Override
    protected void decode(JSONObject json) {
        super.decode(json);
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

    public abstract SerializableEnum getSpecType();

    @Override
    protected JSONObjectBuilder getJSONBuilder() {
        JSONObjectBuilder json = super.getJSONBuilder();
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

    public abstract JSONOption toJSON();

    public boolean equals(KineticRemoteSpec spec) {
        return super.equals(spec) && this.maxVelocity == spec.maxVelocity &&
            this.maxAcceleration == spec.maxAcceleration && this.maxJerk == spec.maxJerk;
    }

}
