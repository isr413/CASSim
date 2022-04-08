package com.seat.rescuesim.common.remote.mobile;

import java.util.Collection;

import com.seat.rescuesim.common.core.TeamColor;
import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.remote.RemoteConfig;

/** A serializable configuration of a Mobile Remote. */
public class MobileRemoteConfig extends RemoteConfig {

    public MobileRemoteConfig(MobileRemoteProto proto, int count, boolean dynamic, boolean active) {
        super(proto, count, dynamic, active);
    }

    public MobileRemoteConfig(MobileRemoteProto proto, TeamColor team, int count, boolean dynamic, boolean active) {
        super(proto, team, count, dynamic, active);
    }

    public MobileRemoteConfig(MobileRemoteProto proto, Collection<String> remoteIDs, boolean dynamic, boolean active) {
        super(proto, remoteIDs, dynamic, active);
    }

    public MobileRemoteConfig(MobileRemoteProto proto, TeamColor team, Collection<String> remoteIDs, boolean dynamic,
            boolean active) {
        super(proto, team, remoteIDs, dynamic, active);
    }

    public MobileRemoteConfig(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    public MobileRemoteProto getProto() {
        return (MobileRemoteProto) super.getProto();
    }

}
