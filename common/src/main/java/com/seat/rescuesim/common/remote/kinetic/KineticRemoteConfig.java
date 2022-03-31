package com.seat.rescuesim.common.remote.kinetic;

import java.util.ArrayList;
import java.util.HashSet;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.remote.RemoteConfig;
import com.seat.rescuesim.common.remote.RemoteType;

/** A serializable configuration of a Kinetic Remote. */
public abstract class KineticRemoteConfig extends RemoteConfig {

    public KineticRemoteConfig(KineticRemoteSpec spec, int count, boolean dynamic) {
        super(RemoteType.KINETIC, spec, count, dynamic);
    }

    public KineticRemoteConfig(KineticRemoteSpec spec, ArrayList<String> remoteIDs, boolean dynamic) {
        super(RemoteType.KINETIC, spec, remoteIDs, dynamic);
    }

    public KineticRemoteConfig(KineticRemoteSpec spec, HashSet<String> remoteIDs, boolean dynamic) {
        super(RemoteType.KINETIC, spec, remoteIDs, dynamic);
    }

    public KineticRemoteConfig(JSONOption option) throws JSONException {
        super(option);
    }

    protected abstract void decodeSpec(JSONOption jsonSpec);

    public KineticRemoteSpec getSpec() {
        return (KineticRemoteSpec) this.spec;
    }

    public KineticRemoteType getSpecType() {
        return (KineticRemoteType) this.spec.getSpecType();
    }

}
