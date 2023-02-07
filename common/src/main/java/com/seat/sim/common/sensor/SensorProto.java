package com.seat.sim.common.sensor;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.seat.sim.common.json.*;

/** A serializable prototype of a generic Sensor. */
public class SensorProto extends Jsonable {
  public static final String MATCHERS = "matchers";
  public static final String MODEL = "model";
  public static final String STATS = "stats";
  public static final String TAGS = "tags";

  protected static final String DEFAULT_MODEL = "default";

  private Set<String> matchers;
  private String model;
  private Optional<SensorStats> stats;
  private Set<String> tags;

  public SensorProto() {
    this(SensorProto.DEFAULT_MODEL, null, null, null);
  }

  public SensorProto(String model, Set<String> tags, Set<String> matchers, SensorStats stats) {
    this.model = (model != null && !model.isBlank()) ? model : SensorProto.DEFAULT_MODEL;
    this.tags = (tags != null) ? new HashSet<>(tags) : new HashSet<>();
    this.matchers = (matchers != null) ? new HashSet<>(matchers) : new HashSet<>();
    this.stats = (stats != null) ? Optional.of(stats) : Optional.empty();
  }

  public SensorProto(Json json) throws JsonException {
    super(json);
  }

  @Override
    protected void decode(JsonObject json) throws JsonException {
        this.model = (json.hasKey(SensorProto.MODEL))
            ? json.getString(SensorProto.MODEL)
            : SensorProto.DEFAULT_MODEL;
        this.tags = (json.hasKey(SensorProto.TAGS))
            ? new HashSet<>(json.getJsonArray(SensorProto.TAGS).toList(String.class))
            : new HashSet<>();
        this.matchers = (json.hasKey(SensorProto.MATCHERS))
            ? new HashSet<>(json.getJsonArray(SensorProto.MATCHERS).toList(String.class))
            : new HashSet<>();
        this.stats = (json.hasKey(SensorProto.STATS))
            ? Optional.of(new SensorStats(json.getJson(SensorProto.STATS)))
            : Optional.empty();
    }

  protected JsonObjectBuilder getJsonBuilder() throws JsonException {
    JsonObjectBuilder json = JsonBuilder.Object();
    json.put(SensorProto.MODEL, this.model);
    if (this.hasTags()) {
      json.put(SensorProto.TAGS, JsonBuilder.toJsonArray(this.tags));
    }
    if (this.hasMatchers()) {
      json.put(SensorProto.MATCHERS, JsonBuilder.toJsonArray(this.matchers));
    }
    if (this.hasStats()) {
      json.put(SensorProto.STATS, this.getStats().toJson());
    }
    return json;
  }

  public String getLabel() {
    return String.format("s:<%s>", this.model);
  }

  public Set<String> getMatchers() {
    return this.matchers;
  }

  public String getModel() {
    return this.model;
  }

  public SensorStats getStats() {
    return this.stats.get();
  }

  public Set<String> getTags() {
    return this.tags;
  }

  public boolean hasMatch(Set<String> matchers) {
    for (String matcher : matchers) {
      if (this.hasTag(matcher)) {
        return true;
      }
    }
    return false;
  }

  public boolean hasMatcher(String matcher) {
    return this.matchers.contains(matcher);
  }

  public boolean hasMatchers() {
    return !this.matchers.isEmpty();
  }

  public boolean hasModel(String model) {
    if (model == null || this.model == null) {
      return this.model == model;
    }
    return this.model.equals(model);
  }

  public boolean hasTag(String tag) {
    return this.tags.contains(tag);
  }

  public boolean hasStats() {
    return this.stats.isPresent();
  }

  public boolean hasTags() {
    return !this.tags.isEmpty();
  }

  public Json toJson() throws JsonException {
    return this.getJsonBuilder().toJson();
  }
}
