package com.seat.sim.common.remote;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.seat.sim.common.core.CommonException;
import com.seat.sim.common.json.JSONAble;
import com.seat.sim.common.json.JSONBuilder;
import com.seat.sim.common.json.JSONException;
import com.seat.sim.common.json.JSONObject;
import com.seat.sim.common.json.JSONObjectBuilder;
import com.seat.sim.common.json.JSONOptional;
import com.seat.sim.common.math.Vector;
import com.seat.sim.common.remote.kinematics.Kinematics;
import com.seat.sim.common.sensor.SensorConfig;
import com.seat.sim.common.sensor.SensorRegistry;

/** A serializable prototype of a Remote. */
public class RemoteProto extends JSONAble {
    public static final String KINEMATICS = "kinematics";
    public static final String SENSORS = "sensors";

    private Kinematics kinematics;
    private Map<String, SensorConfig> sensorConfigByID;
    private List<SensorConfig> sensorConfigs;

    public RemoteProto() {
        this(null, null);
    }

    public RemoteProto(Kinematics kinematics) {
        this(kinematics, null);
    }

    public RemoteProto(Kinematics kinematics, Collection<SensorConfig> sensorConfigs) {
        this.kinematics = (kinematics != null) ? kinematics : new Kinematics();
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
        this.kinematics = (json.hasKey(RemoteProto.KINEMATICS)) ?
            new Kinematics(json.getJSONOptional(RemoteProto.KINEMATICS)) :
            new Kinematics();
        this.sensorConfigs = (json.hasKey(RemoteProto.SENSORS)) ?
            this.sensorConfigs = json.getJSONArray(RemoteProto.SENSORS).toList(JSONOptional.class).stream()
                .map(optional -> SensorRegistry.decodeTo(SensorConfig.class, optional))
                .collect(Collectors.toList()) :
            new ArrayList<>();
        this.init();
    }

    protected JSONObjectBuilder getJSONBuilder() throws JSONException {
        JSONObjectBuilder json = JSONBuilder.Object();
        json.put(RemoteRegistry.REMOTE_TYPE, this.getRemoteType());
        json.put(RemoteProto.KINEMATICS, this.kinematics.toJSON());
        if (this.hasSensors()) {
            json.put(
                RemoteProto.SENSORS,
                JSONBuilder.Array(
                    this.sensorConfigs.stream()
                        .map(config -> config.toJSON())
                        .collect(Collectors.toList())
                ).toJSON()
            );
        }
        return json;
    }

    public Kinematics getKinematics() {
        return this.kinematics;
    }

    public String getLabel() {
        return "r:<remote>";
    }

    public Vector getLocation() {
        return this.kinematics.getLocation();
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
    public <T extends SensorConfig> Collection<T> getSensorConfigsWithType(Class<T> cls) {
        return this.sensorConfigs.stream()
            .filter(config -> cls.isAssignableFrom(config.getClass()))
            .map(config -> (T) config)
            .collect(Collectors.toList());
    }

    public SensorConfig getSensorConfigWithID(String sensorID) throws CommonException {
        if (!this.hasSensorWithID(sensorID)) {
            throw new CommonException(String.format("Remote %s has no sensor %s", this.getLabel(), sensorID));
        }
        return this.sensorConfigByID.get(sensorID);
    }

    public Set<String> getSensorIDs() {
        return this.sensorConfigByID.keySet();
    }

    public boolean hasLocation() {
        return this.kinematics.hasLocation();
    }

    public boolean hasSensorConfigWithModel(String sensorModel) {
        return !this.getSensorConfigsWithModel(sensorModel).isEmpty();
    }

    public boolean hasSensorConfigWithType(Class<? extends SensorConfig> cls) {
        return !this.getSensorConfigsWithType(cls).isEmpty();
    }

    public boolean hasSensors() {
        return !this.getSensorIDs().isEmpty();
    }

    public boolean hasSensorWithID(String sensorID) {
        return this.sensorConfigByID.containsKey(sensorID);
    }

    public boolean isEnabled() {
        return this.kinematics.isEnabled();
    }

    public boolean isMobile() {
        return this.kinematics.hasMotion();
    }

    public JSONOptional toJSON() throws JSONException {
        return this.getJSONBuilder().toJSON();
    }

    public boolean equals(RemoteProto proto) {
        if (proto == null) return false;
        return this.getRemoteType().equals(proto.getRemoteType()) && this.kinematics.equals(proto.kinematics) &&
            this.sensorConfigs.equals(proto.sensorConfigs);
    }

}
