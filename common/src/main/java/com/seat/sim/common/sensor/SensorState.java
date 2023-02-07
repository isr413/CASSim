package com.seat.sim.common.sensor;

import java.util.HashSet;
import java.util.Set;

import com.seat.sim.common.json.*;

/** A serializable generic Sensor state. */
public class SensorState extends Jsonable {
  public static final String ACTIVE = "active";
  public static final String SENSOR_ID = "sensor_id";
  public static final String SENSOR_MODEL = "sensor_model";
  public static final String SUBJECTS = "subjects";
  public static final String TAGS = "tags";

  private boolean active;
  private String sensorID;
  private String sensorModel;
  private Set<String> subjects;
  private Set<String> tags;

  public SensorState(String sensorID, String sensorModel, Set<String> tags, Set<String> subjects, boolean active) {
    this.sensorID = sensorID;
    this.sensorModel = sensorModel;
    this.tags = (tags != null) ? new HashSet<>(tags) : new HashSet<>();
    this.subjects = (subjects != null) ? new HashSet<>(subjects) : new HashSet<>();
    this.active = active;
  }

  public SensorState(Json json) throws JsonException {
    super(json);
  }

  @Override
  protected void decode(JsonObject json) throws JsonException {
    this.sensorID = json.getString(SensorState.SENSOR_ID);
    this.sensorModel = json.getString(SensorState.SENSOR_MODEL);
    this.tags = (json.hasKey(SensorState.TAGS))
        ? new HashSet<>(json.getJsonArray(SensorState.TAGS).toList(String.class))
        : new HashSet<>();
    this.subjects = (json.hasKey(SensorState.SUBJECTS))
        ? new HashSet<>(json.getJsonArray(SensorState.SUBJECTS).toList(String.class))
        : new HashSet<>();
    this.active = json.getBoolean(SensorState.ACTIVE);
  }

  protected JsonObjectBuilder getJsonBuilder() throws JsonException {
    JsonObjectBuilder json = JsonBuilder.Object();
    json.put(SensorState.SENSOR_ID, this.sensorID);
    json.put(SensorState.SENSOR_MODEL, this.sensorModel);
    if (this.hasTags()) {
      json.put(SensorState.TAGS, JsonBuilder.toJsonArray(this.tags));
    }
    if (this.hasSubjects()) {
      json.put(SensorState.SUBJECTS, JsonBuilder.toJsonArray(this.subjects));
    }
    json.put(SensorState.ACTIVE, this.active);
    return json;
  }

  public String getLabel() {
    return String.format("%s:<%s>", this.sensorID, this.sensorModel);
  }

  public String getSensorID() {
    return this.sensorID;
  }

  public String getSensorModel() {
    return this.sensorModel;
  }

  public Set<String> getSubjects() {
    return this.subjects;
  }

  public Set<String> getTags() {
    return this.tags;
  }

  public boolean hasMatch(Set<String> matchers) {
    for (String matcher : matchers) {
      if (this.hasTag(matcher)) {
        return true;
      }
    }
    return false;
  }

  public boolean hasSensorID(String sensorID) {
    if (sensorID == null || this.sensorID == null) {
      return this.sensorID == sensorID;
    }
    return this.sensorID.equals(sensorID);
  }

  public boolean hasSensorModel(String sensorModel) {
    if (sensorModel == null || this.sensorModel == null) {
      return this.sensorModel == sensorModel;
    }
    return this.sensorModel.equals(sensorModel);
  }

  public boolean hasSubjects() {
    return !this.subjects.isEmpty();
  }

  public boolean hasSubjectWithID(String subjectID) {
    return this.subjects.contains(subjectID);
  }

  public boolean hasTag(String tag) {
    return this.tags.contains(tag);
  }

  public boolean hasTags() {
    return !this.tags.isEmpty();
  }

  public boolean isActive() {
    return this.active;
  }

  public Json toJson() throws JsonException {
    return this.getJsonBuilder().toJson();
  }
}
