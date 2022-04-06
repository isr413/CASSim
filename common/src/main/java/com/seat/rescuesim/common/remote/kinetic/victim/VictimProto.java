package com.seat.rescuesim.common.remote.kinetic.victim;

import java.util.Collection;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.JSONObjectBuilder;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.remote.RemoteProto;
import com.seat.rescuesim.common.remote.kinetic.KineticRemoteProto;
import com.seat.rescuesim.common.remote.kinetic.KineticRemoteType;
import com.seat.rescuesim.common.sensor.SensorConfig;

/** A serializable prototype of a Victim. */
public class VictimProto extends KineticRemoteProto {
    public static final String SPEED_MEAN = "speed_mean";
    public static final String SPEED_STDDEV = "speed_stddev";

    private double speedMean;
    private double speedStdDev;

    public VictimProto(double speedMean, double speedStdDev) {
        this(null, RemoteProto.DEFAULT_BATTERY_POWER, null, speedMean, speedStdDev, KineticRemoteProto.DEFAULT_VELOCITY,
            KineticRemoteProto.DEFAULT_ACCELERATION, KineticRemoteProto.DEFAULT_JERK);
    }

    public VictimProto(double speedMean, double speedStdDev, double maxVelocity, double maxAcceleration,
            double maxJerk) {
        this(null, RemoteProto.DEFAULT_BATTERY_POWER, null, speedMean, speedStdDev, maxVelocity, maxAcceleration,
            maxJerk);
    }

    public VictimProto(double maxBatteryPower, double speedMean, double speedStdDev) {
        this(null, maxBatteryPower, null, speedMean, speedStdDev, KineticRemoteProto.DEFAULT_VELOCITY,
            KineticRemoteProto.DEFAULT_ACCELERATION, KineticRemoteProto.DEFAULT_JERK);
    }

    public VictimProto(double maxBatteryPower, double speedMean, double speedStdDev, double maxVelocity,
            double maxAcceleration, double maxJerk) {
        this(null, maxBatteryPower, null, speedMean, speedStdDev, maxVelocity, maxAcceleration, maxJerk);
    }

    public VictimProto(Vector location, double speedMean, double speedStdDev) {
        this(location, RemoteProto.DEFAULT_BATTERY_POWER, null, speedMean, speedStdDev,
            KineticRemoteProto.DEFAULT_VELOCITY, KineticRemoteProto.DEFAULT_ACCELERATION,
            KineticRemoteProto.DEFAULT_JERK);
    }

    public VictimProto(Vector location, double speedMean, double speedStdDev, double maxVelocity,
            double maxAcceleration, double maxJerk) {
        this(location, RemoteProto.DEFAULT_BATTERY_POWER, null, speedMean, speedStdDev, maxVelocity, maxAcceleration,
            maxJerk);
    }

    public VictimProto(Vector location, double maxBatteryPower, double speedMean, double speedStdDev) {
        this(location, maxBatteryPower, null, speedMean, speedStdDev, KineticRemoteProto.DEFAULT_VELOCITY,
            KineticRemoteProto.DEFAULT_ACCELERATION, KineticRemoteProto.DEFAULT_JERK);
    }

    public VictimProto(Vector location, double maxBatteryPower, double speedMean, double speedStdDev,
            double maxVelocity, double maxAcceleration, double maxJerk) {
        this(location, maxBatteryPower, null, speedMean, speedStdDev, maxVelocity, maxAcceleration, maxJerk);
    }

    public VictimProto(double maxBatteryPower, Collection<SensorConfig> sensors, double speedMean, double speedStdDev) {
        this(null, maxBatteryPower, sensors, speedMean, speedStdDev, KineticRemoteProto.DEFAULT_VELOCITY,
            KineticRemoteProto.DEFAULT_ACCELERATION, KineticRemoteProto.DEFAULT_JERK);
    }

    public VictimProto(double maxBatteryPower, Collection<SensorConfig> sensors, double speedMean, double speedStdDev,
            double maxVelocity, double maxAcceleration, double maxJerk) {
        this(null, maxBatteryPower, sensors, speedMean, speedStdDev, maxVelocity, maxAcceleration, maxJerk);
    }

    public VictimProto(Vector location, double maxBatteryPower, Collection<SensorConfig> sensors, double speedMean,
            double speedStdDev) {
        this(location, maxBatteryPower, sensors, speedMean, speedStdDev, KineticRemoteProto.DEFAULT_VELOCITY,
            KineticRemoteProto.DEFAULT_ACCELERATION, KineticRemoteProto.DEFAULT_JERK);
    }

    public VictimProto(Vector location, double maxBatteryPower, Collection<SensorConfig> sensors, double speedMean,
            double speedStdDev, double maxVelocity, double maxAcceleration, double maxJerk) {
        super(KineticRemoteType.VICTIM, location, maxBatteryPower, sensors, maxVelocity, maxAcceleration, maxJerk);
        this.speedMean = speedMean;
        this.speedStdDev = speedStdDev;
    }

    public VictimProto(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        super.decode(json);
        this.speedMean = (json.hasKey(VictimProto.SPEED_MEAN)) ?
            json.getDouble(VictimProto.SPEED_MEAN) :
            0.0;
        this.speedStdDev = (json.hasKey(VictimProto.SPEED_STDDEV)) ?
            json.getDouble(VictimProto.SPEED_STDDEV) :
            0.0;
    }

    @Override
    protected JSONObjectBuilder getJSONBuilder() {
        JSONObjectBuilder json = super.getJSONBuilder();
        json.put(VictimProto.SPEED_MEAN, this.speedMean);
        json.put(VictimProto.SPEED_STDDEV, this.speedStdDev);
        return json;
    }

    @Override
    public String getLabel() {
        return String.format("v:%s", this.getSpecType().getLabel());
    }

    public double getSpeedMean() {
        return this.speedMean;
    }

    public double getSpeedStdDev() {
        return this.speedStdDev;
    }

    public boolean equals(VictimProto proto) {
        if (proto == null) return false;
        return super.equals(proto) && this.speedMean == proto.speedMean && this.speedStdDev == proto.speedStdDev;
    }

}
