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
                Debugger.logger.err(String.format("Drone %s should have sensor with type %s",
                    this.getDroneLabel(), this.getSensorLabel(type)));
            } else if (this.hasActiveSensorWithType(type)) {
                Debugger.logger.err(String.format("Drone has duplicate active sensor of type %s",
                    this.getDroneLabel(), this.getSensorLabel(type)));
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
                Debugger.logger.err(String.format("Drone %s should have sensor with type %s",
                    this.getDroneLabel(), this.getSensorLabel(type)));
                this.activeSensors.remove(type);
            }
        }
        DroneRemote.updateRemoteCount();
    }

    private String getDroneLabel() {
        return String.format("(%d)", this.remoteID);
    }

    private String getSensorLabel(SensorType type) {
        return String.format("(%d)", type.getType());
    }

    public boolean activateSensorWithType(SensorType type) {
        if (!this.hasSensorWithType(type)) {
            Debugger.logger.err(String.format("Cannot activate sensor %s of drone %s",
                this.getSensorLabel(type), this.getDroneLabel()));
            return false;
        }
        if (this.hasActiveSensorWithType(type)) {
            Debugger.logger.warn(String.format("Sensor %s of drone %s is already active",
                this.getSensorLabel(type), this.getDroneLabel()));
            return true;
        }
        this.activeSensors.add(type);
        return true;
    }

    public void chargeBattery(double batteryPowerDelta) {
        if (batteryPowerDelta < 0) {
            Debugger.logger.err(String.format("Cannot update battery power of drone %s by %.2f",
                this.getDroneLabel(), batteryPowerDelta));
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
            Debugger.logger.err(String.format("Cannot deactivate sensor %s of drone %s",
                this.getSensorLabel(type), this.getDroneLabel()));
            return false;
        }
        if (!this.hasActiveSensorWithType(type)) {
            Debugger.logger.warn(String.format("Sensor %s of drone %s is already inactive",
                this.getSensorLabel(type), this.getDroneLabel()));
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
            Debugger.logger.err(String.format("No sensor with type %s on drone %s",
                this.getSensorLabel(type), this.getDroneLabel()));
            return null;
        }
        if (!this.hasActiveSensorWithType(type)) {
            Debugger.logger.err(String.format("Sensor with type %s is inactive on drone %s",
                this.getSensorLabel(type), this.getDroneLabel()));
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
            Debugger.logger.warn(String.format("Cannot set acceleration of drone %s to be %s",
                this.getDroneLabel(), acceleration.toString()));
            acceleration = Vector.scale(acceleration.getUnitVector(), this.spec.getMaxAcceleration());
            Debugger.logger.state(String.format("Setting acceleration of drone %s to be %s",
                this.getDroneLabel(), acceleration.toString()));
        }
        this.acceleration = acceleration;
    }

    private void setBatteryPower(double batteryPower) {
        if (batteryPower < 0 || this.spec.getMaxBatteryPower() < batteryPower) {
            Debugger.logger.warn(String.format("Cannot set battery power of drone %s to be %.2f",
                this.getDroneLabel(), batteryPower));
            batteryPower = (batteryPower < 0) ? 0 : this.spec.getMaxBatteryPower();
            Debugger.logger.warn(String.format("Setting battery power of drone %s to be %.2f",
                this.getDroneLabel(), batteryPower));
        }
        this.batteryPower = batteryPower;
    }

    private void setLocation(Vector location) {
        this.location = location;
    }

    private void setVelocity(Vector velocity) {
        if (this.spec.getMaxVelocity() < velocity.getMagnitude()) {
            Debugger.logger.warn(String.format("Cannot set velocity of drone %s to be %s",
                this.getDroneLabel(), velocity.toString()));
            velocity = Vector.scale(velocity.getUnitVector(), this.spec.getMaxVelocity());
            Debugger.logger.state(String.format("Setting velocity of drone %s to be %s",
                this.getDroneLabel(), velocity.toString()));
        }
        this.velocity = velocity;
    }

    public void update() {
        this.update(null, null, null);
    }

    public void update(HashSet<SensorType> activations) {
        this.update(null, activations, null);
    }

    public void update(HashSet<SensorType> activations, HashSet<SensorType> deactivations) {
        this.update(null, activations, deactivations);
    }

    public void update(Vector jerk) {
        this.update(jerk, null, null);
    }

    public void update(Vector jerk, HashSet<SensorType> activations) {
        this.update(jerk, activations, null);
    }

    /**
     * Pushes the drone with the force of delta, and activates and deactivates the specified sensors.
     *
     * @param jerk the change in acceleration of the drone
     * @param activations the sensors to be activated
     * @param deactivations the sensors to be deactivated
     */
    public void update(Vector jerk, HashSet<SensorType> activations, HashSet<SensorType> deactivations) {
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
        if (this.isMoving() || jerk != null) {
            Vector newAcceleration = this.acceleration;
            if (jerk != null) {
                if (this.spec.getMaxJerk() < jerk.getMagnitude()) {
                    Debugger.logger.warn(String.format("Cannot update acceleration of drone %s by jerk %s",
                        this.getDroneLabel(), jerk.toString()));
                    jerk = Vector.scale(jerk.getUnitVector(), this.spec.getMaxJerk());
                    Debugger.logger.state(String.format("Updating acceleration of drone %s by jerk %s",
                        this.getDroneLabel(), jerk.toString()));
                }
                newAcceleration = Vector.add(this.acceleration, jerk);
                if (this.spec.getMaxAcceleration() < newAcceleration.getMagnitude()) {
                    Debugger.logger.warn(String.format("Cannot update acceleration of drone %s to %s",
                        this.getDroneLabel(), newAcceleration.toString()));
                    newAcceleration = Vector.scale(newAcceleration.getUnitVector(), this.spec.getMaxAcceleration());
                    jerk = Vector.subtract(newAcceleration, this.acceleration);
                    Debugger.logger.state(String.format("Updating acceleration of drone %s to %s",
                        this.getDroneLabel(), newAcceleration.toString()));
                }
            }
            Vector newVelocity = Vector.add(this.velocity, newAcceleration);
            if (this.spec.getMaxVelocity() < newVelocity.getMagnitude()) {
                Debugger.logger.warn(String.format("Cannot update velocity of drone %s to %s",
                        this.getDroneLabel(), newVelocity.toString()));
                newVelocity = Vector.scale(newVelocity.getUnitVector(), this.spec.getMaxVelocity());
                newAcceleration = Vector.subtract(newVelocity, this.velocity);
                jerk = Vector.subtract(newAcceleration, this.acceleration);
                Debugger.logger.state(String.format("Updating velocity of drone %s to %s",
                        this.getDroneLabel(), newVelocity.toString()));
            }
            this.setAcceleration(newAcceleration);
            this.setVelocity(newVelocity);
        }
        this.updateBatteryPower();
        if (this.isAlive() && this.isMoving()) {
            this.updateLocation();
        } else if (!this.isAlive()) {
            this.disable();
        }
    }

    private void updateBatteryPower() {
        double batteryUsage = this.spec.getStaticBatteryUsage();
        Vector horizontalAcceleration = new Vector(this.acceleration.getX(), this.acceleration.getY(), 0);
        batteryUsage += this.spec.getHorizontalKineticBatteryUsage() * horizontalAcceleration.getMagnitude();
        batteryUsage += this.spec.getVerticalKineticBatteryUsage() * this.acceleration.getZ();
        for (SensorType type : this.activeSensors) {
            batteryUsage += this.getSensorWithType(type).getBatteryUsage();
        }
        if (this.batteryPower - batteryUsage > 0) {
            this.setBatteryPower(this.batteryPower - batteryUsage);
        } else {
            this.setBatteryPower(0);
        }
    }

    private void updateLocation() {
        this.setLocation(Vector.add(this.location, this.velocity));
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
