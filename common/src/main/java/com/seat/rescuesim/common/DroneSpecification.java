package com.seat.rescuesim.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DroneSpecification {
    private static final String DRONE_ACCELERATION = "acceleration";
    private static final String DRONE_ACCELERATION_DELTA = "acceleration_delta";
    private static final String DRONE_BATTERY = "battery";
    private static final String DRONE_LOCATION = "location";
    private static final String DRONE_KINETIC_BATTERY_USAGE = "kinetic_battery_usage";
    private static final String DRONE_SENSORS = "sensors";
    private static final String DRONE_STATIC_BATTERY_USAGE = "static_battery_usage";
    private static final String DRONE_VELOCITY = "speed";

    private Vector initialLocation;
    private double kineticBatteryUsage;
    private double maxAcceleration;
    private double maxAccelerationDelta;
    private double maxBatteryPower;
    private double maxVelocity;
    private HashMap<SensorType, Sensor> sensors;
    private double staticBatteryUsage;

    public static DroneSpecification decode(JSONObject json) throws JSONException {
        double batteryPower = json.getDouble(DroneSpecification.DRONE_BATTERY);
        double staticBatteryUsage = json.getDouble(DroneSpecification.DRONE_STATIC_BATTERY_USAGE);
        Vector location = Vector.decode(json.getJSONArray(DroneSpecification.DRONE_LOCATION));
        double kineticBatteryUsage = json.getDouble(DroneSpecification.DRONE_KINETIC_BATTERY_USAGE);
        double velocity = json.getDouble(DroneSpecification.DRONE_VELOCITY);
        double acceleration = json.getDouble(DroneSpecification.DRONE_ACCELERATION);
        double accelerationDelta = json.getDouble(DroneSpecification.DRONE_ACCELERATION_DELTA);
        JSONArray jsonSensors = json.getJSONArray(DroneSpecification.DRONE_SENSORS);
        ArrayList<Sensor> sensors = new ArrayList<>();
        for (int i = 0; i < jsonSensors.length(); i++) {
            sensors.add(Sensor.decode(jsonSensors.getJSONObject(i)));
        }
        return new DroneSpecification(batteryPower, staticBatteryUsage, location,
                kineticBatteryUsage, velocity, acceleration, accelerationDelta, sensors);
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
            double kineticBatteryUsage, double velocity, double acceleration, double delta) {
        this(batteryPower, staticBatteryUsage, location,
                kineticBatteryUsage, velocity, acceleration, delta, new HashMap<SensorType, Sensor>());
    }

    public DroneSpecification(double batteryPower, double staticBatteryUsage, Vector location,
            double kineticBatteryUsage, double velocity, double acceleration, double delta, Sensor[] sensors) {
        this(batteryPower, staticBatteryUsage, location,
            kineticBatteryUsage, velocity, acceleration, delta, new ArrayList<Sensor>(Arrays.asList(sensors)));
    }

    public DroneSpecification(double batteryPower, double staticBatteryUsage, Vector location,
            double kineticBatteryUsage, double velocity, double acceleration, double delta, ArrayList<Sensor> sensors) {
        this(batteryPower, staticBatteryUsage, location, kineticBatteryUsage, velocity, acceleration, delta);
        for (Sensor sensor : sensors) {
            this.sensors.put(sensor.getType(), sensor);
        }
    }

    public DroneSpecification(double batteryPower, double staticBatteryUsage, Vector location,
            double kineticBatteryUsage, double velocity, double acceleration, double delta,
            HashMap<SensorType, Sensor> sensors) {
        this.maxBatteryPower = batteryPower;
        this.staticBatteryUsage = staticBatteryUsage;
        this.initialLocation = location;
        this.kineticBatteryUsage = kineticBatteryUsage;
        this.maxVelocity = velocity;
        this.maxAcceleration = acceleration;
        this.maxAccelerationDelta = delta;
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

    public double getMaxAccelerationDelta() {
        return this.maxAccelerationDelta;
    }

    public double getMaxBatteryPower() {
        return this.maxBatteryPower;
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
        return this.maxAcceleration > 0 && this.maxAccelerationDelta > 0 && this.maxVelocity > 0;
    }

    public String encode() {
        return this.toJSON().toString();
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put(DroneSpecification.DRONE_BATTERY, this.maxBatteryPower);
        json.put(DroneSpecification.DRONE_STATIC_BATTERY_USAGE, this.staticBatteryUsage);
        json.put(DroneSpecification.DRONE_LOCATION, this.initialLocation.toJSON());
        json.put(DroneSpecification.DRONE_KINETIC_BATTERY_USAGE, this.kineticBatteryUsage);
        json.put(DroneSpecification.DRONE_VELOCITY, this.maxVelocity);
        json.put(DroneSpecification.DRONE_ACCELERATION, this.maxAcceleration);
        json.put(DroneSpecification.DRONE_ACCELERATION_DELTA, this.maxAccelerationDelta);
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
                this.maxAccelerationDelta == spec.maxAccelerationDelta && this.sensors.equals(spec.sensors);
    }

    public boolean equals(String encoding) {
        return this.encode().equals(encoding);
    }

}
