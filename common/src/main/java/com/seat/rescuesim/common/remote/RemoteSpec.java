package com.seat.rescuesim.common.remote;

import java.util.ArrayList;

import com.seat.rescuesim.common.json.*;
import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.sensor.SensorConfig;
import com.seat.rescuesim.common.sensor.SensorType;
import com.seat.rescuesim.common.util.Debugger;
import com.seat.rescuesim.common.util.SerializableEnum;

/** A serializable specification of a Remote. */
public abstract class RemoteSpec extends JSONAble {
    private static final String LOCATION = "location";
    private static final String MAX_BATTERY = "max_battery";
    private static final String REMOTE_TYPE = "remote_type";
    private static final String SENSORS = "sensors";

    public static RemoteType decodeRemoteType(JSONObject json) throws JSONException {
        return RemoteType.values()[json.getInt(RemoteSpec.REMOTE_TYPE)];
    }

    public static RemoteType decodeRemoteType(JSONOption option) throws JSONException {
        if (option.isSomeObject()) {
            return RemoteSpec.decodeRemoteType(option.someObject());
        }
        Debugger.logger.err(String.format("Cannot decode remote type of %s", option.toString()));
        return RemoteType.NONE;
    }

    public static RemoteType decodeRemoteType(String encoding) throws JSONException {
        return RemoteSpec.decodeRemoteType(JSONOption.String(encoding));
    }

    protected Vector location;
    protected double maxBatteryPower;
    protected ArrayList<SensorConfig> sensors;
    protected RemoteType type;

    public RemoteSpec(JSONObject json) throws JSONException {
        super(json);
    }

    public RemoteSpec(JSONOption option) throws JSONException {
        super(option);
    }

    public RemoteSpec(String encoding) throws JSONException {
        super(encoding);
    }

    public RemoteSpec(RemoteType type, Vector location, double maxBatteryPower, ArrayList<SensorConfig> sensors) {
        this.type = type;
        this.location = location;
        this.maxBatteryPower = maxBatteryPower;
        this.sensors = sensors;
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        this.type = RemoteType.values()[json.getInt(RemoteSpec.REMOTE_TYPE)];
        this.location = new Vector(json.getJSONArray(RemoteSpec.LOCATION));
        this.maxBatteryPower = json.getDouble(RemoteSpec.MAX_BATTERY);
        this.sensors = new ArrayList<>();
        if (json.hasKey(RemoteSpec.SENSORS)) {
            JSONArray jsonSensors = json.getJSONArray(RemoteSpec.SENSORS);
            for (int i = 0; i < jsonSensors.length(); i++) {
                this.sensors.add(new SensorConfig(jsonSensors.getJSONObject(i)));
            }
        }
    }

    public abstract String getLabel();

    public Vector getLocation() {
        return this.location;
    }

    public double getMaxBatteryPower() {
        return this.maxBatteryPower;
    }

    public RemoteType getRemoteType() {
        return this.type;
    }

    public SensorConfig getSensor(int idx) {
        if (idx < 0 || this.sensors.size() <= idx) {
            Debugger.logger.err(String.format("No sensor at index %d found on spec %s", idx, this.getLabel()));
            return null;
        }
        return this.sensors.get(idx);
    }

    public ArrayList<SensorConfig> getSensors() {
        return this.sensors;
    }

    public SensorConfig getSensorWithID(String sensorID) {
        for (SensorConfig conf : this.sensors) {
            if (conf.hasSensorWithID(sensorID)) {
                return conf;
            }
        }
        Debugger.logger.err(String.format("No sensor with ID %s found on spec %s", sensorID, this.getLabel()));
        return null;
    }

    public ArrayList<SensorConfig> getSensorsWithType(SensorType type) {
        ArrayList<SensorConfig> confs = new ArrayList<>();
        for (SensorConfig conf : this.sensors) {
            if (conf.getSpecType().equals(type)) {
                confs.add(conf);
            }
        }
        if (confs.isEmpty()) {
            Debugger.logger.err(String.format("No sensor with type %s found on spec %s",
                type.getLabel(), this.getLabel()));
        }
        return confs;
    }

    public abstract SerializableEnum getSpecType();

    public boolean hasSensor(int idx) {
        return 0 <= idx && idx < this.sensors.size();
    }

    public boolean hasSensors() {
        return !this.sensors.isEmpty();
    }

    public boolean hasSensorWithID(String sensorID) {
        for (SensorConfig conf : this.sensors) {
            if (conf.hasSensorWithID(sensorID)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasSensorWithType(SensorType type) {
        for (SensorConfig conf : this.sensors) {
            if (conf.getSpecType().equals(type)) {
                return true;
            }
        }
        return false;
    }

    public boolean isDisabled() {
        return !this.isEnabled();
    }

    public boolean isEnabled() {
        return this.maxBatteryPower > 0;
    }

    protected JSONObjectBuilder getJSONBuilder() {
        JSONObjectBuilder json = JSONBuilder.Object();
        json.put(RemoteSpec.REMOTE_TYPE, this.type.getType());
        json.put(RemoteSpec.LOCATION, this.location.toJSON());
        json.put(RemoteSpec.MAX_BATTERY, this.maxBatteryPower);
        if (this.hasSensors()) {
            JSONArrayBuilder jsonSensors = JSONBuilder.Array();
            for (SensorConfig conf : this.sensors) {
                jsonSensors.put(conf.toJSON());
            }
            json.put(RemoteSpec.SENSORS, jsonSensors.toJSON());
        }
        return json;
    }

    public abstract JSONOption toJSON();

    public boolean equals(RemoteSpec spec) {
        return this.type.equals(spec.type) && this.location.equals(spec.location) &&
            this.maxBatteryPower == spec.maxBatteryPower && this.sensors.equals(spec.sensors);
    }

}
