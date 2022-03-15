package com.seat.rescuesim.common.sensor;

import java.util.ArrayList;
import java.util.HashSet;
import com.seat.rescuesim.common.json.*;

/** A serializable configuration of a Sensor.  */
public class SensorConf extends JSONAble {
    private static final String COUNT = "count";
    private static final String SENSOR_IDS = "sensor_ids";
    private static final String SPEC = "spec";

    private int count;
    private HashSet<String> sensorIDs;
    private SensorSpec spec;

    public SensorConf(JSONObject json) {
        super(json);
    }

    public SensorConf(JSONOption option) {
        super(option);
    }

    public SensorConf(String encoding) {
        super(encoding);
    }

    public SensorConf(SensorSpec spec, int count) {
        this.spec = spec;
        this.count = count;
        this.sensorIDs = new HashSet<>();
        for (int i = 0; i < count; i++) {
            this.sensorIDs.add(String.format("%s:(%d)", spec.getLabel(), i));
        }
    }

    public SensorConf(SensorSpec spec, ArrayList<String> sensorIDs) {
        this(spec, new HashSet<String>(sensorIDs));
    }

    public SensorConf(SensorSpec spec, HashSet<String> sensorIDs) {
        this.spec = spec;
        this.count = sensorIDs.size();
        this.sensorIDs = sensorIDs;
    }

    @Override
    protected void decode(JSONObject json) {
        this.spec = new SensorSpec(json.getJSONObject(SensorConf.SPEC));
        this.count = json.getInt(SensorConf.COUNT);
        this.sensorIDs = new HashSet<>();
        JSONArray jsonSensors = json.getJSONArray(SensorConf.SENSOR_IDS);
        for (int i = 0; i < jsonSensors.length(); i++) {
            this.sensorIDs.add(jsonSensors.getString(i));
        }
    }

    public int getCount() {
        return this.count;
    }

    public HashSet<String> getSensorIDs() {
        return this.sensorIDs;
    }

    public SensorSpec getSpec() {
        return this.spec;
    }

    public SensorType getSpecType() {
        return this.spec.getType();
    }

    public boolean hasSensorWithID(String sensorID) {
        return this.sensorIDs.contains(sensorID);
    }

    public boolean hasSensors() {
        return this.count > 0;
    }

    public JSONOption toJSON() {
        JSONObjectBuilder json = JSONBuilder.Object();
        json.put(SensorConf.SPEC, this.spec.toJSON());
        json.put(SensorConf.COUNT, this.count);
        JSONArrayBuilder jsonSensors = JSONBuilder.Array();
        for (String sensorID : this.sensorIDs) {
            jsonSensors.put(sensorID);
        }
        json.put(SensorConf.SENSOR_IDS, jsonSensors.toJSON());
        return json.toJSON();
    }

    public boolean equals(SensorConf conf) {
        return this.spec.equals(conf.spec) && this.count == conf.count && this.sensorIDs.equals(conf.sensorIDs);
    }

}
