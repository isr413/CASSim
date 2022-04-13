package com.seat.sim.common.remote;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

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
    private ArrayList<SensorConfig> sensorConfigs;
    private HashMap<String, SensorConfig> sensorConfigByID;

    public RemoteProto(double maxBatteryPower) {
        this(null, maxBatteryPower, null);
    }

    public RemoteProto(Vector location, double maxBatteryPower) {
        this(location, maxBatteryPower, null);
    }

    public RemoteProto(double maxBatteryPower, Collection<SensorConfig> sensorConfigs) {
        this(null, maxBatteryPower, sensorConfigs);
    }

    public RemoteProto(Vector location, double maxBatteryPower, Collection<SensorConfig> sensorConfigs) {
        this.location = location; // a remote with a null location should be randomly assigned a location
        this.maxBatteryPower = maxBatteryPower;
        this.sensorConfigs = (sensorConfigs != null) ? new ArrayList<>(sensorConfigs) : new ArrayList<>();
        this.init();
    }

    public RemoteProto(JSONOption option) throws JSONException {
        super(option);
    }

    private void init() {
        this.sensorConfigByID = new HashMap<>();
        for (SensorConfig config : this.sensorConfigs) {
            for (String sensorID : config.getSensorIDs()) {
                this.sensorConfigByID.put(sensorID, config);
            }
        }
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        this.location = (json.hasKey(RemoteProto.LOCATION)) ?
            this.location = new Vector(json.getJSONOption(RemoteProto.LOCATION)) :
            null;
        this.maxBatteryPower = json.getDouble(RemoteProto.MAX_BATTERY);
        this.sensorConfigs = new ArrayList<>();
        if (json.hasKey(RemoteProto.SENSORS)) {
            JSONArray jsonSensors = json.getJSONArray(RemoteProto.SENSORS);
            for (int i = 0; i < jsonSensors.length(); i++) {
                this.sensorConfigs.add(SensorRegistry.decodeTo(jsonSensors.getJSONOption(i), SensorConfig.class));
            }
        }
        this.init();
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
            for (SensorConfig config : this.sensorConfigs) {
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

    public int getNumberOfSensors() {
        return this.getSensorIDs().size();
    }

    public String getRemoteType() {
        return this.getClass().getName();
    }

    public Collection<SensorConfig> getSensorConfigs() {
        return this.sensorConfigs;
    }

    public SensorConfig getSensorConfigWithID(String sensorID) throws CommonException {
        if (!this.hasSensorWithID(sensorID)) {
            throw new CommonException(String.format("Remote %s has no sensor %s", this.getLabel(), sensorID));
        }
        return this.sensorConfigByID.get(sensorID);
    }

    public Collection<String> getSensorIDs() {
        return this.sensorConfigByID.keySet();
    }

    public Collection<SensorConfig> getSensorsWithModel(String sensorModel) {
        ArrayList<SensorConfig> sensors = new ArrayList<>();
        for (SensorConfig state : this.sensorConfigs) {
            if (state.getSensorModel().equals(sensorModel)) {
                sensors.add(state);
            }
        }
        return sensors;
    }

    public Collection<SensorConfig> getSensorsWithType(Class<? extends SensorProto> classType) {
        ArrayList<SensorConfig> confs = new ArrayList<>();
        for (SensorConfig config : this.sensorConfigs) {
            if (classType.isAssignableFrom(config.getProto().getClass())) {
                confs.add(config);
            }
        }
        return confs;
    }

    public boolean hasLocation() {
        return this.location != null;
    }

    public boolean hasSensors() {
        return !this.getSensorIDs().isEmpty();
    }

    public boolean hasSensorWithID(String sensorID) {
        return this.sensorConfigByID.containsKey(sensorID);
    }

    public boolean hasSensorWithModel(String sensorModel) {
        for (SensorConfig config : this.sensorConfigs) {
            if (config.getSensorModel().equals(sensorModel)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasSensorWithType(Class<? extends SensorProto> classType) {
        for (SensorConfig config : this.sensorConfigs) {
            if (classType.isAssignableFrom(config.getProto().getClass())) {
                return true;
            }
        }
        return false;
    }

    public boolean isAerial() {
        return false;
    }

    public boolean isDisabled() {
        return !this.isEnabled();
    }

    public boolean isEnabled() {
        return this.maxBatteryPower > 0;
    }

    public boolean isGround() {
        return !this.isAerial();
    }

    public boolean isMobile() {
        return false;
    }

    public boolean isStationary() {
        return !this.isMobile();
    }

    public JSONOption toJSON() throws JSONException {
        return this.getJSONBuilder().toJSON();
    }

    public boolean equals(RemoteProto proto) {
        if (proto == null) return false;
        return this.getRemoteType().equals(proto.getRemoteType()) &&
            ((this.hasLocation() && this.location.equals(proto.location)) || this.location == proto.location) &&
            this.maxBatteryPower == proto.maxBatteryPower && this.sensorConfigs.equals(proto.sensorConfigs);
    }

}
