package com.seat.rescuesim.common.remote.base;

import java.util.Collection;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.remote.RemoteConfig;

/** A serializable configuration of a Base Remote. */
public class BaseConfig extends RemoteConfig {

    public BaseConfig(BaseProto proto, int count, boolean dynamic) {
        super(proto, count, dynamic);
    }

    public BaseConfig(BaseProto proto, Collection<String> remoteIDs, boolean dynamic) {
        super(proto, remoteIDs, dynamic);
    }

    public BaseConfig(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected BaseProto decodeProto(JSONOption option) throws JSONException {
        return new BaseProto(option);
    }

    @Override
    public BaseProto getProto() {
        return (BaseProto) super.getProto();
    }

}
