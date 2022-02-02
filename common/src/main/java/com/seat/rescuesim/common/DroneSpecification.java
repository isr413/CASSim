package com.seat.rescuesim.common;

import java.util.ArrayList;

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
    private Sensor[] sensors;

    public static DroneSpecification decode(JSONObject json) throws JSONException {
        double batteryPower = json.getDouble(DroneSpecification.DRONE_FUEL);
        double batteryUsage = json.getDouble(DroneSpecification.DRONE_MIN_FUEL_COST);
        Vector location = Vector.decode(json.getJSONArray(DroneSpecification.DRONE_LOCATION));
        double velocity = json.getDouble(DroneSpecification.DRONE_VELOCITY);
        double acceleration = json.getDouble(DroneSpecification.DRONE_ACCELERATION);
        JSONArray droneSensors = json.getJSONArray(DroneSpecification.DRONE_SENSORS);
        Sensor[] sensors = new Sensor[droneSensors.length()];
        for (int i = 0; i < sensors.length; i++) {
            sensors[i] = Sensor.decode(droneSensors.getJSONObject(i));
        }
        return new DroneSpecification(batteryPower, batteryUsage, location, velocity, acceleration, sensors);
    }

    public static DroneSpecification decode(String encoding) throws JSONException {
        return DroneSpecification.decode(new JSONObject(encoding));
    }

    public DroneSpecification(double batteryPower, double batteryUsage, Vector location, double velocity,
            double acceleration, Sensor[] sensors) {
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

    public Sensor[] getSensors() {
        return this.sensors;
    }

    public Sensor[] getSensorsWithType(SensorType type) {
        ArrayList<Sensor> sensorsWithType = new ArrayList<>();
        for (int i = 0; i < this.sensors.length; i++) {
            if (this.sensors[i].getType() == type) {
                sensorsWithType.add(this.sensors[i]);
            }
        }
        return (Sensor[]) sensorsWithType.toArray();
    }

    public boolean hasSensorType(SensorType type) {
        for (int i = 0; i < this.sensors.length; i++) {
            if (this.sensors[i].getType() == type) {
                return true;
            }
        }
        return false;
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
        for (int i = 0; i < this.sensors.length; i++) {
            droneSensors.put(this.sensors[i].toJSON());
        }
        json.put(DroneSpecification.DRONE_SENSORS, droneSensors);
        return json;
    }

    public String toString() {
        return this.encode();
    }

}
