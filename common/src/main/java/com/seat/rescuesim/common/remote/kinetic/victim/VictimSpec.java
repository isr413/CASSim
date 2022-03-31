package com.seat.rescuesim.common.remote.kinetic.victim;

import java.util.ArrayList;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.remote.kinetic.KineticRemoteSpec;
import com.seat.rescuesim.common.remote.kinetic.KineticRemoteType;
import com.seat.rescuesim.common.sensor.SensorConfig;

/** A serializable specification of a Victim. */
public class VictimSpec extends KineticRemoteSpec {

    public VictimSpec() {
        super(KineticRemoteType.VICTIM);
    }

    public VictimSpec(double maxVelocity, double maxAcceleration, double maxJerk) {
        super(KineticRemoteType.VICTIM, maxVelocity, maxAcceleration, maxJerk);
    }

    public VictimSpec(Vector location) {
        super(KineticRemoteType.VICTIM, location);
    }

    public VictimSpec(Vector location, double maxVelocity, double maxAcceleration, double maxJerk) {
        super(KineticRemoteType.VICTIM, location, maxVelocity, maxAcceleration, maxJerk);
    }

    public VictimSpec(double maxBatteryPower, ArrayList<SensorConfig> sensors) {
        super(KineticRemoteType.VICTIM, maxBatteryPower, sensors);
    }

    public VictimSpec(double maxBatteryPower, ArrayList<SensorConfig> sensors, double maxVelocity,
            double maxAcceleration, double maxJerk) {
        super(KineticRemoteType.VICTIM, maxBatteryPower, sensors, maxVelocity, maxAcceleration, maxJerk);
    }

    public VictimSpec(Vector location, double maxBatteryPower, ArrayList<SensorConfig> sensors) {
        super(KineticRemoteType.VICTIM, location, maxBatteryPower, sensors);
    }

    public VictimSpec(Vector location, double maxBatteryPower, ArrayList<SensorConfig> sensors, double maxVelocity,
            double maxAcceleration, double maxJerk) {
        super(KineticRemoteType.VICTIM, location, maxBatteryPower, sensors, maxVelocity, maxAcceleration, maxJerk);
    }

    public VictimSpec(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    public String getLabel() {
        return String.format("v%s", this.specType.getLabel());
    }

}
