package com.seat.rescuesim.common.remote.kinetic;

import java.util.Collection;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.remote.RemoteConfig;

/** A serializable configuration of a Kinetic Remote. */
public class KineticRemoteConfig extends RemoteConfig {

    public KineticRemoteConfig(KineticRemoteSpec spec, int count, boolean dynamic) {
        super(spec, count, dynamic);
    }

    public KineticRemoteConfig(KineticRemoteSpec spec, Collection<String> remoteIDs, boolean dynamic) {
        super(spec, remoteIDs, dynamic);
    }

    public KineticRemoteConfig(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected KineticRemoteSpec decodeSpec(JSONOption jsonSpec) {
        return new KineticRemoteSpec(jsonSpec);
    }

    @Override
    public KineticRemoteSpec getSpec() {
        return (KineticRemoteSpec) this.spec;
    }

    @Override
    public KineticRemoteType getSpecType() {
        return (KineticRemoteType) this.spec.getSpecType();
    }

}
