package com.seat.rescuesim.common.drone;

import java.util.ArrayList;
import java.util.HashSet;
import com.seat.rescuesim.common.json.*;
import com.seat.rescuesim.common.remote.RemoteConf;
import com.seat.rescuesim.common.remote.RemoteType;

/** A serializable configuration of a Drone Remote. */
public class DroneConf extends RemoteConf {

    public static DroneConf None() {
        return new DroneConf(DroneSpec.None(), 0, false);
    }

    public DroneConf(JSONObject json) {
        super(json);
    }

    public DroneConf(JSONOption option) {
        super(option);
    }

    public DroneConf(String encoding) {
        super(encoding);
    }

    public DroneConf(DroneSpec spec, int count, boolean dynamic) {
        super(RemoteType.DRONE, spec, count, dynamic);
    }

    public DroneConf(DroneSpec spec, ArrayList<String> remoteIDs, boolean dynamic) {
        super(RemoteType.DRONE, spec, remoteIDs, dynamic);
    }

    public DroneConf(DroneSpec spec, HashSet<String> remoteIDs, boolean dynamic) {
        super(RemoteType.DRONE, spec, remoteIDs, dynamic);
    }

    @Override
    protected void decodeSpec(JSONObject jsonSpec) {
        this.spec = new DroneSpec(jsonSpec);
    }

    public DroneSpec getSpecification() {
        return (DroneSpec) this.spec;
    }

    public DroneType getSpecType() {
        return (DroneType) this.spec.getSpecType();
    }

    public boolean hasSpec() {
        return !this.getSpecType().equals(DroneType.NONE);
    }

}
