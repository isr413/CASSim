package com.seat.rescuesim.common.remote.mobile.ground;

import java.util.Collection;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.remote.mobile.MobileRemoteConfig;

/** A serializable configuration of a Victim Remote. */
public class VictimConfig extends MobileRemoteConfig {

    public VictimConfig(VictimProto proto, int count, boolean dynamic) {
        super(proto, count, dynamic);
    }

    public VictimConfig(VictimProto proto, Collection<String> remoteIDs, boolean dynamic) {
        super(proto, remoteIDs, dynamic);
    }

    public VictimConfig(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected VictimProto decodeProto(JSONOption option) throws JSONException {
        return new VictimProto(option);
    }

    @Override
    public VictimProto getProto() {
        return (VictimProto) super.getProto();
    }

}
