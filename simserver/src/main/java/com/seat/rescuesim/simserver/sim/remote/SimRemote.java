package com.seat.rescuesim.simserver.sim.remote;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.remote.RemoteController;
import com.seat.rescuesim.common.remote.RemoteSpec;
import com.seat.rescuesim.common.remote.RemoteState;
import com.seat.rescuesim.common.remote.RemoteType;
import com.seat.rescuesim.common.remote.intent.ActivateIntention;
import com.seat.rescuesim.common.remote.intent.DeactivateIntention;
import com.seat.rescuesim.common.remote.intent.Intention;
import com.seat.rescuesim.common.remote.intent.IntentionType;
import com.seat.rescuesim.common.sensor.SensorConfig;
import com.seat.rescuesim.common.sensor.SensorSpec;
import com.seat.rescuesim.common.sensor.SensorState;
import com.seat.rescuesim.common.sensor.SensorType;
import com.seat.rescuesim.common.util.Debugger;
import com.seat.rescuesim.common.util.SerializableEnum;
import com.seat.rescuesim.simserver.sim.SimScenario;
import com.seat.rescuesim.simserver.sim.sensor.SimSensor;
import com.seat.rescuesim.simserver.sim.sensor.SimSensorFactory;
import com.seat.rescuesim.simserver.sim.util.SimException;

public abstract class SimRemote {

    protected boolean active;
    protected HashMap<String, SimSensor> activeSensors;
    protected HashMap<String, SimSensor> allSensors;
    protected double battery;
    protected boolean done;
    protected String label;
    protected Vector location;
    protected RemoteSpec spec;

    public SimRemote(RemoteSpec spec, String label) {
        this.spec = spec;
        this.label = label;
        this.location = spec.getLocation();
        this.battery = spec.getMaxBatteryPower();
        this.active = true;
        this.done = false;
        this.initSensors();
    }

    private void initSensors() {
        this.allSensors = new HashMap<>();
        this.activeSensors = new HashMap<>();
        int sensorCount = 0;
        for (SensorConfig sensorConfig : this.spec.getSensors()) {
            SensorSpec sensorSpec = sensorConfig.getSpec();
            Iterator<String> sensorIDs = sensorConfig.getSensorIDs().iterator();
            for (int i = 0; i < sensorConfig.getCount(); i++) {
                String label = (i < sensorConfig.getSensorIDs().size()) ?
                    sensorIDs.next() :
                    String.format("%s:(%d)", sensorSpec.getLabel(), sensorCount);
                sensorCount++;
                SimSensor sensor = SimSensorFactory.getSimSensor(sensorSpec, label);
                this.allSensors.put(label, sensor);
            }
        }
    }

    public boolean activateSensors() throws SimException {
        return this.activateSensors(this.getInactiveSensorIDs());
    }

    public boolean activateSensors(ArrayList<String> sensorIDs) throws SimException {
        return this.activateSensors(new HashSet<String>(sensorIDs));
    }

    public boolean activateSensors(HashSet<String> sensorIDs) throws SimException {
        ArrayList<String> sensorErrors = new ArrayList<>();
        for (String sensorID : sensorIDs) {
            try {
                if (!this.activateSensorWithID(sensorID)) {
                    sensorErrors.add(sensorID);
                }
            } catch (SimException e) {
                sensorErrors.add(sensorID);
            }
        }
        if (!sensorErrors.isEmpty()) {
            throw new SimException(String.format("Remote %s cannot activate sensors %s", this.getRemoteID(),
                sensorErrors.toString()));
        }
        return true;
    }

    public boolean activateSensorWithID(String sensorID) throws SimException {
        if (!this.hasSensorWithID(sensorID)) {
            throw new SimException(String.format("Remote %s has no sensor %s", this.getRemoteID(), sensorID));
        }
        if (this.hasActiveSensorWithID(sensorID)) {
            Debugger.logger.warn(String.format("Sensor %s is already active on remote %s", sensorID,
                this.getRemoteID()));
            return true;
        }
        this.activeSensors.put(sensorID, this.allSensors.get(sensorID));
        return true;
    }

    public boolean deactivateSensors() throws SimException {
        return this.deactivateSensors(this.getActiveSensorIDs());
    }

    public boolean deactivateSensors(ArrayList<String> sensorIDs) throws SimException {
        return this.deactivateSensors(new HashSet<String>(sensorIDs));
    }

    public boolean deactivateSensors(HashSet<String> sensorIDs) throws SimException {
        ArrayList<String> sensorErrors = new ArrayList<>();
        for (String sensorID : sensorIDs) {
            try {
                if (!this.deactivateSensorWithID(sensorID)) {
                    sensorErrors.add(sensorID);
                }
            } catch (SimException e) {
                sensorErrors.add(sensorID);
            }
        }
        if (!sensorErrors.isEmpty()) {
            throw new SimException(String.format("Remote %s cannot deactivate sensors %s", this.getRemoteID(),
                sensorErrors.toString()));
        }
        return true;
    }

    public boolean deactivateSensorWithID(String sensorID) throws SimException {
        if (!this.hasSensorWithID(sensorID)) {
            throw new SimException(String.format("Remote %s has no sensor %s", this.getRemoteID(), sensorID));
        }
        if (!this.hasActiveSensorWithID(sensorID)) {
            Debugger.logger.warn(String.format("Sensor %s is already inactive on remote %s", sensorID,
                this.getRemoteID()));
            return true;
        }
        this.activeSensors.remove(sensorID);
        return true;
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
            Debugger.logger.warn(String.format("Remote %s has no active sensors with type %s", this.getRemoteID(),
                type.getLabel()));
        }
        return sensors;
    }

    public SimSensor getActiveSensorWithID(String sensorID) throws SimException {
        if (!this.hasActiveSensorWithID(sensorID)) {
            throw new SimException(String.format("Remote %s has no active sensor %s", this.getRemoteID(), sensorID));
        }
        return this.activeSensors.get(sensorID);
    }

    public double getBattery() {
        return this.battery;
    }

    public HashSet<String> getInactiveSensorIDs() {
        HashSet<String> sensorIDs = new HashSet<>();
        for (String sensorID : this.allSensors.keySet()) {
            if (!this.hasActiveSensorWithID(sensorID)) {
                sensorIDs.add(sensorID);
            }
        }
        return sensorIDs;
    }

    public ArrayList<SimSensor> getInactiveSensors() {
        ArrayList<SimSensor> sensors = new ArrayList<>();
        for (String sensorID : this.getInactiveSensorIDs()) {
            sensors.add(this.getSensorWithID(sensorID));
        }
        return sensors;
    }

    public ArrayList<SimSensor> getInactiveSensorsWithType(SensorType type) {
        ArrayList<SimSensor> sensors = new ArrayList<>();
        for (SimSensor sensor : this.allSensors.values()) {
            if (this.hasActiveSensorWithID(sensor.getSensorID())) {
                continue;
            }
            if (sensor.getSensorType().equals(type)) {
                sensors.add(sensor);
            }
        }
        if (sensors.isEmpty()) {
            Debugger.logger.warn(String.format("Remote %s has no inactive sensors with type %s", this.getRemoteID(),
                type.getLabel()));
        }
        return sensors;
    }

    public SimSensor getInactiveSensorWithID(String sensorID) throws SimException {
        if (!this.hasInactiveSensorWithID(sensorID)) {
            throw new SimException(String.format("Remote %s has no inactive sensor %s", this.getRemoteID(), sensorID));
        }
        return this.allSensors.get(sensorID);
    }

    public String getLabel() {
        return this.label;
    }

    public Vector getLocation() {
        return this.location;
    }

    public double getMaxBatteryPower() {
        return this.spec.getMaxBatteryPower();
    }

    public String getRemoteID() {
        return this.getLabel();
    }

    public RemoteType getRemoteType() {
        return this.spec.getRemoteType();
    }

    public HashSet<String> getSensorIDs() {
        return new HashSet<String>(this.allSensors.keySet());
    }

    public ArrayList<SimSensor> getSensors() {
        return new ArrayList<SimSensor>(this.allSensors.values());
    }

    public ArrayList<SensorState> getSensorState() {
        ArrayList<SensorState> state = new ArrayList<>();
        for (SimSensor sensor : this.activeSensors.values()) {
            state.add(sensor.getState());
        }
        return state;
    }

    public ArrayList<SimSensor> getSensorsWithType(SensorType type) {
        ArrayList<SimSensor> sensors = new ArrayList<>();
        for (SimSensor sensor : this.allSensors.values()) {
            if (sensor.getSensorType().equals(type)) {
                sensors.add(sensor);
            }
        }
        if (sensors.isEmpty()) {
            Debugger.logger.warn(String.format("Remote %s has no sensors with type %s", this.getRemoteID(),
                type.getLabel()));
        }
        return sensors;
    }

    public SimSensor getSensorWithID(String sensorID) throws SimException {
        if (!this.hasSensorWithID(sensorID)) {
            throw new SimException(String.format("Remote %s has no sensor %s", this.getRemoteID(), sensorID));
        }
        return this.allSensors.get(sensorID);
    }

    public RemoteSpec getSpec() {
        return this.spec;
    }

    public SerializableEnum getSpecType() {
        return this.spec.getSpecType();
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

    public boolean hasInactiveSensors() {
        return this.activeSensors.size() < this.allSensors.size();
    }

    public boolean hasInactiveSensorWithID(String sensorID) {
        return this.hasSensorWithID(sensorID) && !this.hasActiveSensorWithID(sensorID);
    }

    public boolean hasInactiveSensorWithType(SensorType type) {
        for (SimSensor sensor : this.activeSensors.values()) {
            if (this.hasActiveSensorWithID(sensor.getSensorID())) {
                continue;
            }
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
        return this.active && this.battery > 0;
    }

    public boolean isDone() {
        return this.done;
    }

    public boolean isInactive() {
        return !this.isActive();
    }

    public boolean isKinetic() {
        return false;
    }

    public boolean isStatic() {
        return !this.isKinetic();
    }

    public void setActive() {
        this.active = true;
    }

    public void setBattery(double battery) throws SimException{
        if (battery < 0 || this.getMaxBatteryPower() < battery) {
            throw new SimException(String.format("Remote %s cannot set battery to %f", this.getRemoteID(), battery));
        }
        this.battery = battery;
    }

    public void setDone() {
        this.setInactive();
        this.done = true;
    }

    public void setInactive() {
        this.deactivateSensors();
        this.active = false;
    }

    public void setLocation(Vector location) {
        this.location = location;
    }

    public void update(SimScenario scenario, double stepSize) throws SimException {
        this.update(scenario, null, stepSize);
    }

    public void update(SimScenario scenario, RemoteController controller, double stepSize) throws SimException {
        if (this.isDone()) {
            return;
        }
        if (controller != null) {
            if (controller.hasIntentionWithType(IntentionType.STARTUP)) {
                this.setActive();
            }
            if (controller.hasIntentionWithType(IntentionType.ACTIVATE)) {
                ActivateIntention intent = (ActivateIntention) controller.getIntentionWithType(IntentionType.ACTIVATE);
                this.activateSensors(intent.getActivations());
            }
            if (controller.hasIntentionWithType(IntentionType.DEACTIVATE)) {
                Intention intent = controller.getIntentionWithType(IntentionType.DEACTIVATE);
                this.deactivateSensors(((DeactivateIntention) intent).getDeactivations());
            }
            if (controller.hasIntentionWithType(IntentionType.SHUTDOWN)) {
                this.setInactive();
            }
            if (controller.hasIntentionWithType(IntentionType.DONE)) {
                this.setDone();
            }
        }
        if (this.isInactive() || this.isDone()) {
            return;
        }
        double batteryUsage = 0;
        for (SimSensor sensor : this.activeSensors.values()) {
            sensor.update(scenario, this, stepSize);
            batteryUsage += sensor.getBatteryUsage();
        }
        this.updateBattery(batteryUsage, stepSize);
    }

    protected void updateBattery(double usage, double stepSize) {
        this.battery -= (usage * stepSize);
        if (this.battery < 0) {
            this.battery = 0;
        }
        if (this.getMaxBatteryPower() < this.battery) {
            this.battery = this.getMaxBatteryPower();
        }
        if (this.isInactive()) {
            this.setInactive();;
        }
    }

    protected void updateLocation(Vector delta, double stepSize) {
        this.location = Vector.add(this.location, Vector.scale(delta, stepSize));
    }

}
