package com.seat.sim.common.remote;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.seat.sim.common.core.TeamColor;
import com.seat.sim.common.json.JSONAble;
import com.seat.sim.common.json.JSONArray;
import com.seat.sim.common.json.JSONArrayBuilder;
import com.seat.sim.common.json.JSONBuilder;
import com.seat.sim.common.json.JSONException;
import com.seat.sim.common.json.JSONObject;
import com.seat.sim.common.json.JSONObjectBuilder;
import com.seat.sim.common.json.JSONOptional;

/** A serializable Remote configuration. */
public class RemoteConfig extends JSONAble {
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
        this.proto = proto;
        this.team = team;
        this.count = count;
        this.remoteIDs = remoteIDs;
        this.active = active;
        this.dynamic = dynamic;
    }

    public RemoteConfig(JSONOptional optional) throws JSONException {
        super(optional);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        this.proto = RemoteRegistry.decodeTo(json.getJSONOption(RemoteConfig.PROTO), RemoteProto.class);
        this.team = (json.hasKey(TeamColor.TEAM)) ?
            TeamColor.decodeType(json) :
            TeamColor.NONE;
        this.count = json.getInt(RemoteConfig.COUNT);
        this.remoteIDs = new HashSet<>();
        if (json.hasKey(RemoteConfig.REMOTE_IDS)) {
            JSONArray jsonRemotes = json.getJSONArray(RemoteConfig.REMOTE_IDS);
            for (int i = 0; i < jsonRemotes.length(); i++) {
                this.remoteIDs.add(jsonRemotes.getString(i));
            }
        }
        this.active = json.getBoolean(RemoteState.ACTIVE);
        this.dynamic = json.getBoolean(RemoteConfig.DYNAMIC);
    }

    protected JSONObjectBuilder getJSONBuilder() throws JSONException {
        JSONObjectBuilder json = JSONBuilder.Object();
        json.put(RemoteRegistry.REMOTE_TYPE, this.getRemoteType());
        json.put(RemoteConfig.PROTO, this.proto.toJSON());
        if (this.hasTeam()) {
            json.put(TeamColor.TEAM, this.team.getType());
        }
        json.put(RemoteConfig.COUNT, this.count);
        if (this.hasRemoteIDs()) {
            JSONArrayBuilder jsonRemotes = JSONBuilder.Array();
            for (String remoteID : this.remoteIDs) {
                jsonRemotes.put(remoteID);
            }
            json.put(RemoteConfig.REMOTE_IDS, jsonRemotes.toJSON());
        }
        json.put(RemoteState.ACTIVE, this.active);
        json.put(RemoteConfig.DYNAMIC, this.dynamic);
        return json;
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

    public Collection<String> getRemoteIDs() {
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
        return !(this.team == null || this.team.equals(TeamColor.NONE));
    }

    public boolean isActive() {
        return this.active;
    }

    public boolean isDynamic() {
        return this.dynamic;
    }

    public boolean isInactive() {
        return !this.isActive();
    }

    public boolean isPassive() {
        return !this.isDynamic();
    }

    public JSONOptional toJSON() throws JSONException {
        return this.getJSONBuilder().toJSON();
    }

    public boolean equals(RemoteConfig config) {
        if (config == null) return false;
        return this.getRemoteType().equals(config.getRemoteType()) && this.proto.equals(config.proto) &&
            this.team.equals(config.team) && this.count == config.count && this.remoteIDs.equals(config.remoteIDs) &&
            this.active == config.active && this.dynamic == config.dynamic;
    }

}
