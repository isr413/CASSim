package com.seat.rescuesim.common.remote.kinetic.drone;

import java.util.Collection;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.remote.kinetic.KineticRemoteConfig;

/** A serializable configuration of a Drone Remote. */
public class DroneConfig extends KineticRemoteConfig {

    public DroneConfig(DroneProto proto, int count, boolean dynamic) {
        super(proto, count, dynamic);
    }

    public DroneConfig(DroneProto proto, Collection<String> remoteIDs, boolean dynamic) {
        super(proto, remoteIDs, dynamic);
    }

    public DroneConfig(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected DroneProto decodeProto(JSONOption option) throws JSONException {
        return new DroneProto(option);
    }

    @Override
    public DroneProto getProto() {
        return (DroneProto) super.getProto();
    }

}
