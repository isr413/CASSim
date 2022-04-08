package com.seat.rescuesim.common.remote.mobile.aerial;

import java.util.Collection;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.remote.mobile.MobileRemoteConfig;

/** A serializable configuration of a Drone Remote. */
public class DroneRemoteConfig extends MobileRemoteConfig {

    public DroneRemoteConfig(DroneRemoteProto proto, int count) {
        super(proto, count);
    }

    public DroneRemoteConfig(DroneRemoteProto proto, int count, boolean dynamic) {
        super(proto, count, dynamic);
    }

    public DroneRemoteConfig(DroneRemoteProto proto, int count, boolean dynamic, boolean active) {
        super(proto, count, dynamic, active);
    }

    public DroneRemoteConfig(DroneRemoteProto proto, Collection<String> remoteIDs) {
        super(proto, remoteIDs);
    }

    public DroneRemoteConfig(DroneRemoteProto proto, Collection<String> remoteIDs, boolean dynamic) {
        super(proto, remoteIDs, dynamic);
    }

    public DroneRemoteConfig(DroneRemoteProto proto, Collection<String> remoteIDs, boolean dynamic, boolean active) {
        super(proto, remoteIDs, dynamic, active);
    }

    public DroneRemoteConfig(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    public DroneRemoteProto getProto() {
        return (DroneRemoteProto) super.getProto();
    }

}
