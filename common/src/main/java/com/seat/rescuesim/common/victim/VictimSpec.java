package com.seat.rescuesim.common.victim;

import java.util.ArrayList;

import com.seat.rescuesim.common.json.*;
import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.remote.KineticRemoteSpec;
import com.seat.rescuesim.common.remote.RemoteType;
import com.seat.rescuesim.common.sensor.SensorConfig;

/** A serializable specification of a Victim. */
public class VictimSpec extends KineticRemoteSpec {

    public static VictimSpec None() {
        return new VictimSpec(VictimType.NONE, new Vector(), 0, new ArrayList<SensorConfig>(), 0, 0, 0);
    }

    public static VictimSpec Static(Vector location) {
        return new VictimSpec(VictimType.DEFAULT, location, 1, new ArrayList<SensorConfig>(), 0, 0, 0);
    }

    public static VictimSpec StaticWithSensors(Vector location, double maxBatteryPower, ArrayList<SensorConfig> sensors) {
        return new VictimSpec(VictimType.DEFAULT, location, maxBatteryPower, sensors, 0, 0, 0);
    }

    public static VictimSpec Kinetic(Vector location, double maxBatteryPower, Vector batteryUsage, double maxVelocity,
            double maxAcceleration, double maxJerk, double mean, double stddev) {
        return new VictimSpec(VictimType.DEFAULT, location, maxBatteryPower, new ArrayList<SensorConfig>(),
            maxVelocity, maxAcceleration, maxJerk, mean, stddev);
    }

    public static VictimSpec KineticWithSensors(Vector location, double maxBatteryPower, ArrayList<SensorConfig> sensors,
            double maxVelocity, double maxAcceleration, double maxJerk, double mean, double stddev) {
        return new VictimSpec(VictimType.DEFAULT, location, maxBatteryPower, sensors, maxVelocity, maxAcceleration,
            maxJerk, mean, stddev);
    }

    private double speedMean;
    private double speedStddev;
    private VictimType type;

    public VictimSpec(JSONObject json) throws JSONException {
        super(json);
    }

    public VictimSpec(JSONOption option) throws JSONException {
        super(option);
    }

    public VictimSpec(String encoding) throws JSONException {
        super(encoding);
    }

    public VictimSpec(VictimType type, Vector location, double maxBatteryPower, ArrayList<SensorConfig> sensors,
            double maxVelocity, double maxAcceleration, double maxJerk) {
        this(type, location, maxBatteryPower, sensors, maxVelocity, maxAcceleration, maxJerk, 1, 0);
    }

    public VictimSpec(VictimType type, Vector location, double maxBatteryPower, ArrayList<SensorConfig> sensors,
            double maxVelocity, double maxAcceleration, double maxJerk, double mean, double stddev) {
        super(RemoteType.VICTIM, location, maxBatteryPower, sensors, maxVelocity, maxAcceleration, maxJerk);
        this.type = type;
        this.speedMean = mean;
        this.speedStddev = stddev;
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        super.decode(json);
        this.type = VictimType.values()[json.getInt(VictimConst.VICTIM_TYPE)];
        this.speedMean = json.getDouble(VictimConst.SPEED_MEAN);
        this.speedStddev = json.getDouble(VictimConst.SPEED_STDDEV);
    }

    @Override
    public String getLabel() {
        return String.format("v%s", this.type.getLabel());
    }

    public VictimType getSpecType() {
        return this.type;
    }

    public double getSpeedMean() {
        return this.speedMean;
    }

    public double getSpeedStddev() {
        return this.speedStddev;
    }

    public JSONOption toJSON() {
        JSONObjectBuilder json = super.getJSONBuilder();
        json.put(VictimConst.VICTIM_TYPE, this.type.getType());
        json.put(VictimConst.SPEED_MEAN, this.speedMean);
        json.put(VictimConst.SPEED_STDDEV, this.speedStddev);
        return json.toJSON();
    }

    public boolean equals(VictimSpec spec) {
        return super.equals(spec) && this.type.equals(spec.type) && this.speedMean == spec.speedMean &&
            this.speedStddev == spec.speedStddev;
    }

}
