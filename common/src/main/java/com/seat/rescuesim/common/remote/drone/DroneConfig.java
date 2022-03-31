package com.seat.rescuesim.common.remote.drone;

import java.util.ArrayList;
import java.util.HashSet;
import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.remote.RemoteConfig;
import com.seat.rescuesim.common.remote.RemoteType;

/** A serializable configuration of a Drone Remote. */
public class DroneConfig extends RemoteConfig {

    public DroneConfig(DroneSpec spec, int count, boolean dynamic) {
        super(RemoteType.DRONE, spec, count, dynamic);
    }

    public DroneConfig(DroneSpec spec, ArrayList<String> remoteIDs, boolean dynamic) {
        super(RemoteType.DRONE, spec, remoteIDs, dynamic);
    }

    public DroneConfig(DroneSpec spec, HashSet<String> remoteIDs, boolean dynamic) {
        super(RemoteType.DRONE, spec, remoteIDs, dynamic);
    }

    public DroneConfig(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected void decodeSpec(JSONOption jsonSpec) throws JSONException {
        this.spec = new DroneSpec(jsonSpec);
    }

    public DroneSpec getSpec() {
        return (DroneSpec) this.spec;
    }

    public DroneType getSpecType() {
        return (DroneType) this.spec.getSpecType();
    }

    public boolean hasSpec() {
        return !this.getSpecType().equals(DroneType.NONE);
    }

}
