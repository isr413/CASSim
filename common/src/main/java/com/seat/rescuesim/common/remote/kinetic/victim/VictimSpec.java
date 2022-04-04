package com.seat.rescuesim.common.remote.kinetic.victim;

import java.util.Collection;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.JSONObjectBuilder;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.remote.kinetic.KineticRemoteSpec;
import com.seat.rescuesim.common.remote.kinetic.KineticRemoteType;
import com.seat.rescuesim.common.sensor.SensorConfig;

/** A serializable specification of a Victim. */
public class VictimSpec extends KineticRemoteSpec {
    public static final String SPEED_MEAN = "speed_mean";
    public static final String SPEED_STDDEV = "speed_stddev";

    private double speedMean;
    private double speedStdDev;

    public VictimSpec(double speedMean, double speedStdDev) {
        super(KineticRemoteType.VICTIM);
        this.speedMean = speedMean;
        this.speedStdDev = speedStdDev;
    }

    public VictimSpec(double speedMean, double speedStdDev, double maxVelocity, double maxAcceleration,
            double maxJerk) {
        super(KineticRemoteType.VICTIM, maxVelocity, maxAcceleration, maxJerk);
        this.speedMean = speedMean;
        this.speedStdDev = speedStdDev;
    }

    public VictimSpec(double maxBatteryPower, double speedMean, double speedStdDev) {
        super(KineticRemoteType.VICTIM, maxBatteryPower);
        this.speedMean = speedMean;
        this.speedStdDev = speedStdDev;
    }

    public VictimSpec(double maxBatteryPower, double speedMean, double speedStdDev, double maxVelocity,
            double maxAcceleration, double maxJerk) {
        super(KineticRemoteType.VICTIM, maxBatteryPower, maxVelocity, maxAcceleration, maxJerk);
        this.speedMean = speedMean;
        this.speedStdDev = speedStdDev;
    }

    public VictimSpec(Vector location, double speedMean, double speedStdDev) {
        super(KineticRemoteType.VICTIM, location);
        this.speedMean = speedMean;
        this.speedStdDev = speedStdDev;
    }

    public VictimSpec(Vector location, double speedMean, double speedStdDev, double maxVelocity,
            double maxAcceleration, double maxJerk) {
        super(KineticRemoteType.VICTIM, location, maxVelocity, maxAcceleration, maxJerk);
        this.speedMean = speedMean;
        this.speedStdDev = speedStdDev;
    }

    public VictimSpec(Vector location, double maxBatteryPower, double speedMean, double speedStdDev) {
        super(KineticRemoteType.VICTIM, location, maxBatteryPower);
        this.speedMean = speedMean;
        this.speedStdDev = speedStdDev;
    }

    public VictimSpec(Vector location, double maxBatteryPower, double speedMean, double speedStdDev, double maxVelocity,
            double maxAcceleration, double maxJerk) {
        super(KineticRemoteType.VICTIM, location, maxBatteryPower, maxVelocity, maxAcceleration, maxJerk);
        this.speedMean = speedMean;
        this.speedStdDev = speedStdDev;
    }

    public VictimSpec(double maxBatteryPower, Collection<SensorConfig> sensors, double speedMean, double speedStdDev) {
        super(KineticRemoteType.VICTIM, maxBatteryPower, sensors);
        this.speedMean = speedMean;
        this.speedStdDev = speedStdDev;
    }

    public VictimSpec(double maxBatteryPower, Collection<SensorConfig> sensors, double speedMean, double speedStdDev,
            double maxVelocity, double maxAcceleration, double maxJerk) {
        super(KineticRemoteType.VICTIM, maxBatteryPower, sensors, maxVelocity, maxAcceleration, maxJerk);
        this.speedMean = speedMean;
        this.speedStdDev = speedStdDev;
    }

    public VictimSpec(Vector location, double maxBatteryPower, Collection<SensorConfig> sensors, double speedMean,
            double speedStdDev) {
        super(KineticRemoteType.VICTIM, location, maxBatteryPower, sensors);
        this.speedMean = speedMean;
        this.speedStdDev = speedStdDev;
    }

    public VictimSpec(Vector location, double maxBatteryPower, Collection<SensorConfig> sensors, double speedMean,
            double speedStdDev, double maxVelocity, double maxAcceleration, double maxJerk) {
        super(KineticRemoteType.VICTIM, location, maxBatteryPower, sensors, maxVelocity, maxAcceleration, maxJerk);
        this.speedMean = speedMean;
        this.speedStdDev = speedStdDev;
    }

    public VictimSpec(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        super.decode(json);
        this.speedMean = json.getDouble(VictimSpec.SPEED_MEAN);
        this.speedStdDev = json.getDouble(VictimSpec.SPEED_STDDEV);
    }

    @Override
    protected JSONObjectBuilder getJSONBuilder() {
        JSONObjectBuilder json = super.getJSONBuilder();
        json.put(VictimSpec.SPEED_MEAN, this.speedMean);
        json.put(VictimSpec.SPEED_STDDEV, this.speedStdDev);
        return json;
    }

    @Override
    public String getLabel() {
        return String.format("v:%s", this.specType.getLabel());
    }

    public double getSpeedMean() {
        return this.speedMean;
    }

    public double getSpeedStdDev() {
        return this.speedStdDev;
    }

    public boolean equals(VictimSpec spec) {
        return super.equals(spec) && this.speedMean == spec.speedMean && this.speedStdDev == spec.speedStdDev;
    }

}
