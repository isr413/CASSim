package com.seat.sim.common.remote.base;

import java.util.Collection;

import com.seat.sim.common.core.TeamColor;
import com.seat.sim.common.json.JSONException;
import com.seat.sim.common.json.JSONOption;
import com.seat.sim.common.remote.RemoteConfig;

/** A serializable configuration of a Base Remote. */
public class BaseRemoteConfig extends RemoteConfig {

    public BaseRemoteConfig(BaseRemoteProto proto, TeamColor team, int count, boolean active, boolean dynamic) {
        super(proto, team, count, active, dynamic);
    }

    public BaseRemoteConfig(BaseRemoteProto proto, TeamColor team, Collection<String> remoteIDs, boolean active,
            boolean dynamic) {
        super(proto, team, remoteIDs.size(), active, dynamic);
    }

    public BaseRemoteConfig(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    public BaseRemoteProto getProto() {
        return (BaseRemoteProto) super.getProto();
    }

}
