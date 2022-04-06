package com.seat.rescuesim.common.remote.kinetic;

import java.util.Collection;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.remote.RemoteConfig;

/** A serializable configuration of a Kinetic Remote. */
public class KineticRemoteConfig extends RemoteConfig {

    public KineticRemoteConfig(KineticRemoteProto proto, int count, boolean dynamic) {
        super(proto, count, dynamic);
    }

    public KineticRemoteConfig(KineticRemoteProto proto, Collection<String> remoteIDs, boolean dynamic) {
        super(proto, remoteIDs, dynamic);
    }

    public KineticRemoteConfig(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected KineticRemoteProto decodeProto(JSONOption option) {
        return new KineticRemoteProto(option);
    }

    @Override
    public KineticRemoteProto getProto() {
        return (KineticRemoteProto) super.getProto();
    }

    @Override
    public KineticRemoteType getSpecType() {
        return (KineticRemoteType) super.getSpecType();
    }

}
