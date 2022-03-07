package com.seat.rescuesim.common.drone;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import com.seat.rescuesim.common.SensorSpecification;
import com.seat.rescuesim.common.SensorType;
import com.seat.rescuesim.common.json.*;
import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.remote.RemoteSpecification;
import com.seat.rescuesim.common.util.Debugger;

/** A serializable class to represent the specification of a Drone.  */
public class DroneSpecification extends JSONAble implements RemoteSpecification {
    private static final String DRONE_BATTERY_USAGE = "battery_usage";
    private static final String DRONE_LOCATION = "location";
    private static final String DRONE_MAX_ACCELERATION = "max_acceleration";
    private static final String DRONE_MAX_BATTERY = "max_battery";
    private static final String DRONE_MAX_JERK = "max_jerk";
    private static final String DRONE_MAX_VELOCITY = "max_velocity";
    private static final String DRONE_SENSORS = "sensors";
    private static final String DRONE_TYPE = "drone_type";

    public static DroneSpecification None() {
        return new DroneSpecification();
    }

    private Vector batteryUsage; // [static (hovering), horizontal movement, vertical movement]
    private Vector initialLocation;
    private double maxAcceleration;
    private double maxBatteryPower;
    private double maxJerk;
    private double maxVelocity;
    private HashMap<String, SensorSpecification> sensors;
    private DroneType type;

    public DroneSpecification(JSONObject json) {
        super(json);
    }

    public DroneSpecification(JSONOption option) {
        super(option);
    }

    public DroneSpecification(String encoding) {
        super(encoding);
    }

    public DroneSpecification() {
        this(DroneType.NONE, 0, new Vector(), new Vector(), 0, 0, 0, new HashMap<String, SensorSpecification>());
    }

    public DroneSpecification(DroneType type, double maxBatteryPower, Vector batteryUsage, Vector location) {
        this(type, maxBatteryPower, batteryUsage, location, 0, 0, 0, new HashMap<String, SensorSpecification>());
    }

    public DroneSpecification(DroneType type, double maxBatteryPower, Vector batteryUsage, Vector location,
            SensorSpecification[] sensors) {
        this(type, maxBatteryPower, batteryUsage, location, 0, 0, 0, sensors);
    }

    public DroneSpecification(DroneType type, double maxBatteryPower, Vector batteryUsage, Vector location,
            ArrayList<SensorSpecification> sensors) {
        this(type, maxBatteryPower, batteryUsage, location, 0, 0, 0, sensors);
    }

    public DroneSpecification(DroneType type, double maxBatteryPower, Vector batteryUsage, Vector location,
            HashMap<String, SensorSpecification> sensors) {
        this(type, maxBatteryPower, batteryUsage, location, 0, 0, 0, sensors);
    }

    public DroneSpecification(DroneType type, double maxBatteryPower, Vector batteryUsage, Vector location,
            double maxVelocity, double maxAccleration, double maxJerk) {
        this(type, maxBatteryPower, batteryUsage, location, maxVelocity, maxAccleration, maxJerk,
            new HashMap<String, SensorSpecification>());
    }

    public DroneSpecification(DroneType type, double maxBatteryPower, Vector batteryUsage, Vector location,
            double maxVelocity, double maxAccleration, double maxJerk, SensorSpecification[] sensors) {
        this(type, maxBatteryPower, batteryUsage, location, maxVelocity, maxAccleration, maxJerk,
            new ArrayList<SensorSpecification>(Arrays.asList(sensors)));
    }

    public DroneSpecification(DroneType type, double maxBatteryPower, Vector batteryUsage, Vector location,
            double maxVelocity, double maxAccleration, double maxJerk, ArrayList<SensorSpecification> sensors) {
        this(type, maxBatteryPower, batteryUsage, location, maxVelocity, maxAccleration, maxJerk,
            new HashMap<String, SensorSpecification>());
        for (SensorSpecification spec : sensors) {
            this.sensors.put(spec.getSpecID(), spec);
        }
    }

    public DroneSpecification(DroneType type, double maxBatteryPower, Vector batteryUsage, Vector location,
            double maxVelocity, double maxAcceleration, double maxJerk,
            HashMap<String, SensorSpecification> sensors) {
        this.type = type;
        this.maxBatteryPower = maxBatteryPower;
        this.batteryUsage = batteryUsage;
        this.initialLocation = location;
        this.maxVelocity = maxVelocity;
        this.maxAcceleration = maxAcceleration;
        this.maxJerk = maxJerk;
        this.sensors = sensors;
    }

    @Override
    protected void decode(JSONObject json) {
        this.type = DroneType.values()[json.getInt(DroneSpecification.DRONE_TYPE)];
        this.maxBatteryPower = json.getDouble(DroneSpecification.DRONE_MAX_BATTERY);
        this.batteryUsage = new Vector(json.getJSONArray(DroneSpecification.DRONE_BATTERY_USAGE));
        this.initialLocation = new Vector(json.getJSONArray(DroneSpecification.DRONE_LOCATION));
        this.maxVelocity = json.getDouble(DroneSpecification.DRONE_MAX_VELOCITY);
        this.maxAcceleration = json.getDouble(DroneSpecification.DRONE_MAX_ACCELERATION);
        this.maxJerk = json.getDouble(DroneSpecification.DRONE_MAX_JERK);
        this.sensors = new HashMap<>();
        JSONArray jsonSensors = json.getJSONArray(DroneSpecification.DRONE_SENSORS);
        for (int i = 0; i < jsonSensors.length(); i++) {
            SensorSpecification spec = new SensorSpecification(jsonSensors.getJSONObject(i));
            this.sensors.put(spec.getSpecID(), spec);
        }
    }

    public Vector getBatteryUsage() {
        return this.batteryUsage;
    }

    public DroneType getDroneType() {
        return this.type;
    }

    public Vector getInitialLocation() {
        return this.initialLocation;
    }

    public double getHorizontalKineticBatteryUsage() {
        return this.batteryUsage.getY();
    }

    public String getLabel() {
        return String.format("d%s", this.type.getLabel());
    }

    public double getMaxAcceleration() {
        return this.maxAcceleration;
    }

    public double getMaxBatteryPower() {
        return this.maxBatteryPower;
    }

    public double getMaxJerk() {
        return this.maxJerk;
    }

    public double getMaxVelocity() {
        return this.maxVelocity;
    }

    public SensorSpecification getSensor(String sensor) {
        if (!this.hasSensor(sensor)) {
            Debugger.logger.err(String.format("No sensor with type %s found on drone spec %s",
                sensor, this.type.getLabel()));
            return null;
        }
        return this.sensors.get(sensor);
    }

    public ArrayList<SensorSpecification> getSensors() {
        return new ArrayList<SensorSpecification>(this.sensors.values());
    }

    public ArrayList<SensorSpecification> getSensorsWithType(SensorType type) {
        ArrayList<SensorSpecification> specs = new ArrayList<>();
        for (SensorSpecification spec : this.sensors.values()) {
            if (spec.getSensorType() == type) {
                specs.add(spec);
            }
        }
        return specs;
    }

    public double getStaticBatteryUsage() {
        return this.batteryUsage.getX();
    }

    public double getVerticalKineticBatteryUsage() {
        return this.batteryUsage.getZ();
    }

    public boolean hasSensor(String sensor) {
        return this.sensors.containsKey(sensor);
    }

    public boolean hasSensors() {
        return !this.sensors.isEmpty();
    }

    public boolean hasSensorWithType(SensorType type) {
        for (SensorSpecification spec : this.sensors.values()) {
            if (spec.getSensorType() == type) {
                return true;
            }
        }
        return false;
    }

    public boolean isDisabled() {
        return !this.isEnabled();
    }

    public boolean isEnabled() {
        return this.maxBatteryPower > 0;
    }

    public boolean isKinetic() {
        return this.maxVelocity > 0 && this.maxAcceleration > 0 && this.maxJerk > 0;
    }

    public JSONOption toJSON() {
        JSONObjectBuilder json = JSONBuilder.Object();
        json.put(DroneSpecification.DRONE_TYPE, this.type.getType());
        json.put(DroneSpecification.DRONE_MAX_BATTERY, this.maxBatteryPower);
        json.put(DroneSpecification.DRONE_BATTERY_USAGE, this.batteryUsage.toJSON());
        json.put(DroneSpecification.DRONE_LOCATION, this.initialLocation.toJSON());
        json.put(DroneSpecification.DRONE_MAX_VELOCITY, this.maxVelocity);
        json.put(DroneSpecification.DRONE_MAX_ACCELERATION, this.maxAcceleration);
        json.put(DroneSpecification.DRONE_MAX_JERK, this.maxJerk);
        JSONArrayBuilder jsonSensors = JSONBuilder.Array();
        for (SensorSpecification spec : this.sensors.values()) {
            jsonSensors.put(spec.toJSON());
        }
        json.put(DroneSpecification.DRONE_SENSORS, jsonSensors.toJSON());
        return json.toJSON();
    }

    public boolean equals(DroneSpecification spec) {
        return this.type.equals(spec.type) && this.maxBatteryPower == spec.maxBatteryPower &&
            this.batteryUsage.equals(spec.batteryUsage) && this.initialLocation.equals(spec.initialLocation) &&
            this.maxVelocity == spec.maxVelocity && this.maxAcceleration == spec.maxAcceleration &&
            this.maxJerk == spec.maxJerk && this.sensors.equals(spec.sensors);
    }

}
