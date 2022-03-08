package com.seat.rescuesim.common.drone;

import java.util.ArrayList;
import java.util.HashSet;
import com.seat.rescuesim.common.json.*;
import com.seat.rescuesim.common.remote.RemoteConfiguration;
import com.seat.rescuesim.common.remote.RemoteType;

/** A serializable class to represent the configuration of Drone Remotes. */
public class DroneConf extends RemoteConfiguration {

    public static DroneConf None() {
        return new DroneConf();
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

    public DroneConf() {
        super();
    }

    public DroneConf(DroneSpec spec) {
        super(RemoteType.DRONE, spec);
    }

    public DroneConf(DroneSpec spec, String[] remotes) {
        super(RemoteType.DRONE, spec, remotes);
    }

    public DroneConf(DroneSpec spec, ArrayList<String> remotes) {
        super(RemoteType.DRONE, spec, remotes);
    }

    public DroneConf(DroneSpec spec, HashSet<String> remotes) {
        super(RemoteType.DRONE, spec, remotes);
    }

    public DroneConf(DroneSpec spec, int count) {
        super(RemoteType.DRONE, spec, count);
    }

    public DroneConf(DroneSpec spec, boolean dynamic) {
        super(RemoteType.DRONE, spec, dynamic);
    }

    public DroneConf(DroneSpec spec, String[] remotes, boolean dynamic) {
        super(RemoteType.DRONE, spec, remotes, dynamic);
    }

    public DroneConf(DroneSpec spec, ArrayList<String> remotes, boolean dynamic) {
        super(RemoteType.DRONE, spec, remotes, dynamic);
    }

    public DroneConf(DroneSpec spec, HashSet<String> remotes, boolean dynamic) {
        super(RemoteType.DRONE, spec, remotes, dynamic);
    }

    public DroneSpec getSpecification() {
        return (DroneSpec) this.spec;
    }

    @Override
    protected void decodeSpecification(JSONObject jsonSpec) {
        this.spec = new DroneSpec(jsonSpec);
    }

}
