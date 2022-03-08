package com.seat.rescuesim.common.base;

import java.util.ArrayList;
import java.util.Arrays;

import com.seat.rescuesim.common.json.*;
import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.remote.RemoteSpecification;
import com.seat.rescuesim.common.sensor.SensorConf;
import com.seat.rescuesim.common.sensor.SensorType;
import com.seat.rescuesim.common.util.Debugger;

/** A serializable class to represent the Drone HQ, or Base. */
public class BaseSpec extends JSONAble implements RemoteSpecification {
    private static final String BASE_LOCATION = "location";
    private static final String BASE_SENSORS = "sensors";
    private static final String BASE_TYPE = "base_type";

    public static BaseSpec None() {
        return new BaseSpec(BaseType.NONE, new Vector(), new ArrayList<SensorConf>());
    }

    private Vector location;
    private ArrayList<SensorConf> sensors;
    private BaseType type;

    public BaseSpec(JSONObject json) {
        super(json);
    }

    public BaseSpec(JSONOption option) {
        super(option);
    }

    public BaseSpec(String encoding) {
        super(encoding);
    }

    public BaseSpec() {
        this(BaseType.DEFAULT, new Vector(), new ArrayList<SensorConf>());
    }

    public BaseSpec(BaseType type, Vector location) {
        this(type, location, new ArrayList<SensorConf>());
    }

    public BaseSpec(Vector location) {
        this(BaseType.DEFAULT, location, new ArrayList<SensorConf>());
    }

    public BaseSpec(BaseType type, Vector location, SensorConf[] sensors) {
        this(type, location, new ArrayList<SensorConf>(Arrays.asList(sensors)));
    }

    public BaseSpec(Vector location, SensorConf[] sensors) {
        this(BaseType.DEFAULT, location, new ArrayList<SensorConf>(Arrays.asList(sensors)));
    }

    public BaseSpec(Vector location, ArrayList<SensorConf> sensors) {
        this(BaseType.DEFAULT, location, sensors);
    }

    public BaseSpec(BaseType type, Vector location, ArrayList<SensorConf> sensors) {
        this.type = type;
        this.location = location;
        this.sensors = sensors;
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        this.type = BaseType.values()[json.getInt(BaseSpec.BASE_TYPE)];
        this.location = new Vector(json.getJSONArray(BaseSpec.BASE_LOCATION));
        this.sensors = new ArrayList<>();
        JSONArray jsonSensors = json.getJSONArray(BaseSpec.BASE_SENSORS);
        for (int i = 0; i < jsonSensors.length(); i++) {
            this.sensors.add(new SensorConf(jsonSensors.getJSONObject(i)));
        }
    }

    public String getLabel() {
        return String.format("b<%s>", this.location.toString());
    }

    public Vector getLocation() {
        return this.location;
    }

    public SensorConf getSensor(int idx) {
        if (!this.hasSensor(idx)) {
            Debugger.logger.err(String.format("No sensor at idx %d found on base spec %s",
                idx, this.type.getLabel()));
            return null;
        }
        return this.sensors.get(idx);
    }

    public SensorConf getSensor(String remote) {
        for (SensorConf conf : this.sensors) {
            if (conf.hasRemote(remote)) {
                return conf;
            }
        }
        Debugger.logger.err(String.format("No sensor with ID %s found on base spec %s",
            remote, this.type.getLabel()));
        return null;
    }

    public ArrayList<SensorConf> getSensors() {
        return this.sensors;
    }

    public ArrayList<SensorConf> getSensorsWithType(SensorType type) {
        ArrayList<SensorConf> confs = new ArrayList<>();
        for (SensorConf conf : this.sensors) {
            if (conf.getSpecification().getType().equals(type)) {
                confs.add(conf);
            }
        }
        if (confs.isEmpty()) {
            Debugger.logger.err(String.format("No sensor with type %s found on base spec %s",
                type.getLabel(), this.type.getLabel()));
        }
        return confs;
    }

    public BaseType getType() {
        return this.type;
    }

    public boolean hasSensor(int idx) {
        return 0 <= idx && idx < this.sensors.size();
    }

    public boolean hasSensor(String remote) {
        for (SensorConf conf : this.sensors) {
            if (conf.hasRemote(remote)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasSensors() {
        return !this.sensors.isEmpty();
    }

    public boolean hasSensorWithType(SensorType type) {
        for (SensorConf conf : this.sensors) {
            if (conf.getSpecification().getType().equals(type)) {
                return true;
            }
        }
        return false;
    }

    public JSONOption toJSON() {
        JSONObjectBuilder json = JSONBuilder.Object();
        json.put(BaseSpec.BASE_TYPE, this.type.getType());
        json.put(BaseSpec.BASE_LOCATION, this.location.toJSON());
        JSONArrayBuilder jsonSensors = JSONBuilder.Array();
        for (SensorConf conf : this.sensors) {
            jsonSensors.put(conf.toJSON());
        }
        json.put(BaseSpec.BASE_SENSORS, jsonSensors.toJSON());
        return json.toJSON();
    }

    public boolean equals(BaseSpec base) {
        return this.location.equals(base.location) && this.sensors.equals(base.sensors);
    }

}
