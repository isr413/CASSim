package com.seat.sim.common.sensor;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.seat.sim.common.json.JSONAble;
import com.seat.sim.common.json.JSONArray;
import com.seat.sim.common.json.JSONArrayBuilder;
import com.seat.sim.common.json.JSONBuilder;
import com.seat.sim.common.json.JSONException;
import com.seat.sim.common.json.JSONObject;
import com.seat.sim.common.json.JSONObjectBuilder;
import com.seat.sim.common.json.JSONOptional;

/** A serializable configuration of a generic Sensor. */
public class SensorConfig extends JSONAble {
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

    public SensorConfig(JSONOptional optional) throws JSONException {
        super(optional);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        this.proto = SensorRegistry.decodeTo(json.getJSONOptional(SensorConfig.PROTO), SensorProto.class);
        this.count = json.getInt(SensorConfig.COUNT);
        this.sensorIDs = new HashSet<>();
        if (json.hasKey(SensorConfig.SENSOR_IDS)) {
            JSONArray jsonSensors = json.getJSONArray(SensorConfig.SENSOR_IDS);
            for (int i = 0; i < jsonSensors.length(); i++) {
                this.sensorIDs.add(jsonSensors.getString(i));
            }
        }
        this.active = json.getBoolean(SensorState.ACTIVE);
    }

    public int getCount() {
        return this.count;
    }

    public SensorProto getProto() {
        return this.proto;
    }

    public Collection<String> getSensorIDs() {
        return this.sensorIDs;
    }

    public String getSensorModel() {
        return this.proto.getSensorModel();
    }

    public String getSensorType() {
        return this.getClass().getName();
    }

    public boolean hasProto() {
        return this.proto != null;
    }

    public boolean hasSensorWithID(String sensorID) {
        return this.sensorIDs.contains(sensorID);
    }

    public boolean hasSensors() {
        return this.count > 0;
    }

    public boolean isActive() {
        return this.active;
    }

    public boolean isInactive() {
        return !this.isActive();
    }

    public JSONOptional toJSON() throws JSONException {
        JSONObjectBuilder json = JSONBuilder.Object();
        json.put(SensorRegistry.SENSOR_TYPE, this.getSensorType());
        json.put(SensorConfig.PROTO, this.proto.toJSON());
        json.put(SensorConfig.COUNT, this.count);
        if (this.hasSensors()) {
            JSONArrayBuilder jsonSensors = JSONBuilder.Array();
            for (String sensorID : this.sensorIDs) {
                jsonSensors.put(sensorID);
            }
            json.put(SensorConfig.SENSOR_IDS, jsonSensors.toJSON());
        }
        json.put(SensorState.ACTIVE, this.active);
        return json.toJSON();
    }

    public boolean equals(SensorConfig config) {
        if (config == null) return false;
        return this.getSensorType().equals(config.getSensorType()) && this.proto.equals(config.proto) &&
            this.count == config.count && this.sensorIDs.equals(config.sensorIDs) && this.active == config.active;
    }

}
