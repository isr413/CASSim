package com.seat.sim.common.sensor;

import java.util.HashSet;
import java.util.Set;

import com.seat.sim.common.json.*;

/** A serializable generic Sensor state. */
public class SensorState extends Jsonable {
    public static final String ACTIVE = "active";
    public static final String SENSOR_GROUPS = "sensor_groups";
    public static final String SENSOR_ID = "sensor_id";
    public static final String SENSOR_MODEL = "sensor_model";
    public static final String SUBJECTS = "subjects";

    private boolean active;
    private Set<String> sensorGroups;
    private String sensorID;
    private String sensorModel;
    private Set<String> subjects;

    public SensorState(String sensorID, String sensorModel, Set<String> sensorGroups, Set<String> subjects,
            boolean active) {
        this.sensorID = sensorID;
        this.sensorModel = sensorModel;
        this.sensorGroups = (sensorGroups != null) ? new HashSet<>(sensorGroups) : new HashSet<>();
        this.subjects = (subjects != null) ? new HashSet<>(subjects) : new HashSet<>();
        this.active = active;
    }

    public SensorState(Json json) throws JsonException {
        super(json);
    }

    @Override
    protected void decode(JsonObject json) throws JsonException {
        this.sensorID = json.getString(SensorState.SENSOR_ID);
        this.sensorModel = json.getString(SensorProto.SENSOR_MODEL);
        this.sensorGroups = (json.hasKey(SensorState.SENSOR_GROUPS)) ?
            new HashSet<>(json.getJsonArray(SensorState.SENSOR_GROUPS).toList(String.class)) :
            new HashSet<>();
        this.subjects = (json.hasKey(SensorState.SUBJECTS)) ?
            new HashSet<>(json.getJsonArray(SensorState.SUBJECTS).toList(String.class)) :
            new HashSet<>();
        this.active = json.getBoolean(SensorState.ACTIVE);
    }

    protected JsonObjectBuilder getJsonBuilder() throws JsonException {
        JsonObjectBuilder json = JsonBuilder.Object();
        json.put(SensorState.SENSOR_ID, this.sensorID);
        json.put(SensorProto.SENSOR_MODEL, this.sensorModel);
        if (this.hasSensorGroups()) {
            json.put(SensorState.SENSOR_GROUPS, JsonBuilder.toJsonArray(this.sensorGroups));
        }
        if (this.hasSubjects()) {
            json.put(SensorState.SUBJECTS, JsonBuilder.toJsonArray(this.subjects));
        }
        json.put(SensorState.ACTIVE, this.active);
        return json;
    }

    public boolean equals(SensorState state) {
        if (state == null) return false;
        return this.sensorID.equals(state.sensorID) && this.sensorModel.equals(state.sensorModel);
    }

    public String getLabel() {
        return String.format("%s:<%s>", this.sensorID, this.sensorModel);
    }

    public Set<String> getSensorGroups() {
        return this.sensorGroups;
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

    public boolean hasSensorGroups() {
        return !this.sensorGroups.isEmpty();
    }

    public boolean hasSensorGroupWithTag(String groupTag) {
        return this.sensorGroups.contains(groupTag);
    }

    public boolean hasSensorID(String sensorID) {
        if (sensorID == null || this.sensorID == null) return this.sensorID == sensorID;
        return this.sensorID.equals(sensorID);
    }

    public boolean hasSensorModel(String sensorModel) {
        if (sensorModel == null || this.sensorModel == null) return this.sensorModel == sensorModel;
        return this.sensorModel.equals(sensorModel);
    }

    public boolean hasSubjects() {
        return !this.subjects.isEmpty();
    }

    public boolean hasSubjectWithID(String subjectID) {
        return this.subjects.contains(subjectID);
    }

    public boolean isActive() {
        return this.active;
    }

    public Json toJson() throws JsonException {
        return this.getJsonBuilder().toJson();
    }
}
