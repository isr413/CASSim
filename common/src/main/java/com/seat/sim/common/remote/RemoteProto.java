package com.seat.sim.common.remote;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.seat.sim.common.core.CommonException;
import com.seat.sim.common.json.JSONAble;
import com.seat.sim.common.json.JSONArray;
import com.seat.sim.common.json.JSONArrayBuilder;
import com.seat.sim.common.json.JSONBuilder;
import com.seat.sim.common.json.JSONException;
import com.seat.sim.common.json.JSONObject;
import com.seat.sim.common.json.JSONObjectBuilder;
import com.seat.sim.common.json.JSONOptional;
import com.seat.sim.common.math.Vector;
import com.seat.sim.common.sensor.SensorConfig;
import com.seat.sim.common.sensor.SensorRegistry;

/** A serializable prototype of a Remote. */
public class RemoteProto extends JSONAble {
    public static final String LOCATION = "location";
    public static final String MAX_BATTERY = "max_battery";
    public static final String SENSORS = "sensors";

    private Vector location;
    private double maxBatteryPower;
    private Map<String, SensorConfig> sensorConfigByID;
    private List<SensorConfig> sensorConfigs;

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

    public RemoteProto(JSONOptional optional) throws JSONException {
        super(optional);
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
            this.location = new Vector(json.getJSONOptional(RemoteProto.LOCATION)) :
            null;
        this.maxBatteryPower = json.getDouble(RemoteProto.MAX_BATTERY);
        this.sensorConfigs = new ArrayList<>();
        if (json.hasKey(RemoteProto.SENSORS)) {
            JSONArray jsonSensorConfigs = json.getJSONArray(RemoteProto.SENSORS);
            for (int i = 0; i < jsonSensorConfigs.length(); i++) {
                this.sensorConfigs.add(SensorRegistry.decodeTo(jsonSensorConfigs.getJSONOptional(i), SensorConfig.class));
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
            JSONArrayBuilder jsonSensorConfigs = JSONBuilder.Array();
            for (SensorConfig config : this.sensorConfigs) {
                jsonSensorConfigs.put(config.toJSON());
            }
            json.put(RemoteProto.SENSORS, jsonSensorConfigs.toJSON());
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

    public Collection<SensorConfig> getSensorConfigsWithModel(String sensorModel) {
        return this.sensorConfigs.stream()
            .filter(config -> config.getSensorModel().equals(sensorModel))
            .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    public <T extends SensorConfig> Collection<T> getSensorConfigsWithType(Class<? extends T> classType) {
        return this.sensorConfigs.stream()
            .filter(config -> classType.isAssignableFrom(config.getClass()))
            .map(config -> (T) config)
            .collect(Collectors.toList());
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

    public boolean hasLocation() {
        return this.location != null;
    }

    public boolean hasSensorConfigWithModel(String sensorModel) {
        return !this.getSensorConfigsWithModel(sensorModel).isEmpty();
    }

    public boolean hasSensorConfigWithType(Class<? extends SensorConfig> classType) {
        return !this.getSensorConfigsWithType(classType).isEmpty();
    }

    public boolean hasSensors() {
        return !this.getSensorIDs().isEmpty();
    }

    public boolean hasSensorWithID(String sensorID) {
        return this.sensorConfigByID.containsKey(sensorID);
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

    public JSONOptional toJSON() throws JSONException {
        return this.getJSONBuilder().toJSON();
    }

    public boolean equals(RemoteProto proto) {
        if (proto == null) return false;
        return this.getRemoteType().equals(proto.getRemoteType()) &&
            ((this.hasLocation() && this.location.equals(proto.location)) || this.location == proto.location) &&
            this.maxBatteryPower == proto.maxBatteryPower && this.sensorConfigs.equals(proto.sensorConfigs);
    }

}
