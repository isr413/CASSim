package com.seat.sim.common.sensor;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.seat.sim.common.json.*;

/** A serializable configuration of a generic Sensor. */
public class SensorConfig extends Jsonable {
  public static final String COUNT = "count";
  public static final String PROTO = "__proto__";
  public static final String SENSOR_IDS = "sensor_ids";

  private boolean active;
  private SensorProto proto;
  private Set<String> sensorIDs;

  public SensorConfig(SensorProto proto, int count, boolean active) {
    this(proto, new HashSet<String>(), active);
    this.init(count);
  }

  public SensorConfig(SensorProto proto, Collection<String> sensorIDs, boolean active) {
    this(proto, new HashSet<String>(sensorIDs), active);
  }

  private SensorConfig(SensorProto proto, HashSet<String> sensorIDs, boolean active) {
    this.proto = (proto != null) ? proto : new SensorProto();
    this.sensorIDs = (sensorIDs != null) ? sensorIDs : new HashSet<>();
    this.active = active;
  }

  public SensorConfig(Json json) throws JsonException {
    super(json);
  }

  private void init(int count) {
    for (int i = 0; i < count; i++) {
      this.sensorIDs.add(String.format("%s:(%d)", proto.getLabel(), i));
    }
  }

  @Override
  protected void decode(JsonObject json) throws JsonException {
    this.proto = (json.hasKey(SensorConfig.PROTO)) ? new SensorProto(json.getJson(SensorConfig.PROTO))
        : new SensorProto();
    this.sensorIDs = (json.hasKey(SensorConfig.SENSOR_IDS))
        ? new HashSet<>(json.getJsonArray(SensorConfig.SENSOR_IDS).toList(String.class))
        : new HashSet<>();
    if (this.sensorIDs.isEmpty() && json.hasKey(SensorConfig.COUNT)) {
      this.init(json.getInt(SensorConfig.COUNT));
    }
    this.active = json.getBoolean(SensorState.ACTIVE);
  }

  protected JsonObjectBuilder getJsonBuilder() throws JsonException {
    JsonObjectBuilder json = JsonBuilder.Object();
    json.put(SensorConfig.PROTO, this.proto.toJson());
    json.put(SensorConfig.COUNT, this.getCount());
    if (this.hasSensors()) {
      json.put(SensorConfig.SENSOR_IDS, JsonBuilder.toJsonArray(this.sensorIDs));
    }
    json.put(SensorState.ACTIVE, this.active);
    return json;
  }

  public int getCount() {
    return this.sensorIDs.size();
  }

  public SensorProto getProto() {
    return this.proto;
  }

  public Set<String> getSensorIDs() {
    return this.sensorIDs;
  }

  public String getSensorModel() {
    return this.proto.getModel();
  }

  public boolean hasProto() {
    return this.proto != null;
  }

  public boolean hasSensors() {
    return !this.sensorIDs.isEmpty();
  }

  public boolean hasSensorMatch(Set<String> matchers) {
    return this.proto.hasMatch(matchers);
  }

  public boolean hasSensorWithID(String sensorID) {
    return this.sensorIDs.contains(sensorID);
  }

  public boolean hasSensorWithModel(String sensorModel) {
    return this.proto.hasModel(sensorModel);
  }

  public boolean hasSensorWithTag(String tag) {
    return this.proto.hasTag(tag);
  }

  public boolean isActive() {
    return this.active;
  }

  public Json toJson() throws JsonException {
    return this.getJsonBuilder().toJson();
  }
}
