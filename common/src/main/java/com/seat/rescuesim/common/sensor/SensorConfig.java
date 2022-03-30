package com.seat.rescuesim.common.sensor;

import java.util.ArrayList;
import java.util.HashSet;
import com.seat.rescuesim.common.json.JSONAble;
import com.seat.rescuesim.common.json.JSONArray;
import com.seat.rescuesim.common.json.JSONArrayBuilder;
import com.seat.rescuesim.common.json.JSONBuilder;
import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.JSONObjectBuilder;
import com.seat.rescuesim.common.json.JSONOption;

/** A serializable configuration of a Sensor.  */
public class SensorConfig extends JSONAble {

    private int count;
    private HashSet<String> sensorIDs;
    private SensorSpec spec;

    public SensorConfig(SensorSpec spec, int count) {
        this.spec = spec;
        this.count = count;
        this.sensorIDs = new HashSet<>();
        for (int i = 0; i < count; i++) {
            this.sensorIDs.add(String.format("%s:(%d)", spec.getLabel(), i));
        }
    }

    public SensorConfig(SensorSpec spec, ArrayList<String> sensorIDs) {
        this(spec, new HashSet<String>(sensorIDs));
    }

    public SensorConfig(SensorSpec spec, HashSet<String> sensorIDs) {
        this.spec = spec;
        this.count = sensorIDs.size();
        this.sensorIDs = sensorIDs;
    }

    public SensorConfig(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        this.spec = new SensorSpec(json.getJSONOption(SensorConst.SPEC));
        this.count = json.getInt(SensorConst.COUNT);
        this.sensorIDs = new HashSet<>();
        JSONArray jsonSensors = json.getJSONArray(SensorConst.SENSOR_IDS);
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
        json.put(SensorConst.SPEC, this.spec.toJSON());
        json.put(SensorConst.COUNT, this.count);
        JSONArrayBuilder jsonSensors = JSONBuilder.Array();
        for (String sensorID : this.sensorIDs) {
            jsonSensors.put(sensorID);
        }
        json.put(SensorConst.SENSOR_IDS, jsonSensors.toJSON());
        return json.toJSON();
    }

    public boolean equals(SensorConfig conf) {
        return this.spec.equals(conf.spec) && this.count == conf.count && this.sensorIDs.equals(conf.sensorIDs);
    }

}
