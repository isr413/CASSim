package com.seat.rescuesim.common.remote.stat;

import java.util.Collection;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.remote.RemoteConfig;

/** A serializable configuration of a Static Remote. */
public class StaticRemoteConfig extends RemoteConfig {

    public StaticRemoteConfig(StaticRemoteSpec spec, int count, boolean dynamic) {
        super(spec, count, dynamic);
    }

    public StaticRemoteConfig(StaticRemoteSpec spec, Collection<String> remoteIDs, boolean dynamic) {
        super(spec, remoteIDs, dynamic);
    }

    public StaticRemoteConfig(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected StaticRemoteSpec decodeSpec(JSONOption jsonSpec) {
        return new StaticRemoteSpec(jsonSpec);
    }

    @Override
    public StaticRemoteSpec getSpec() {
        return (StaticRemoteSpec) super.getSpec();
    }

    @Override
    public StaticRemoteType getSpecType() {
        return (StaticRemoteType) super.getSpecType();
    }

}
