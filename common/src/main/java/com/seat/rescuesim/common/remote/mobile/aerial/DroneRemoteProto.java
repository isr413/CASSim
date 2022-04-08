package com.seat.rescuesim.common.remote.mobile.aerial;

import java.util.Collection;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.JSONObjectBuilder;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.remote.RemoteProto;
import com.seat.rescuesim.common.remote.RemoteType;
import com.seat.rescuesim.common.remote.mobile.MobileRemoteProto;
import com.seat.rescuesim.common.sensor.SensorConfig;

/** A serializable prototype of a Drone Remote. */
public class DroneRemoteProto extends MobileRemoteProto {
    public static final String BATTERY_USAGE = "battery_usage";

    private Vector batteryUsage; // [static (hovering), horizontal movement, vertical movement]

    public DroneRemoteProto(Vector batteryUsage) {
        this(null, RemoteProto.DEFAULT_BATTERY_POWER, null, batteryUsage, MobileRemoteProto.DEFAULT_VELOCITY,
            MobileRemoteProto.DEFAULT_ACCELERATION, MobileRemoteProto.DEFAULT_JERK);
    }

    public DroneRemoteProto(Vector batteryUsage, double maxVelocity, double maxAcceleration, double maxJerk) {
        this(null, RemoteProto.DEFAULT_BATTERY_POWER, null, batteryUsage, maxVelocity, maxAcceleration, maxJerk);
    }

    public DroneRemoteProto(double maxBatteryPower, Vector batteryUsage) {
        this(null, maxBatteryPower, null, batteryUsage, MobileRemoteProto.DEFAULT_VELOCITY,
            MobileRemoteProto.DEFAULT_ACCELERATION, MobileRemoteProto.DEFAULT_JERK);
    }

    public DroneRemoteProto(double maxBatteryPower, Vector batteryUsage, double maxVelocity, double maxAcceleration,
            double maxJerk) {
        this(null, maxBatteryPower, null, batteryUsage, maxVelocity, maxAcceleration, maxJerk);
    }

    public DroneRemoteProto(Vector location, Vector batteryUsage) {
        this(location, RemoteProto.DEFAULT_BATTERY_POWER, null, batteryUsage, MobileRemoteProto.DEFAULT_VELOCITY,
            MobileRemoteProto.DEFAULT_ACCELERATION, MobileRemoteProto.DEFAULT_JERK);
    }

    public DroneRemoteProto(Vector location, Vector batteryUsage, double maxVelocity, double maxAcceleration,
            double maxJerk) {
        this(location, RemoteProto.DEFAULT_BATTERY_POWER, null, batteryUsage, maxVelocity, maxAcceleration, maxJerk);
    }

    public DroneRemoteProto(Vector location, double maxBatteryPower, Vector batteryUsage) {
        this(location, maxBatteryPower, null, batteryUsage, MobileRemoteProto.DEFAULT_VELOCITY,
            MobileRemoteProto.DEFAULT_ACCELERATION, MobileRemoteProto.DEFAULT_JERK);
    }

    public DroneRemoteProto(Vector location, double maxBatteryPower, Vector batteryUsage, double maxVelocity,
            double maxAcceleration, double maxJerk) {
        this(location, maxBatteryPower, null, batteryUsage, maxVelocity, maxAcceleration, maxJerk);
    }

    public DroneRemoteProto(double maxBatteryPower, Collection<SensorConfig> sensors, Vector batteryUsage) {
        this(null, maxBatteryPower, sensors, batteryUsage, MobileRemoteProto.DEFAULT_VELOCITY,
            MobileRemoteProto.DEFAULT_ACCELERATION, MobileRemoteProto.DEFAULT_JERK);
    }

    public DroneRemoteProto(double maxBatteryPower, Collection<SensorConfig> sensors, Vector batteryUsage,
            double maxVelocity, double maxAcceleration, double maxJerk) {
        this(null, maxBatteryPower, sensors, batteryUsage, maxVelocity, maxAcceleration, maxJerk);
    }

    public DroneRemoteProto(Vector location, double maxBatteryPower, Collection<SensorConfig> sensors,
            Vector batteryUsage) {
        this(location, maxBatteryPower, sensors, batteryUsage, MobileRemoteProto.DEFAULT_VELOCITY,
            MobileRemoteProto.DEFAULT_ACCELERATION, MobileRemoteProto.DEFAULT_JERK);
    }

    public DroneRemoteProto(Vector location, double maxBatteryPower, Collection<SensorConfig> sensors,
            Vector batteryUsage, double maxVelocity, double maxAcceleration, double maxJerk) {
        super(RemoteType.DRONE, location, maxBatteryPower, sensors, maxVelocity, maxAcceleration, maxJerk);
        this.batteryUsage = batteryUsage;
    }

    public DroneRemoteProto(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        super.decode(json);
        this.batteryUsage = (json.hasKey(DroneRemoteProto.BATTERY_USAGE)) ?
            new Vector(json.getJSONOption(DroneRemoteProto.BATTERY_USAGE)) :
            new Vector();
    }

    @Override
    protected JSONObjectBuilder getJSONBuilder() throws JSONException {
        JSONObjectBuilder json = super.getJSONBuilder();
        json.put(DroneRemoteProto.BATTERY_USAGE, this.batteryUsage.toJSON());
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
        return String.format("d:%s", this.getRemoteType().getLabel());
    }

    public double getStaticBatteryUsage() {
        return this.batteryUsage.getX();
    }

    public double getVerticalKineticBatteryUsage() {
        return this.batteryUsage.getZ();
    }

    public boolean equals(DroneRemoteProto proto) {
        if (proto == null) return false;
        return super.equals(proto) && this.batteryUsage.equals(proto.batteryUsage);
    }

}