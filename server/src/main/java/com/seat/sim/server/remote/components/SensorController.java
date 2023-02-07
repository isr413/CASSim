package com.seat.sim.server.remote.components;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.seat.sim.common.sensor.SensorConfig;
import com.seat.sim.common.sensor.SensorProto;
import com.seat.sim.common.sensor.SensorState;
import com.seat.sim.server.core.SimException;
import com.seat.sim.server.remote.Remote;
import com.seat.sim.server.scenario.Scenario;
import com.seat.sim.server.sensor.Sensor;

public class SensorController {

  private Map<String, Sensor> sensors;

  public SensorController(Scenario scenario, Remote remote) {
    this.sensors = new HashMap<>();
    this.init(scenario, remote);
  }

  private void init(Scenario scenario, Remote remote) {
    int sensorCount = 0;
    for (SensorConfig sensorConfig : remote.getProto().getSensorConfigs()) {
      SensorProto sensorProto = sensorConfig.getProto();
      Iterator<String> sensorIDs = sensorConfig.getSensorIDs().iterator();
      for (int i = 0; i < sensorConfig.getCount(); i++) {
        String sensorID = (i < sensorConfig.getSensorIDs().size()) ? sensorIDs.next()
            : String.format("%s:(%d)", sensorProto.getLabel(), sensorCount);
        sensorCount++;
        Sensor sensor = new Sensor(scenario, remote, sensorProto, sensorID, sensorConfig.isActive());
        this.sensors.put(sensorID, sensor);
      }
    }
  }

  public boolean activateSensors() {
    return this.activateSensors(this.sensors.keySet());
  }

  public boolean activateSensors(Collection<String> sensorIDs) {
    sensorIDs
        .stream()
        .filter(sensorID -> this.hasSensorWithID(sensorID) && !this.hasActiveSensorWithID(sensorID))
        .forEach(sensorID -> this.activateSensorWithID(sensorID));
    return true;
  }

  public boolean activateSensorWithID(String sensorID) {
    if (!this.hasSensorWithID(sensorID)) {
      return false;
    }
    if (this.hasActiveSensorWithID(sensorID)) {
      return true;
    }
    this.sensors.get(sensorID).setActive();
    return true;
  }

  public boolean deactivateSensors() {
    return this.deactivateSensors(this.sensors.keySet());
  }

  public boolean deactivateSensors(Collection<String> sensorIDs) {
    sensorIDs
        .stream()
        .filter(sensorID -> this.hasSensorWithID(sensorID) && this.hasActiveSensorWithID(sensorID))
        .map(sensorID -> this.deactivateSensorWithID(sensorID));
    return true;
  }

  public boolean deactivateSensorWithID(String sensorID) {
    if (!this.hasSensorWithID(sensorID)) {
      return false;
    }
    if (!this.hasActiveSensorWithID(sensorID)) {
      return true;
    }
    this.sensors.get(sensorID).setInactive();
    return true;
  }

  public Set<String> getActiveSensorIDs() {
    return this.getSensorIDs()
        .stream()
        .filter(sensorID -> this.hasActiveSensorWithID(sensorID))
        .collect(Collectors.toSet());
  }

  public Collection<Sensor> getActiveSensors() {
    return this.getActiveSensorIDs()
        .stream()
        .map(sensorID -> this.getSensorWithID(sensorID))
        .collect(Collectors.toList());
  }

  public double getSensorFuelUsage() {
    return this.getActiveSensors()
        .stream()
        .mapToDouble(sensor -> sensor.getBatteryUsage())
        .sum();
  }

  public Set<String> getSensorIDs() {
    return this.sensors.keySet();
  }

  public Collection<Sensor> getSensors() {
    return this.sensors.values();
  }

  public Collection<SensorState> getSensorStates() {
    return this.getSensors()
        .stream()
        .map(sensor -> sensor.getState())
        .collect(Collectors.toList());
  }

  public Collection<Sensor> getSensorsWithTag(String tag) {
    return this.getSensors()
        .stream()
        .filter(sensor -> sensor.hasTag(tag))
        .collect(Collectors.toList());
  }

  public Collection<Sensor> getSensorsWithMatch(Set<String> matchers) {
    return this.getSensors()
        .stream()
        .filter(sensor -> sensor.hasMatch(matchers))
        .collect(Collectors.toList());
  }

  public Collection<Sensor> getSensorsWithModel(String sensorModel) {
    return this.getSensors()
        .stream()
        .filter(sensor -> sensor.hasModel(sensorModel))
        .collect(Collectors.toList());
  }

  public Sensor getSensorWithID(String sensorID) throws SimException {
    if (!this.hasSensorWithID(sensorID)) {
      throw new SimException(String.format("Has no sensor with ID `%s`", sensorID));
    }
    return this.sensors.get(sensorID);
  }

  public boolean hasActiveSensors() {
    return !this.getActiveSensors().isEmpty();
  }

  public boolean hasActiveSensorWithID(String sensorID) {
    return this.hasSensorWithID(sensorID) && this.getSensorWithID(sensorID).isActive();
  }

  public boolean hasSensors() {
    return !this.sensors.isEmpty();
  }

  public boolean hasSensorWithID(String sensorID) {
    return this.sensors.containsKey(sensorID);
  }

  public boolean hasSensorWithMatch(Set<String> matchers) {
    for (Sensor sensor : this.getSensors()) {
      if (sensor.hasMatch(matchers)) {
        return true;
      }
    }
    return false;
  }

  public boolean hasSensorWithModel(String sensorModel) {
    for (Sensor sensor : this.getSensors()) {
      if (sensor.hasModel(sensorModel)) {
        return true;
      }
    }
    return false;
  }
}
