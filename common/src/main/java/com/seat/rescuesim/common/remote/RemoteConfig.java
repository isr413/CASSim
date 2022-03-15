package com.seat.rescuesim.common.remote;

import java.util.ArrayList;
import java.util.HashSet;

import com.seat.rescuesim.common.json.*;
import com.seat.rescuesim.common.util.Debugger;
import com.seat.rescuesim.common.util.SerializableEnum;

/** A serializable Remote configuration. */
public abstract class RemoteConfig extends JSONAble {
    private static final String COUNT = "count";
    private static final String IS_DYNAMIC = "is_dynamic";
    private static final String REMOTE_IDS = "remote_ids";
    private static final String REMOTE_TYPE = "remote_type";
    private static final String SPEC = "spec";

    public static RemoteType decodeRemoteType(JSONObject json) {
        return RemoteType.values()[json.getInt(RemoteConfig.REMOTE_TYPE)];
    }

    public static RemoteType decodeRemoteType(JSONOption option) {
        if (option.isSomeObject()) {
            return RemoteConfig.decodeRemoteType(option.someObject());
        }
        Debugger.logger.err(String.format("Cannot decode remote type of %s", option.toString()));
        return RemoteType.NONE;
    }

    public static RemoteType decodeRemoteType(String encoding) {
        return RemoteConfig.decodeRemoteType(JSONOption.String(encoding));
    }

    protected int count;
    protected boolean dynamic;
    protected HashSet<String> remoteIDs;
    protected RemoteSpec spec;
    protected RemoteType type;

    public RemoteConfig(JSONObject json) {
        super(json);
    }

    public RemoteConfig(JSONOption option) {
        super(option);
    }

    public RemoteConfig(String encoding) {
        super(encoding);
    }

    public RemoteConfig(RemoteType type, RemoteSpec spec, int count, boolean dynamic) {
        this.type = type;
        this.spec = spec;
        this.count = count;
        this.remoteIDs = new HashSet<>();
        this.dynamic = dynamic;
        for (int i = 0; i < count; i++) {
            this.remoteIDs.add(String.format("%s:(%d)", spec.getLabel(), i));
        }
    }

    public RemoteConfig(RemoteType type, RemoteSpec spec, ArrayList<String> remoteIDs, boolean dynamic) {
        this(type, spec, new HashSet<String>(remoteIDs), dynamic);
    }

    public RemoteConfig(RemoteType type, RemoteSpec spec, HashSet<String> remoteIDs, boolean dynamic) {
        this.type = type;
        this.spec = spec;
        this.count = remoteIDs.size();
        this.remoteIDs = remoteIDs;
        this.dynamic = dynamic;
    }

    @Override
    protected void decode(JSONObject json) {
        this.type = RemoteType.values()[json.getInt(RemoteConfig.REMOTE_TYPE)];
        this.decodeSpec(json.getJSONObject(RemoteConfig.SPEC));
        this.count = json.getInt(RemoteConfig.COUNT);
        this.remoteIDs = new HashSet<>();
        JSONArray jsonRemotes = json.getJSONArray(RemoteConfig.REMOTE_IDS);
        for (int i = 0; i < jsonRemotes.length(); i++) {
            this.remoteIDs.add(jsonRemotes.getString(i));
        }
        this.dynamic = json.getBoolean(RemoteConfig.IS_DYNAMIC);
    }

    protected abstract void decodeSpec(JSONObject jsonSpec);

    public int getCount() {
        return this.count;
    }

    public HashSet<String> getRemoteIDs() {
        return this.remoteIDs;
    }

    public RemoteType getRemoteType() {
        return this.type;
    }

    public RemoteSpec getSpec() {
        return this.spec;
    }

    public SerializableEnum getSpecType() {
        return this.spec.getSpecType();
    }

    public boolean hasRemotes() {
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
        json.put(RemoteConfig.REMOTE_TYPE, this.type.getType());
        json.put(RemoteConfig.SPEC, this.spec.toJSON());
        json.put(RemoteConfig.COUNT, this.count);
        JSONArrayBuilder jsonRemotes = JSONBuilder.Array();
        for (String remoteID : this.remoteIDs) {
            jsonRemotes.put(remoteID);
        }
        json.put(RemoteConfig.REMOTE_IDS, jsonRemotes.toJSON());
        json.put(RemoteConfig.IS_DYNAMIC, this.dynamic);
        return json.toJSON();
    }

    public boolean equals(RemoteConfig conf) {
        return this.type.equals(conf.type) && this.spec.equals(conf.spec) && this.count == conf.count &&
            this.remoteIDs.equals(conf.remoteIDs) && this.dynamic == conf.dynamic;
    }

}
