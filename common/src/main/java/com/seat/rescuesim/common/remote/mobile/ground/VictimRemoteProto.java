package com.seat.rescuesim.common.remote.mobile.ground;

import java.util.Collection;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.JSONObjectBuilder;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.remote.RemoteProto;
import com.seat.rescuesim.common.remote.RemoteType;
import com.seat.rescuesim.common.remote.mobile.MobileRemoteProto;
import com.seat.rescuesim.common.sensor.SensorConfig;

/** A serializable prototype of a Victim. */
public class VictimRemoteProto extends MobileRemoteProto {
    public static final String SPEED_MEAN = "speed_mean";
    public static final String SPEED_STDDEV = "speed_stddev";

    private double speedMean;
    private double speedStdDev;

    public VictimRemoteProto(double speedMean, double speedStdDev) {
        this(null, RemoteProto.DEFAULT_BATTERY_POWER, null, speedMean, speedStdDev, MobileRemoteProto.DEFAULT_VELOCITY,
            MobileRemoteProto.DEFAULT_ACCELERATION, MobileRemoteProto.DEFAULT_JERK);
    }

    public VictimRemoteProto(double speedMean, double speedStdDev, double maxVelocity, double maxAcceleration,
            double maxJerk) {
        this(null, RemoteProto.DEFAULT_BATTERY_POWER, null, speedMean, speedStdDev, maxVelocity, maxAcceleration,
            maxJerk);
    }

    public VictimRemoteProto(double maxBatteryPower, double speedMean, double speedStdDev) {
        this(null, maxBatteryPower, null, speedMean, speedStdDev, MobileRemoteProto.DEFAULT_VELOCITY,
            MobileRemoteProto.DEFAULT_ACCELERATION, MobileRemoteProto.DEFAULT_JERK);
    }

    public VictimRemoteProto(double maxBatteryPower, double speedMean, double speedStdDev, double maxVelocity,
            double maxAcceleration, double maxJerk) {
        this(null, maxBatteryPower, null, speedMean, speedStdDev, maxVelocity, maxAcceleration, maxJerk);
    }

    public VictimRemoteProto(Vector location, double speedMean, double speedStdDev) {
        this(location, RemoteProto.DEFAULT_BATTERY_POWER, null, speedMean, speedStdDev,
            MobileRemoteProto.DEFAULT_VELOCITY, MobileRemoteProto.DEFAULT_ACCELERATION,
            MobileRemoteProto.DEFAULT_JERK);
    }

    public VictimRemoteProto(Vector location, double speedMean, double speedStdDev, double maxVelocity,
            double maxAcceleration, double maxJerk) {
        this(location, RemoteProto.DEFAULT_BATTERY_POWER, null, speedMean, speedStdDev, maxVelocity, maxAcceleration,
            maxJerk);
    }

    public VictimRemoteProto(Vector location, double maxBatteryPower, double speedMean, double speedStdDev) {
        this(location, maxBatteryPower, null, speedMean, speedStdDev, MobileRemoteProto.DEFAULT_VELOCITY,
            MobileRemoteProto.DEFAULT_ACCELERATION, MobileRemoteProto.DEFAULT_JERK);
    }

    public VictimRemoteProto(Vector location, double maxBatteryPower, double speedMean, double speedStdDev,
            double maxVelocity, double maxAcceleration, double maxJerk) {
        this(location, maxBatteryPower, null, speedMean, speedStdDev, maxVelocity, maxAcceleration, maxJerk);
    }

    public VictimRemoteProto(double maxBatteryPower, Collection<SensorConfig> sensors, double speedMean,
            double speedStdDev) {
        this(null, maxBatteryPower, sensors, speedMean, speedStdDev, MobileRemoteProto.DEFAULT_VELOCITY,
            MobileRemoteProto.DEFAULT_ACCELERATION, MobileRemoteProto.DEFAULT_JERK);
    }

    public VictimRemoteProto(double maxBatteryPower, Collection<SensorConfig> sensors, double speedMean,
            double speedStdDev, double maxVelocity, double maxAcceleration, double maxJerk) {
        this(null, maxBatteryPower, sensors, speedMean, speedStdDev, maxVelocity, maxAcceleration, maxJerk);
    }

    public VictimRemoteProto(Vector location, double maxBatteryPower, Collection<SensorConfig> sensors,
            double speedMean, double speedStdDev) {
        this(location, maxBatteryPower, sensors, speedMean, speedStdDev, MobileRemoteProto.DEFAULT_VELOCITY,
            MobileRemoteProto.DEFAULT_ACCELERATION, MobileRemoteProto.DEFAULT_JERK);
    }

    public VictimRemoteProto(Vector location, double maxBatteryPower, Collection<SensorConfig> sensors,
            double speedMean, double speedStdDev, double maxVelocity, double maxAcceleration, double maxJerk) {
        super(RemoteType.VICTIM, location, maxBatteryPower, sensors, maxVelocity, maxAcceleration, maxJerk);
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
            0.0;
        this.speedStdDev = (json.hasKey(VictimRemoteProto.SPEED_STDDEV)) ?
            json.getDouble(VictimRemoteProto.SPEED_STDDEV) :
            0.0;
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
        return String.format("v:%s", this.getRemoteType().getLabel());
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
