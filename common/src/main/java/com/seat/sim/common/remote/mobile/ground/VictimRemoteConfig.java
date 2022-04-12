package com.seat.sim.common.remote.mobile.ground;

import java.util.Collection;

import com.seat.sim.common.core.TeamColor;
import com.seat.sim.common.json.JSONException;
import com.seat.sim.common.json.JSONOption;
import com.seat.sim.common.remote.mobile.MobileRemoteConfig;

/** A serializable configuration of a Victim Remote. */
public class VictimRemoteConfig extends MobileRemoteConfig {

    public VictimRemoteConfig(VictimRemoteProto proto, int count, boolean dynamic, boolean active) {
        super(proto, count, dynamic, active);
    }

    public VictimRemoteConfig(VictimRemoteProto proto, TeamColor team, int count, boolean dynamic, boolean active) {
        super(proto, team, count, dynamic, active);
    }

    public VictimRemoteConfig(VictimRemoteProto proto, Collection<String> remoteIDs, boolean dynamic, boolean active) {
        super(proto, remoteIDs, dynamic, active);
    }

    public VictimRemoteConfig(VictimRemoteProto proto, TeamColor team, Collection<String> remoteIDs, boolean dynamic,
            boolean active) {
        super(proto, team, remoteIDs, dynamic, active);
    }

    public VictimRemoteConfig(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    public VictimRemoteProto getProto() {
        return (VictimRemoteProto) super.getProto();
    }

}
