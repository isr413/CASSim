package com.seat.rescuesim.common.remote;

import java.util.ArrayList;
import java.util.HashSet;

import com.seat.rescuesim.common.json.*;
import com.seat.rescuesim.common.util.Debugger;
import com.seat.rescuesim.common.util.SerializableEnum;

/** Serializable Remote configurations. */
public abstract class RemoteConf extends JSONAble {
    private static final String COUNT = "count";
    private static final String IS_DYNAMIC = "is_dynamic";
    private static final String REMOTE_IDS = "remote_ids";
    private static final String REMOTE_TYPE = "remote_type";
    private static final String SPEC = "spec";

    public static RemoteType decodeRemoteType(JSONObject json) {
        return RemoteType.values()[json.getInt(RemoteConf.REMOTE_TYPE)];
    }

    public static RemoteType decodeRemoteType(JSONOption option) {
        if (option.isSomeObject()) {
            return RemoteConf.decodeRemoteType(option.someObject());
        }
        Debugger.logger.err(String.format("Cannot decode remote type of %s", option.toString()));
        return RemoteType.NONE;
    }

    public static RemoteType decodeRemoteType(String encoding) {
        return RemoteConf.decodeRemoteType(JSONOption.String(encoding));
    }

    protected int count;
    protected boolean dynamic;
    protected HashSet<String> remoteIDs;
    protected RemoteSpec spec;
    protected RemoteType type;

    public RemoteConf(JSONObject json) {
        super(json);
    }

    public RemoteConf(JSONOption option) {
        super(option);
    }

    public RemoteConf(String encoding) {
        super(encoding);
    }

    public RemoteConf(RemoteType type, RemoteSpec spec, int count, boolean dynamic) {
        this.type = type;
        this.spec = spec;
        this.count = count;
        this.remoteIDs = new HashSet<>();
        this.dynamic = dynamic;
        for (int i = 0; i < count; i++) {
            this.remoteIDs.add(String.format("%s:(%d)", spec.getLabel(), i));
        }
    }

    public RemoteConf(RemoteType type, RemoteSpec spec, ArrayList<String> remoteIDs, boolean dynamic) {
        this(type, spec, new HashSet<String>(remoteIDs), dynamic);
    }

    public RemoteConf(RemoteType type, RemoteSpec spec, HashSet<String> remoteIDs, boolean dynamic) {
        this.type = type;
        this.spec = spec;
        this.count = remoteIDs.size();
        this.remoteIDs = remoteIDs;
        this.dynamic = dynamic;
    }

    @Override
    protected void decode(JSONObject json) {
        this.type = RemoteType.values()[json.getInt(RemoteConf.REMOTE_TYPE)];
        this.decodeSpecification(json.getJSONObject(RemoteConf.SPEC));
        this.count = json.getInt(RemoteConf.COUNT);
        JSONArray jsonRemotes = json.getJSONArray(RemoteConf.REMOTE_IDS);
        this.remoteIDs = new HashSet<>();
        for (int i = 0; i < jsonRemotes.length(); i++) {
            this.remoteIDs.add(jsonRemotes.getString(i));
        }
        this.dynamic = json.getBoolean(RemoteConf.IS_DYNAMIC);
    }

    protected abstract void decodeSpecification(JSONObject jsonSpec);

    public int getCount() {
        return this.count;
    }

    public HashSet<String> getRemoteIDs() {
        return this.remoteIDs;
    }

    public RemoteSpec getSpecification() {
        return this.spec;
    }

    public RemoteType getRemoteType() {
        return this.type;
    }

    public SerializableEnum getSpecType() {
        return this.spec.getSpecType();
    }

    public boolean hasRemoteWithID(String remoteID) {
        return this.remoteIDs.contains(remoteID);
    }

    public boolean hasRemotes() {
        return this.count > 0;
    }

    public boolean isDynamic() {
        return this.dynamic;
    }

    public boolean isPassive() {
        return !this.isDynamic();
    }

    public JSONOption toJSON() {
        JSONObjectBuilder json = JSONBuilder.Object();
        json.put(RemoteConf.REMOTE_TYPE, this.type.getType());
        json.put(RemoteConf.SPEC, this.spec.toJSON());
        json.put(RemoteConf.COUNT, this.count);
        JSONArrayBuilder jsonRemotes = JSONBuilder.Array();
        for (String remoteID : this.remoteIDs) {
            jsonRemotes.put(remoteID);
        }
        json.put(RemoteConf.REMOTE_IDS, jsonRemotes.toJSON());
        json.put(RemoteConf.IS_DYNAMIC, this.dynamic);
        return json.toJSON();
    }

    public boolean equals(RemoteConf conf) {
        return this.type.equals(conf.type) && this.spec.equals(conf.spec) && this.count == conf.count &&
            this.remoteIDs.equals(conf.remoteIDs) && this.dynamic == conf.dynamic;
    }

}
