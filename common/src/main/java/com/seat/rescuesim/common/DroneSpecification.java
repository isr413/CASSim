package com.seat.rescuesim.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DroneSpecification {
    private static final String DRONE_BATTERY_USAGE = "battery_usage";
    private static final String DRONE_LOCATION = "location";
    private static final String DRONE_MAX_ACCELERATION = "max_acceleration";
    private static final String DRONE_MAX_BATTERY = "max_battery";
    private static final String DRONE_MAX_JERK = "max_jerk";
    private static final String DRONE_MAX_VELOCITY = "max_velocity";
    private static final String DRONE_SENSORS = "sensors";

    private Vector batteryUsage; // [static (hovering), horizontal movement, vertical movement]
    private Vector initialLocation;
    private double maxAcceleration;
    private double maxAccelerationDelta;
    private double maxBatteryPower;
    private double maxJerk;
    private double maxVelocity;
    private HashMap<SensorType, Sensor> sensors;

    public static DroneSpecification decode(JSONObject json) throws JSONException {
        double batteryPower = json.getDouble(DroneSpecification.DRONE_MAX_BATTERY);
        Vector batteryUsage = Vector.decode(json.getJSONArray(DroneSpecification.DRONE_BATTERY_USAGE));
        Vector location = Vector.decode(json.getJSONArray(DroneSpecification.DRONE_LOCATION));
        double velocity = json.getDouble(DroneSpecification.DRONE_MAX_VELOCITY);
        double acceleration = json.getDouble(DroneSpecification.DRONE_MAX_ACCELERATION);
        double jerk = json.getDouble(DroneSpecification.DRONE_MAX_JERK);
        JSONArray jsonSensors = json.getJSONArray(DroneSpecification.DRONE_SENSORS);
        ArrayList<Sensor> sensors = new ArrayList<>();
        for (int i = 0; i < jsonSensors.length(); i++) {
            sensors.add(Sensor.decode(jsonSensors.getJSONObject(i)));
        }
        return new DroneSpecification(batteryPower, batteryUsage, location,
                velocity, acceleration, jerk, sensors);
    }

    public static DroneSpecification decode(String encoding) throws JSONException {
        return DroneSpecification.decode(new JSONObject(encoding));
    }

    public DroneSpecification(double batteryPower, Vector batteryUsage, Vector location) {
        this(batteryPower, batteryUsage, location, 0, 0, 0, new HashMap<SensorType, Sensor>());
    }

    public DroneSpecification(double batteryPower, Vector batteryUsage, Vector location, Sensor[] sensors) {
        this(batteryPower, batteryUsage, location, 0, 0, 0, sensors);
    }

    public DroneSpecification(double batteryPower, Vector batteryUsage, Vector location, ArrayList<Sensor> sensors) {
        this(batteryPower, batteryUsage, location, 0, 0, 0, sensors);
    }

    public DroneSpecification(double batteryPower, Vector batteryUsage, Vector location,
            HashMap<SensorType, Sensor> sensors) {
        this(batteryPower, batteryUsage, location, 0, 0, 0, sensors);
    }

    public DroneSpecification(double batteryPower, Vector batteryUsage, Vector location,
            double velocity, double acceleration, double jerk) {
        this(batteryPower, batteryUsage, location, velocity, acceleration, jerk, new HashMap<SensorType, Sensor>());
    }

    public DroneSpecification(double batteryPower, Vector batteryUsage, Vector location,
            double velocity, double acceleration, double jerk, Sensor[] sensors) {
        this(batteryPower, batteryUsage, location, velocity, acceleration, jerk,
            new ArrayList<Sensor>(Arrays.asList(sensors)));
    }

    public DroneSpecification(double batteryPower, Vector batteryUsage, Vector location,
            double velocity, double acceleration, double jerk, ArrayList<Sensor> sensors) {
        this(batteryPower, batteryUsage, location, velocity, acceleration, jerk);
        for (Sensor sensor : sensors) {
            this.sensors.put(sensor.getType(), sensor);
        }
    }

    public DroneSpecification(double batteryPower, Vector batteryUsage, Vector location,
            double velocity, double acceleration, double jerk, HashMap<SensorType, Sensor> sensors) {
        this.maxBatteryPower = batteryPower;
        this.batteryUsage = batteryUsage;
        this.initialLocation = location;
        this.maxVelocity = velocity;
        this.maxAcceleration = acceleration;
        this.maxJerk = jerk;
        this.sensors = sensors;
    }

    public Vector getBatteryUsage() {
        return this.batteryUsage;
    }

    public Vector getInitialLocation() {
        return this.initialLocation;
    }

    public double getHorizontalKineticBatteryUsage() {
        return this.batteryUsage.getY();
    }

    public double getMaxAcceleration() {
        return this.maxAcceleration;
    }

    public double getMaxAccelerationDelta() {
        return this.maxAccelerationDelta;
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

    public ArrayList<Sensor> getSensors() {
        return new ArrayList<Sensor>(this.sensors.values());
    }

    public Sensor getSensorWithType(SensorType type) {
        if (!this.hasSensorWithType(type)) {
            Debugger.logger.err(String.format("No sensor with type %s found on drone spec", type.getLabel()));
            return null;
        }
        return this.sensors.get(type);
    }

    public double getStaticBatteryUsage() {
        return this.batteryUsage.getX();
    }

    public double getVerticalKineticBatteryUsage() {
        return this.batteryUsage.getZ();
    }

    public boolean hasSensors() {
        return !this.sensors.isEmpty();
    }

    public boolean hasSensorWithType(SensorType type) {
        return this.sensors.containsKey(type);
    }

    public boolean isKinetic() {
        return this.maxVelocity > 0 && this.maxAcceleration > 0 && this.maxJerk > 0;
    }

    public String encode() {
        return this.toJSON().toString();
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put(DroneSpecification.DRONE_MAX_BATTERY, this.maxBatteryPower);
        json.put(DroneSpecification.DRONE_BATTERY_USAGE, this.batteryUsage.toJSON());
        json.put(DroneSpecification.DRONE_LOCATION, this.initialLocation.toJSON());
        json.put(DroneSpecification.DRONE_MAX_VELOCITY, this.maxVelocity);
        json.put(DroneSpecification.DRONE_MAX_ACCELERATION, this.maxAcceleration);
        json.put(DroneSpecification.DRONE_MAX_JERK, this.maxJerk);
        JSONArray droneSensors = new JSONArray();
        for (Sensor sensor : this.sensors.values()) {
            droneSensors.put(sensor.toJSON());
        }
        json.put(DroneSpecification.DRONE_SENSORS, droneSensors);
        return json;
    }

    public String toString() {
        return this.encode();
    }

    public boolean equals(DroneSpecification spec) {
        return this.maxBatteryPower == spec.maxBatteryPower && this.batteryUsage.equals(spec.batteryUsage) &&
                this.initialLocation.equals(spec.initialLocation) && this.maxVelocity == spec.maxVelocity &&
                this.maxAcceleration == spec.maxAcceleration &&
                this.maxJerk == spec.maxJerk && this.sensors.equals(spec.sensors);
    }

    public boolean equals(String encoding) {
        return this.encode().equals(encoding);
    }

}
