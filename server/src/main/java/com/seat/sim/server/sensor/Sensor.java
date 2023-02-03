package com.seat.sim.server.sensor;

import java.util.Set;
import java.util.stream.Collectors;

import com.seat.sim.common.math.Vector;
import com.seat.sim.common.sensor.SensorProto;
import com.seat.sim.common.sensor.SensorState;
import com.seat.sim.server.core.SimException;
import com.seat.sim.server.remote.Remote;
import com.seat.sim.server.scenario.Scenario;

public class Sensor {

    private boolean active;
    private SensorProto proto;
    private Remote remote;
    private Scenario scenario;
    private String sensorID;

    public Sensor(Scenario scenario, Remote remote, SensorProto proto, String sensorID, boolean active) {
        this.scenario = scenario;
        this.remote = remote;
        this.proto = proto;
        this.sensorID = sensorID;
        this.active = active;
    }

    public boolean equals(Sensor sensor) {
        if (sensor == null)
            return false;
        return this.sensorID.equals(sensor.sensorID);
    }

    public double getBatteryUsage() {
        return this.proto.getBatteryUsage();
    }

    public String getLabel() {
        return this.getSensorID();
    }

    public SensorProto getProto() {
        return this.proto;
    }

    public double getSensorAccuracy() {
        return this.proto.getSensorAccuracy();
    }

    public double getSensorDelay() {
        return this.proto.getSensorDelay();
    }

    public Set<String> getSensorGroups() {
        return this.proto.getSensorGroups();
    }

    public String getSensorID() {
        return this.sensorID;
    }

    public Set<String> getSensorMatchers() {
        return this.proto.getSensorMatchers();
    }

    public String getSensorModel() {
        return this.proto.getSensorModel();
    }

    public double getSensorRange() {
        return this.proto.getSensorRange();
    }

    public SensorState getState() {
        return new SensorState(this.getSensorID(), this.getSensorModel(), this.getSensorGroups(), this.getSubjects(),
                this.isActive());
    }

    public Set<String> getSubjects() {
        return this.scenario
                .getActiveRemotes()
                .stream()
                .filter(remote -> !this.hasSensorMatchers() || remote.hasSensorMatch(this.getSensorMatchers()))
                .filter(remote -> !this.remote.hasLocation() ||
                        Vector.dist(this.remote.getLocation(), remote.getLocation()) <= this.getSensorRange())
                .map(remote -> remote.getRemoteID())
                .collect(Collectors.toSet());
    }

    public boolean hasAccuracy() {
        return this.proto.hasAccuracy();
    }

    public boolean hasBatteryUsage() {
        return this.proto.hasBatteryUsage();
    }

    public boolean hasDelay() {
        return this.proto.hasDelay();
    }

    public boolean hasRange() {
        return this.proto.hasRange();
    }

    public boolean hasSensorGroups() {
        return this.proto.hasSensorGroups();
    }

    public boolean hasSensorGroupWithTag(String groupTag) {
        return this.proto.hasSensorGroupWithTag(groupTag);
    }

    public boolean hasSensorID(String sensorID) {
        if (sensorID == null || this.sensorID == null)
            return this.sensorID == sensorID;
        return this.sensorID.equals(sensorID);
    }

    public boolean hasSensorMatch(Set<String> matchers) {
        return this.proto.hasSensorMatch(matchers);
    }

    public boolean hasSensorMatchers() {
        return this.proto.hasSensorMatchers();
    }

    public boolean hasSensorMatcherWithTag(String sensorMatcher) {
        return this.proto.hasSensorMatcherWithTag(sensorMatcher);
    }

    public boolean hasSensorModel(String sensorModel) {
        return this.proto.hasSensorModel(sensorModel);
    }

    public boolean hasSubjects() {
        return !this.getSubjects().isEmpty();
    }

    public boolean isActive() {
        return this.active;
    }

    public void setActive() {
        this.active = true;
    }

    public void setInactive() {
        this.active = false;
    }

    public String toString() {
        return this.getLabel();
    }

    public void update(double stepSize) throws SimException {
        if (this.isActive() && !this.remote.isActive()) {
            this.setInactive();
        }
    }
}
