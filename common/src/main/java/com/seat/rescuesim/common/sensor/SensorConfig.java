package com.seat.rescuesim.common.sensor;

import java.util.Collection;
import java.util.HashSet;

import com.seat.rescuesim.common.json.JSONAble;
import com.seat.rescuesim.common.json.JSONArray;
import com.seat.rescuesim.common.json.JSONArrayBuilder;
import com.seat.rescuesim.common.json.JSONBuilder;
import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.JSONObjectBuilder;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.json.SerializableEnum;

/** A serializable configuration of a generic Sensor. */
public class SensorConfig extends JSONAble {
    public static final String COUNT = "count";
    public static final String PROTO = "__proto__";
    public static final String SENSOR_IDS = "sensor_ids";

    private int count;
    private SensorProto proto;
    private HashSet<String> sensorIDs;

    public SensorConfig(SensorProto proto, int count) {
        this.proto = proto;
        this.count = count;
        this.sensorIDs = new HashSet<>();
        for (int i = 0; i < count; i++) {
            this.sensorIDs.add(String.format("%s:(%d)", proto.getLabel(), i));
        }
    }

    public SensorConfig(SensorProto proto, Collection<String> sensorIDs) {
        this.proto = proto;
        this.count = sensorIDs.size();
        this.sensorIDs = new HashSet<>(sensorIDs);
    }

    public SensorConfig(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        this.proto = this.decodeProto(json.getJSONOption(SensorConfig.PROTO));
        this.count = json.getInt(SensorConfig.COUNT);
        this.sensorIDs = new HashSet<>();
        if (json.hasKey(SensorConfig.SENSOR_IDS)) {
            JSONArray jsonSensors = json.getJSONArray(SensorConfig.SENSOR_IDS);
            for (int i = 0; i < jsonSensors.length(); i++) {
                this.sensorIDs.add(jsonSensors.getString(i));
            }
        }
    }

    protected SensorProto decodeProto(JSONOption option) throws JSONException {
        return new SensorProto(option);
    }

    public int getCount() {
        return this.count;
    }

    public SensorProto getProto() {
        return this.proto;
    }

    public HashSet<String> getSensorIDs() {
        return this.sensorIDs;
    }

    public SensorType getSensorType() {
        return this.proto.getSensorType();
    }

    public SerializableEnum getSpecType() {
        return this.proto.getSpecType();
    }

    public boolean hasProto() {
        return this.proto != null && !this.proto.getSensorType().equals(SensorType.NONE);
    }

    public boolean hasSensorWithID(String sensorID) {
        return this.sensorIDs.contains(sensorID);
    }

    public boolean hasSensors() {
        return this.count > 0;
    }

    public JSONOption toJSON() {
        JSONObjectBuilder json = JSONBuilder.Object();
        json.put(SensorConfig.PROTO, this.proto.toJSON());
        json.put(SensorConfig.COUNT, this.count);
        if (this.hasSensors()) {
            JSONArrayBuilder jsonSensors = JSONBuilder.Array();
            for (String sensorID : this.sensorIDs) {
                jsonSensors.put(sensorID);
            }
            json.put(SensorConfig.SENSOR_IDS, jsonSensors.toJSON());
        }
        return json.toJSON();
    }

    public boolean equals(SensorConfig config) {
        if (config == null) return false;
        return this.proto.equals(config.proto) && this.count == config.count && this.sensorIDs.equals(config.sensorIDs);
    }

}
