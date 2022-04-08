package com.seat.rescuesim.common.remote.base;

import java.util.Collection;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.remote.RemoteConfig;

/** A serializable configuration of a Base Remote. */
public class BaseRemoteConfig extends RemoteConfig {

    public BaseRemoteConfig(BaseRemoteProto proto, int count) {
        super(proto, count);
    }

    public BaseRemoteConfig(BaseRemoteProto proto, int count, boolean dynamic) {
        super(proto, count, dynamic);
    }

    public BaseRemoteConfig(BaseRemoteProto proto, int count, boolean dynamic, boolean active) {
        super(proto, count, dynamic, active);
    }

    public BaseRemoteConfig(BaseRemoteProto proto, Collection<String> remoteIDs) {
        super(proto, remoteIDs);
    }

    public BaseRemoteConfig(BaseRemoteProto proto, Collection<String> remoteIDs, boolean dynamic) {
        super(proto, remoteIDs, dynamic);
    }

    public BaseRemoteConfig(BaseRemoteProto proto, Collection<String> remoteIDs, boolean dynamic, boolean active) {
        super(proto, remoteIDs, dynamic, active);
    }

    public BaseRemoteConfig(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected BaseRemoteProto decodeProto(JSONOption option) throws JSONException {
        return new BaseRemoteProto(option);
    }

    @Override
    public BaseRemoteProto getProto() {
        return (BaseRemoteProto) super.getProto();
    }

}
