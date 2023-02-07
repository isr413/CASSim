package com.seat.sim.common.remote.intent;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.seat.sim.common.json.*;
import com.seat.sim.common.util.Debugger;

public class DeactivateIntention extends Intention {
  private static final String DEACTIVATIONS = "deactivations";

  private HashSet<String> deactivations;

  public DeactivateIntention() {
    super(IntentionType.DEACTIVATE);
    this.deactivations = new HashSet<>();
  }

  public DeactivateIntention(String sensorID) {
    this();
    this.addDeactivation(sensorID);
  }

  public DeactivateIntention(Collection<String> sensorIDs) {
    this();
    this.addDeactivations(sensorIDs);
  }

  public DeactivateIntention(Json json) throws JsonException {
    super(json);
  }

  @Override
  protected void decode(JsonObject json) throws JsonException {
    super.decode(json);
    this.deactivations = new HashSet<>();
    if (json.hasKey(DeactivateIntention.DEACTIVATIONS)) {
      this.deactivations = (json.hasKey(DeactivateIntention.DEACTIVATIONS))
          ? new HashSet<>(json.getJsonArray(DeactivateIntention.DEACTIVATIONS).toList(String.class))
          : new HashSet<>();
    }
  }

  @Override
  protected JsonObjectBuilder getJsonBuilder() throws JsonException {
    JsonObjectBuilder json = super.getJsonBuilder();
    if (this.hasDeactivations()) {
      json.put(DeactivateIntention.DEACTIVATIONS, JsonBuilder.toJsonArray(this.deactivations));
    }
    return json;
  }

  public boolean addDeactivation(String sensorID) {
    if (this.hasDeactivationOfSensor(sensorID)) {
      Debugger.logger.warn(String.format("Sensor %s is already intended to be deactivated", sensorID));
      return true;
    }
    this.deactivations.add(sensorID);
    return true;
  }

  public boolean addDeactivations(Collection<String> sensorIDs) {
    if (sensorIDs == null) {
      return false;
    }
    for (String sensorID : sensorIDs) {
      this.addDeactivation(sensorID);
    }
    return true;
  }

  public Set<String> getDeactivations() {
    return this.deactivations;
  }

  @Override
  public String getLabel() {
    return "<DEACTIVATE>";
  }

  public boolean hasDeactivationOfSensor(String sensorID) {
    return this.deactivations.contains(sensorID);
  }

  public boolean hasDeactivations() {
    return !this.deactivations.isEmpty();
  }

  public boolean removeDeactivation(String sensorID) {
    if (!this.hasDeactivationOfSensor(sensorID)) {
      Debugger.logger.warn(String.format("Sensor %s is not intended to be deactivated", sensorID));
      return true;
    }
    this.deactivations.remove(sensorID);
    return true;
  }

  public boolean removeDeactivations(Collection<String> sensorIDs) {
    for (String sensorID : sensorIDs) {
      this.removeDeactivation(sensorID);
    }
    return true;
  }
}
