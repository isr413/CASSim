package com.seat.rescuesim.common.remote.mobile.ground;

import java.util.Collection;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.JSONObjectBuilder;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.remote.mobile.MobileRemoteProto;
import com.seat.rescuesim.common.sensor.SensorConfig;

/** A serializable prototype of a Victim. */
public class VictimRemoteProto extends MobileRemoteProto {
    public static final String SPEED_MEAN = "speed_mean";
    public static final String SPEED_STDDEV = "speed_stddev";

    protected static final double DEFAULT_SPEED_MEAN = 0.0;
    protected static final double DEFAULT_SPEED_STD_DEV = 0.0;

    private double speedMean;
    private double speedStdDev;

    public VictimRemoteProto(double maxBatteryPower, double speedMean, double speedStdDev, double maxVelocity,
            double maxAcceleration, double maxJerk) {
        this(null, maxBatteryPower, null, speedMean, speedStdDev, maxVelocity, maxAcceleration, maxJerk);
    }

    public VictimRemoteProto(Vector location, double maxBatteryPower, double speedMean, double speedStdDev,
            double maxVelocity, double maxAcceleration, double maxJerk) {
        this(location, maxBatteryPower, null, speedMean, speedStdDev, maxVelocity, maxAcceleration, maxJerk);
    }

    public VictimRemoteProto(double maxBatteryPower, Collection<SensorConfig> sensors, double speedMean,
            double speedStdDev, double maxVelocity, double maxAcceleration, double maxJerk) {
        this(null, maxBatteryPower, sensors, speedMean, speedStdDev, maxVelocity, maxAcceleration, maxJerk);
    }

    public VictimRemoteProto(Vector location, double maxBatteryPower, Collection<SensorConfig> sensors,
            double speedMean, double speedStdDev, double maxVelocity, double maxAcceleration, double maxJerk) {
        super(location, maxBatteryPower, sensors, maxVelocity, maxAcceleration, maxJerk);
        this.speedMean = speedMean;
        this.speedStdDev = speedStdDev;
    }

    public VictimRemoteProto(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        super.decode(json);
        this.speedMean = (json.hasKey(VictimRemoteProto.SPEED_MEAN)) ?
            json.getDouble(VictimRemoteProto.SPEED_MEAN) :
            VictimRemoteProto.DEFAULT_SPEED_MEAN;
        this.speedStdDev = (json.hasKey(VictimRemoteProto.SPEED_STDDEV)) ?
            json.getDouble(VictimRemoteProto.SPEED_STDDEV) :
            VictimRemoteProto.DEFAULT_SPEED_STD_DEV;
    }

    @Override
    protected JSONObjectBuilder getJSONBuilder() throws JSONException {
        JSONObjectBuilder json = super.getJSONBuilder();
        json.put(VictimRemoteProto.SPEED_MEAN, this.speedMean);
        json.put(VictimRemoteProto.SPEED_STDDEV, this.speedStdDev);
        return json;
    }

    @Override
    public String getLabel() {
        return "r:<victim>";
    }

    public double getSpeedMean() {
        return this.speedMean;
    }

    public double getSpeedStdDev() {
        return this.speedStdDev;
    }

    public boolean equals(VictimRemoteProto proto) {
        if (proto == null) return false;
        return super.equals(proto) && this.speedMean == proto.speedMean && this.speedStdDev == proto.speedStdDev;
    }

}
