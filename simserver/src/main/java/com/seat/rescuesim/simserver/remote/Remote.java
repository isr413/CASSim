package com.seat.rescuesim.simserver.remote;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import com.seat.rescuesim.common.core.TeamColor;
import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.remote.RemoteProto;
import com.seat.rescuesim.common.remote.RemoteState;
import com.seat.rescuesim.common.remote.RemoteType;
import com.seat.rescuesim.common.remote.intent.ActivateIntention;
import com.seat.rescuesim.common.remote.intent.DeactivateIntention;
import com.seat.rescuesim.common.remote.intent.Intention;
import com.seat.rescuesim.common.remote.intent.IntentionSet;
import com.seat.rescuesim.common.remote.intent.IntentionType;
import com.seat.rescuesim.common.sensor.SensorConfig;
import com.seat.rescuesim.common.sensor.SensorProto;
import com.seat.rescuesim.common.sensor.SensorState;
import com.seat.rescuesim.common.sensor.SensorType;
import com.seat.rescuesim.common.util.Debugger;
import com.seat.rescuesim.simserver.core.SimException;
import com.seat.rescuesim.simserver.scenario.SimScenario;
import com.seat.rescuesim.simserver.sensor.Sensor;
import com.seat.rescuesim.simserver.sensor.SensorFactory;

public class Remote {

    private boolean active;
    private HashMap<String, Sensor> activeSensors;
    private HashMap<String, Sensor> allSensors;
    private double battery;
    private boolean done;
    private Vector location;
    private RemoteProto proto;
    private String remoteID;
    private TeamColor team;

    public Remote(RemoteProto proto, String remoteID, TeamColor team, boolean active) {
        this.proto = proto;
        this.remoteID = remoteID;
        this.team = team;
        this.location = proto.getLocation();
        this.battery = proto.getMaxBatteryPower();
        this.active = active;
        this.done = false;
        this.initSensors();
    }

    private void initSensors() {
        this.allSensors = new HashMap<>();
        this.activeSensors = new HashMap<>();
        int sensorCount = 0;
        for (SensorConfig sensorConfig : this.proto.getSensors()) {
            SensorProto sensorProto = sensorConfig.getProto();
            Iterator<String> sensorIDs = sensorConfig.getSensorIDs().iterator();
            for (int i = 0; i < sensorConfig.getCount(); i++) {
                String sensorID = (i < sensorConfig.getSensorIDs().size()) ?
                    sensorIDs.next() :
                    String.format("%s:(%d)", sensorProto.getLabel(), sensorCount);
                sensorCount++;
                Sensor sensor = SensorFactory.getSensor(sensorProto, sensorID, sensorConfig.isActive());
                this.allSensors.put(sensorID, sensor);
                if (sensorConfig.isActive()) {
                    this.activeSensors.put(sensorID, sensor);
                }
            }
        }
    }

    public boolean activateSensors() throws SimException {
        return this.activateSensors(this.getInactiveSensorIDs());
    }

    public boolean activateSensors(Collection<String> sensorIDs) throws SimException {
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

    public boolean deactivateSensors(Collection<String> sensorIDs) throws SimException {
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

    public Collection<String> getActiveSensorIDs() {
        return this.activeSensors.keySet();
    }

    public Collection<Sensor> getActiveSensors() {
        return this.activeSensors.values();
    }

    public Collection<Sensor> getActiveSensorsWithType(SensorType type) {
        ArrayList<Sensor> sensors = new ArrayList<>();
        for (Sensor sensor : this.activeSensors.values()) {
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

    public Sensor getActiveSensorWithID(String sensorID) throws SimException {
        if (!this.hasActiveSensorWithID(sensorID)) {
            throw new SimException(String.format("Remote %s has no active sensor %s", this.getRemoteID(), sensorID));
        }
        return this.activeSensors.get(sensorID);
    }

    public double getBattery() {
        return this.battery;
    }

    public Collection<String> getInactiveSensorIDs() {
        HashSet<String> sensorIDs = new HashSet<>();
        for (String sensorID : this.allSensors.keySet()) {
            if (!this.hasActiveSensorWithID(sensorID)) {
                sensorIDs.add(sensorID);
            }
        }
        return sensorIDs;
    }

    public Collection<Sensor> getInactiveSensors() {
        ArrayList<Sensor> sensors = new ArrayList<>();
        for (String sensorID : this.getInactiveSensorIDs()) {
            sensors.add(this.getSensorWithID(sensorID));
        }
        return sensors;
    }

    public Collection<Sensor> getInactiveSensorsWithType(SensorType type) {
        ArrayList<Sensor> sensors = new ArrayList<>();
        for (Sensor sensor : this.allSensors.values()) {
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

    public Sensor getInactiveSensorWithID(String sensorID) throws SimException {
        if (!this.hasInactiveSensorWithID(sensorID)) {
            throw new SimException(String.format("Remote %s has no inactive sensor %s", this.getRemoteID(), sensorID));
        }
        return this.allSensors.get(sensorID);
    }

    public String getLabel() {
        return this.getRemoteID();
    }

    public Vector getLocation() {
        return this.location;
    }

    public double getMaxBatteryPower() {
        return this.proto.getMaxBatteryPower();
    }

    public RemoteProto getProto() {
        return this.proto;
    }

    public String getRemoteID() {
        return this.remoteID;
    }

    public RemoteType getRemoteType() {
        return this.proto.getRemoteType();
    }

    public Collection<String> getSensorIDs() {
        return this.allSensors.keySet();
    }

    public Collection<Sensor> getSensors() {
        return this.allSensors.values();
    }

    public Collection<SensorState> getSensorState() {
        ArrayList<SensorState> state = new ArrayList<>();
        for (Sensor sensor : this.activeSensors.values()) {
            state.add(sensor.getState());
        }
        return state;
    }

    public Collection<Sensor> getSensorsWithType(SensorType type) {
        ArrayList<Sensor> sensors = new ArrayList<>();
        for (Sensor sensor : this.allSensors.values()) {
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

    public Sensor getSensorWithID(String sensorID) throws SimException {
        if (!this.hasSensorWithID(sensorID)) {
            throw new SimException(String.format("Remote %s has no sensor %s", this.getRemoteID(), sensorID));
        }
        return this.allSensors.get(sensorID);
    }

    public RemoteState getState() {
        return new RemoteState(this.getRemoteType(), this.remoteID, this.team, this.location, this.battery,
            this.active);
    }

    public TeamColor getTeam() {
        return this.team;
    }

    public boolean hasActiveSensors() {
        return !this.activeSensors.isEmpty();
    }

    public boolean hasActiveSensorWithID(String sensorID) {
        return this.activeSensors.containsKey(sensorID);
    }

    public boolean hasActiveSensorWithType(SensorType type) {
        for (Sensor sensor : this.activeSensors.values()) {
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
        for (Sensor sensor : this.activeSensors.values()) {
            if (this.hasActiveSensorWithID(sensor.getSensorID())) {
                continue;
            }
            if (sensor.getSensorType().equals(type)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasLocation() {
        return this.location != null;
    }

    public boolean hasSensors() {
        return !this.allSensors.isEmpty();
    }

    public boolean hasSensorWithID(String sensorID) {
        return this.allSensors.containsKey(sensorID);
    }

    public boolean hasSensorWithType(SensorType type) {
        for (Sensor sensor : this.allSensors.values()) {
            if (sensor.getSensorType().equals(type)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasTeam() {
        return !(this.team == null || this.team.equals(TeamColor.NONE));
    }

    public boolean isActive() {
        return this.active && this.isEnabled();
    }

    public boolean isDisabled() {
        return !this.isEnabled();
    }

    public boolean isDone() {
        return this.done;
    }

    public boolean isEnabled() {
        return this.getProto().isEnabled() && this.battery > 0;
    }

    public boolean isInactive() {
        return !this.isActive();
    }

    public boolean isMobile() {
        return false;
    }

    public boolean isStationary() {
        return !this.isMobile();
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

    public void update(SimScenario scenario, IntentionSet intentions, double stepSize) throws SimException {
        if (this.isDisabled() || this.isDone()) {
            if (this.isActive()) {
                this.setInactive();
            }
            return;
        }
        if (intentions != null) {
            if (intentions.hasIntentionWithType(IntentionType.STARTUP)) {
                this.setActive();
            }
            if (intentions.hasIntentionWithType(IntentionType.ACTIVATE)) {
                ActivateIntention intent = (ActivateIntention) intentions.getIntentionWithType(IntentionType.ACTIVATE);
                this.activateSensors(intent.getActivations());
            }
            if (intentions.hasIntentionWithType(IntentionType.DEACTIVATE)) {
                Intention intent = intentions.getIntentionWithType(IntentionType.DEACTIVATE);
                this.deactivateSensors(((DeactivateIntention) intent).getDeactivations());
            }
            if (intentions.hasIntentionWithType(IntentionType.SHUTDOWN)) {
                this.setInactive();
            }
            if (intentions.hasIntentionWithType(IntentionType.DONE)) {
                this.setDone();
            }
        }
        if (this.isInactive() || this.isDone()) {
            return;
        }
        double batteryUsage = 0;
        for (Sensor sensor : this.activeSensors.values()) {
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
        if (this.isDisabled()) {
            this.setInactive();
        }
    }

    protected void updateLocation(Vector delta, double stepSize) {
        this.location = Vector.add(this.location, Vector.scale(delta, stepSize));
    }

    public boolean equals(Remote remote) {
        if (remote == null) return false;
        return this.remoteID.equals(remote.remoteID);
    }

}
