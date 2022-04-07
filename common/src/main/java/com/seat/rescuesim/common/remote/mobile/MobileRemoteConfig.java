package com.seat.rescuesim.common.remote.mobile;

import java.util.Collection;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.remote.RemoteConfig;

/** A serializable configuration of a Kinetic Remote. */
public class MobileRemoteConfig extends RemoteConfig {

    public MobileRemoteConfig(MobileRemoteProto proto, int count, boolean dynamic) {
        super(proto, count, dynamic);
    }

    public MobileRemoteConfig(MobileRemoteProto proto, Collection<String> remoteIDs, boolean dynamic) {
        super(proto, remoteIDs, dynamic);
    }

    public MobileRemoteConfig(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected MobileRemoteProto decodeProto(JSONOption option) throws JSONException {
        return new MobileRemoteProto(option);
    }

    @Override
    public MobileRemoteProto getProto() {
        return (MobileRemoteProto) super.getProto();
    }

}
