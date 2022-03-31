package com.seat.rescuesim.common.remote.stat;

import java.util.ArrayList;
import java.util.HashSet;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.remote.RemoteConfig;
import com.seat.rescuesim.common.remote.RemoteType;

/** A serializable configuration of a Static Remote. */
public abstract class StaticRemoteConfig extends RemoteConfig {

    public StaticRemoteConfig(StaticRemoteSpec spec, int count, boolean dynamic) {
        super(RemoteType.STATIC, spec, count, dynamic);
    }

    public StaticRemoteConfig(StaticRemoteSpec spec, ArrayList<String> remoteIDs, boolean dynamic) {
        super(RemoteType.STATIC, spec, remoteIDs, dynamic);
    }

    public StaticRemoteConfig(StaticRemoteSpec spec, HashSet<String> remoteIDs, boolean dynamic) {
        super(RemoteType.STATIC, spec, remoteIDs, dynamic);
    }

    public StaticRemoteConfig(JSONOption option) throws JSONException {
        super(option);
    }

    protected abstract void decodeSpec(JSONOption jsonSpec);

    public StaticRemoteSpec getSpec() {
        return (StaticRemoteSpec) this.spec;
    }

    public StaticRemoteType getSpecType() {
        return (StaticRemoteType) this.spec.getSpecType();
    }

}
