package com.seat.rescuesim.common.remote.mobile.aerial;

import java.util.Collection;

import com.seat.rescuesim.common.core.TeamColor;
import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.remote.mobile.MobileRemoteConfig;

/** A serializable configuration of a Drone Remote. */
public class DroneRemoteConfig extends MobileRemoteConfig {

    public DroneRemoteConfig(DroneRemoteProto proto, int count, boolean dynamic, boolean active) {
        super(proto, count, dynamic, active);
    }

    public DroneRemoteConfig(DroneRemoteProto proto, TeamColor team, int count, boolean dynamic, boolean active) {
        super(proto, team, count, dynamic, active);
    }

    public DroneRemoteConfig(DroneRemoteProto proto, Collection<String> remoteIDs, boolean dynamic, boolean active) {
        super(proto, remoteIDs, dynamic, active);
    }

    public DroneRemoteConfig(DroneRemoteProto proto, TeamColor team, Collection<String> remoteIDs, boolean dynamic,
            boolean active) {
        super(proto, team, remoteIDs, dynamic, active);
    }

    public DroneRemoteConfig(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    public DroneRemoteProto getProto() {
        return (DroneRemoteProto) super.getProto();
    }

}
