package com.seat.rescuesim.simserver.sim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.remote.RemoteSpec;
import com.seat.rescuesim.common.remote.RemoteState;
import com.seat.rescuesim.common.remote.intent.Intention;
import com.seat.rescuesim.common.sensor.SensorConfig;
import com.seat.rescuesim.common.sensor.SensorSpec;
import com.seat.rescuesim.common.sensor.SensorType;
import com.seat.rescuesim.common.util.Debugger;

public abstract class SimRemote {

    protected HashMap<String, SimSensor> activeSensors;
    protected HashMap<String, SimSensor> allSensors;
    protected double battery;
    protected String label;
    protected Vector location;
    protected RemoteSpec spec;

    public SimRemote(String label, RemoteSpec spec, Vector location) {
        this(label, spec, location, spec.getMaxBatteryPower(), new ArrayList<String>());
    }

    public SimRemote(String label, RemoteSpec spec, Vector location, double battery) {
        this(label, spec, location, battery, new ArrayList<String>());
    }

    public SimRemote(String label, RemoteSpec spec, Vector location, double battery,
            ArrayList<String> sensorIDs) {
        this.label = label;
        this.spec = spec;
        this.location = location;
        this.battery = battery;
        this.allSensors = new HashMap<>();
        this.activeSensors = new HashMap<>();
        this.initSensors();
        this.activateSensors(sensorIDs);
    }

    private void initSensors() {
        for (SensorConfig sensorConfig : this.spec.getSensors()) {
            SensorSpec sensorSpec = sensorConfig.getSpec();
            Iterator<String> sensorIDs = sensorConfig.getSensorIDs().iterator();
            for (int i = 0; i < sensorConfig.getCount(); i++) {
                String label = (i < sensorConfig.getSensorIDs().size()) ? sensorIDs.next() : String.format("s<%d>", i);
                this.allSensors.put(label, new SimSensor(label, sensorSpec));
            }
        }
    }

    public boolean activateSensors(ArrayList<String> sensorIDs) {
        return this.activateSensors(new HashSet<String>(sensorIDs));
    }

    public boolean activateSensors(HashSet<String> sensorIDs) {
        boolean flag = true;
        for (String sensorID : sensorIDs) {
            flag = this.activateSensorWithID(sensorID) && flag;
        }
        return flag;
    }

    public boolean activateSensorWithID(String sensorID) {
        if (!this.hasSensorWithID(sensorID)) {
            Debugger.logger.err(String.format("Remote %s has no sensor %s", this.getRemoteID(), sensorID));
            return false;
        }
        if (this.hasActiveSensorWithID(sensorID)) {
            Debugger.logger.warn(String.format("Sensor %s is already active on remote %s", sensorID,
                this.getRemoteID()));
            return true;
        }
        this.activeSensors.put(sensorID, this.allSensors.get(sensorID));
        return true;
    }

    public boolean deactivateSensors(ArrayList<String> sensorIDs) {
        return this.deactivateSensors(new HashSet<String>(sensorIDs));
    }

    public boolean deactivateSensors(HashSet<String> sensorIDs) {
        boolean flag = true;
        for (String sensorID : sensorIDs) {
            flag = this.deactivateSensorWithID(sensorID) && flag;
        }
        return flag;
    }

    public boolean deactivateSensorWithID(String sensorID) {
        if (!this.hasSensorWithID(sensorID)) {
            Debugger.logger.warn(String.format("Remote %s has no sensor %s", this.getRemoteID(), sensorID));
            return true;
        }
        if (!this.hasActiveSensorWithID(sensorID)) {
            Debugger.logger.warn(String.format("Sensor %s is already inactive on remote %s", sensorID,
                this.getRemoteID()));
            return true;
        }
        this.activeSensors.remove(sensorID);
        return true;
    }

    public double getBattery() {
        return this.battery;
    }

    public String getLabel() {
        return this.label;
    }

    public Vector getLocation() {
        return this.location;
    }

    public String getRemoteID() {
        return this.getLabel();
    }

    public HashSet<String> getActiveSensorIDs() {
        return new HashSet<String>(this.activeSensors.keySet());
    }

    public ArrayList<SimSensor> getActiveSensors() {
        return new ArrayList<SimSensor>(this.activeSensors.values());
    }

    public ArrayList<SimSensor> getActiveSensorsWithType(SensorType type) {
        ArrayList<SimSensor> sensors = new ArrayList<>();
        for (SimSensor sensor : this.activeSensors.values()) {
            if (sensor.getSensorType().equals(type)) {
                sensors.add(sensor);
            }
        }
        if (sensors.isEmpty()) {
            Debugger.logger.err(String.format("Remote %s has no active sensors with type %s", this.getRemoteID(),
                type.getLabel()));
        }
        return sensors;
    }

    public SimSensor getActiveSensorWithID(String sensorID) {
        if (!this.hasActiveSensorWithID(sensorID)) {
            Debugger.logger.err(String.format("Remote %s has no active sensor %s", this.getRemoteID(), sensorID));
            return null;
        }
        return this.activeSensors.get(sensorID);
    }

    public HashSet<String> getSensorIDs() {
        return new HashSet<String>(this.allSensors.keySet());
    }

    public ArrayList<SimSensor> getSensors() {
        return new ArrayList<SimSensor>(this.allSensors.values());
    }

    public ArrayList<SimSensor> getSensorsWithType(SensorType type) {
        ArrayList<SimSensor> sensors = new ArrayList<>();
        for (SimSensor sensor : this.allSensors.values()) {
            if (sensor.getSensorType().equals(type)) {
                sensors.add(sensor);
            }
        }
        if (sensors.isEmpty()) {
            Debugger.logger.err(String.format("Remote %s has no sensors with type %s", this.getRemoteID(),
                type.getLabel()));
        }
        return sensors;
    }

    public SimSensor getSensorWithID(String sensorID) {
        if (!this.hasSensorWithID(sensorID)) {
            Debugger.logger.err(String.format("Remote %s has no sensor %s", this.getRemoteID(), sensorID));
            return null;
        }
        return this.allSensors.get(sensorID);
    }

    public RemoteSpec getSpec() {
        return this.spec;
    }

    public abstract RemoteState getState();

    public boolean hasActiveSensors() {
        return !this.activeSensors.isEmpty();
    }

    public boolean hasActiveSensorWithID(String sensorID) {
        return this.activeSensors.containsKey(sensorID);
    }

    public boolean hasActiveSensorWithType(SensorType type) {
        for (SimSensor sensor : this.activeSensors.values()) {
            if (sensor.getSensorType().equals(type)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasSensors() {
        return !this.allSensors.isEmpty();
    }

    public boolean hasSensorWithID(String sensorID) {
        return this.allSensors.containsKey(sensorID);
    }

    public boolean hasSensorWithType(SensorType type) {
        for (SimSensor sensor : this.allSensors.values()) {
            if (sensor.getSensorType().equals(type)) {
                return true;
            }
        }
        return false;
    }

    public boolean isActive() {
        return this.battery > 0;
    }

    public boolean isInactive() {
        return !this.isActive();
    }

    public boolean isKinetic() {
        return false;
    }

    public void setBattery(double battery) {
        if (battery < 0) {
            Debugger.logger.err(String.format("Battery cannot be negative on remote %s", this.getRemoteID()));
            battery = 0;
        }
        if (this.spec.getMaxBatteryPower() < battery) {
            Debugger.logger.err(String.format("Battery exceeds max battery power of remote %s", this.getRemoteID()));
        }
        this.battery = battery;
    }

    public void setLocation(Vector location) {
        this.location = location;
    }

    public void update(double stepSize) throws SimException {
        this.update(null, stepSize);
    }

    public void update(ArrayList<Intention> intentions, double stepSize) throws SimException {}

    protected void updateBattery(double usage, double stepSize) {
        this.battery += usage * stepSize;
    }

    protected void updateLocation(Vector delta, double stepSize) {
        this.location = Vector.add(this.location, Vector.scale(delta, stepSize));
    }

}
