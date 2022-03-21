package com.seat.rescuesim.common.drone;

import java.util.ArrayList;

import com.seat.rescuesim.common.json.*;
import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.remote.KineticRemoteSpec;
import com.seat.rescuesim.common.remote.RemoteType;
import com.seat.rescuesim.common.sensor.SensorConfig;

/** A serializable specification of a Drone Remote. */
public class DroneSpec extends KineticRemoteSpec {

    public static DroneSpec None() {
        return new DroneSpec(DroneType.NONE, new Vector(), 0, new Vector(), new ArrayList<SensorConfig>(), 0, 0, 0);
    }

    public static DroneSpec Static(Vector location) {
        return new DroneSpec(DroneType.DEFAULT, location, 1, new Vector(), new ArrayList<SensorConfig>(), 0, 0, 0);
    }

    public static DroneSpec StaticWithSensors(Vector location, double maxBatteryPower, Vector batteryUsage,
            ArrayList<SensorConfig> sensors) {
        return new DroneSpec(DroneType.DEFAULT, location, maxBatteryPower, batteryUsage, sensors, 0, 0, 0);
    }

    public static DroneSpec Kinetic(Vector location, double maxBatteryPower, Vector batteryUsage,
            double maxVelocity, double maxAcceleration, double maxJerk) {
        return new DroneSpec(DroneType.DEFAULT, location, maxBatteryPower, batteryUsage, new ArrayList<SensorConfig>(),
            maxVelocity, maxAcceleration, maxJerk);
    }

    public static DroneSpec KineticWithSensors(Vector location, double maxBatteryPower, Vector batteryUsage,
            ArrayList<SensorConfig> sensors, double maxVelocity, double maxAcceleration, double maxJerk) {
        return new DroneSpec(DroneType.DEFAULT, location, maxBatteryPower, batteryUsage, sensors, maxVelocity,
            maxAcceleration, maxJerk);
    }

    private Vector batteryUsage; // [static (hovering), horizontal movement, vertical movement]
    private DroneType type;

    public DroneSpec(JSONObject json) throws JSONException {
        super(json);
    }

    public DroneSpec(JSONOption option) throws JSONException {
        super(option);
    }

    public DroneSpec(String encoding) throws JSONException {
        super(encoding);
    }

    public DroneSpec(DroneType type, Vector location, double maxBatteryPower, Vector batteryUsage,
            ArrayList<SensorConfig> sensors, double maxVelocity, double maxAcceleration, double maxJerk) {
        super(RemoteType.DRONE, location, maxBatteryPower, sensors, maxVelocity, maxAcceleration, maxJerk);
        this.type = type;
        this.batteryUsage = batteryUsage;
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        super.decode(json);
        this.type = DroneType.values()[json.getInt(DroneConst.DRONE_TYPE)];
        this.batteryUsage = new Vector(json.getJSONArray(DroneConst.BATTERY_USAGE));
    }

    public Vector getBatteryUsage() {
        return this.batteryUsage;
    }

    public double getHorizontalKineticBatteryUsage() {
        return this.batteryUsage.getY();
    }

    @Override
    public String getLabel() {
        return String.format("d%s", this.type.getLabel());
    }

    public double getStaticBatteryUsage() {
        return this.batteryUsage.getX();
    }

    public DroneType getSpecType() {
        return this.type;
    }

    public double getVerticalKineticBatteryUsage() {
        return this.batteryUsage.getZ();
    }

    public JSONOption toJSON() {
        JSONObjectBuilder json = super.getJSONBuilder();
        json.put(DroneConst.DRONE_TYPE, this.type.getType());
        json.put(DroneConst.BATTERY_USAGE, this.batteryUsage.toJSON());
        return json.toJSON();
    }

    public boolean equals(DroneSpec spec) {
        return super.equals(spec) && this.type.equals(spec.type) && this.batteryUsage.equals(spec.batteryUsage);
    }

}
