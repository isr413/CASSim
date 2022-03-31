package com.seat.rescuesim.common.remote.kinetic.drone;

import java.util.ArrayList;
import java.util.HashSet;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.remote.kinetic.KineticRemoteConfig;

/** A serializable configuration of a Drone Remote. */
public class DroneConfig extends KineticRemoteConfig {

    public DroneConfig(DroneSpec spec, int count, boolean dynamic) {
        super(spec, count, dynamic);
    }

    public DroneConfig(DroneSpec spec, ArrayList<String> remoteIDs, boolean dynamic) {
        super(spec, remoteIDs, dynamic);
    }

    public DroneConfig(DroneSpec spec, HashSet<String> remoteIDs, boolean dynamic) {
        super(spec, remoteIDs, dynamic);
    }

    public DroneConfig(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected DroneSpec decodeSpec(JSONOption jsonSpec) throws JSONException {
        return new DroneSpec(jsonSpec);
    }

    public DroneSpec getSpec() {
        return (DroneSpec) this.spec;
    }

}