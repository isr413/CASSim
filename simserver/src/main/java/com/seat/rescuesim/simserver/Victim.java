package com.seat.rescuesim.simserver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import com.seat.rescuesim.common.Debugger;
import com.seat.rescuesim.common.VictimSpecification;
import com.seat.rescuesim.common.Sensor;
import com.seat.rescuesim.common.SensorType;
import com.seat.rescuesim.common.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Victim {
    private static final String VICTIM_ACTIVE_SENSORS = "active_sensors";
    private static final String VICTIM_BATTERY = "battery_power";
    private static final String VICTIM_ID = "victim_id";
    private static final String VICTIM_LOCATION = "location";
    private static final String VICTIM_VELOCITY = "velocity";

    private static int victimCount = 0;

    public static Victim decode(VictimSpecification victimSpec, JSONObject json) throws JSONException {
        int victimID = json.getInt(Victim.VICTIM_ID);
        double batteryPower = json.getDouble(Victim.VICTIM_BATTERY);
        Vector location = Vector.decode(json.getJSONArray(Victim.VICTIM_LOCATION));
        Vector velocity = Vector.decode(json.getJSONArray(Victim.VICTIM_VELOCITY));
        JSONArray jsonSensors = json.getJSONArray(Victim.VICTIM_ACTIVE_SENSORS);
        ArrayList<SensorType> activeSensors = new ArrayList<>();
        for (int i = 0; i < jsonSensors.length(); i++) {
           activeSensors.add(SensorType.values()[jsonSensors.getInt(i)]);
        }
        return new Victim(victimSpec, victimID, batteryPower, location, velocity, activeSensors);
    }

    public static Victim decode(VictimSpecification victimSpec, String encoding) throws JSONException {
        return Victim.decode(victimSpec, new JSONObject(encoding));
    }

    public static int getID() {
        return Victim.victimCount;
    }

    private static void updateVictimCount() {
        Victim.victimCount++;
    }

    private HashSet<SensorType> activeSensors;
    private double batteryPower;
    private Vector location;
    private VictimSpecification spec;
    private Vector velocity;
    private int victimID;

    public Victim(VictimSpecification victimSpec) {
        this(victimSpec, Victim.getID(), victimSpec.getMaxBatteryPower(), new Vector(), new Vector(),
            new HashSet<SensorType>());
    }

    public Victim(VictimSpecification victimSpec, int victimID, double batteryPower) {
        this(victimSpec, victimID, batteryPower, new Vector(), new Vector());
    }

    public Victim(VictimSpecification victimSpec, int victimID, double batteryPower, SensorType[] activeSensors) {
        this(victimSpec, victimID, batteryPower, new Vector(), new Vector(), activeSensors);
    }

    public Victim(VictimSpecification victimSpec, int victimID, double batteryPower,
            ArrayList<SensorType> activeSensors) {
        this(victimSpec, victimID, batteryPower, new Vector(), new Vector(), activeSensors);
    }

    public Victim(VictimSpecification victimSpec, int victimID, double batteryPower,
            HashSet<SensorType> activeSensors) {
        this(victimSpec, victimID, batteryPower, new Vector(), new Vector(), activeSensors);
    }

    public Victim(VictimSpecification victimSpec, int victimID, double batteryPower, Vector location, Vector velocity) {
        this(victimSpec, victimID, batteryPower, location, velocity, new HashSet<SensorType>());
    }

    public Victim(VictimSpecification victimSpec, int victimID, double batteryPower, Vector location, Vector velocity,
            SensorType[] activeSensors) {
        this(victimSpec, victimID, batteryPower, location, velocity,
            new ArrayList<SensorType>(Arrays.asList(activeSensors)));
    }

    public Victim(VictimSpecification victimSpec, int victimID, double batteryPower, Vector location, Vector velocity,
            ArrayList<SensorType> activeSensors) {
        this(victimSpec, victimID, batteryPower, location, velocity, new HashSet<SensorType>());
        for (SensorType type : activeSensors) {
            if (!this.spec.hasSensorWithType(type)) {
                Debugger.logger.err(String.format("Victim %s should have sensor with type %s",
                    this.getLabel(), type.getLabel()));
            } else if (this.hasActiveSensorWithType(type)) {
                Debugger.logger.err(String.format("Victim has duplicate active sensor of type %s",
                    this.getLabel(), type.getLabel()));
            } else {
                this.activeSensors.add(type);
            }
        }
    }

    public Victim(VictimSpecification victimSpec, int victimID, double batteryPower, Vector location, Vector velocity,
            HashSet<SensorType> activeSensors) {
        this.spec = victimSpec;
        this.victimID = victimID;
        this.batteryPower = batteryPower;
        this.location = location;
        this.velocity = velocity;
        this.activeSensors = activeSensors;
        for (SensorType type : this.activeSensors) {
            if (!this.spec.hasSensorWithType(type)) {
                Debugger.logger.err(String.format("Victim %s should have sensor with type %s",
                    this.getLabel(), type.getLabel()));
                this.activeSensors.remove(type);
            }
        }
        Victim.updateVictimCount();
    }

    public boolean activateSensorWithType(SensorType type) {
        if (!this.hasSensorWithType(type)) {
            Debugger.logger.err(String.format("Cannot activate sensor %s of victim %s",
                type.getLabel(), this.getLabel()));
            return false;
        }
        if (this.hasActiveSensorWithType(type)) {
            Debugger.logger.warn(String.format("Sensor %s of victim %s is already active",
                type.getLabel(), this.getLabel()));
            return true;
        }
        this.activeSensors.add(type);
        return true;
    }

    public void chargeBattery(double batteryPowerDelta) {
        if (batteryPowerDelta < 0) {
            Debugger.logger.err(String.format("Cannot update battery power of victim %s by %.2f",
                this.getLabel(), batteryPowerDelta));
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
            Debugger.logger.err(String.format("Cannot deactivate sensor %s of victim %s",
                type.getLabel(), this.getLabel()));
            return false;
        }
        if (!this.hasActiveSensorWithType(type)) {
            Debugger.logger.warn(String.format("Sensor %s of victim %s is already inactive",
                type.getLabel(), this.getLabel()));
            return true;
        }
        this.activeSensors.remove(type);
        return true;
    }

    public void disableBattery() {
        this.batteryPower = 0;
        this.activeSensors = new HashSet<>();
    }

    public void disableVictim() {
        this.velocity = new Vector();
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
            Debugger.logger.err(String.format("No sensor with type %s on victim %s",
                type.getLabel(), this.getLabel()));
            return null;
        }
        if (!this.hasActiveSensorWithType(type)) {
            Debugger.logger.err(String.format("Sensor with type %s is inactive on victim %s",
                type.getLabel(), this.getLabel()));
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

    public String getLabel() {
        return String.format("<%d>", this.victimID);
    }

    public Vector getLocation() {
        return this.location;
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

    public int getVictimID() {
        return this.victimID;
    }

    public VictimSpecification getVictimSpecification() {
        return this.spec;
    }

    public boolean hasActiveSensors() {
        return !this.activeSensors.isEmpty();
    }

    public boolean hasActiveSensorWithType(SensorType type) {
        return this.activeSensors.contains(type);
    }

    public boolean hasLiveBattery() {
        return this.batteryPower > 0;
    }

    public boolean hasDisabledBattery() {
        return !this.hasLiveBattery();
    }

    public boolean hasSensors() {
        return this.spec.hasSensors();
    }

    public boolean hasSensorWithType(SensorType type) {
        return this.spec.hasSensorWithType(type);
    }

    public boolean isMoving() {
        return this.velocity.getMagnitude() > 0;
    }

    public boolean isProne() {
        return !this.isMoving();
    }

    private void setBatteryPower(double batteryPower) {
        if (batteryPower < 0 || this.spec.getMaxBatteryPower() < batteryPower) {
            Debugger.logger.warn(String.format("Cannot set battery power of victim %s to be %.2f",
                this.getLabel(), batteryPower));
            batteryPower = (batteryPower < 0) ? 0 : this.spec.getMaxBatteryPower();
            Debugger.logger.state(String.format("Setting battery power of victim %s to be %.2f",
                this.getLabel(), batteryPower));
        }
        this.batteryPower = batteryPower;
    }

    private void setLocation(Vector location) {
        this.location = location;
    }

    private void setVelocity(Vector velocity) {
        if (this.spec.getMaxVelocity() < velocity.getMagnitude()) {
            Debugger.logger.warn(String.format("Cannot set velocity of victim %s to be %s",
                this.getLabel(), velocity.toString()));
            velocity = Vector.scale(velocity.getUnitVector(), this.spec.getMaxVelocity());
            Debugger.logger.state(String.format("Setting velocity of victim %s to be %s",
                this.getLabel(), velocity.toString()));
        }
        this.velocity = velocity;
    }

    public void update(double stepSize) {
        this.update(stepSize, null, null, null);
    }

    public void update(double stepSize, HashSet<SensorType> activations) {
        this.update(stepSize, null, activations, null);
    }

    public void update(double stepSize, HashSet<SensorType> activations, HashSet<SensorType> deactivations) {
        this.update(stepSize, null, activations, deactivations);
    }

    public void update(double stepSize, Vector jerk) {
        this.update(stepSize, jerk, null, null);
    }

    public void update(double stepSize, Vector jerk, HashSet<SensorType> activations) {
        this.update(stepSize, jerk, activations, null);
    }

    /**
     * Pushes the victim with the force of jerk, and activates and deactivates the specified sensors.
     *
     * @param stepSize the change in time measured in seconds from the last update
     * @param jerk the change in acceleration of the victim for this step
     * @param activations the sensors to be activated
     * @param deactivations the sensors to be deactivated
     */
    public void update(double stepSize, Vector jerk, HashSet<SensorType> activations,
            HashSet<SensorType> deactivations) {
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
        this.updateBatteryPower(stepSize);
        if (!this.hasLiveBattery()) {
            this.disableBattery();
        }
        if (jerk != null) {
            Vector newVelocity = Vector.add(this.velocity, jerk);
            if (this.spec.getMaxVelocity() < newVelocity.getMagnitude()) {
                Debugger.logger.warn(String.format("Cannot update velocity of victim %s to %s",
                        this.getLabel(), newVelocity.toString()));
                newVelocity = Vector.scale(newVelocity.getUnitVector(), this.spec.getMaxVelocity());
                jerk = Vector.subtract(newVelocity, this.velocity);
                Debugger.logger.state(String.format("Updating velocity of victim %s to %s",
                        this.getLabel(), newVelocity.toString()));
            }
            this.setVelocity(newVelocity);
        }
        if (this.isMoving()) {
            this.updateLocation(stepSize);
        }
    }

    private void updateBatteryPower(double stepSize) {
        double batteryUsage = this.spec.getStaticBatteryUsage();
        for (SensorType type : this.activeSensors) {
            batteryUsage += this.getSensorWithType(type).getBatteryUsage();
        }
        batteryUsage *= stepSize;
        if (this.batteryPower - batteryUsage > 0) {
            this.setBatteryPower(this.batteryPower - batteryUsage);
        } else {
            this.setBatteryPower(0);
        }
    }

    private void updateLocation(double stepSize) {
        this.setLocation(Vector.add(this.location, Vector.scale(this.velocity, stepSize)));
    }

    public String encode() {
        return this.toJSON().toString();
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put(Victim.VICTIM_ID, this.victimID);
        json.put(Victim.VICTIM_BATTERY, this.batteryPower);
        json.put(Victim.VICTIM_LOCATION, this.location.toJSON());
        json.put(Victim.VICTIM_VELOCITY, this.velocity.toJSON());
        JSONArray jsonSensors = new JSONArray();
        for (SensorType type : this.activeSensors) {
            jsonSensors.put(type.getType());
        }
        json.put(Victim.VICTIM_ACTIVE_SENSORS, jsonSensors);
        return new JSONObject();
    }

    public String toString() {
        return this.encode();
    }

}
