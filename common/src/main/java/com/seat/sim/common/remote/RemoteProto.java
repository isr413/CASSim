package com.seat.sim.common.remote;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.seat.sim.common.json.*;
import com.seat.sim.common.remote.kinematics.KinematicsProto;
import com.seat.sim.common.sensor.SensorConfig;

/** A serializable prototype of a Remote. */
public class RemoteProto extends Jsonable {
  public static final String KINEMATICS_PROTO = "kinematics_proto";
  public static final String SENSOR_COUNT = "sensor_count";
  public static final String SENSORS = "sensors";
  public static final String TAGS = "tags";

  private Optional<KinematicsProto> kinematicsProto;
  private List<SensorConfig> sensorConfigs;
  private Set<String> tags;

  public RemoteProto() {
    this(null, null, null);
  }

  public RemoteProto(Set<String> tags, Collection<SensorConfig> sensorConfigs, KinematicsProto kinematicsProto) {
    this.tags = (tags != null) ? new HashSet<>(tags) : new HashSet<>();
    this.sensorConfigs = (sensorConfigs != null) ? new ArrayList<>(sensorConfigs) : new ArrayList<>();
    this.kinematicsProto = (kinematicsProto != null) ? Optional.of(kinematicsProto) : Optional.empty();
  }

  public RemoteProto(Json json) throws JsonException {
    super(json);
  }

  @Override
  protected void decode(JsonObject json) throws JsonException {
    this.tags = (json.hasKey(RemoteProto.TAGS))
        ? new HashSet<>(json.getJsonArray(RemoteProto.TAGS).toList(String.class))
        : new HashSet<>();
    this.sensorConfigs = (json.hasKey(RemoteProto.SENSORS))
        ? this.sensorConfigs = json.getJsonArray(RemoteProto.SENSORS)
            .toList(Json.class)
            .stream()
            .map(config -> new SensorConfig(config))
            .collect(Collectors.toList())
        : new ArrayList<>();
    this.kinematicsProto = (json.hasKey(RemoteProto.KINEMATICS_PROTO))
        ? Optional.of(new KinematicsProto(json.getJson(RemoteProto.KINEMATICS_PROTO)))
        : Optional.empty();
  }

  protected JsonObjectBuilder getJsonBuilder() throws JsonException {
    JsonObjectBuilder json = JsonBuilder.Object();
    if (this.hasTags()) {
      json.put(RemoteProto.TAGS, JsonBuilder.toJsonArray(this.tags));
    }
    json.put(RemoteProto.SENSOR_COUNT, this.getSensorCount());
    if (this.hasSensors()) {
      json.put(
          RemoteProto.SENSORS,
          JsonBuilder.toJsonArray(
              this.sensorConfigs
                  .stream()
                  .map(config -> config.toJson())
                  .collect(Collectors.toList())));
    }
    if (this.hasKinematicsProto()) {
      json.put(RemoteProto.KINEMATICS_PROTO, this.getKinematicsProto().toJson());
    }
    return json;
  }

  public KinematicsProto getKinematicsProto() {
    return this.kinematicsProto.get();
  }

  public String getLabel() {
    return "r:<remote>";
  }

  public Collection<SensorConfig> getSensorConfigs() {
    return this.sensorConfigs;
  }

  public int getSensorCount() {
    return this.sensorConfigs
        .stream()
        .mapToInt(config -> config.getCount())
        .sum();
  }

  public Set<String> getTags() {
    return this.tags;
  }

  public boolean hasKinematicsProto() {
    return this.kinematicsProto.isPresent();
  }

  public boolean hasRemoteMatch(Set<String> matchers) {
    for (String matcher : matchers) {
      if (this.hasTag(matcher)) {
        return true;
      }
    }
    return false;
  }

  public boolean hasSensorMatch(Set<String> matchers) {
    for (SensorConfig config : this.sensorConfigs) {
      if (config.hasSensorMatch(matchers)) {
        return true;
      }
    }
    return false;
  }

  public boolean hasSensors() {
    return !this.sensorConfigs.isEmpty();
  }

  public boolean hasTag(String tag) {
    return this.tags.contains(tag);
  }

  public boolean hasTags() {
    return !this.tags.isEmpty();
  }

  public boolean isEnabled() {
    return !this.hasKinematicsProto() || this.getKinematicsProto().isEnabled();
  }

  public boolean isMobile() {
    return this.hasKinematicsProto() && this.getKinematicsProto().isMobile();
  }

  public Json toJson() throws JsonException {
    return this.getJsonBuilder().toJson();
  }
}
