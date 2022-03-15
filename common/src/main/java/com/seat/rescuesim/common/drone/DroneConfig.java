package com.seat.rescuesim.common.drone;

import java.util.ArrayList;
import java.util.HashSet;
import com.seat.rescuesim.common.json.*;
import com.seat.rescuesim.common.remote.RemoteConfig;
import com.seat.rescuesim.common.remote.RemoteType;

/** A serializable configuration of a Drone Remote. */
public class DroneConfig extends RemoteConfig {

    public static DroneConfig None() {
        return new DroneConfig(DroneSpec.None(), 0, false);
    }

    public DroneConfig(JSONObject json) {
        super(json);
    }

    public DroneConfig(JSONOption option) {
        super(option);
    }

    public DroneConfig(String encoding) {
        super(encoding);
    }

    public DroneConfig(DroneSpec spec, int count, boolean dynamic) {
        super(RemoteType.DRONE, spec, count, dynamic);
    }

    public DroneConfig(DroneSpec spec, ArrayList<String> remoteIDs, boolean dynamic) {
        super(RemoteType.DRONE, spec, remoteIDs, dynamic);
    }

    public DroneConfig(DroneSpec spec, HashSet<String> remoteIDs, boolean dynamic) {
        super(RemoteType.DRONE, spec, remoteIDs, dynamic);
    }

    @Override
    protected void decodeSpec(JSONObject jsonSpec) {
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
