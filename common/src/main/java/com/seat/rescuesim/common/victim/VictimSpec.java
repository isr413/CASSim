package com.seat.rescuesim.common.victim;

import java.util.ArrayList;

import com.seat.rescuesim.common.json.*;
import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.remote.KineticRemoteSpec;
import com.seat.rescuesim.common.remote.RemoteType;
import com.seat.rescuesim.common.sensor.SensorConf;

/** A serializable specification of a Victim. */
public class VictimSpec extends KineticRemoteSpec {
    private static final String VICTIM_TYPE = "victim_type";

    public static VictimSpec None() {
        return new VictimSpec(VictimType.NONE, new Vector(), 0, new ArrayList<SensorConf>(), 0, 0, 0);
    }

    public static VictimSpec Static(Vector location) {
        return new VictimSpec(VictimType.DEFAULT, location, 1, new ArrayList<SensorConf>(), 0, 0, 0);
    }

    public static VictimSpec StaticWithSensors(Vector location, double maxBatteryPower, ArrayList<SensorConf> sensors) {
        return new VictimSpec(VictimType.DEFAULT, location, maxBatteryPower, sensors, 0, 0, 0);
    }

    public static VictimSpec Kinetic(Vector location, double maxBatteryPower, Vector batteryUsage, double maxVelocity,
            double maxAcceleration, double maxJerk) {
        return new VictimSpec(VictimType.DEFAULT, location, maxBatteryPower, new ArrayList<SensorConf>(),
            maxVelocity, maxAcceleration, maxJerk);
    }

    public static VictimSpec KineticWithSensors(Vector location, double maxBatteryPower, ArrayList<SensorConf> sensors,
            double maxVelocity, double maxAcceleration, double maxJerk) {
        return new VictimSpec(VictimType.DEFAULT, location, maxBatteryPower, sensors, maxVelocity, maxAcceleration,
            maxJerk);
    }

    private VictimType type;

    public VictimSpec(JSONObject json) {
        super(json);
    }

    public VictimSpec(JSONOption option) {
        super(option);
    }

    public VictimSpec(String encoding) {
        super(encoding);
    }

    public VictimSpec(VictimType type, Vector location, double maxBatteryPower, ArrayList<SensorConf> sensors,
            double maxVelocity, double maxAcceleration, double maxJerk) {
        super(RemoteType.VICTIM, location, maxBatteryPower, sensors, maxVelocity, maxAcceleration, maxJerk);
        this.type = type;
    }

    @Override
    protected void decode(JSONObject json) {
        super.decode(json);
        this.type = VictimType.values()[json.getInt(VictimSpec.VICTIM_TYPE)];
    }

    @Override
    public String getLabel() {
        return String.format("v%s", this.type.getLabel());
    }

    public VictimType getSpecType() {
        return this.type;
    }

    public JSONOption toJSON() {
        JSONObjectBuilder json = super.getJSONBuilder();
        json.put(VictimSpec.VICTIM_TYPE, this.type.getType());
        return json.toJSON();
    }

    public boolean equals(VictimSpec spec) {
        return super.equals(spec) && this.type.equals(spec.type);
    }

}
