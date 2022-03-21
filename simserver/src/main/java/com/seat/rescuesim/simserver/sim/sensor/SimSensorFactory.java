package com.seat.rescuesim.simserver.sim.sensor;

import com.seat.rescuesim.common.sensor.SensorSpec;
import com.seat.rescuesim.common.sensor.SensorType;

public class SimSensorFactory {

    public static SimSensor getSimSensor(SensorSpec spec, String label) {
        if (spec.getSpecType().equals(SensorType.Comms)) {
            return new SimComms(spec, label);
        } else if (spec.getSpecType().equals(SensorType.Monitor)) {
            return new SimMonitor(spec, label);
        } else if (spec.getSpecType().equals(SensorType.Vision)) {
            return new SimVision(spec, label);
        } else {
            return null;
        }
    }

}
