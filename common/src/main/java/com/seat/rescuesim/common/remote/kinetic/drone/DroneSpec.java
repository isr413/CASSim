package com.seat.rescuesim.common.remote.kinetic.drone;

import java.util.Collection;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.JSONObjectBuilder;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.remote.RemoteProto;
import com.seat.rescuesim.common.remote.kinetic.KineticRemoteSpec;
import com.seat.rescuesim.common.remote.kinetic.KineticRemoteType;
import com.seat.rescuesim.common.sensor.SensorConfig;

/** A serializable specification of a Drone Remote. */
public class DroneSpec extends KineticRemoteSpec {
    public static final String BATTERY_USAGE = "battery_usage";

    private Vector batteryUsage; // [static (hovering), horizontal movement, vertical movement]

    public DroneSpec(Vector batteryUsage) {
        this(null, RemoteProto.DEFAULT_BATTERY_POWER, null, batteryUsage, KineticRemoteSpec.DEFAULT_VELOCITY,
            KineticRemoteSpec.DEFAULT_ACCELERATION, KineticRemoteSpec.DEFAULT_JERK);
    }

    public DroneSpec(Vector batteryUsage, double maxVelocity, double maxAcceleration, double maxJerk) {
        this(null, RemoteProto.DEFAULT_BATTERY_POWER, null, batteryUsage, maxVelocity, maxAcceleration, maxJerk);
    }

    public DroneSpec(double maxBatteryPower, Vector batteryUsage) {
        this(null, maxBatteryPower, null, batteryUsage, KineticRemoteSpec.DEFAULT_VELOCITY,
            KineticRemoteSpec.DEFAULT_ACCELERATION, KineticRemoteSpec.DEFAULT_JERK);
    }

    public DroneSpec(double maxBatteryPower, Vector batteryUsage, double maxVelocity, double maxAcceleration,
            double maxJerk) {
        this(null, maxBatteryPower, null, batteryUsage, maxVelocity, maxAcceleration, maxJerk);
    }

    public DroneSpec(Vector location, Vector batteryUsage) {
        this(location, RemoteProto.DEFAULT_BATTERY_POWER, null, batteryUsage, KineticRemoteSpec.DEFAULT_VELOCITY,
            KineticRemoteSpec.DEFAULT_ACCELERATION, KineticRemoteSpec.DEFAULT_JERK);
    }

    public DroneSpec(Vector location, Vector batteryUsage, double maxVelocity, double maxAcceleration, double maxJerk) {
        this(location, RemoteProto.DEFAULT_BATTERY_POWER, null, batteryUsage, maxVelocity, maxAcceleration, maxJerk);
    }

    public DroneSpec(Vector location, double maxBatteryPower, Vector batteryUsage) {
        this(location, maxBatteryPower, null, batteryUsage, KineticRemoteSpec.DEFAULT_VELOCITY,
            KineticRemoteSpec.DEFAULT_ACCELERATION, KineticRemoteSpec.DEFAULT_JERK);
    }

    public DroneSpec(Vector location, double maxBatteryPower, Vector batteryUsage, double maxVelocity,
            double maxAcceleration, double maxJerk) {
        this(location, maxBatteryPower, null, batteryUsage, maxVelocity, maxAcceleration, maxJerk);
    }

    public DroneSpec(double maxBatteryPower, Collection<SensorConfig> sensors, Vector batteryUsage) {
        this(null, maxBatteryPower, sensors, batteryUsage, KineticRemoteSpec.DEFAULT_VELOCITY,
            KineticRemoteSpec.DEFAULT_ACCELERATION, KineticRemoteSpec.DEFAULT_JERK);
    }

    public DroneSpec(double maxBatteryPower, Collection<SensorConfig> sensors, Vector batteryUsage, double maxVelocity,
            double maxAcceleration, double maxJerk) {
        this(null, maxBatteryPower, sensors, batteryUsage, maxVelocity, maxAcceleration, maxJerk);
    }

    public DroneSpec(Vector location, double maxBatteryPower, Collection<SensorConfig> sensors, Vector batteryUsage) {
        this(location, maxBatteryPower, sensors, batteryUsage, KineticRemoteSpec.DEFAULT_VELOCITY,
            KineticRemoteSpec.DEFAULT_ACCELERATION, KineticRemoteSpec.DEFAULT_JERK);
    }

    public DroneSpec(Vector location, double maxBatteryPower, Collection<SensorConfig> sensors, Vector batteryUsage,
            double maxVelocity, double maxAcceleration, double maxJerk) {
        super(KineticRemoteType.DRONE, location, maxBatteryPower, sensors, maxVelocity, maxAcceleration, maxJerk);
        this.batteryUsage = batteryUsage;
    }

    public DroneSpec(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        super.decode(json);
        this.batteryUsage = new Vector(json.getJSONOption(DroneSpec.BATTERY_USAGE));
    }

    @Override
    protected JSONObjectBuilder getJSONBuilder() {
        JSONObjectBuilder json = super.getJSONBuilder();
        json.put(DroneSpec.BATTERY_USAGE, this.batteryUsage.toJSON());
        return json;
    }

    public Vector getBatteryUsage() {
        return this.batteryUsage;
    }

    public double getHorizontalKineticBatteryUsage() {
        return this.batteryUsage.getY();
    }

    @Override
    public String getLabel() {
        return String.format("d:%s", this.getSpecType().getLabel());
    }

    public double getStaticBatteryUsage() {
        return this.batteryUsage.getX();
    }

    public double getVerticalKineticBatteryUsage() {
        return this.batteryUsage.getZ();
    }

    public boolean equals(DroneSpec spec) {
        return super.equals(spec) && this.batteryUsage.equals(spec.batteryUsage);
    }

}
