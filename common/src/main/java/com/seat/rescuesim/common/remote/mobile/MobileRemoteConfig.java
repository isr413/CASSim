package com.seat.rescuesim.common.remote.mobile;

import java.util.Collection;

import com.seat.rescuesim.common.core.TeamCode;
import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.remote.RemoteConfig;

/** A serializable configuration of a Mobile Remote. */
public class MobileRemoteConfig extends RemoteConfig {

    public MobileRemoteConfig(MobileRemoteProto proto, int count, boolean dynamic, boolean active) {
        super(proto, count, dynamic, active);
    }

    public MobileRemoteConfig(MobileRemoteProto proto, int count, boolean dynamic, boolean active, TeamCode team) {
        super(proto, count, dynamic, active, team);
    }

    public MobileRemoteConfig(MobileRemoteProto proto, Collection<String> remoteIDs, boolean dynamic, boolean active) {
        super(proto, remoteIDs, dynamic, active);
    }

    public MobileRemoteConfig(MobileRemoteProto proto, Collection<String> remoteIDs, boolean dynamic, boolean active,
            TeamCode team) {
        super(proto, remoteIDs, dynamic, active, team);
    }

    public MobileRemoteConfig(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    public MobileRemoteProto getProto() {
        return (MobileRemoteProto) super.getProto();
    }

}
