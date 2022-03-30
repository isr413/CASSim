package com.seat.rescuesim.common.victim;

import java.util.ArrayList;
import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.JSONObjectBuilder;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.remote.KineticRemoteSpec;
import com.seat.rescuesim.common.remote.RemoteType;
import com.seat.rescuesim.common.sensor.SensorConfig;

/** A serializable specification of a Victim. */
public class VictimSpec extends KineticRemoteSpec {

    private VictimType type;

    public VictimSpec(VictimType type) {
        super(RemoteType.VICTIM);
        this.type = type;
    }

    public VictimSpec(VictimType type, double maxVelocity, double maxAcceleration, double maxJerk) {
        super(RemoteType.VICTIM, maxVelocity, maxAcceleration, maxJerk);
        this.type = type;
    }

    public VictimSpec(VictimType type, Vector location) {
        super(RemoteType.VICTIM, location);
        this.type = type;
    }

    public VictimSpec(VictimType type, Vector location, double maxVelocity, double maxAcceleration,
            double maxJerk) {
        super(RemoteType.VICTIM, location, maxVelocity, maxAcceleration, maxJerk);
        this.type = type;
    }

    public VictimSpec(VictimType type, double maxBatteryPower, ArrayList<SensorConfig> sensors) {
        super(RemoteType.VICTIM, maxBatteryPower, sensors);
        this.type = type;
    }

    public VictimSpec(VictimType type, double maxBatteryPower, ArrayList<SensorConfig> sensors,
            double maxVelocity, double maxAcceleration, double maxJerk) {
        super(RemoteType.VICTIM, maxBatteryPower, sensors, maxVelocity, maxAcceleration, maxJerk);
        this.type = type;
    }

    public VictimSpec(VictimType type, Vector location, double maxBatteryPower,
            ArrayList<SensorConfig> sensors) {
        super(RemoteType.VICTIM, location, maxBatteryPower, sensors);
        this.type = type;
    }

    public VictimSpec(VictimType type, Vector location, double maxBatteryPower,
            ArrayList<SensorConfig> sensors, double maxVelocity, double maxAcceleration, double maxJerk) {
        super(RemoteType.VICTIM, location, maxBatteryPower, sensors, maxVelocity, maxAcceleration, maxJerk);
        this.type = type;
    }

    public VictimSpec(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        super.decode(json);
        this.type = VictimType.values()[json.getInt(VictimConst.VICTIM_TYPE)];
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
        json.put(VictimConst.VICTIM_TYPE, this.type.getType());
        return json.toJSON();
    }

    public boolean equals(VictimSpec spec) {
        return super.equals(spec) && this.type.equals(spec.type);
    }

}
