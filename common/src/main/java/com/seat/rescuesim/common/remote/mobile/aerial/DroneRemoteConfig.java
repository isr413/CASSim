package com.seat.rescuesim.common.remote.mobile.aerial;

import java.util.Collection;

import com.seat.rescuesim.common.core.TeamCode;
import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.remote.mobile.MobileRemoteConfig;

/** A serializable configuration of a Drone Remote. */
public class DroneRemoteConfig extends MobileRemoteConfig {

    public DroneRemoteConfig(DroneRemoteProto proto, int count, boolean dynamic, boolean active) {
        super(proto, count, dynamic, active);
    }

    public DroneRemoteConfig(DroneRemoteProto proto, int count, boolean dynamic, boolean active, TeamCode team) {
        super(proto, count, dynamic, active, team);
    }

    public DroneRemoteConfig(DroneRemoteProto proto, Collection<String> remoteIDs, boolean dynamic, boolean active) {
        super(proto, remoteIDs, dynamic, active);
    }

    public DroneRemoteConfig(DroneRemoteProto proto, Collection<String> remoteIDs, boolean dynamic, boolean active,
            TeamCode team) {
        super(proto, remoteIDs, dynamic, active, team);
    }

    public DroneRemoteConfig(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    public DroneRemoteProto getProto() {
        return (DroneRemoteProto) super.getProto();
    }

}
