package com.seat.rescuesim.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DroneSpecification {
    private static final String DRONE_ACCELERATION = "acceleration";
    private static final String DRONE_FUEL = "fuel";
    private static final String DRONE_LOCATION = "location";
    private static final String DRONE_MIN_FUEL_COST = "min_fuel_cost";
    private static final String DRONE_SENSORS = "sensors";
    private static final String DRONE_VELOCITY = "speed";

    private Vector initialLocation;
    private double maxAcceleration;
    private double maxBatteryPower;
    private double maxVelocity;
    private double minBatteryUsage;
    private HashMap<SensorType, Sensor> sensors;

    public static DroneSpecification decode(JSONObject json) throws JSONException {
        double batteryPower = json.getDouble(DroneSpecification.DRONE_FUEL);
        double batteryUsage = json.getDouble(DroneSpecification.DRONE_MIN_FUEL_COST);
        Vector location = Vector.decode(json.getJSONArray(DroneSpecification.DRONE_LOCATION));
        double velocity = json.getDouble(DroneSpecification.DRONE_VELOCITY);
        double acceleration = json.getDouble(DroneSpecification.DRONE_ACCELERATION);
        JSONArray jsonSensors = json.getJSONArray(DroneSpecification.DRONE_SENSORS);
        ArrayList<Sensor> sensors = new ArrayList<>();
        for (int i = 0; i < jsonSensors.length(); i++) {
            sensors.add(Sensor.decode(jsonSensors.getJSONObject(i)));
        }
        return new DroneSpecification(batteryPower, batteryUsage, location, velocity, acceleration, sensors);
    }

    public static DroneSpecification decode(String encoding) throws JSONException {
        return DroneSpecification.decode(new JSONObject(encoding));
    }

    public DroneSpecification(double batteryPower, double batteryUsage, Vector location) {
        this(batteryPower, batteryUsage, location, 0.0, 0.0, new HashMap<SensorType, Sensor>());
    }

    public DroneSpecification(double batteryPower, double batteryUsage, Vector location, Sensor[] sensors) {
        this(batteryPower, batteryUsage, location, 0.0, 0.0, sensors);
    }

    public DroneSpecification(double batteryPower, double batteryUsage, Vector location, ArrayList<Sensor> sensors) {
        this(batteryPower, batteryUsage, location, 0.0, 0.0, sensors);
    }

    public DroneSpecification(double batteryPower, double batteryUsage, Vector location,
            HashMap<SensorType, Sensor> sensors) {
        this(batteryPower, batteryUsage, location, 0.0, 0.0, sensors);
    }

    public DroneSpecification(double batteryPower, double batteryUsage, Vector location, double velocity,
            double acceleration) {
        this(batteryPower, batteryUsage, location, velocity, acceleration, new HashMap<SensorType, Sensor>());
    }

    public DroneSpecification(double batteryPower, double batteryUsage, Vector location, double velocity,
            double acceleration, Sensor[] sensors) {
        this(batteryPower, batteryUsage, location, velocity, acceleration,
                new ArrayList<Sensor>(Arrays.asList(sensors)));
    }

    public DroneSpecification(double batteryPower, double batteryUsage, Vector location, double velocity,
            double acceleration, ArrayList<Sensor> sensors) {
        this(batteryPower, batteryUsage, location, velocity, acceleration);
        for (Sensor sensor : sensors) {
            this.sensors.put(sensor.getType(), sensor);
        }
    }

    public DroneSpecification(double batteryPower, double batteryUsage, Vector location, double velocity,
            double acceleration, HashMap<SensorType, Sensor> sensors) {
        this.maxBatteryPower = batteryPower;
        this.minBatteryUsage = batteryUsage;
        this.initialLocation = location;
        this.maxVelocity = velocity;
        this.maxAcceleration = acceleration;
        this.sensors = sensors;
    }

    public Vector getInitialLocation() {
        return this.initialLocation;
    }

    public double getMaxAcceleration() {
        return this.maxAcceleration;
    }

    public double getMaxBatteryPower() {
        return this.maxBatteryPower;
    }

    public double getMaxVelocity() {
        return this.maxVelocity;
    }

    public double getMinBatteryUsage() {
        return this.minBatteryUsage;
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

    public boolean hasSensors() {
        return !this.sensors.isEmpty();
    }

    public boolean hasSensorWithType(SensorType type) {
        return this.sensors.containsKey(type);
    }

    public boolean isKinetic() {
        return this.maxVelocity > 0;
    }

    public String encode() {
        return this.toJSON().toString();
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put(DroneSpecification.DRONE_FUEL, this.maxBatteryPower);
        json.put(DroneSpecification.DRONE_MIN_FUEL_COST, this.minBatteryUsage);
        json.put(DroneSpecification.DRONE_LOCATION, this.initialLocation.toJSON());
        json.put(DroneSpecification.DRONE_VELOCITY, this.maxVelocity);
        json.put(DroneSpecification.DRONE_ACCELERATION, this.maxAcceleration);
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
        return this.maxBatteryPower == spec.maxBatteryPower && this.minBatteryUsage == spec.minBatteryUsage &&
                this.initialLocation.equals(spec.initialLocation) && this.maxVelocity == spec.maxVelocity &&
                this.maxAcceleration == spec.maxAcceleration && this.sensors.equals(spec.sensors);
    }

    public boolean equals(String encoding) {
        return this.encode().equals(encoding);
    }

}
