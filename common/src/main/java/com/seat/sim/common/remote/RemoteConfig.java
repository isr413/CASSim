package com.seat.sim.common.remote;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.seat.sim.common.core.TeamColor;
import com.seat.sim.common.json.*;

/** A serializable Remote configuration. */
public class RemoteConfig extends Jsonable {
    public static final String COUNT = "count";
    public static final String DYNAMIC = "dynamic";
    public static final String PROTO = "__proto__";
    public static final String REMOTE_IDS = "remote_ids";

    private boolean active;
    private int count;
    private boolean dynamic;
    private RemoteProto proto;
    private Set<String> remoteIDs;
    private TeamColor team;

    public RemoteConfig(RemoteProto proto, TeamColor team, int count, boolean active, boolean dynamic) {
        this(proto, team, count, new HashSet<String>(), active, dynamic);
        for (int i = 0; i < count; i++) {
            this.remoteIDs.add(String.format("%s:(%d:%d)", proto.getLabel(), team.getType(), i));
        }
    }

    public RemoteConfig(RemoteProto proto, TeamColor team, Collection<String> remoteIDs, boolean active,
            boolean dynamic) {
        this(proto, team, remoteIDs.size(), new HashSet<String>(remoteIDs), active, dynamic);
    }

    private RemoteConfig(RemoteProto proto, TeamColor team, int count, HashSet<String> remoteIDs, boolean active,
            boolean dynamic) {
        this.proto = (proto != null) ? proto : new RemoteProto();
        this.team = (team != null) ? team : TeamColor.NONE;
        this.count = count;
        this.remoteIDs = (remoteIDs != null) ? remoteIDs : new HashSet<>();
        this.active = active;
        this.dynamic = dynamic;
    }

    public RemoteConfig(Json json) throws JsonException {
        super(json);
    }

    @Override
    protected void decode(JsonObject json) throws JsonException {
        this.proto = RemoteRegistry.decodeTo(RemoteProto.class, json.getJson(RemoteConfig.PROTO));
        this.team = (json.hasKey(TeamColor.TEAM)) ?
            TeamColor.decodeType(json) :
            TeamColor.NONE;
        this.count = json.getInt(RemoteConfig.COUNT);
        this.remoteIDs = (json.hasKey(RemoteConfig.REMOTE_IDS)) ?
            json.getJsonArray(RemoteConfig.REMOTE_IDS).toList(String.class).stream().collect(Collectors.toSet()) :
            new HashSet<>();
        this.active = json.getBoolean(RemoteState.ACTIVE);
        this.dynamic = json.getBoolean(RemoteConfig.DYNAMIC);
    }

    protected JsonObjectBuilder getJsonBuilder() throws JsonException {
        JsonObjectBuilder json = JsonBuilder.Object();
        json.put(RemoteRegistry.REMOTE_TYPE, this.getRemoteType());
        json.put(RemoteConfig.PROTO, this.proto.toJson());
        if (this.hasTeam()) {
            json.put(TeamColor.TEAM, this.team.getType());
        }
        json.put(RemoteConfig.COUNT, this.count);
        if (this.hasRemoteIDs()) {
            json.put(RemoteConfig.REMOTE_IDS, JsonBuilder.toJsonArray(this.remoteIDs));
        }
        json.put(RemoteState.ACTIVE, this.active);
        json.put(RemoteConfig.DYNAMIC, this.dynamic);
        return json;
    }

    public boolean equals(RemoteConfig config) {
        if (config == null) return false;
        return this.getRemoteType().equals(config.getRemoteType()) && this.proto.equals(config.proto) &&
            this.team.equals(config.team) && this.count == config.count && this.remoteIDs.equals(config.remoteIDs) &&
            this.active == config.active && this.dynamic == config.dynamic;
    }

    public int getCount() {
        return this.count;
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

    public String getRemoteType() {
        return this.getClass().getName();
    }

    public TeamColor getTeam() {
        return this.team;
    }

    public boolean hasProto() {
        return this.proto != null;
    }

    public boolean hasRemoteIDs() {
        return this.count > 0;
    }

    public boolean hasRemoteWithID(String remoteID) {
        return this.remoteIDs.contains(remoteID);
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
