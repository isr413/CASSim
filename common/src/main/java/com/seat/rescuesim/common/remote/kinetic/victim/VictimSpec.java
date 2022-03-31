package com.seat.rescuesim.common.remote.kinetic.victim;

import java.util.ArrayList;

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

    public VictimSpec(double speedMean, double speedStdDev, double maxBatteryPower, ArrayList<SensorConfig> sensors) {
        super(KineticRemoteType.VICTIM, maxBatteryPower, sensors);
        this.speedMean = speedMean;
        this.speedStdDev = speedStdDev;
    }

    public VictimSpec(double speedMean, double speedStdDev, double maxBatteryPower, ArrayList<SensorConfig> sensors,
            double maxVelocity, double maxAcceleration, double maxJerk) {
        super(KineticRemoteType.VICTIM, maxBatteryPower, sensors, maxVelocity, maxAcceleration, maxJerk);
        this.speedMean = speedMean;
        this.speedStdDev = speedStdDev;
    }

    public VictimSpec(Vector location, double speedMean, double speedStdDev, double maxBatteryPower,
            ArrayList<SensorConfig> sensors) {
        super(KineticRemoteType.VICTIM, location, maxBatteryPower, sensors);
        this.speedMean = speedMean;
        this.speedStdDev = speedStdDev;
    }

    public VictimSpec(Vector location, double speedMean, double speedStdDev, double maxBatteryPower,
            ArrayList<SensorConfig> sensors, double maxVelocity, double maxAcceleration, double maxJerk) {
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
        this.speedMean = json.getDouble(VictimConst.SPEED_MEAN);
        this.speedStdDev = json.getDouble(VictimConst.SPEED_STDDEV);
    }

    @Override
    protected JSONObjectBuilder getJSONBuilder() {
        JSONObjectBuilder json = super.getJSONBuilder();
        json.put(VictimConst.SPEED_MEAN, this.speedMean);
        json.put(VictimConst.SPEED_STDDEV, this.speedStdDev);
        return json;
    }

    @Override
    public String getLabel() {
        return String.format("v%s", this.specType.getLabel());
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
