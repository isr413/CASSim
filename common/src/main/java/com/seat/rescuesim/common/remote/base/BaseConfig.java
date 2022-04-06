package com.seat.rescuesim.common.remote.base;

import java.util.Collection;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.remote.RemoteConfig;
import com.seat.rescuesim.common.remote.RemoteType;

/** A serializable configuration of a Base Remote. */
public class BaseConfig extends RemoteConfig {

    public BaseConfig(BaseProto spec, int count, boolean dynamic) {
        super(spec, count, dynamic);
    }

    public BaseConfig(BaseProto spec, Collection<String> remoteIDs, boolean dynamic) {
        super(spec, remoteIDs, dynamic);
    }

    public BaseConfig(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected BaseProto decodeProto(JSONOption jsonSpec) throws JSONException {
        return new BaseProto(jsonSpec);
    }

    @Override
    public BaseProto getProto() {
        return (BaseProto) super.getProto();
    }

    public RemoteType getSpecType() {
        return this.getProto().getSpecType();
    }

}
