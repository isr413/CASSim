package com.seat.sim.common.remote;

import java.util.ArrayList;
import java.util.Collection;

import com.seat.sim.common.core.CommonException;
import com.seat.sim.common.json.JSONAble;
import com.seat.sim.common.json.JSONArray;
import com.seat.sim.common.json.JSONArrayBuilder;
import com.seat.sim.common.json.JSONBuilder;
import com.seat.sim.common.json.JSONException;
import com.seat.sim.common.json.JSONObject;
import com.seat.sim.common.json.JSONObjectBuilder;
import com.seat.sim.common.json.JSONOption;
import com.seat.sim.common.math.Vector;
import com.seat.sim.common.sensor.SensorConfig;
import com.seat.sim.common.sensor.SensorProto;
import com.seat.sim.common.sensor.SensorRegistry;

/** A serializable prototype of a Remote. */
public class RemoteProto extends JSONAble {
    public static final String LOCATION = "location";
    public static final String MAX_BATTERY = "max_battery";
    public static final String SENSORS = "sensors";

    private Vector location;
    private double maxBatteryPower;
    private ArrayList<SensorConfig> sensors;

    public RemoteProto(double maxBatteryPower) {
        this(null, maxBatteryPower, null);
    }

    public RemoteProto(Vector location, double maxBatteryPower) {
        this(location, maxBatteryPower, null);
    }

    public RemoteProto(double maxBatteryPower, Collection<SensorConfig> sensors) {
        this(null, maxBatteryPower, sensors);
    }

    public RemoteProto(Vector location, double maxBatteryPower,
            Collection<SensorConfig> sensors) {
        this.location = location; // a remote with a null location should be randomly assigned a location
        this.maxBatteryPower = maxBatteryPower;
        this.sensors = (sensors != null) ? new ArrayList<>(sensors) : new ArrayList<>();
    }

    public RemoteProto(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        this.location = (json.hasKey(RemoteProto.LOCATION)) ?
            this.location = new Vector(json.getJSONOption(RemoteProto.LOCATION)) :
            null;
        this.maxBatteryPower = json.getDouble(RemoteProto.MAX_BATTERY);
        this.sensors = new ArrayList<>();
        if (json.hasKey(RemoteProto.SENSORS)) {
            JSONArray jsonSensors = json.getJSONArray(RemoteProto.SENSORS);
            for (int i = 0; i < jsonSensors.length(); i++) {
                this.sensors.add(SensorRegistry.decodeTo(jsonSensors.getJSONOption(i), SensorConfig.class));
            }
        }
    }

    protected JSONObjectBuilder getJSONBuilder() throws JSONException {
        JSONObjectBuilder json = JSONBuilder.Object();
        json.put(RemoteRegistry.REMOTE_TYPE, this.getRemoteType());
        if (this.hasLocation()) {
            json.put(RemoteProto.LOCATION, this.location.toJSON());
        }
        json.put(RemoteProto.MAX_BATTERY, this.maxBatteryPower);
        if (this.hasSensors()) {
            JSONArrayBuilder jsonSensors = JSONBuilder.Array();
            for (SensorConfig config : this.sensors) {
                jsonSensors.put(config.toJSON());
            }
            json.put(RemoteProto.SENSORS, jsonSensors.toJSON());
        }
        return json;
    }

    public String getLabel() {
        return "r:<remote>";
    }

    public Vector getLocation() {
        return this.location;
    }

    public double getMaxBatteryPower() {
        return this.maxBatteryPower;
    }

    public String getRemoteType() {
        return this.getClass().getName();
    }

    public SensorConfig getSensor(int idx) throws CommonException {
        if (idx < 0 || this.sensors.size() <= idx) {
            throw new CommonException(String.format("No sensor at index %d found on proto %s", idx, this.getLabel()));
        }
        return this.sensors.get(idx);
    }

    public Collection<SensorConfig> getSensors() {
        return this.sensors;
    }

    public Collection<SensorConfig> getSensorsWithModel(String sensorModel) {
        ArrayList<SensorConfig> sensors = new ArrayList<>();
        for (SensorConfig state : this.sensors) {
            if (state.getSensorModel().equals(sensorModel)) {
                sensors.add(state);
            }
        }
        return sensors;
    }

    public Collection<SensorConfig> getSensorsWithType(Class<? extends SensorProto> classType) {
        ArrayList<SensorConfig> confs = new ArrayList<>();
        for (SensorConfig config : this.sensors) {
            if (classType.isAssignableFrom(config.getProto().getClass())) {
                confs.add(config);
            }
        }
        return confs;
    }

    public SensorConfig getSensorWithID(String sensorID) throws CommonException {
        for (SensorConfig config : this.sensors) {
            if (config.hasSensorWithID(sensorID)) {
                return config;
            }
        }
        throw new CommonException(String.format("No sensor with ID %s found on remote %s", sensorID, this.getLabel()));
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
        for (SensorConfig config : this.sensors) {
            if (config.hasSensorWithID(sensorID)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasSensorWithModel(String sensorModel) {
        for (SensorConfig config : this.sensors) {
            if (config.getSensorModel().equals(sensorModel)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasSensorWithType(Class<? extends SensorProto> classType) {
        for (SensorConfig config : this.sensors) {
            if (classType.isAssignableFrom(config.getProto().getClass())) {
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

    public JSONOption toJSON() throws JSONException {
        return this.getJSONBuilder().toJSON();
    }

    public boolean equals(RemoteProto proto) {
        if (proto == null) return false;
        return this.getRemoteType().equals(proto.getRemoteType()) &&
            ((this.hasLocation() && this.location.equals(proto.location)) || this.location == proto.location) &&
            this.maxBatteryPower == proto.maxBatteryPower && this.sensors.equals(proto.sensors);
    }

}
