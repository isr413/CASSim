package com.seat.rescuesim.common.remote;

import java.util.Collection;
import java.util.HashSet;

import com.seat.rescuesim.common.json.JSONAble;
import com.seat.rescuesim.common.json.JSONArray;
import com.seat.rescuesim.common.json.JSONArrayBuilder;
import com.seat.rescuesim.common.json.JSONBuilder;
import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.JSONObjectBuilder;
import com.seat.rescuesim.common.json.JSONOption;

/** A serializable Remote configuration. */
public class RemoteConfig extends JSONAble {
    public static final String COUNT = "count";
    public static final String DYNAMIC = "dynamic";
    public static final String PROTO = "__proto__";
    public static final String REMOTE_IDS = "remote_ids";

    protected static final boolean DEFAULT_DYNAMIC = false;

    private int count;
    private boolean dynamic;
    private RemoteProto proto;
    private HashSet<String> remoteIDs;

    public RemoteConfig(RemoteProto proto, int count) {
        this(proto, count, RemoteConfig.DEFAULT_DYNAMIC);
    }

    public RemoteConfig(RemoteProto proto, int count, boolean dynamic) {
        this(proto, count, new HashSet<String>(), dynamic);
        for (int i = 0; i < count; i++) {
            this.remoteIDs.add(String.format("%s:(%d)", proto.getLabel(), i));
        }
    }

    public RemoteConfig(RemoteProto proto, Collection<String> remoteIDs) {
        this(proto, remoteIDs.size(), RemoteConfig.DEFAULT_DYNAMIC);
    }

    public RemoteConfig(RemoteProto proto, Collection<String> remoteIDs, boolean dynamic) {
        this(proto, remoteIDs.size(), new HashSet<String>(remoteIDs), dynamic);
    }

    private RemoteConfig(RemoteProto proto, int count, HashSet<String> remoteIDs, boolean dynamic) {
        this.proto = proto;
        this.count = remoteIDs.size();
        this.remoteIDs = remoteIDs;
        this.dynamic = dynamic;
    }

    public RemoteConfig(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        this.proto = this.decodeProto(json.getJSONOption(RemoteConfig.PROTO));
        this.count = json.getInt(RemoteConfig.COUNT);
        this.remoteIDs = new HashSet<>();
        if (json.hasKey(RemoteConfig.REMOTE_IDS)) {
            JSONArray jsonRemotes = json.getJSONArray(RemoteConfig.REMOTE_IDS);
            for (int i = 0; i < jsonRemotes.length(); i++) {
                this.remoteIDs.add(jsonRemotes.getString(i));
            }
        }
        this.dynamic = json.getBoolean(RemoteConfig.DYNAMIC);
    }

    protected RemoteProto decodeProto(JSONOption option) throws JSONException {
        return new RemoteProto(option);
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

    public HashSet<String> getRemoteIDs() {
        return this.remoteIDs;
    }

    public RemoteType getRemoteType() {
        return this.proto.getRemoteType();
    }

    public boolean hasProto() {
        return this.proto != null && !this.proto.getRemoteType().equals(RemoteType.NONE);
    }

    public boolean hasRemoteIDs() {
        return this.count > 0;
    }

    public boolean hasRemoteWithID(String remoteID) {
        return this.remoteIDs.contains(remoteID);
    }

    public boolean isDynamic() {
        return this.dynamic;
    }

    public boolean isPassive() {
        return !this.isDynamic();
    }

    public JSONOption toJSON() {
        JSONObjectBuilder json = JSONBuilder.Object();
        json.put(RemoteConfig.PROTO, this.proto.toJSON());
        json.put(RemoteConfig.COUNT, this.count);
        if (this.hasRemoteIDs()) {
            JSONArrayBuilder jsonRemotes = JSONBuilder.Array();
            for (String remoteID : this.remoteIDs) {
                jsonRemotes.put(remoteID);
            }
            json.put(RemoteConfig.REMOTE_IDS, jsonRemotes.toJSON());
        }
        json.put(RemoteConfig.DYNAMIC, this.dynamic);
        return json.toJSON();
    }

    public boolean equals(RemoteConfig config) {
        if (config == null) return false;
        return this.proto.equals(config.proto) && this.count == config.count &&
            this.remoteIDs.equals(config.remoteIDs) && this.dynamic == config.dynamic;
    }

}
