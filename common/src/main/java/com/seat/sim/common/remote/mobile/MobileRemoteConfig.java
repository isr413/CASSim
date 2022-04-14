package com.seat.sim.common.remote.mobile;

import java.util.Collection;

import com.seat.sim.common.core.TeamColor;
import com.seat.sim.common.json.JSONException;
import com.seat.sim.common.json.JSONObject;
import com.seat.sim.common.json.JSONObjectBuilder;
import com.seat.sim.common.json.JSONOptional;
import com.seat.sim.common.remote.RemoteConfig;

/** A serializable configuration of a Mobile Remote. */
public class MobileRemoteConfig extends RemoteConfig {
    public static final String SPEED_MEAN = "speed_mean";
    public static final String SPEED_STDDEV = "speed_stddev";

    private double speedMean;
    private double speedStdDev;

    public MobileRemoteConfig(MobileRemoteProto proto, TeamColor team, int count, boolean dynamic, boolean active) {
        this(proto, team, count, dynamic, active, 0, 0);
    }

    public MobileRemoteConfig(MobileRemoteProto proto, TeamColor team, int count, boolean dynamic, boolean active,
            double speedMean, double speedStdDev) {
        super(proto, team, count, dynamic, active);
        this.speedMean = speedMean;
        this.speedStdDev = speedStdDev;
    }

    public MobileRemoteConfig(MobileRemoteProto proto, TeamColor team, Collection<String> remoteIDs, boolean dynamic,
            boolean active) {
        this(proto, team, remoteIDs, dynamic, active, 0, 0);
    }

    public MobileRemoteConfig(MobileRemoteProto proto, TeamColor team, Collection<String> remoteIDs, boolean dynamic,
            boolean active, double speedMean, double speedStdDev) {
        super(proto, team, remoteIDs, dynamic, active);
        this.speedMean = speedMean;
        this.speedStdDev = speedStdDev;
    }

    public MobileRemoteConfig(JSONOptional optional) throws JSONException {
        super(optional);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        super.decode(json);
        this.speedMean = (json.hasKey(MobileRemoteConfig.SPEED_MEAN)) ?
            json.getDouble(MobileRemoteConfig.SPEED_MEAN) :
            0;
        this.speedStdDev = (json.hasKey(MobileRemoteConfig.SPEED_STDDEV)) ?
            json.getDouble(MobileRemoteConfig.SPEED_STDDEV) :
            0;
    }

    @Override
    protected JSONObjectBuilder getJSONBuilder() throws JSONException {
        JSONObjectBuilder json = super.getJSONBuilder();
        if (this.hasSpeedMean()) {
            json.put(MobileRemoteConfig.SPEED_MEAN, this.speedMean);
        }
        if (this.hasSpeedStdDev()) {
            json.put(MobileRemoteConfig.SPEED_STDDEV, this.speedStdDev);
        }
        return json;
    }

    @Override
    public MobileRemoteProto getProto() {
        return (MobileRemoteProto) super.getProto();
    }

    public double getSpeedMean() {
        return this.speedMean;
    }

    public double getSpeedStdDev() {
        return this.speedStdDev;
    }

    public boolean hasSpeedMean() {
        return this.speedMean > 0;
    }

    public boolean hasSpeedStdDev() {
        return this.speedStdDev > 0;
    }

    public boolean equals(MobileRemoteConfig config) {
        if (config == null) return false;
        return super.equals(config) && this.speedMean == config.speedMean && this.speedStdDev == config.speedStdDev;
    }

}
