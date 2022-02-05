package com.seat.rescuesim.simserver;

import java.util.ArrayList;
import java.util.Arrays;
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
    private static final String DRONE_LOCATION = "location";
    private static final String DRONE_REMOTE_ID = "remote_id";
    private static final String DRONE_VELOCITY = "velocity";

    private static int remoteCount = 0;

    public static DroneRemote decode(DroneSpecification droneSpec, JSONObject json) throws JSONException {
        int remoteID = json.getInt(DroneRemote.DRONE_REMOTE_ID);
        double batteryPower = json.getDouble(DroneRemote.DRONE_BATTERY);
        Vector location = Vector.decode(json.getJSONArray(DroneRemote.DRONE_LOCATION));
        Vector velocity = Vector.decode(json.getJSONArray(DroneRemote.DRONE_VELOCITY));
        Vector acceleration = Vector.decode(json.getJSONArray(DroneRemote.DRONE_ACCELERATION));
        JSONArray jsonSensors = json.getJSONArray(DroneRemote.DRONE_ACTIVE_SENSORS);
        ArrayList<SensorType> activeSensors = new ArrayList<>();
        for (int i = 0; i < jsonSensors.length(); i++) {
           activeSensors.add(SensorType.values()[jsonSensors.getInt(i)]);
        }
        return new DroneRemote(droneSpec, remoteID, batteryPower, location, velocity, acceleration, activeSensors);
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

    private Vector acceleration;
    private HashSet<SensorType> activeSensors;
    private double batteryPower;
    private Vector location;
    private int remoteID;
    private DroneSpecification spec;
    private Vector velocity;

    public DroneRemote(DroneSpecification droneSpec) {
        this(droneSpec, DroneRemote.getID(), droneSpec.getMaxBatteryPower(), droneSpec.getInitialLocation(),
            new Vector(), new Vector(), new HashSet<SensorType>());
    }

    public DroneRemote(DroneSpecification droneSpec, int remoteID, double batteryPower) {
        this(droneSpec, remoteID, batteryPower, droneSpec.getInitialLocation(), new Vector(), new Vector(),
                new HashSet<SensorType>());
    }

    public DroneRemote(DroneSpecification droneSpec, int remoteID, double batteryPower,
            SensorType[] activeSensors) {
        this(droneSpec, remoteID, batteryPower, droneSpec.getInitialLocation(), new Vector(), new Vector(),
            activeSensors);
    }

    public DroneRemote(DroneSpecification droneSpec, int remoteID, double batteryPower,
            ArrayList<SensorType> activeSensors) {
        this(droneSpec, remoteID, batteryPower, droneSpec.getInitialLocation(), new Vector(), new Vector(),
            activeSensors);
    }

    public DroneRemote(DroneSpecification droneSpec, int remoteID, double batteryPower,
            HashSet<SensorType> activeSensors) {
        this(droneSpec, remoteID, batteryPower, droneSpec.getInitialLocation(), new Vector(), new Vector(),
            activeSensors);
    }

    public DroneRemote(DroneSpecification droneSpec, int remoteID, double batteryPower, Vector location,
            Vector velocity, Vector acceleration) {
        this(droneSpec, remoteID, batteryPower, location, velocity, acceleration, new HashSet<SensorType>());
    }

    public DroneRemote(DroneSpecification droneSpec, int remoteID, double batteryPower, Vector location,
            Vector velocity, Vector acceleration, SensorType[] activeSensors) {
        this(droneSpec, remoteID, batteryPower, location, velocity, acceleration,
                new ArrayList<SensorType>(Arrays.asList(activeSensors)));
    }

    public DroneRemote(DroneSpecification droneSpec, int remoteID, double batteryPower, Vector location,
            Vector velocity, Vector acceleration, ArrayList<SensorType> activeSensors) {
        this(droneSpec, remoteID, batteryPower, location, velocity, acceleration);
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
    }

    public DroneRemote(DroneSpecification droneSpec, int remoteID, double batteryPower, Vector location,
            Vector velocity, Vector acceleration, HashSet<SensorType> activeSensors) {
        this.spec = droneSpec;
        this.remoteID = remoteID;
        this.batteryPower = batteryPower;
        this.location = location;
        this.velocity = velocity;
        this.acceleration = acceleration;
        this.activeSensors = activeSensors;
        for (SensorType type : this.activeSensors) {
            if (!this.spec.hasSensorWithType(type)) {
                Debugger.logger.err("drone (" + this.remoteID +
                        ") should have sensor with type (" + type.toString() + ")");
                this.activeSensors.remove(type);
            }
        }
        DroneRemote.updateRemoteCount();
    }

    public boolean activateSensorWithType(SensorType type) {
        if (!this.hasSensorWithType(type)) {
            Debugger.logger.err("Cannot activate sensor (" + type.toString() + ") of drone (" + this.remoteID + ")");
            return false;
        }
        if (this.hasActiveSensorWithType(type)) {
            Debugger.logger.warn("Sensor (" + type.toString() + ") of drone (" + this.remoteID + ") is already active");
            return true;
        }
        this.activeSensors.add(type);
        return true;
    }

    public void chargeBattery(double batteryPowerDelta) {
        if (batteryPowerDelta < 0) {
            Debugger.logger.err("Cannot update battery power of drone (" + this.remoteID + ") by " + batteryPowerDelta);
            return;
        }
        if (this.spec.getMaxBatteryPower() < this.batteryPower + batteryPowerDelta) {
            this.setBatteryPower(this.spec.getMaxBatteryPower());
        } else {
            this.setBatteryPower(this.batteryPower + batteryPowerDelta);
        }
    }

    public boolean deactivateSensorWithType(SensorType type) {
        if (!this.hasSensorWithType(type)) {
            Debugger.logger.err("Cannot deactivate sensor (" + type.toString() + ") of drone (" + this.remoteID + ")");
            return false;
        }
        if (!this.hasActiveSensorWithType(type)) {
            Debugger.logger.warn("Sensor (" + type.toString() + ") of drone (" +
                    this.remoteID + ") is already inactive");
            return true;
        }
        this.activeSensors.remove(type);
        return true;
    }

    public void disable() {
        this.batteryPower = 0;
        this.acceleration = new Vector();
        this.velocity = new Vector();
        this.activeSensors = new HashSet<>();
    }

    public Vector getAcceleration() {
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

    public Vector getVelocity() {
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

    public boolean isAlive() {
        return this.batteryPower > 0;
    }

    public boolean isDisabled() {
        return !this.isAlive();
    }

    public boolean isMoving() {
        return this.velocity.getMagnitude() > 0;
    }

    public boolean isKinetic() {
        return this.spec.isKinetic();
    }

    private void setBatteryPower(double batteryPower) {
        if (this.spec.getMaxBatteryPower() < batteryPower) {
            Debugger.logger.warn("Cannot set battery power of drone (" + this.remoteID + ") to be " + batteryPower);
            Debugger.logger.state("Setting battery power of drone (" + this.remoteID +
                ") to be " + this.spec.getMaxBatteryPower());
            batteryPower = this.spec.getMaxBatteryPower();
        } else if (batteryPower < 0) {
            Debugger.logger.err("Cannot set battery power of drone (" + this.remoteID + ") to be " + batteryPower);
            Debugger.logger.state("Setting battery power of drone (" + this.remoteID + ") to be 0");
            batteryPower = 0;
        }
        this.batteryPower = batteryPower;
    }

    public String encode() {
        return this.toJSON().toString();
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put(DroneRemote.DRONE_REMOTE_ID, this.remoteID);
        json.put(DroneRemote.DRONE_BATTERY, this.batteryPower);
        json.put(DroneRemote.DRONE_LOCATION, this.location.toJSON());
        json.put(DroneRemote.DRONE_VELOCITY, this.velocity.toJSON());
        json.put(DroneRemote.DRONE_ACCELERATION, this.acceleration.toJSON());
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
