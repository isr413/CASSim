package com.seat.rescuesim.common.remote.stat.base;

import java.util.Collection;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.remote.stat.StaticRemoteConfig;

/** A serializable configuration of a Base Remote. */
public class BaseConfig extends StaticRemoteConfig {

    public BaseConfig(BaseSpec spec, int count, boolean dynamic) {
        super(spec, count, dynamic);
    }

    public BaseConfig(BaseSpec spec, Collection<String> remoteIDs, boolean dynamic) {
        super(spec, remoteIDs, dynamic);
    }

    public BaseConfig(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected BaseSpec decodeProto(JSONOption jsonSpec) throws JSONException {
        return new BaseSpec(jsonSpec);
    }

    @Override
    public BaseSpec getProto() {
        return (BaseSpec) super.getProto();
    }

}
