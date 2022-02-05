package com.seat.rescuesim.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DroneSpecification {
    private static final String DRONE_LOCATION = "location";
    private static final String DRONE_MAX_ACCELERATION = "max_acceleration";
    private static final String DRONE_MAX_BATTERY = "max_battery";
    private static final String DRONE_MAX_JERK = "max_jerk";
    private static final String DRONE_MAX_VELOCITY = "max_velocity";
    private static final String DRONE_KINETIC_BATTERY_USAGE = "kinetic_battery_usage";
    private static final String DRONE_SENSORS = "sensors";
    private static final String DRONE_STATIC_BATTERY_USAGE = "static_battery_usage";

    private Vector initialLocation;
    private double kineticBatteryUsage;
    private double maxAcceleration;
    private double maxBatteryPower;
    private double maxJerk;
    private double maxVelocity;
    private HashMap<SensorType, Sensor> sensors;
    private double staticBatteryUsage;

    public static DroneSpecification decode(JSONObject json) throws JSONException {
        double batteryPower = json.getDouble(DroneSpecification.DRONE_MAX_BATTERY);
        double staticBatteryUsage = json.getDouble(DroneSpecification.DRONE_STATIC_BATTERY_USAGE);
        Vector location = Vector.decode(json.getJSONArray(DroneSpecification.DRONE_LOCATION));
        double kineticBatteryUsage = json.getDouble(DroneSpecification.DRONE_KINETIC_BATTERY_USAGE);
        double velocity = json.getDouble(DroneSpecification.DRONE_MAX_VELOCITY);
        double acceleration = json.getDouble(DroneSpecification.DRONE_MAX_ACCELERATION);
        double jerk = json.getDouble(DroneSpecification.DRONE_MAX_JERK);
        JSONArray jsonSensors = json.getJSONArray(DroneSpecification.DRONE_SENSORS);
        ArrayList<Sensor> sensors = new ArrayList<>();
        for (int i = 0; i < jsonSensors.length(); i++) {
            sensors.add(Sensor.decode(jsonSensors.getJSONObject(i)));
        }
        return new DroneSpecification(batteryPower, staticBatteryUsage, location,
                kineticBatteryUsage, velocity, acceleration, jerk, sensors);
    }

    public static DroneSpecification decode(String encoding) throws JSONException {
        return DroneSpecification.decode(new JSONObject(encoding));
    }

    public DroneSpecification(double batteryPower, double staticBatteryUsage, Vector location) {
        this(batteryPower, staticBatteryUsage, location, 0.0, 0.0, 0.0, 0.0, new HashMap<SensorType, Sensor>());
    }

    public DroneSpecification(double batteryPower, double staticBatteryUsage, Vector location, Sensor[] sensors) {
        this(batteryPower, staticBatteryUsage, location, 0.0, 0.0, 0.0, 0.0, sensors);
    }

    public DroneSpecification(double batteryPower, double staticBatteryUsage,
            Vector location, ArrayList<Sensor> sensors) {
        this(batteryPower, staticBatteryUsage, location, 0.0, 0.0, 0.0, 0.0, sensors);
    }

    public DroneSpecification(double batteryPower, double staticBatteryUsage, Vector location,
            HashMap<SensorType, Sensor> sensors) {
        this(batteryPower, staticBatteryUsage, location, 0.0, 0.0, 0.0, 0.0, sensors);
    }

    public DroneSpecification(double batteryPower, double staticBatteryUsage, Vector location,
            double kineticBatteryUsage, double velocity, double acceleration, double jerk) {
        this(batteryPower, staticBatteryUsage, location,
                kineticBatteryUsage, velocity, acceleration, jerk, new HashMap<SensorType, Sensor>());
    }

    public DroneSpecification(double batteryPower, double staticBatteryUsage, Vector location,
            double kineticBatteryUsage, double velocity, double acceleration, double jerk, Sensor[] sensors) {
        this(batteryPower, staticBatteryUsage, location,
            kineticBatteryUsage, velocity, acceleration, jerk, new ArrayList<Sensor>(Arrays.asList(sensors)));
    }

    public DroneSpecification(double batteryPower, double staticBatteryUsage, Vector location,
            double kineticBatteryUsage, double velocity, double acceleration, double jerk, ArrayList<Sensor> sensors) {
        this(batteryPower, staticBatteryUsage, location, kineticBatteryUsage, velocity, acceleration, jerk);
        for (Sensor sensor : sensors) {
            this.sensors.put(sensor.getType(), sensor);
        }
    }

    public DroneSpecification(double batteryPower, double staticBatteryUsage, Vector location,
            double kineticBatteryUsage, double velocity, double acceleration, double jerk,
            HashMap<SensorType, Sensor> sensors) {
        this.maxBatteryPower = batteryPower;
        this.staticBatteryUsage = staticBatteryUsage;
        this.initialLocation = location;
        this.kineticBatteryUsage = kineticBatteryUsage;
        this.maxVelocity = velocity;
        this.maxAcceleration = acceleration;
        this.maxJerk = jerk;
        this.sensors = sensors;
    }

    public Vector getInitialLocation() {
        return this.initialLocation;
    }

    public double getKineticBatteryUsage() {
        return this.kineticBatteryUsage;
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

    public ArrayList<Sensor> getSensors() {
        return new ArrayList<Sensor>(this.sensors.values());
    }

    public Sensor getSensorWithType(SensorType type) {
        if (!this.hasSensorWithType(type)) {
            Debugger.logger.err("No sensor with type (" + type.toString() + ") found on drone spec");
            return null;
        }
        return this.sensors.get(type);
    }

    public double getStaticBatteryUsage() {
        return this.staticBatteryUsage;
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
        json.put(DroneSpecification.DRONE_STATIC_BATTERY_USAGE, this.staticBatteryUsage);
        json.put(DroneSpecification.DRONE_LOCATION, this.initialLocation.toJSON());
        json.put(DroneSpecification.DRONE_KINETIC_BATTERY_USAGE, this.kineticBatteryUsage);
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
        return this.maxBatteryPower == spec.maxBatteryPower && this.staticBatteryUsage == spec.staticBatteryUsage &&
                this.initialLocation.equals(spec.initialLocation) &&
                this.kineticBatteryUsage == spec.kineticBatteryUsage && this.maxVelocity == spec.maxVelocity &&
                this.maxAcceleration == spec.maxAcceleration &&
                this.maxJerk == spec.maxJerk && this.sensors.equals(spec.sensors);
    }

    public boolean equals(String encoding) {
        return this.encode().equals(encoding);
    }

}
