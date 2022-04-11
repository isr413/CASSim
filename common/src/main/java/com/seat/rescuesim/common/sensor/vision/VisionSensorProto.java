package com.seat.rescuesim.common.sensor.vision;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.sensor.SensorProto;

/** A serializable prototype of a Vision Sensor. */
public class VisionSensorProto extends SensorProto {

    public VisionSensorProto(double range, double accuracy, double batteryUsage) {
        super(range, accuracy, batteryUsage);
    }

    public VisionSensorProto(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    public String getLabel() {
        return "s:<vision>";
    }

}
