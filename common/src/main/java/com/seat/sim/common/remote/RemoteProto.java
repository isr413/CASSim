package com.seat.sim.common.remote;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

    private Optional<Kinematics> kinematics;
    private Set<String> remoteGroups;
    private Map<String, SensorConfig> sensorConfigByID;
    private List<SensorConfig> sensorConfigs;

    public RemoteProto() {
        this(null, null, null);
    }

    public RemoteProto(Set<String> remoteGroups) {
        this(remoteGroups, null, null);
    }

    public RemoteProto(Set<String> remoteGroups, Kinematics kinematics) {
        this(remoteGroups, kinematics, null);
    }

    public RemoteProto(Collection<SensorConfig> sensorConfigs) {
        this(null, null, sensorConfigs);
    }

    public RemoteProto(Set<String> remoteGroups, Collection<SensorConfig> sensorConfigs) {
        this(remoteGroups, null, sensorConfigs);
    }

    public RemoteProto(Kinematics kinematics) {
        this(null, kinematics, null);
    }

    public RemoteProto(Kinematics kinematics, Collection<SensorConfig> sensorConfigs) {
        this(null, kinematics, sensorConfigs);
    }

    public RemoteProto(Set<String> remoteGroups, Kinematics kinematics, Collection<SensorConfig> sensorConfigs) {
        this.remoteGroups = (remoteGroups != null) ? new HashSet<>(remoteGroups) : new HashSet<>();
        this.kinematics = (kinematics != null) ? Optional.of(kinematics)
                : Optional.empty();
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
        this.remoteGroups = (json.hasKey(RemoteProto.REMOTE_GROUPS))
                ? new HashSet<>(json.getJsonArray(RemoteProto.REMOTE_GROUPS).toList(String.class))
                : new HashSet<>();
        this.kinematics = (json.hasKey(RemoteProto.KINEMATICS))
                ? Optional.of(new Kinematics(json.getJson(RemoteProto.KINEMATICS)))
                : Optional.empty();
        this.sensorConfigs = (json.hasKey(RemoteProto.SENSORS))
                ? this.sensorConfigs = json.getJsonArray(RemoteProto.SENSORS).toList(Json.class).stream()
                        .map(config -> new SensorConfig(config))
                        .collect(Collectors.toList())
                : new ArrayList<>();
        this.init();
    }

    protected JsonObjectBuilder getJsonBuilder() throws JsonException {
        JsonObjectBuilder json = JsonBuilder.Object();
        if (this.hasRemoteGroups()) {
            json.put(RemoteProto.REMOTE_GROUPS, JsonBuilder.toJsonArray(this.remoteGroups));
        }
        if (this.hasKinematics()) {
            json.put(RemoteProto.KINEMATICS, this.getKinematics().toJson());
        }
        if (this.hasSensors()) {
            json.put(
                    RemoteProto.SENSORS,
                    JsonBuilder.toJsonArray(
                            this.sensorConfigs.stream()
                                    .map(config -> config.toJson())
                                    .collect(Collectors.toList())));
        }
        return json;
    }

    public boolean equals(RemoteProto proto) {
        if (proto == null)
            return false;
        return this.remoteGroups.equals(proto.remoteGroups) && this.kinematics.equals(proto.kinematics) &&
                this.sensorConfigs.equals(proto.sensorConfigs);
    }

    public double getInitialFuel() {
        return this.getKinematics().getInitialFuel();
    }

    public double getInitialVelocity() {
        return this.getKinematics().getInitialVelocity();
    }

    public Kinematics getKinematics() {
        return this.kinematics.get();
    }

    public String getLabel() {
        return "r:<remote>";
    }

    public Vector getLocation() {
        return this.getKinematics().getLocation();
    }

    public double getMaxAcceleration() {
        return this.getKinematics().getMaxAcceleration();
    }

    public double getMaxFuel() {
        return this.getKinematics().getMaxFuel();
    }

    public double getMaxVelocity() {
        return this.getKinematics().getMaxVelocity();
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

    public boolean hasFuel() {
        return this.hasKinematics() && this.getKinematics().hasFuel();
    }

    public boolean hasKinematics() {
        return this.kinematics.isPresent();
    }

    public boolean hasLocation() {
        return this.hasKinematics() && this.getKinematics().hasLocation();
    }

    public boolean hasMaxAcceleration() {
        return this.hasKinematics() && this.getKinematics().hasMaxAcceleration();
    }

    public boolean hasMaxFuel() {
        return this.hasKinematics() && this.getKinematics().hasMaxFuel();
    }

    public boolean hasMaxVelocity() {
        return this.hasKinematics() && this.getKinematics().hasMaxVelocity();
    }

    public boolean hasRemoteGroups() {
        return !this.remoteGroups.isEmpty();
    }

    public boolean hasRemoteGroupWithTag(String groupTag) {
        return this.remoteGroups.contains(groupTag);
    }

    public boolean hasRemoteMatch(Set<String> matchers) {
        for (String matcher : matchers) {
            if (this.hasRemoteGroupWithTag(matcher))
                return true;
        }
        return false;
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

    public boolean hasSensorMatch(Set<String> matchers) {
        for (String matcher : matchers) {
            if (this.hasSensorConfigWithTag(matcher))
                return true;
        }
        return false;
    }

    public boolean hasSensors() {
        return !this.getSensorIDs().isEmpty();
    }

    public boolean isEnabled() {
        return !this.hasKinematics() || this.getKinematics().isEnabled();
    }

    public boolean isMobile() {
        return this.hasKinematics() && this.getKinematics().isMobile();
    }

    public Json toJson() throws JsonException {
        return this.getJsonBuilder().toJson();
    }
}
