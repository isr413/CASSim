package com.seat.rescuesim.common.remote.kinetic.drone;

import java.util.Collection;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.JSONObjectBuilder;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.remote.kinetic.KineticRemoteSpec;
import com.seat.rescuesim.common.remote.kinetic.KineticRemoteType;
import com.seat.rescuesim.common.sensor.SensorConfig;

/** A serializable specification of a Drone Remote. */
public class DroneSpec extends KineticRemoteSpec {
    public static final String BATTERY_USAGE = "battery_usage";

    private Vector batteryUsage; // [static (hovering), horizontal movement, vertical movement]

    public DroneSpec(Vector batteryUsage) {
        super(KineticRemoteType.DRONE);
        this.batteryUsage = batteryUsage;
    }

    public DroneSpec(Vector batteryUsage, double maxVelocity, double maxAcceleration, double maxJerk) {
        super(KineticRemoteType.DRONE, maxVelocity, maxAcceleration, maxJerk);
        this.batteryUsage = batteryUsage;
    }

    public DroneSpec(double maxBatteryPower, Vector batteryUsage) {
        super(KineticRemoteType.DRONE, maxBatteryPower);
        this.batteryUsage = batteryUsage;
    }

    public DroneSpec(double maxBatteryPower, Vector batteryUsage, double maxVelocity, double maxAcceleration,
            double maxJerk) {
        super(KineticRemoteType.DRONE, maxBatteryPower, maxVelocity, maxAcceleration, maxJerk);
        this.batteryUsage = batteryUsage;
    }

    public DroneSpec(Vector location, Vector batteryUsage) {
        super(KineticRemoteType.DRONE, location);
        this.batteryUsage = batteryUsage;
    }

    public DroneSpec(Vector location, Vector batteryUsage, double maxVelocity, double maxAcceleration, double maxJerk) {
        super(KineticRemoteType.DRONE, location, maxVelocity, maxAcceleration, maxJerk);
        this.batteryUsage = batteryUsage;
    }

    public DroneSpec(Vector location, double maxBatteryPower, Vector batteryUsage) {
        super(KineticRemoteType.DRONE, location, maxBatteryPower);
        this.batteryUsage = batteryUsage;
    }

    public DroneSpec(Vector location, double maxBatteryPower, Vector batteryUsage, double maxVelocity,
            double maxAcceleration, double maxJerk) {
        super(KineticRemoteType.DRONE, location, maxBatteryPower, maxVelocity, maxAcceleration, maxJerk);
        this.batteryUsage = batteryUsage;
    }

    public DroneSpec(double maxBatteryPower, Collection<SensorConfig> sensors, Vector batteryUsage) {
        super(KineticRemoteType.DRONE, maxBatteryPower, sensors);
        this.batteryUsage = batteryUsage;
    }

    public DroneSpec(double maxBatteryPower, Collection<SensorConfig> sensors, Vector batteryUsage, double maxVelocity,
            double maxAcceleration, double maxJerk) {
        super(KineticRemoteType.DRONE, maxBatteryPower, sensors, maxVelocity, maxAcceleration, maxJerk);
        this.batteryUsage = batteryUsage;
    }

    public DroneSpec(Vector location, double maxBatteryPower, Collection<SensorConfig> sensors, Vector batteryUsage) {
        super(KineticRemoteType.DRONE, location, maxBatteryPower, sensors);
        this.batteryUsage = batteryUsage;
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
        return String.format("d:%s", this.specType.getLabel());
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
