package com.seat.rescuesim.common.remote.mobile.ground;

import java.util.Collection;

import com.seat.rescuesim.common.core.TeamCode;
import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.remote.mobile.MobileRemoteConfig;

/** A serializable configuration of a Victim Remote. */
public class VictimRemoteConfig extends MobileRemoteConfig {

    public VictimRemoteConfig(VictimRemoteProto proto, int count, boolean dynamic, boolean active) {
        super(proto, count, dynamic, active);
    }

    public VictimRemoteConfig(VictimRemoteProto proto, int count, boolean dynamic, boolean active, TeamCode team) {
        super(proto, count, dynamic, active, team);
    }

    public VictimRemoteConfig(VictimRemoteProto proto, Collection<String> remoteIDs, boolean dynamic, boolean active) {
        super(proto, remoteIDs, dynamic, active);
    }

    public VictimRemoteConfig(VictimRemoteProto proto, Collection<String> remoteIDs, boolean dynamic, boolean active,
            TeamCode team) {
        super(proto, remoteIDs, dynamic, active, team);
    }

    public VictimRemoteConfig(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    public VictimRemoteProto getProto() {
        return (VictimRemoteProto) super.getProto();
    }

}
