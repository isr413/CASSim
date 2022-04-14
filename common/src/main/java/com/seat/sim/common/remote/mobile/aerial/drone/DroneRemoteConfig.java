package com.seat.sim.common.remote.mobile.aerial.drone;

import java.util.Collection;

import com.seat.sim.common.core.TeamColor;
import com.seat.sim.common.json.JSONException;
import com.seat.sim.common.json.JSONOptional;
import com.seat.sim.common.remote.mobile.aerial.AerialRemoteConfig;

/** A serializable configuration of a Drone Remote. */
public class DroneRemoteConfig extends AerialRemoteConfig {

    public DroneRemoteConfig(DroneRemoteProto proto, TeamColor team, int count, boolean dynamic, boolean active) {
        super(proto, team, count, dynamic, active);
    }

    public DroneRemoteConfig(DroneRemoteProto proto, TeamColor team, int count, boolean dynamic, boolean active,
            double speedMean, double speedStdDev) {
        super(proto, team, count, dynamic, active, speedMean, speedStdDev);
    }

    public DroneRemoteConfig(DroneRemoteProto proto, TeamColor team, Collection<String> remoteIDs, boolean dynamic,
            boolean active) {
        super(proto, team, remoteIDs, dynamic, active);
    }

    public DroneRemoteConfig(DroneRemoteProto proto, TeamColor team, Collection<String> remoteIDs, boolean dynamic,
            boolean active, double speedMean, double speedStdDev) {
        super(proto, team, remoteIDs, dynamic, active, speedMean, speedStdDev);
    }

    public DroneRemoteConfig(JSONOptional optional) throws JSONException {
        super(optional);
    }

    @Override
    public DroneRemoteProto getProto() {
        return (DroneRemoteProto) super.getProto();
    }

}
