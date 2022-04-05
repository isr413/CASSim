package com.seat.rescuesim.common.remote;

import java.util.ArrayList;
import java.util.Collection;

import com.seat.rescuesim.common.json.JSONAble;
import com.seat.rescuesim.common.json.JSONArray;
import com.seat.rescuesim.common.json.JSONArrayBuilder;
import com.seat.rescuesim.common.json.JSONBuilder;
import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.JSONObjectBuilder;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.json.SerializableEnum;
import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.sensor.SensorConfig;
import com.seat.rescuesim.common.sensor.SensorType;
import com.seat.rescuesim.common.util.CoreException;
import com.seat.rescuesim.common.util.Debugger;

/** A serializable specification of a Remote. */
public class RemoteSpec extends JSONAble {

    protected static final double DEFAULT_BATTERY_POWER = 1.0;
    protected static final RemoteType DEFAULT_REMOTE_TYPE = RemoteType.GENERIC;

    private Vector location;
    private double maxBatteryPower;
    private RemoteType remoteType;
    private ArrayList<SensorConfig> sensors;

    public RemoteSpec() {
        this(RemoteSpec.DEFAULT_REMOTE_TYPE, null, RemoteSpec.DEFAULT_BATTERY_POWER, null);
    }

    public RemoteSpec(Vector location) {
        this(RemoteSpec.DEFAULT_REMOTE_TYPE, location, RemoteSpec.DEFAULT_BATTERY_POWER, null);
    }

    public RemoteSpec(double maxBatteryPower) {
        this(RemoteSpec.DEFAULT_REMOTE_TYPE, null, maxBatteryPower, null);
    }

    public RemoteSpec(Vector location, double maxBatteryPower) {
        this(RemoteSpec.DEFAULT_REMOTE_TYPE, location, maxBatteryPower, null);
    }

    public RemoteSpec(double maxBatteryPower, Collection<SensorConfig> sensors) {
        this(RemoteSpec.DEFAULT_REMOTE_TYPE, null, maxBatteryPower, sensors);
    }

    public RemoteSpec(Vector location, double maxBatteryPower, Collection<SensorConfig> sensors) {
        this(RemoteSpec.DEFAULT_REMOTE_TYPE, location, maxBatteryPower, sensors);
    }

    protected RemoteSpec(RemoteType remoteType) {
        this(remoteType, null, RemoteSpec.DEFAULT_BATTERY_POWER, null);
    }

    protected RemoteSpec(RemoteType remoteType, double maxBatteryPower) {
        this(remoteType, null, maxBatteryPower, null);
    }

    protected RemoteSpec(RemoteType remoteType, Vector location) {
        this(remoteType, location, RemoteSpec.DEFAULT_BATTERY_POWER, null);
    }

    protected RemoteSpec(RemoteType remoteType, Vector location, double maxBatteryPower) {
        this(remoteType, location, maxBatteryPower, null);
    }

    protected RemoteSpec(RemoteType remoteType, double maxBatteryPower, Collection<SensorConfig> sensors) {
        this(remoteType, null, maxBatteryPower, sensors);
    }

    protected RemoteSpec(RemoteType remoteType, Vector location, double maxBatteryPower,
            Collection<SensorConfig> sensors) {
        this.remoteType = remoteType;
        this.location = location; // a remote with a null location should be randomly assigned a location
        this.maxBatteryPower = maxBatteryPower;
        this.sensors = (sensors != null) ? new ArrayList<>(sensors) : new ArrayList<>();
    }

    public RemoteSpec(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        this.remoteType = RemoteType.decodeType(json);
        if (json.hasKey(RemoteConst.LOCATION)) {
            this.location = new Vector(json.getJSONOption(RemoteConst.LOCATION));
        } else {
            this.location = null;
        }
        this.maxBatteryPower = json.getDouble(RemoteConst.MAX_BATTERY);
        this.sensors = new ArrayList<>();
        if (json.hasKey(RemoteConst.SENSORS)) {
            JSONArray jsonSensors = json.getJSONArray(RemoteConst.SENSORS);
            for (int i = 0; i < jsonSensors.length(); i++) {
                this.sensors.add(new SensorConfig(jsonSensors.getJSONOption(i)));
            }
        }
    }

    protected JSONObjectBuilder getJSONBuilder() {
        JSONObjectBuilder json = JSONBuilder.Object();
        json.put(RemoteConst.REMOTE_TYPE, this.remoteType.getType());
        if (this.hasLocation()) {
            json.put(RemoteConst.LOCATION, this.location.toJSON());
        }
        json.put(RemoteConst.MAX_BATTERY, this.maxBatteryPower);
        if (this.hasSensors()) {
            JSONArrayBuilder jsonSensors = JSONBuilder.Array();
            for (SensorConfig conf : this.sensors) {
                jsonSensors.put(conf.toJSON());
            }
            json.put(RemoteConst.SENSORS, jsonSensors.toJSON());
        }
        return json;
    }

    public String getLabel() {
        return String.format("r:%s:%s", this.remoteType.getLabel(), this.getSpecType().getLabel());
    }

    public Vector getLocation() {
        return this.location;
    }

    public double getMaxBatteryPower() {
        return this.maxBatteryPower;
    }

    public RemoteType getRemoteType() {
        return this.remoteType;
    }

    public SensorConfig getSensor(int idx) throws CoreException {
        if (idx < 0 || this.sensors.size() <= idx) {
            throw new CoreException(String.format("No sensor at index %d found on spec %s", idx, this.getLabel()));
        }
        return this.sensors.get(idx);
    }

    public ArrayList<SensorConfig> getSensors() {
        return this.sensors;
    }

    public SensorConfig getSensorWithID(String sensorID) throws CoreException {
        for (SensorConfig conf : this.sensors) {
            if (conf.hasSensorWithID(sensorID)) {
                return conf;
            }
        }
        throw new CoreException(String.format("No sensor with ID %s found on spec %s", sensorID, this.getLabel()));
    }

    public ArrayList<SensorConfig> getSensorsWithType(SensorType sensorType) {
        ArrayList<SensorConfig> confs = new ArrayList<>();
        for (SensorConfig conf : this.sensors) {
            if (conf.getSpecType().equals(sensorType)) {
                confs.add(conf);
            }
        }
        if (confs.isEmpty()) {
            Debugger.logger.warn(String.format("No sensors with type %s found on spec %s", sensorType.getLabel(),
                this.getLabel()));
        }
        return confs;
    }

    public SerializableEnum getSpecType() {
        return RemoteSpec.DEFAULT_REMOTE_TYPE;
    }

    public boolean hasLocation() {
        return this.location != null;
    }

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

    public boolean hasSensorWithType(SensorType sensorType) {
        for (SensorConfig conf : this.sensors) {
            if (conf.getSpecType().equals(sensorType)) {
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

    public JSONOption toJSON() {
        return this.getJSONBuilder().toJSON();
    }

    public boolean equals(RemoteSpec spec) {
        return this.remoteType.equals(spec.remoteType) && this.location.equals(spec.location) &&
            this.maxBatteryPower == spec.maxBatteryPower && this.sensors.equals(spec.sensors);
    }

}
