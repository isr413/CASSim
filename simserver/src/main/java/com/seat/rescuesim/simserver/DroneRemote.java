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
    private static final String DRONE_VELOCITY = "speed";

    private static int remoteCount = 0;

    public static DroneRemote decode(JSONObject json, DroneSpecification droneSpec) throws JSONException {
        double batteryPower = json.getDouble(DroneRemote.DRONE_BATTERY);
        Vector location = Vector.decode(json.getJSONArray(DroneRemote.DRONE_LOCATION));
        Vector velocity = Vector.decode(json.getJSONArray(DroneRemote.DRONE_VELOCITY));
        Vector acceleration = Vector.decode(json.getJSONArray(DroneRemote.DRONE_ACCELERATION));
        JSONArray jsonSensors = json.getJSONArray(DroneRemote.DRONE_ACTIVE_SENSORS);
        ArrayList<SensorType> activeSensors = new ArrayList<>();
        for (int i = 0; i < jsonSensors.length(); i++) {
           activeSensors.add(SensorType.values()[jsonSensors.getInt(i)]);
        }
        return new DroneRemote(droneSpec, batteryPower, location, velocity, acceleration, activeSensors);
    }

    public static DroneRemote decode(String encoding, DroneSpecification droneSpec) throws JSONException {
        return DroneRemote.decode(new JSONObject(encoding), droneSpec);
    }

    public static int getID() {
        DroneRemote.remoteCount++;
        return DroneRemote.remoteCount-1;
    }

    private Vector acceleration;
    private HashSet<SensorType> activeSensors;
    private double batteryPower;
    private Vector location;
    private int remoteID;
    private DroneSpecification spec;
    private Vector velocity;

    public DroneRemote(DroneSpecification droneSpec) {
        this(droneSpec, droneSpec.getMaxBatteryPower(), droneSpec.getInitialLocation(), new Vector(), new Vector(),
                new HashSet<SensorType>());
    }

    public DroneRemote(DroneSpecification droneSpec, double batteryPower) {
        this(droneSpec, batteryPower, droneSpec.getInitialLocation(), new Vector(), new Vector(),
                new HashSet<SensorType>());
    }

    public DroneRemote(DroneSpecification droneSpec, double batteryPower, SensorType[] activeSensors) {
        this(droneSpec, batteryPower, droneSpec.getInitialLocation(), new Vector(), new Vector(), activeSensors);
    }

    public DroneRemote(DroneSpecification droneSpec, double batteryPower, ArrayList<SensorType> activeSensors) {
        this(droneSpec, batteryPower, droneSpec.getInitialLocation(), new Vector(), new Vector(), activeSensors);
    }

    public DroneRemote(DroneSpecification droneSpec, double batteryPower, HashSet<SensorType> activeSensors) {
        this(droneSpec, batteryPower, droneSpec.getInitialLocation(), new Vector(), new Vector(), activeSensors);
    }

    public DroneRemote(DroneSpecification droneSpec, double batteryPower, Vector location,
            Vector velocity, Vector acceleration) {
        this(droneSpec, batteryPower, location, velocity, acceleration, new HashSet<SensorType>());
    }

    public DroneRemote(DroneSpecification droneSpec, double batteryPower, Vector location,
            Vector velocity, Vector acceleration, SensorType[] activeSensors) {
        this(droneSpec, batteryPower, location, velocity, acceleration,
                new ArrayList<SensorType>(Arrays.asList(activeSensors)));
    }

    public DroneRemote(DroneSpecification droneSpec, double batteryPower, Vector location,
            Vector velocity, Vector acceleration, ArrayList<SensorType> activeSensors) {
        this(droneSpec, batteryPower, location, velocity, acceleration);
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

    public DroneRemote(DroneSpecification droneSpec, double batteryPower, Vector location,
            Vector velocity, Vector acceleration, HashSet<SensorType> activeSensors) {
        this.spec = droneSpec;
        this.remoteID = DroneRemote.getID();
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
        this.batteryPower = 0.0;
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
            Debugger.logger.err("No sensor with type (" + type.toString() + ") found on drone (" + this.remoteID + ")");
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

    private void setAcceleration(Vector acceleration) {
        if (this.spec.getMaxAcceleration() < acceleration.getMagnitude()) {
            Debugger.logger.warn("Cannot set acceleration of drone (" + this.remoteID +
                ") to be " + acceleration.toString());
            this.acceleration = Vector.scale(acceleration.getUnitVector(), this.spec.getMaxAcceleration());
        } else {
            this.acceleration = acceleration;
        }
    }

    private void setBatteryPower(double batteryPower) {
        if (this.spec.getMaxBatteryPower() < batteryPower) {
            Debugger.logger.warn("Cannot set battery power of drone (" + this.remoteID + ") to be " + batteryPower);
            this.batteryPower = this.spec.getMaxBatteryPower();
        } else if (batteryPower < 0) {
            Debugger.logger.err("Cannot set battery power of drone (" + this.remoteID + ") to be " + batteryPower);
            this.batteryPower = 0.0;
        } else {
            this.batteryPower = batteryPower;
        }
    }

    private void setLocation(Vector location) {
        this.location = location;
    }

    private void setVelocity(Vector velocity) {
        if (this.spec.getMaxVelocity() < velocity.getMagnitude()) {
            Debugger.logger.warn("Cannot set velocity of drone (" + this.remoteID + ") to be " + velocity.toString());
            this.velocity = Vector.scale(velocity.getUnitVector(), this.spec.getMaxVelocity());
        } else {
            this.velocity = velocity;
        }
    }

    public void update() {
        this.update(null, 0.0, null, null);
    }

    public void update(HashSet<SensorType> activations) {
        this.update(null, 0.0, activations, null);
    }

    public void update(HashSet<SensorType> activations, HashSet<SensorType> deactivations) {
        this.update(null, 0.0, activations, deactivations);
    }

    public void update(Vector direction, double delta) {
        this.update(direction, delta, null, null);
    }

    public void update(Vector direction, double delta, HashSet<SensorType> activations) {
        this.update(direction, delta, activations, null);
    }

    /**
     * Pushes the drone with the force of delta, and activates and deactivates the specified sensors.
     *
     * @param delta the vector of the acceleration/deceleration force acting on the drone
     * @param activations the sensors to be activated
     * @param deactivations the sensors to be deactivated
     */
    public void update(Vector delta, HashSet<SensorType> activations, HashSet<SensorType> deactivations) {
        if (activations != null) {
            for (SensorType type : activations) {
                this.activateSensorWithType(type);
            }
        }
        if (deactivations != null) {
            for (SensorType type : deactivations) {
                this.deactivateSensorWithType(type);
            }
        }
        if (this.spec.getMaxAccelerationDelta() < delta.getMagnitude()) {
            Debugger.logger.warn("Cannot update acceleration of drone (" + this.remoteID + ") by " + delta.toString());
            delta = Vector.scale(delta.getUnitVector(), this.spec.getMaxAccelerationDelta());
        }
        this.updateBatteryPower(delta);
        if (!this.isAlive()) {
            this.disable();
            return;
        }
        if ()
        this.updateAcceleration(delta);
        this.updateVelocity(this.acceleration);
        this.updateLocation(this.velocity);
    }

    private void updateBatteryPower(Vector delta) {
        double batteryUsage = this.spec.getStaticBatteryUsage();
        batteryUsage += this.spec.getKineticBatteryUsage() * delta.getMagnitude();
        for (SensorType type : this.activeSensors) {
            batteryUsage += this.getSensorWithType(type).getBatteryUsage();
        }
        if (this.batteryPower - batteryUsage > 0) {
            this.setBatteryPower(this.batteryPower - batteryUsage);
        } else {
            this.setBatteryPower(0);
        }
    }

    public String encode() {
        return this.toJSON().toString();
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
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
