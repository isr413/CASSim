package com.seat.sim.common.remote;

import java.util.Collection;
import java.util.HashSet;

import com.seat.sim.common.core.TeamColor;
import com.seat.sim.common.json.JSONAble;
import com.seat.sim.common.json.JSONArray;
import com.seat.sim.common.json.JSONArrayBuilder;
import com.seat.sim.common.json.JSONBuilder;
import com.seat.sim.common.json.JSONException;
import com.seat.sim.common.json.JSONObject;
import com.seat.sim.common.json.JSONObjectBuilder;
import com.seat.sim.common.json.JSONOption;

/** A serializable Remote configuration. */
public class RemoteConfig extends JSONAble {
    public static final String COUNT = "count";
    public static final String DYNAMIC = "dynamic";
    public static final String PROTO = "__proto__";
    public static final String REMOTE_IDS = "remote_ids";

    protected static final TeamColor DEFAULT_TEAM_COLOR = TeamColor.NONE;

    private boolean active;
    private int count;
    private boolean dynamic;
    private RemoteProto proto;
    private HashSet<String> remoteIDs;
    private TeamColor team;

    public RemoteConfig(RemoteProto proto, int count, boolean dynamic, boolean active) {
        this(proto, RemoteConfig.DEFAULT_TEAM_COLOR, count, dynamic, active);
    }

    public RemoteConfig(RemoteProto proto, TeamColor team, int count, boolean dynamic, boolean active) {
        this(proto, team, count, new HashSet<String>(), dynamic, active);
        for (int i = 0; i < count; i++) {
            this.remoteIDs.add(String.format("%s:(%d:%d)", proto.getLabel(), team.getType(), i));
        }
    }

    public RemoteConfig(RemoteProto proto, Collection<String> remoteIDs, boolean dynamic, boolean active) {
        this(proto, RemoteConfig.DEFAULT_TEAM_COLOR, remoteIDs.size(), new HashSet<String>(remoteIDs), dynamic, active);
    }

    public RemoteConfig(RemoteProto proto, TeamColor team, Collection<String> remoteIDs, boolean dynamic,
            boolean active) {
        this(proto, team, remoteIDs.size(), new HashSet<String>(remoteIDs), dynamic, active);
    }

    private RemoteConfig(RemoteProto proto, TeamColor team, int count, HashSet<String> remoteIDs, boolean dynamic,
            boolean active) {
        this.proto = proto;
        this.team = team;
        this.count = count;
        this.remoteIDs = remoteIDs;
        this.dynamic = dynamic;
        this.active = active;
    }

    public RemoteConfig(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        this.proto = RemoteRegistry.decodeTo(json.getJSONOption(RemoteConfig.PROTO), RemoteProto.class);
        this.team = (json.hasKey(TeamColor.TEAM)) ?
            TeamColor.decodeType(json) :
            RemoteConfig.DEFAULT_TEAM_COLOR;
        this.count = json.getInt(RemoteConfig.COUNT);
        this.remoteIDs = new HashSet<>();
        if (json.hasKey(RemoteConfig.REMOTE_IDS)) {
            JSONArray jsonRemotes = json.getJSONArray(RemoteConfig.REMOTE_IDS);
            for (int i = 0; i < jsonRemotes.length(); i++) {
                this.remoteIDs.add(jsonRemotes.getString(i));
            }
        }
        this.dynamic = json.getBoolean(RemoteConfig.DYNAMIC);
        this.active = json.getBoolean(RemoteState.ACTIVE);
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

    public JSONOption toJSON() throws JSONException {
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
        json.put(RemoteConfig.DYNAMIC, this.dynamic);
        json.put(RemoteState.ACTIVE, this.active);
        return json.toJSON();
    }

    public boolean equals(RemoteConfig config) {
        if (config == null) return false;
        return this.getRemoteType().equals(config.getRemoteType()) && this.proto.equals(config.proto) &&
            this.team.equals(config.team) && this.count == config.count && this.remoteIDs.equals(config.remoteIDs) &&
            this.dynamic == config.dynamic && this.active == config.active;
    }

}
