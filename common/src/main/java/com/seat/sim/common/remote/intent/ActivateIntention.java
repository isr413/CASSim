package com.seat.sim.common.remote.intent;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.seat.sim.common.json.*;
import com.seat.sim.common.util.Debugger;

public class ActivateIntention extends Intention {
  private static final String ACTIVATIONS = "activations";

  private Set<String> activations;

  public ActivateIntention() {
    super(IntentionType.ACTIVATE);
    this.activations = new HashSet<>();
  }

  public ActivateIntention(String sensorID) {
    this();
    this.addActivation(sensorID);
  }

  public ActivateIntention(Collection<String> sensorIDs) {
    this();
    this.addActivations(sensorIDs);
  }

  public ActivateIntention(Json json) throws JsonException {
    super(json);
  }

  @Override
  protected void decode(JsonObject json) throws JsonException {
    super.decode(json);
    this.activations = (json.hasKey(ActivateIntention.ACTIVATIONS))
        ? new HashSet<>(json.getJsonArray(ActivateIntention.ACTIVATIONS).toList(String.class))
        : new HashSet<>();
  }

  @Override
  protected JsonObjectBuilder getJsonBuilder() throws JsonException {
    JsonObjectBuilder json = super.getJsonBuilder();
    if (this.hasActivations()) {
      json.put(ActivateIntention.ACTIVATIONS, JsonBuilder.toJsonArray(this.activations));
    }
    return json;
  }

  public boolean addActivation(String sensorID) {
    if (this.hasActivationOfSensor(sensorID)) {
      Debugger.logger.warn(String.format("Sensor %s is already intended to be activated", sensorID));
      return true;
    }
    this.activations.add(sensorID);
    return true;
  }

  public boolean addActivations(Collection<String> sensorIDs) {
    if (sensorIDs == null) {
      return false;
    }
    for (String sensorID : sensorIDs) {
      this.addActivation(sensorID);
    }
    return true;
  }

  public Set<String> getActivations() {
    return this.activations;
  }

  @Override
  public String getLabel() {
    return "<ACTIVATE>";
  }

  public boolean hasActivationOfSensor(String sensorID) {
    return this.activations.contains(sensorID);
  }

  public boolean hasActivations() {
    return !this.activations.isEmpty();
  }

  public boolean removeActivation(String sensorID) {
    if (!this.hasActivationOfSensor(sensorID)) {
      Debugger.logger.warn(String.format("Sensor %s is not intended to be activated", sensorID));
      return true;
    }
    this.activations.remove(sensorID);
    return true;
  }

  public boolean removeActivations(Collection<String> sensorIDs) {
    for (String sensorID : sensorIDs) {
      this.removeActivation(sensorID);
    }
    return true;
  }
}
