package com.seat.sim.common.remote.mobile.aerial.drone;

import java.util.Collection;

import com.seat.sim.common.json.JSONException;
import com.seat.sim.common.json.JSONOptional;
import com.seat.sim.common.math.Vector;
import com.seat.sim.common.remote.mobile.aerial.AerialRemoteProto;
import com.seat.sim.common.sensor.SensorConfig;

/** A serializable prototype of a Drone Remote. */
public class DroneRemoteProto extends AerialRemoteProto {

    public DroneRemoteProto(double maxBatteryPower, double maxVelocity, double maxAcceleration, Vector batteryUsage) {
        super(maxBatteryPower, maxVelocity, maxAcceleration, batteryUsage);
    }

    public DroneRemoteProto(Vector location, double maxBatteryPower, double maxVelocity, double maxAcceleration,
            Vector batteryUsage) {
        super(location, maxBatteryPower, maxVelocity, maxAcceleration, batteryUsage);
    }

    public DroneRemoteProto(double maxBatteryPower, Collection<SensorConfig> sensors, double maxVelocity,
            double maxAcceleration, Vector batteryUsage) {
        super(maxBatteryPower, sensors, maxVelocity, maxAcceleration, batteryUsage);
    }

    public DroneRemoteProto(Vector location, double maxBatteryPower, Collection<SensorConfig> sensors,
            double maxVelocity, double maxAcceleration, Vector batteryUsage) {
        super(location, maxBatteryPower, sensors, maxVelocity, maxAcceleration, batteryUsage);
    }

    public DroneRemoteProto(JSONOptional optional) throws JSONException {
        super(optional);
    }

    @Override
    public String getLabel() {
        return "r:<drone>";
    }

}
