package com.seat.sim.server.remote;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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

public class Remote {

    private boolean active;
    private Map<String, Sensor> activeSensors;
    private Map<String, Sensor> allSensors;
    private boolean done;
    private Optional<Double> fuel;
    private Optional<Vector> location;
    private RemoteProto proto;
    private String remoteID;
    private Scenario scenario;
    private TeamColor team;

    public Remote(Scenario scenario, RemoteProto proto, String remoteID, TeamColor team, boolean active) {
        this.scenario = scenario;
        this.proto = proto;
        this.remoteID = remoteID;
        this.team = team;
        this.location = (proto.hasLocation()) ? Optional.of(this.proto.getLocation()) : Optional.empty();
        this.fuel = (proto.hasFuel()) ? Optional.of(this.proto.getInitialFuel()) : Optional.empty();
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
                String sensorID = (i < sensorConfig.getSensorIDs().size()) ? sensorIDs.next()
                        : String.format("%s:(%d)", sensorProto.getLabel(), sensorCount);
                sensorCount++;
                Sensor sensor = new Sensor(scenario, this, sensorProto, sensorID, sensorConfig.isActive());
                this.allSensors.put(sensorID, sensor);
                if (sensorConfig.isActive()) {
                    this.activeSensors.put(sensorID, sensor);
                }
            }
        }
    }

    protected void updateFuel(double usage, double stepSize) {
        if (!this.hasFuel())
            return;
        if (this.hasMaxFuel()) {
            this.fuel = Optional.of(Math.min(Math.max(this.getFuel() - (usage * stepSize), 0), this.getMaxFuel()));
        } else {
            this.fuel = Optional.of(Math.max(this.getFuel() - (usage * stepSize), 0));
        }
        if (!this.isEnabled()) {
            this.setInactive();
        }
    }

    protected void updateLocation(Vector delta, double stepSize) {
        if (!this.hasLocation())
            return;
        this.location = Optional.of(Vector.add(this.getLocation(), Vector.scale(delta, stepSize)));
    }

    public boolean activateAllSensors() {
        return this.activateSensors(this.allSensors.keySet());
    }

    public boolean activateSensors(Collection<String> sensorIDs) {
        sensorIDs
                .stream()
                .filter(sensorID -> this.hasInactiveSensorWithID(sensorID))
                .forEach(sensorID -> this.activateSensorWithID(sensorID));
        return true;
    }

    public boolean activateSensorWithID(String sensorID) {
        if (!this.hasSensorWithID(sensorID))
            return false;
        if (this.hasActiveSensorWithID(sensorID))
            return true;
        this.activeSensors.put(sensorID, this.allSensors.get(sensorID));
        return true;
    }

    public boolean deactivateAllSensors() {
        return this.deactivateSensors(this.allSensors.keySet());
    }

    public boolean deactivateSensors(Collection<String> sensorIDs) {
        sensorIDs
                .stream()
                .filter(sensorID -> this.hasActiveSensorWithID(sensorID))
                .map(sensorID -> this.deactivateSensorWithID(sensorID));
        return true;
    }

    public boolean deactivateSensorWithID(String sensorID) {
        if (!this.hasSensorWithID(sensorID))
            return false;
        if (this.hasInactiveSensorWithID(sensorID))
            return true;
        this.activeSensors.remove(sensorID);
        return true;
    }

    public boolean equals(Remote remote) {
        if (remote == null)
            return false;
        return this.remoteID.equals(remote.remoteID);
    }

    public Collection<String> getActiveSensorIDs() {
        return this.activeSensors.keySet();
    }

    public Collection<Sensor> getActiveSensors() {
        return this.activeSensors.values();
    }

    public double getFuel() {
        return this.fuel.get();
    }

    public Collection<String> getInactiveSensorIDs() {
        return this.getSensorIDs()
                .stream()
                .filter(sensorID -> this.hasInactiveSensorWithID(sensorID))
                .collect(Collectors.toList());
    }

    public Collection<Sensor> getInactiveSensors() {
        return this.getInactiveSensorIDs()
                .stream()
                .map(sensorID -> this.getSensorWithID(sensorID))
                .collect(Collectors.toList());
    }

    public String getLabel() {
        return this.getRemoteID();
    }

    public Vector getLocation() {
        return this.location.get();
    }

    public double getMaxFuel() {
        return this.proto.getMaxFuel();
    }

    public RemoteProto getProto() {
        return this.proto;
    }

    public Set<String> getRemoteGroups() {
        return this.proto.getRemoteGroups();
    }

    public String getRemoteID() {
        return this.remoteID;
    }

    public RemoteState getRemoteState() {
        if (this.hasLocation() && this.hasFuel()) {
            return new RemoteState(this.getRemoteID(), this.getRemoteGroups(), this.getTeam(), this.getLocation(),
                    this.getFuel(), this.getSensorStates(), this.isActive());
        } else if (this.hasLocation()) {
            return new RemoteState(this.getRemoteID(), this.getRemoteGroups(), this.getTeam(), this.getLocation(),
                    this.getSensorStates(), this.isActive());
        } else if (this.hasFuel()) {
            return new RemoteState(this.getRemoteID(), this.getRemoteGroups(), this.getTeam(), this.getFuel(),
                    this.getSensorStates(), this.isActive());
        } else {
            return new RemoteState(this.getRemoteID(), this.getRemoteGroups(), this.getTeam(), this.getSensorStates(),
                    this.isActive());
        }
    }

    public Collection<String> getSensorIDs() {
        return this.allSensors.keySet();
    }

    public Collection<Sensor> getSensors() {
        return this.allSensors.values();
    }

    public Collection<SensorState> getSensorStates() {
        return this.getSensors()
                .stream()
                .map(sensor -> sensor.getState())
                .collect(Collectors.toList());
    }

    public Collection<Sensor> getSensorsWithMatch(String matcher) {
        return this.getSensors()
                .stream()
                .filter(sensor -> sensor.hasSensorGroupWithTag(matcher))
                .collect(Collectors.toList());
    }

    public Collection<Sensor> getSensorsWithModel(String sensorModel) {
        return this.getSensors()
                .stream()
                .filter(sensor -> sensor.hasSensorModel(sensorModel))
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

    public boolean hasFuel() {
        return this.proto.hasFuel();
    }

    public boolean hasMaxFuel() {
        return this.proto.hasMaxFuel();
    }

    public boolean hasInactiveSensors() {
        return this.activeSensors.size() < this.allSensors.size();
    }

    public boolean hasInactiveSensorWithID(String sensorID) {
        return this.hasSensorWithID(sensorID) && !this.hasActiveSensorWithID(sensorID);
    }

    public boolean hasLocation() {
        return this.location.isPresent();
    }

    public boolean hasRemoteGroups() {
        return this.proto.hasRemoteGroups();
    }

    public boolean hasRemoteGroupWithTag(String groupTag) {
        return this.proto.hasRemoteGroupWithTag(groupTag);
    }

    public boolean hasRemoteMatch(Set<String> matchers) {
        return this.proto.hasRemoteMatch(matchers);
    }

    public boolean hasSensorMatch(Set<String> matchers) {
        return this.proto.hasSensorMatch(matchers);
    }

    public boolean hasSensors() {
        return !this.allSensors.isEmpty();
    }

    public boolean hasSensorWithID(String sensorID) {
        return this.allSensors.containsKey(sensorID);
    }

    public boolean hasSensorWithModel(String sensorModel) {
        return this.proto.hasSensorConfigWithModel(sensorModel);
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
        return this.getProto().isEnabled() && (!this.hasFuel() || this.getFuel() > 0);
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

    public void setFuel(double fuel) throws SimException {
        if (fuel < 0 || (this.hasMaxFuel() && this.getMaxFuel() < fuel)) {
            throw new SimException(String.format("Remote %s cannot set fuel to %f", this.getRemoteID(), fuel));
        }
        this.fuel = Optional.of(fuel);
    }

    public void setInactive() {
        this.deactivateAllSensors();
        this.active = false;
    }

    public void setLocation(Vector location) {
        this.location = (location != null) ? Optional.of(location) : Optional.empty();
    }

    public void update(double stepSize) throws SimException {
        this.update(null, stepSize);
    }

    public void update(IntentionSet intentions, double stepSize) throws SimException {
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
                DeactivateIntention intent = (DeactivateIntention) intentions
                        .getIntentionWithType(IntentionType.DEACTIVATE);
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
        if (!this.isActive() || this.isDone())
            return;
        double fuelUsage = 0;
        for (Sensor sensor : this.activeSensors.values()) {
            sensor.update(stepSize);
            if (sensor.hasBatteryUsage()) {
                fuelUsage += sensor.getBatteryUsage();
            }
        }
        this.updateFuel(fuelUsage, stepSize);
    }
}
