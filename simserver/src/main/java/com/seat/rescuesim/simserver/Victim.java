package com.seat.rescuesim.simserver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import com.seat.rescuesim.common.Debugger;
import com.seat.rescuesim.common.VictimSpecification;
import com.seat.rescuesim.common.Sensor;
import com.seat.rescuesim.common.SensorType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Victim {
    private static final String VICTIM_ACTIVE_SENSORS = "active_sensors";
    private static final String VICTIM_BATTERY = "battery_power";
    private static final String VICTIM_ID = "victim_id";

    private static int victimCount = 0;

    public static Victim decode(VictimSpecification victimSpec, JSONObject json) throws JSONException {
        int victimID = json.getInt(Victim.VICTIM_ID);
        double batteryPower = json.getDouble(Victim.VICTIM_BATTERY);
        JSONArray jsonSensors = json.getJSONArray(Victim.VICTIM_ACTIVE_SENSORS);
        ArrayList<SensorType> activeSensors = new ArrayList<>();
        for (int i = 0; i < jsonSensors.length(); i++) {
           activeSensors.add(SensorType.values()[jsonSensors.getInt(i)]);
        }
        return new Victim(victimSpec, victimID, batteryPower, activeSensors);
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
    private VictimSpecification spec;
    private int victimID;

    public Victim(VictimSpecification victimSpec) {
        this(victimSpec, Victim.getID(), victimSpec.getMaxBatteryPower(), new HashSet<SensorType>());
    }

    public Victim(VictimSpecification victimSpec, int victimID, double batteryPower) {
        this(victimSpec, victimID, batteryPower, new HashSet<SensorType>());
    }

    public Victim(VictimSpecification victimSpec, int victimID, double batteryPower, SensorType[] activeSensors) {
        this(victimSpec, victimID, batteryPower, new ArrayList<SensorType>(Arrays.asList(activeSensors)));
    }

    public Victim(VictimSpecification victimSpec, int victimID, double batteryPower,
            ArrayList<SensorType> activeSensors) {
        this(victimSpec, victimID, batteryPower, new HashSet<SensorType>());
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

    public Victim(VictimSpecification victimSpec, int victimID, double batteryPower,
            HashSet<SensorType> activeSensors) {
        this.spec = victimSpec;
        this.victimID = victimID;
        this.batteryPower = batteryPower;
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

    public void disable() {
        this.batteryPower = 0;
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

    public ArrayList<Sensor> getSensors() {
        return this.spec.getSensors();
    }

    public Sensor getSensorWithType(SensorType type) {
        return this.spec.getSensorWithType(type);
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

    public String encode() {
        return this.toJSON().toString();
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put(Victim.VICTIM_ID, this.victimID);
        json.put(Victim.VICTIM_BATTERY, this.batteryPower);
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
