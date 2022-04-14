package com.seat.sim.common.remote.mobile.victim;

import java.util.Collection;

import com.seat.sim.common.json.JSONException;
import com.seat.sim.common.json.JSONOptional;
import com.seat.sim.common.math.Vector;
import com.seat.sim.common.remote.mobile.MobileRemoteProto;
import com.seat.sim.common.sensor.SensorConfig;

/** A serializable prototype of a Victim Remote. */
public class VictimRemoteProto extends MobileRemoteProto {

    public VictimRemoteProto(double maxBatteryPower, double maxVelocity, double maxAcceleration) {
        super(maxBatteryPower, maxVelocity, maxAcceleration);
    }

    public VictimRemoteProto(Vector location, double maxBatteryPower, double maxVelocity, double maxAcceleration) {
        super(location, maxBatteryPower, maxVelocity, maxAcceleration);
    }

    public VictimRemoteProto(double maxBatteryPower, Collection<SensorConfig> sensors, double maxVelocity,
            double maxAcceleration) {
        super(maxBatteryPower, sensors, maxVelocity, maxAcceleration);
    }

    public VictimRemoteProto(Vector location, double maxBatteryPower, Collection<SensorConfig> sensors,
            double maxVelocity, double maxAcceleration) {
        super(location, maxBatteryPower, sensors, maxVelocity, maxAcceleration);
    }

    public VictimRemoteProto(JSONOptional optional) throws JSONException {
        super(optional);
    }

    @Override
    public String getLabel() {
        return "r:<victim>";
    }

}
