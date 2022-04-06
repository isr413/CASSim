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

/** A serializable configuration of a Sensor. */
public class SensorConfig extends JSONAble {
    public static final String COUNT = "count";
    public static final String SENSOR_IDS = "sensor_ids";
    public static final String SPEC = "spec";

    private int count;
    private HashSet<String> sensorIDs;
    private SensorProto spec;

    public SensorConfig(SensorProto spec, int count) {
        this.spec = spec;
        this.count = count;
        this.sensorIDs = new HashSet<>();
        for (int i = 0; i < count; i++) {
            this.sensorIDs.add(String.format("%s:(%d)", spec.getLabel(), i));
        }
    }

    public SensorConfig(SensorProto spec, Collection<String> sensorIDs) {
        this.spec = spec;
        this.count = sensorIDs.size();
        this.sensorIDs = new HashSet<>(sensorIDs);
    }

    public SensorConfig(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        this.spec = this.decodeSpec(json.getJSONOption(SensorConfig.SPEC));
        this.count = json.getInt(SensorConfig.COUNT);
        this.sensorIDs = new HashSet<>();
        if (json.hasKey(SensorConfig.SENSOR_IDS)) {
            JSONArray jsonSensors = json.getJSONArray(SensorConfig.SENSOR_IDS);
            for (int i = 0; i < jsonSensors.length(); i++) {
                this.sensorIDs.add(jsonSensors.getString(i));
            }
        }
    }

    protected SensorProto decodeSpec(JSONOption jsonSpec) throws JSONException {
        return new SensorProto(jsonSpec);
    }

    public int getCount() {
        return this.count;
    }

    public HashSet<String> getSensorIDs() {
        return this.sensorIDs;
    }

    public SensorType getSensorType() {
        return this.spec.getSensorType();
    }

    public SensorProto getSpec() {
        return this.spec;
    }

    public SerializableEnum getSpecType() {
        return this.spec.getSpecType();
    }

    public boolean hasSensorWithID(String sensorID) {
        return this.sensorIDs.contains(sensorID);
    }

    public boolean hasSensors() {
        return this.count > 0;
    }

    public JSONOption toJSON() {
        JSONObjectBuilder json = JSONBuilder.Object();
        json.put(SensorConfig.SPEC, this.spec.toJSON());
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
        return this.spec.equals(config.spec) && this.count == config.count && this.sensorIDs.equals(config.sensorIDs);
    }

}
