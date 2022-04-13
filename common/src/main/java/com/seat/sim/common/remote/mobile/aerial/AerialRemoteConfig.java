package com.seat.sim.common.remote.mobile.aerial;

import java.util.Collection;

import com.seat.sim.common.core.TeamColor;
import com.seat.sim.common.json.JSONException;
import com.seat.sim.common.json.JSONOption;
import com.seat.sim.common.remote.mobile.MobileRemoteConfig;

/** A serializable configuration of an Aerial Remote. */
public class AerialRemoteConfig extends MobileRemoteConfig {

    public AerialRemoteConfig(AerialRemoteProto proto, TeamColor team, int count, boolean dynamic, boolean active) {
        super(proto, team, count, dynamic, active);
    }

    public AerialRemoteConfig(AerialRemoteProto proto, TeamColor team, int count, boolean dynamic, boolean active,
            double speedMean, double speedStdDev) {
        super(proto, team, count, dynamic, active, speedMean, speedStdDev);
    }

    public AerialRemoteConfig(AerialRemoteProto proto, TeamColor team, Collection<String> remoteIDs, boolean dynamic,
            boolean active) {
        super(proto, team, remoteIDs, dynamic, active);
    }

    public AerialRemoteConfig(AerialRemoteProto proto, TeamColor team, Collection<String> remoteIDs, boolean dynamic,
            boolean active, double speedMean, double speedStdDev) {
        super(proto, team, remoteIDs, dynamic, active, speedMean, speedStdDev);
    }

    public AerialRemoteConfig(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    public AerialRemoteProto getProto() {
        return (AerialRemoteProto) super.getProto();
    }

}
