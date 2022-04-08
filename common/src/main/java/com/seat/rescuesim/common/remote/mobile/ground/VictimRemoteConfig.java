package com.seat.rescuesim.common.remote.mobile.ground;

import java.util.Collection;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.remote.mobile.MobileRemoteConfig;

/** A serializable configuration of a Victim Remote. */
public class VictimRemoteConfig extends MobileRemoteConfig {

    public VictimRemoteConfig(VictimRemoteProto proto, int count) {
        super(proto, count);
    }

    public VictimRemoteConfig(VictimRemoteProto proto, int count, boolean dynamic) {
        super(proto, count, dynamic);
    }

    public VictimRemoteConfig(VictimRemoteProto proto, int count, boolean dynamic, boolean active) {
        super(proto, count, dynamic, active);
    }

    public VictimRemoteConfig(VictimRemoteProto proto, Collection<String> remoteIDs) {
        super(proto, remoteIDs);
    }

    public VictimRemoteConfig(VictimRemoteProto proto, Collection<String> remoteIDs, boolean dynamic) {
        super(proto, remoteIDs, dynamic);
    }

    public VictimRemoteConfig(VictimRemoteProto proto, Collection<String> remoteIDs, boolean dynamic, boolean active) {
        super(proto, remoteIDs, dynamic, active);
    }

    public VictimRemoteConfig(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected VictimRemoteProto decodeProto(JSONOption option) throws JSONException {
        return new VictimRemoteProto(option);
    }

    @Override
    public VictimRemoteProto getProto() {
        return (VictimRemoteProto) super.getProto();
    }

}
