package com.seat.sim.common.remote;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.seat.sim.common.json.*;
import com.seat.sim.common.math.Vector;
import com.seat.sim.common.remote.kinematics.Kinematics;
import com.seat.sim.common.sensor.SensorConfig;

/** A serializable prototype of a Remote. */
public class RemoteProto extends Jsonable {
    public static final String KINEMATICS = "kinematics";
    public static final String REMOTE_GROUPS = "remote_groups";
    public static final String SENSORS = "sensors";

    private Kinematics kinematics;
    private Set<String> remoteGroups;
    private Map<String, SensorConfig> sensorConfigByID;
    private List<SensorConfig> sensorConfigs;

    public RemoteProto(Set<String> remoteGroups, Kinematics kinematics, Collection<SensorConfig> sensorConfigs) {
        this.remoteGroups = (remoteGroups != null) ? new HashSet<>(remoteGroups) : new HashSet<>();
        this.kinematics = (kinematics != null) ? kinematics : new Kinematics(null, null, null, null);
        this.sensorConfigs = (sensorConfigs != null) ? new ArrayList<>(sensorConfigs) : new ArrayList<>();
        this.init();
    }

    public RemoteProto(Json json) throws JsonException {
        super(json);
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
    protected void decode(JsonObject json) throws JsonException {
        this.remoteGroups = (json.hasKey(RemoteProto.REMOTE_GROUPS)) ?
            new HashSet<>(json.getJsonArray(RemoteProto.REMOTE_GROUPS).toList(String.class)) :
            new HashSet<>();
        this.kinematics = (json.hasKey(RemoteProto.KINEMATICS)) ?
            new Kinematics(json.getJson(RemoteProto.KINEMATICS)) :
            new Kinematics(null, null, null, null);
        this.sensorConfigs = (json.hasKey(RemoteProto.SENSORS)) ?
            this.sensorConfigs = json.getJsonArray(RemoteProto.SENSORS).toList(Json.class).stream()
                .map(config -> new SensorConfig(config))
                .collect(Collectors.toList()) :
            new ArrayList<>();
        this.init();
    }

    protected JsonObjectBuilder getJsonBuilder() throws JsonException {
        JsonObjectBuilder json = JsonBuilder.Object();
        if (this.hasRemoteGroups()) {
            json.put(RemoteProto.REMOTE_GROUPS, JsonBuilder.toJsonArray(this.remoteGroups));
        }
        if (this.hasKinematics()) {
            json.put(RemoteProto.KINEMATICS, this.kinematics.toJson());
        }
        if (this.hasSensors()) {
            json.put(
                RemoteProto.SENSORS,
                JsonBuilder.toJsonArray(
                    this.sensorConfigs.stream()
                        .map(config -> config.toJson())
                        .collect(Collectors.toList())
                )
            );
        }
        return json;
    }

    public boolean equals(RemoteProto proto) {
        if (proto == null) return false;
        return this.remoteGroups.equals(proto.remoteGroups) && this.kinematics.equals(proto.kinematics) && 
            this.sensorConfigs.equals(proto.sensorConfigs);
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

    public Set<String> getRemoteGroups() {
        return this.remoteGroups;
    }

    public Collection<SensorConfig> getSensorConfigs() {
        return this.sensorConfigs;
    }

    public Collection<SensorConfig> getSensorConfigsWithModel(String sensorModel) {
        return this.sensorConfigs.stream()
            .filter(config -> config.hasSensorWithModel(sensorModel))
            .collect(Collectors.toList());
    }

    public Collection<SensorConfig> getSensorConfigsWithTag(String groupTag) {
        return this.sensorConfigs.stream()
            .filter(config -> config.hasSensorWithTag(groupTag))
            .collect(Collectors.toList());
    }

    public SensorConfig getSensorConfigWithID(String sensorID) {
        return this.sensorConfigByID.get(sensorID);
    }

    public Set<String> getSensorIDs() {
        return this.sensorConfigByID.keySet();
    }

    public boolean hasKinematics() {
        return this.kinematics != null;
    }

    public boolean hasLocation() {
        return this.kinematics.hasLocation();
    }

    public boolean hasRemoteGroups() {
        return !this.remoteGroups.isEmpty();
    }

    public boolean hasRemoteGroupWithTag(String groupTag) {
        return this.remoteGroups.contains(groupTag);
    }

    public boolean hasSensorConfigWithID(String sensorID) {
        return this.sensorConfigByID.containsKey(sensorID);
    }

    public boolean hasSensorConfigWithModel(String sensorModel) {
        return !this.getSensorConfigsWithModel(sensorModel).isEmpty();
    }

    public boolean hasSensorConfigWithTag(String groupTag) {
        return !this.getSensorConfigsWithTag(groupTag).isEmpty();
    }

    public boolean hasSensors() {
        return !this.getSensorIDs().isEmpty();
    }

    public boolean isEnabled() {
        return this.kinematics.isEnabled();
    }

    public boolean isMobile() {
        return this.kinematics.hasMotion();
    }

    public Json toJson() throws JsonException {
        return this.getJsonBuilder().toJson();
    }
}
