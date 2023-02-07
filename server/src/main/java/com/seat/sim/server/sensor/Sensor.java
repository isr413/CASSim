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
    if (this.scenario == null) {
      throw new RuntimeException("cannot create Sensor with no scenario");
    }
    if (this.remote == null) {
      throw new RuntimeException("cannot create Sensor with no remote");
    }
    if (this.proto == null) {
      throw new RuntimeException("cannot create Sensor with no proto");
    }
    if (this.sensorID == null || this.sensorID.isBlank() || this.sensorID.isEmpty()) {
      throw new RuntimeException("cannot create Sensor with empty sensorID");
    }
  }

  public double getAccuracy() {
    return this.proto.getStats().getAccuracy();
  }

  public double getBatteryUsage() {
    return this.proto.getStats().getBatteryUsage();
  }

  public double getDelay() {
    return this.proto.getStats().getDelay();
  }

  public String getLabel() {
    return this.getSensorID();
  }

  public Set<String> getMatchers() {
    return this.proto.getMatchers();
  }

  public String getModel() {
    return this.proto.getModel();
  }

  public SensorProto getProto() {
    return this.proto;
  }

  public double getRange() {
    return this.proto.getStats().getRange();
  }

  public String getSensorID() {
    return this.sensorID;
  }

  public SensorState getState() {
    return new SensorState(this.getSensorID(), this.getModel(), this.getTags(), this.getSubjects(), this.isActive());
  }

  public Set<String> getSubjects() {
    return this.scenario
        .getActiveRemotes()
        .stream()
        .filter(remote -> !this.hasMatchers() || remote.hasMatch(this.getMatchers()))
        .filter(remote -> !this.remote.hasKinematics() || !this.remote.getKinematics().hasLocation() ||
            !this.hasRange() || Vector.dist(this.remote.getKinematics().getLocation(),
                remote.getKinematics().getLocation()) <= this.getRange())
        .map(remote -> remote.getRemoteID())
        .collect(Collectors.toSet());
  }

  public Set<String> getTags() {
    return this.proto.getTags();
  }

  public boolean hasAccuracy() {
    return this.proto.getStats().hasAccuracy();
  }

  public boolean hasBatteryUsage() {
    return this.proto.getStats().hasBatteryUsage();
  }

  public boolean hasDelay() {
    return this.proto.getStats().hasDelay();
  }

  public boolean hasMatch(Set<String> matchers) {
    return this.proto.hasMatch(matchers);
  }

  public boolean hasMatcher(String matcher) {
    return this.proto.hasMatcher(matcher);
  }

  public boolean hasMatchers() {
    return this.proto.hasMatchers();
  }

  public boolean hasModel(String sensorModel) {
    return this.proto.hasModel(sensorModel);
  }

  public boolean hasRange() {
    return this.proto.getStats().hasRange();
  }

  public boolean hasSensorID(String sensorID) {
    if (sensorID == null || this.sensorID == null) {
      return this.sensorID == sensorID;
    }
    return this.sensorID.equals(sensorID);
  }

  public boolean hasSubjects() {
    return !this.getSubjects().isEmpty();
  }

  public boolean hasTag(String tag) {
    return this.proto.hasTag(tag);
  }

  public boolean hasTags() {
    return this.proto.hasTags();
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
