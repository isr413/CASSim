package com.seat.sim.server.remote;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

import com.seat.sim.common.core.TeamColor;
import com.seat.sim.common.math.Vector;
import com.seat.sim.common.remote.RemoteProto;
import com.seat.sim.common.remote.RemoteState;
import com.seat.sim.common.remote.intent.ActivateIntention;
import com.seat.sim.common.remote.intent.DeactivateIntention;
import com.seat.sim.common.remote.intent.IntentionSet;
import com.seat.sim.common.remote.intent.IntentionType;
import com.seat.sim.common.sensor.SensorConfig;
import com.seat.sim.common.sensor.SensorProto;
import com.seat.sim.common.sensor.SensorState;
import com.seat.sim.server.core.SimException;
import com.seat.sim.server.scenario.Scenario;
import com.seat.sim.server.sensor.Sensor;
import com.seat.sim.server.sensor.SensorFactory;

public class Remote {

    private boolean active;
    private Map<String, Sensor> activeSensors;
    private Map<String, Sensor> allSensors;
    private boolean done;
    private double fuel;
    private Vector location;
    private RemoteProto proto;
    private String remoteID;
    private TeamColor team;

    public Remote(RemoteProto proto, String remoteID, TeamColor team, boolean active) {
        this.proto = proto;
        this.remoteID = remoteID;
        this.team = team;
        this.location = proto.getLocation();
        this.fuel = proto.getKinematics().getMaxFuel();
        this.active = active;
        this.done = false;
        this.init();
    }

    private void init() {
        this.allSensors = new HashMap<>();
        this.activeSensors = new HashMap<>();
        int sensorCount = 0;
        for (SensorConfig sensorConfig : this.proto.getSensorConfigs()) {
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

    public boolean activateAllSensors() {
        this.allSensors.keySet().stream()
            .filter(sensorID -> this.hasInactiveSensorWithID(sensorID))
            .forEach(sensorID -> this.activateSensorWithID(sensorID));
        return true;
    }

    public boolean activateSensors(Collection<String> sensorIDs) {
        boolean flag = true;
        for (String sensorID : sensorIDs) {
            if (this.hasActiveSensorWithID(sensorID)) continue;
            flag = this.activateSensorWithID(sensorID) && flag;
        }
        return flag;
    }

    public boolean activateSensorWithID(String sensorID) {
        if (!this.hasSensorWithID(sensorID)) return false;
        if (this.hasActiveSensorWithID(sensorID)) return true;
        this.activeSensors.put(sensorID, this.allSensors.get(sensorID));
        return true;
    }

    public boolean deactivateAllSensors() {
        this.getSensors().stream()
            .filter(sensor -> this.hasActiveSensorWithID(sensor.getSensorID()))
            .forEach(sensor -> this.deactivateSensorWithID(sensor.getSensorID()));
        return true;
    }

    public boolean deactivateSensors(Collection<String> sensorIDs) {
        boolean flag = true;
        for (String sensorID : sensorIDs) {
            if (this.hasInactiveSensorWithID(sensorID)) continue;
            flag = this.deactivateSensorWithID(sensorID) && flag;
        }
        return flag;
    }

    public boolean deactivateSensorWithID(String sensorID) {
        if (!this.hasSensorWithID(sensorID)) return false;
        if (this.hasInactiveSensorWithID(sensorID)) return true;
        this.activeSensors.remove(sensorID);
        return true;
    }

    public Collection<String> getActiveSensorIDs() {
        return this.activeSensors.keySet();
    }

    public Collection<Sensor> getActiveSensors() {
        return this.activeSensors.values();
    }

    public double getFuel() {
        return this.fuel;
    }

    public Collection<String> getInactiveSensorIDs() {
        return this.getSensorIDs().stream()
            .filter(sensorID -> this.hasInactiveSensorWithID(sensorID))
            .collect(Collectors.toList());
    }

    public Collection<Sensor> getInactiveSensors() {
        return this.getInactiveSensorIDs().stream()
            .map(sensorID -> this.getSensorWithID(sensorID))
            .collect(Collectors.toList());
    }

    public String getLabel() {
        return this.getRemoteID();
    }

    public Vector getLocation() {
        return this.location;
    }

    public double getMaxFuel() {
        return this.proto.getKinematics().getMaxFuel();
    }

    public RemoteProto getProto() {
        return this.proto;
    }

    public String getRemoteID() {
        return this.remoteID;
    }

    public RemoteState getRemoteState() {
        return new RemoteState(this.remoteID, this.team, this.location, null, this.fuel, this.active,
            this.getSensorStates());
    }

    public String getRemoteType() {
        return this.proto.getRemoteType();
    }

    public Collection<String> getSensorIDs() {
        return this.allSensors.keySet();
    }

    public Collection<Sensor> getSensors() {
        return this.allSensors.values();
    }

    public Collection<SensorState> getSensorStates() {
        return this.getSensors().stream()
            .map(sensor -> sensor.getState())
            .collect(Collectors.toList());
    }

    public Collection<Sensor> getSensorsWithModel(String sensorModel) {
        return this.getSensors().stream()
            .filter(sensor -> sensor.getSensorModel().equals(sensorModel))
            .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    public <T extends Sensor> Collection<T> getSensorsWithType(Class<T> classType) {
        return this.getSensors().stream()
            .filter(sensor -> classType.isAssignableFrom(sensor.getClass()))
            .map(sensor -> (T) sensor)
            .collect(Collectors.toList());
    }

    public Sensor getSensorWithID(String sensorID) throws SimException {
        if (!this.hasSensorWithID(sensorID)) {
            throw new SimException(String.format("Remote %s has no sensor %s", this.getRemoteID(), sensorID));
        }
        return this.allSensors.get(sensorID);
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

    public boolean hasInactiveSensors() {
        return this.activeSensors.size() < this.allSensors.size();
    }

    public boolean hasInactiveSensorWithID(String sensorID) {
        return this.hasSensorWithID(sensorID) && !this.hasActiveSensorWithID(sensorID);
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

    public boolean hasSensorWithModel(String sensorModel) {
        return !this.getSensorsWithModel(sensorModel).isEmpty();
    }

    public boolean hasSensorWithType(Class<? extends Sensor> classType) {
        return !this.getSensorsWithType(classType).isEmpty();
    }

    public boolean hasTeam() {
        return !(this.team == null || this.team.equals(TeamColor.NONE));
    }

    public boolean isActive() {
        return this.active && this.isEnabled();
    }

    public boolean isDone() {
        return this.done;
    }

    public boolean isEnabled() {
        return this.getProto().isEnabled() && this.fuel > 0;
    }

    public boolean isMobile() {
        return this.proto.isMobile();
    }

    public void setActive() {
        this.active = true;
    }

    public void setDone() {
        this.setInactive();
        this.done = true;
    }

    public void setFuel(double fuel) throws SimException{
        if (fuel < 0 || this.getMaxFuel() < fuel) {
            throw new SimException(String.format("Remote %s cannot set fuel to %f", this.getRemoteID(), fuel));
        }
        this.fuel = fuel;
    }

    public void setInactive() {
        this.deactivateAllSensors();
        this.active = false;
    }

    public void setLocation(Vector location) {
        this.location = location;
    }

    public void update(Scenario scenario, double stepSize) throws SimException {
        this.update(scenario, null, stepSize);
    }

    public void update(Scenario scenario, IntentionSet intentions, double stepSize) throws SimException {
        if (!this.isEnabled() || this.isDone()) {
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
                if (intent.hasActivations()) {
                    this.activateSensors(intent.getActivations());
                } else {
                    this.activateAllSensors();
                }
            }
            if (intentions.hasIntentionWithType(IntentionType.DEACTIVATE)) {
                DeactivateIntention intent =
                    (DeactivateIntention) intentions.getIntentionWithType(IntentionType.DEACTIVATE);
                if (intent.hasDeactivations()) {
                    this.deactivateSensors(intent.getDeactivations());
                } else {
                    this.deactivateAllSensors();
                }
            }
            if (intentions.hasIntentionWithType(IntentionType.SHUTDOWN)) {
                this.setInactive();
            }
            if (intentions.hasIntentionWithType(IntentionType.DONE)) {
                this.setDone();
            }
        }
        if (!this.isActive() || this.isDone()) return;
        double fuelUsage = 0;
        for (Sensor sensor : this.activeSensors.values()) {
            sensor.update(scenario, this, stepSize);
            fuelUsage += sensor.getBatteryUsage();
        }
        this.updateFuel(fuelUsage, stepSize);
    }

    protected void updateFuel(double usage, double stepSize) {
        this.fuel -= (usage * stepSize);
        if (this.fuel < 0) {
            this.fuel = 0;
        }
        if (this.getMaxFuel() < this.fuel) {
            this.fuel = this.getMaxFuel();
        }
        if (!this.isEnabled()) {
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
