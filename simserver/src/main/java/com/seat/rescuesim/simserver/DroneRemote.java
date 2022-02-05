package com.seat.rescuesim.simserver;

import java.util.ArrayList;
import java.util.HashSet;

import com.seat.rescuesim.common.Debugger;
import com.seat.rescuesim.common.DroneSpecification;
import com.seat.rescuesim.common.Sensor;
import com.seat.rescuesim.common.SensorType;
import com.seat.rescuesim.common.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DroneRemote {
    private static final String DRONE_ACCELERATION = "acceleration";
    private static final String DRONE_ACTIVE_SENSORS = "active_sensors";
    private static final String DRONE_BATTERY = "battery_power";
    private static final String DRONE_DIRECTION = "direction";
    private static final String DRONE_LOCATION = "location";
    private static final String DRONE_REMOTE_ID = "remote_id";
    private static final String DRONE_VELOCITY = "speed";

    private static int remoteCount = 0;

    public static DroneRemote decode(DroneSpecification droneSpec, JSONObject json) throws JSONException {
        int remoteID = json.getInt(DroneRemote.DRONE_REMOTE_ID);
        double batteryPower = json.getDouble(DroneRemote.DRONE_BATTERY);
        Vector location = Vector.decode(json.getJSONArray(DroneRemote.DRONE_LOCATION));
        Vector direction = Vector.decode(json.getJSONArray(DroneRemote.DRONE_DIRECTION));
        double velocity = json.getDouble(DroneRemote.DRONE_VELOCITY);
        double acceleration = json.getDouble(DroneRemote.DRONE_ACCELERATION);
        JSONArray jsonSensors = json.getJSONArray(DroneRemote.DRONE_ACTIVE_SENSORS);
        ArrayList<SensorType> activeSensors = new ArrayList<>();
        for (int i = 0; i < jsonSensors.length(); i++) {
           activeSensors.add(SensorType.values()[jsonSensors.getInt(i)]);
        }
        return new DroneRemote(droneSpec, remoteID, batteryPower, location, direction, velocity, acceleration, activeSensors);
    }

    public static DroneRemote decode(DroneSpecification droneSpec, String encoding) throws JSONException {
        return DroneRemote.decode(droneSpec, new JSONObject(encoding));
    }

    public static int getID() {
        return DroneRemote.remoteCount;
    }

    private static void updateRemoteCount() {
        DroneRemote.remoteCount++;
    }

    private double acceleration;
    private HashSet<SensorType> activeSensors;
    private double batteryPower;
    private Vector direction;
    private Vector location;
    private int remoteID;
    private DroneSpecification spec;
    private double velocity;

    public DroneRemote(DroneSpecification droneSpec) {
        this.spec = droneSpec;
        this.remoteID = DroneRemote.getID();
        this.batteryPower = this.spec.getMaxBatteryPower();
        this.location = this.spec.getInitialLocation();
        this.direction = new Vector();
        this.velocity = 0.0;
        this.acceleration = 0.0;
        this.activeSensors = new HashSet<>();
        DroneRemote.updateRemoteCount();
    }

    public DroneRemote(DroneSpecification droneSpec, int remoteID, double batteryPower, Vector location,
            Vector direction, double velocity, double acceleration, ArrayList<SensorType> activeSensors) {
        this.spec = droneSpec;
        this.remoteID = remoteID;
        this.batteryPower = batteryPower;
        this.location = location;
        this.direction = direction;
        this.velocity = velocity;
        this.acceleration = acceleration;
        this.activeSensors = new HashSet<>();
        for (SensorType type : activeSensors) {
            if (!this.spec.hasSensorWithType(type)) {
                Debugger.logger.err("drone (" + this.remoteID +
                        ") should have sensor with type (" + type.toString() + ")");
            } else if (this.hasActiveSensorWithType(type)) {
                Debugger.logger.err("drone (" + this.remoteID +
                        ") has duplicate active sensor of type (" + type.toString() + ")");
            } else {
                this.activeSensors.add(type);
            }
        }
        DroneRemote.updateRemoteCount();
    }

    public double getAcceleration() {
        return this.acceleration;
    }

    public ArrayList<Sensor> getActiveSensors() {
        ArrayList<Sensor> sensors = new ArrayList<>();
        for (SensorType type : this.activeSensors) {
            sensors.add(this.spec.getSensorWithType(type));
        }
        return sensors;
    }

    public Sensor getActiveSensorWithType(SensorType type) {
        if (!this.spec.hasSensorWithType(type)) {
            Debugger.logger.err("No sensor with type (" + type.toString() + ") on drone (" + this.remoteID + ")");
            return null;
        }
        if (!this.hasActiveSensorWithType(type)) {
            Debugger.logger.err("Sensor with type (" + type.toString() +
                    ") is inactive on drone (" + this.remoteID + ")");
            return null;
        }
        return this.spec.getSensorWithType(type);
    }

    public ArrayList<SensorType> getActiveSensorTypes() {
        return new ArrayList<SensorType>(this.activeSensors);
    }

    public double getBatteryPower() {
        return this.batteryPower;
    }

    public Vector getDirection() {
        return this.direction;
    }

    public DroneSpecification getDroneSpecification() {
        return this.spec;
    }

    public Vector getLocation() {
        return this.location;
    }

    public int getRemoteID() {
        return this.remoteID;
    }

    public ArrayList<Sensor> getSensors() {
        return this.spec.getSensors();
    }

    public Sensor getSensorWithType(SensorType type) {
        return this.spec.getSensorWithType(type);
    }

    public double getVelocity() {
        return this.velocity;
    }

    public boolean hasActiveSensors() {
        return !this.activeSensors.isEmpty();
    }

    public boolean hasActiveSensorWithType(SensorType type) {
        return this.activeSensors.contains(type);
    }

    public boolean hasSensors() {
        return this.spec.hasSensors();
    }

    public boolean hasSensorWithType(SensorType type) {
        return this.spec.hasSensorWithType(type);
    }

    public String encode() {
        return this.toJSON().toString();
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put(DroneRemote.DRONE_REMOTE_ID, this.remoteID);
        json.put(DroneRemote.DRONE_BATTERY, this.batteryPower);
        json.put(DroneRemote.DRONE_LOCATION, this.location.toJSON());
        json.put(DroneRemote.DRONE_DIRECTION, this.direction.toJSON());
        json.put(DroneRemote.DRONE_VELOCITY, this.velocity);
        json.put(DroneRemote.DRONE_ACCELERATION, this.acceleration);
        JSONArray jsonSensors = new JSONArray();
        for (SensorType type : this.activeSensors) {
            jsonSensors.put(type.getType());
        }
        json.put(DroneRemote.DRONE_ACTIVE_SENSORS, jsonSensors);
        return json;
    }

    public String toString() {
        return this.encode();
    }

}
