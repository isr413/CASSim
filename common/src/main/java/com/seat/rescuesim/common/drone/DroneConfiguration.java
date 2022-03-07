package com.seat.rescuesim.common.drone;

import java.util.ArrayList;
import java.util.HashSet;
import com.seat.rescuesim.common.json.*;
import com.seat.rescuesim.common.remote.RemoteConfiguration;
import com.seat.rescuesim.common.remote.RemoteType;

/** A serializable class to represent the configuration of Drone Remotes.  */
public class DroneConfiguration extends RemoteConfiguration {

    public static DroneConfiguration None() {
        return new DroneConfiguration();
    }

    public DroneConfiguration(JSONObject json) {
        super(json);
    }

    public DroneConfiguration(JSONOption option) {
        super(option);
    }

    public DroneConfiguration(String encoding) {
        super(encoding);
    }

    public DroneConfiguration() {
        super();
    }

    public DroneConfiguration(DroneSpecification spec) {
        super(RemoteType.DRONE, spec);
    }

    public DroneConfiguration(DroneSpecification spec, String[] remotes) {
        super(RemoteType.DRONE, spec, remotes);
    }

    public DroneConfiguration(DroneSpecification spec, ArrayList<String> remotes) {
        super(RemoteType.DRONE, spec, remotes);
    }

    public DroneConfiguration(DroneSpecification spec, HashSet<String> remotes) {
        super(RemoteType.DRONE, spec, remotes);
    }

    public DroneConfiguration(DroneSpecification spec, int count) {
        super(RemoteType.DRONE, spec, count);
    }

    public DroneConfiguration(DroneSpecification spec, boolean dynamic) {
        super(RemoteType.DRONE, spec, dynamic);
    }

    public DroneConfiguration(DroneSpecification spec, String[] remotes, boolean dynamic) {
        super(RemoteType.DRONE, spec, remotes, dynamic);
    }

    public DroneConfiguration(DroneSpecification spec, ArrayList<String> remotes, boolean dynamic) {
        super(RemoteType.DRONE, spec, remotes, dynamic);
    }

    public DroneConfiguration(DroneSpecification spec, HashSet<String> remotes, boolean dynamic) {
        super(RemoteType.DRONE, spec, remotes, dynamic);
    }

    public DroneSpecification getSpecification() {
        return (DroneSpecification) this.spec;
    }

    @Override
    protected void decodeSpecification(JSONObject json) {
        this.spec = new DroneSpecification(json);
    }

}
