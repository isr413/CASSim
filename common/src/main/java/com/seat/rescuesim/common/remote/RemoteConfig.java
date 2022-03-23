package com.seat.rescuesim.common.remote;

import java.util.ArrayList;
import java.util.HashSet;

import com.seat.rescuesim.common.json.*;
import com.seat.rescuesim.common.util.SerializableEnum;

/** A serializable Remote configuration. */
public abstract class RemoteConfig extends JSONAble {

    protected int count;
    protected boolean dynamic;
    protected HashSet<String> remoteIDs;
    protected RemoteSpec spec;
    protected RemoteType type;

    public RemoteConfig(JSONObject json) throws JSONException {
        super(json);
    }

    public RemoteConfig(JSONOption option) throws JSONException {
        super(option);
    }

    public RemoteConfig(String encoding) throws JSONException {
        super(encoding);
    }

    public RemoteConfig(RemoteType type, RemoteSpec spec, int count, boolean dynamic) {
        this(type, spec, new HashSet<String>(), dynamic);
        this.count = count;
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
    protected void decode(JSONObject json) throws JSONException {
        this.type = RemoteType.values()[json.getInt(RemoteConst.REMOTE_TYPE)];
        this.decodeSpec(json.getJSONObject(RemoteConst.SPEC));
        this.count = json.getInt(RemoteConst.COUNT);
        this.remoteIDs = new HashSet<>();
        if (json.hasKey(RemoteConst.REMOTE_IDS)) {
            JSONArray jsonRemotes = json.getJSONArray(RemoteConst.REMOTE_IDS);
            for (int i = 0; i < jsonRemotes.length(); i++) {
                this.remoteIDs.add(jsonRemotes.getString(i));
            }
        }
        this.dynamic = json.getBoolean(RemoteConst.DYNAMIC);
    }

    protected abstract void decodeSpec(JSONObject jsonSpec) throws JSONException;

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
        json.put(RemoteConst.REMOTE_TYPE, this.type.getType());
        json.put(RemoteConst.SPEC, this.spec.toJSON());
        json.put(RemoteConst.COUNT, this.count);
        if (this.hasRemoteIDs()) {
            JSONArrayBuilder jsonRemotes = JSONBuilder.Array();
            for (String remoteID : this.remoteIDs) {
                jsonRemotes.put(remoteID);
            }
            json.put(RemoteConst.REMOTE_IDS, jsonRemotes.toJSON());
        }
        json.put(RemoteConst.DYNAMIC, this.dynamic);
        return json.toJSON();
    }

    public boolean equals(RemoteConfig config) {
        return this.type.equals(config.type) && this.spec.equals(config.spec) && this.count == config.count &&
            this.remoteIDs.equals(config.remoteIDs) && this.dynamic == config.dynamic;
    }

}
