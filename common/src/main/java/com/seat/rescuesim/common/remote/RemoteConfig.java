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
import com.seat.rescuesim.common.json.SerializableEnum;

/** A serializable Remote configuration. */
public class RemoteConfig extends JSONAble {

    protected int count;
    protected boolean dynamic;
    protected HashSet<String> remoteIDs;
    protected RemoteSpec spec;

    public RemoteConfig(RemoteSpec spec, int count, boolean dynamic) {
        this.spec = spec;
        this.count = count;
        this.remoteIDs = new HashSet<>();
        for (int i = 0; i < count; i++) {
            this.remoteIDs.add(String.format("%s:(%d)", spec.getLabel(), i));
        }
        this.dynamic = dynamic;
    }

    public RemoteConfig(RemoteSpec spec, Collection<String> remoteIDs, boolean dynamic) {
        this.spec = spec;
        this.count = remoteIDs.size();
        this.remoteIDs = new HashSet<>(remoteIDs);
        this.dynamic = dynamic;
    }

    public RemoteConfig(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        this.spec = this.decodeSpec(json.getJSONOption(RemoteConst.SPEC));
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

    protected RemoteSpec decodeSpec(JSONOption jsonSpec) throws JSONException {
        return new RemoteSpec(jsonSpec);
    }

    public int getCount() {
        return this.count;
    }

    public String getLabel() {
        return this.spec.getLabel();
    }

    public HashSet<String> getRemoteIDs() {
        return this.remoteIDs;
    }

    public RemoteType getRemoteType() {
        return this.spec.getRemoteType();
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

    public boolean hasSpec() {
        return this.spec != null && !this.spec.getRemoteType().equals(RemoteType.NONE);
    }

    public boolean isDynamic() {
        return this.dynamic;
    }

    public boolean isPassive() {
        return !this.isDynamic();
    }

    public JSONOption toJSON() {
        JSONObjectBuilder json = JSONBuilder.Object();
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
        return this.spec.equals(config.spec) && this.count == config.count && this.remoteIDs.equals(config.remoteIDs) &&
            this.dynamic == config.dynamic;
    }

}
