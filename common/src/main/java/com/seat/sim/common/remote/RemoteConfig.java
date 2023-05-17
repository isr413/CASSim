package com.seat.sim.common.remote;

import java.util.HashSet;
import java.util.Set;

import com.seat.sim.common.gui.TeamColor;
import com.seat.sim.common.json.*;

/** A serializable Remote configuration. */
public class RemoteConfig extends Jsonable {
  public static final String ACTIVE = "active";
  public static final String COUNT = "count";
  public static final String DYNAMIC = "dynamic";
  public static final String PROTO = "__proto__";
  public static final String REMOTE_IDS = "remote_ids";

  private boolean active;
  private boolean dynamic;
  private RemoteProto proto;
  private Set<String> remoteIDs;
  private TeamColor team;

  public RemoteConfig(RemoteProto proto, TeamColor team, int count, boolean active, boolean dynamic) {
    this(proto, team, new HashSet<>(), active, dynamic);
    this.init(count);
  }

  public RemoteConfig(RemoteProto proto, TeamColor team, Set<String> remoteIDs, boolean active, boolean dynamic) {
    this.proto = (proto != null) ? proto : new RemoteProto();
    this.team = (team != null) ? team : TeamColor.NONE;
    this.remoteIDs = (remoteIDs != null) ? new HashSet<>(remoteIDs) : new HashSet<>();
    this.active = active;
    this.dynamic = dynamic;
  }

  public RemoteConfig(Json json) throws JsonException {
    super(json);
  }

  private void init(int count) {
    for (int i = 0; i < count; i++) {
      this.remoteIDs.add(String.format("%s:(%d:%d)", proto.getLabel(), team.getType(), i));
    }
  }

  @Override
  protected void decode(JsonObject json) throws JsonException {
    this.proto = (json.hasKey(RemoteConfig.PROTO)) ? new RemoteProto(json.getJson(RemoteConfig.PROTO))
        : new RemoteProto();
    this.team = (json.hasKey(TeamColor.TEAM)) ? TeamColor.decodeType(json) : TeamColor.NONE;
    this.remoteIDs = (json.hasKey(RemoteConfig.REMOTE_IDS))
        ? new HashSet<>(json.getJsonArray(RemoteConfig.REMOTE_IDS).toList(String.class))
        : new HashSet<>();
    if (this.remoteIDs.isEmpty() && json.hasKey(RemoteConfig.COUNT)) {
      this.init(json.getInt(RemoteConfig.COUNT));
    }
    this.active = json.getBoolean(RemoteConfig.ACTIVE);
    this.dynamic = json.getBoolean(RemoteConfig.DYNAMIC);
  }

  protected JsonObjectBuilder getJsonBuilder() throws JsonException {
    JsonObjectBuilder json = JsonBuilder.Object();
    json.put(RemoteConfig.PROTO, this.proto.toJson());
    if (this.hasTeam()) {
      json.put(TeamColor.TEAM, this.team.getType());
    }
    if (this.getCount() > 0) {
      json.put(RemoteConfig.COUNT, this.getCount());
    }
    if (this.hasRemoteIDs()) {
      json.put(RemoteConfig.REMOTE_IDS, JsonBuilder.toJsonArray(this.remoteIDs));
    }
    json.put(RemoteConfig.ACTIVE, this.active);
    json.put(RemoteConfig.DYNAMIC, this.dynamic);
    return json;
  }

  public int getCount() {
    return this.remoteIDs.size();
  }

  public String getLabel() {
    return this.proto.getLabel();
  }

  public RemoteProto getProto() {
    return this.proto;
  }

  public Set<String> getRemoteIDs() {
    return this.remoteIDs;
  }

  public TeamColor getTeam() {
    return this.team;
  }

  public boolean hasRemoteIDs() {
    return !this.remoteIDs.isEmpty();
  }

  public boolean hasRemoteMatch(Set<String> matchers) {
    return this.proto.hasRemoteMatch(matchers);
  }

  public boolean hasRemoteWithID(String remoteID) {
    return this.remoteIDs.contains(remoteID);
  }

  public boolean hasRemoteWithTag(String tag) {
    return this.proto.hasTag(tag);
  }

  public boolean hasTeam() {
    return !this.team.equals(TeamColor.NONE);
  }

  public boolean isActive() {
    return this.active;
  }

  public boolean isDynamic() {
    return this.dynamic;
  }

  public Json toJson() throws JsonException {
    return this.getJsonBuilder().toJson();
  }
}
