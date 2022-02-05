package com.seat.rescuesim.simserver;

import java.util.ArrayList;
import java.util.Arrays;

import com.seat.rescuesim.common.DroneSpecification;
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
        JSONArray sensors = json.getJSONArray(DroneRemote.DRONE_ACTIVE_SENSORS);
        SensorType[] activeSensors = new SensorType[sensors.length()];
        for (int i = 0; i < activeSensors.length; i++) {
            activeSensors[i] = SensorType.values()[sensors.getInt(i)];
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
    private ArrayList<SensorType> activeSensors;
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
        this.activeSensors = new ArrayList<>();
        DroneRemote.updateRemoteCount();
    }

    public DroneRemote(DroneSpecification droneSpec, int remoteID, double batteryPower, Vector location, Vector direction,
            double velocity, double acceleration, SensorType[] activeSensors) {
        this.spec = droneSpec;
        this.remoteID = remoteID;
        this.batteryPower = batteryPower;
        this.location = location;
        this.direction = direction;
        this.velocity = velocity;
        this.acceleration = acceleration;
        if (activeSensors.length > 0) {
            this.activeSensors = new ArrayList<>(Arrays.asList(activeSensors));
        } else {
            this.activeSensors = new ArrayList<>();
        }
        DroneRemote.updateRemoteCount();
    }

    public double getAcceleration() {
        return this.acceleration;
    }

    public SensorType[] getActiveSensors() {
        return (SensorType[]) this.activeSensors.toArray();
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

    public double getVelocity() {
        return this.velocity;
    }

    public boolean hasActiveSensor(SensorType type) {
        for (int i = 0; i < this.activeSensors.size(); i++) {
            if (this.activeSensors.get(i) == type) {
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
        json.put(DroneRemote.DRONE_REMOTE_ID, this.remoteID);
        json.put(DroneRemote.DRONE_BATTERY, this.batteryPower);
        json.put(DroneRemote.DRONE_LOCATION, this.location.toJSON());
        json.put(DroneRemote.DRONE_DIRECTION, this.direction.toJSON());
        json.put(DroneRemote.DRONE_VELOCITY, this.velocity);
        json.put(DroneRemote.DRONE_ACCELERATION, this.acceleration);
        JSONArray sensors = new JSONArray();
        for (int i = 0; i < this.activeSensors.size(); i++) {
            sensors.put(this.activeSensors.get(i).getType());
        }
        json.put(DroneRemote.DRONE_ACTIVE_SENSORS, sensors);
        return json;
    }

    public String toString() {
        return this.encode();
    }

}
