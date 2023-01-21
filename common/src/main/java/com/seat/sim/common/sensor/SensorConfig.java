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
    private int count;
    private SensorProto proto;
    private Set<String> sensorIDs;

    public SensorConfig(SensorProto proto, int count, boolean active) {
        this(proto, count, new HashSet<String>(), active);
        for (int i = 0; i < count; i++) {
            this.sensorIDs.add(String.format("%s:(%d)", proto.getLabel(), i));
        }
    }

    public SensorConfig(SensorProto proto, Collection<String> sensorIDs, boolean active) {
        this(proto, sensorIDs.size(), new HashSet<String>(sensorIDs), active);
    }

    private SensorConfig(SensorProto proto, int count, HashSet<String> sensorIDs, boolean active) {
        this.proto = proto;
        this.count = count;
        this.sensorIDs = sensorIDs;
        this.active = active;
    }

    public SensorConfig(Json json) throws JsonException {
        super(json);
    }

    @Override
    protected void decode(JsonObject json) throws JsonException {
        this.proto = new SensorProto(json.getJson(SensorConfig.PROTO));
        this.count = json.getInt(SensorConfig.COUNT);
        this.sensorIDs = (json.hasKey(SensorConfig.SENSOR_IDS)) ?
            new HashSet<>(json.getJsonArray(SensorConfig.SENSOR_IDS).toList(String.class)) :
            new HashSet<>();
        this.active = json.getBoolean(SensorState.ACTIVE);
    }

    protected JsonObjectBuilder getJsonBuilder() throws JsonException {
        JsonObjectBuilder json = JsonBuilder.Object();
        json.put(SensorConfig.PROTO, this.proto.toJson());
        json.put(SensorConfig.COUNT, this.count);
        if (this.hasSensors()) {
            json.put(SensorConfig.SENSOR_IDS, JsonBuilder.toJsonArray(this.sensorIDs));
        }
        json.put(SensorState.ACTIVE, this.active);
        return json;
    }

    public boolean equals(SensorConfig config) {
        if (config == null) return false;
        return this.proto.equals(config.proto) && this.count == config.count && 
            this.sensorIDs.equals(config.sensorIDs) && this.active == config.active;
    }

    public int getCount() {
        return this.count;
    }

    public SensorProto getProto() {
        return this.proto;
    }

    public Set<String> getSensorIDs() {
        return this.sensorIDs;
    }

    public String getSensorModel() {
        return this.proto.getSensorModel();
    }

    public boolean hasProto() {
        return this.proto != null;
    }

    public boolean hasSensors() {
        return this.count > 0;
    }

    public boolean hasSensorWithID(String sensorID) {
        return this.sensorIDs.contains(sensorID);
    }

    public boolean hasSensorWithModel(String sensorModel) {
        return this.proto.hasSensorModel(sensorModel);
    }

    public boolean hasSensorWithTag(String groupTag) {
        return this.proto.hasSensorGroupWithTag(groupTag);
    }

    public boolean isActive() {
        return this.active;
    }

    public Json toJson() throws JsonException {
        return this.getJsonBuilder().toJson();
    }
}
