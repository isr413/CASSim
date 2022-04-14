package com.seat.sim.common.sensor.vision;

import com.seat.sim.common.json.JSONException;
import com.seat.sim.common.json.JSONOptional;
import com.seat.sim.common.sensor.SensorProto;

/** A serializable prototype of a Vision Sensor. */
public class VisionSensorProto extends SensorProto {
    public static final String DEFAULT_VISION_SENSOR_MODEL = "vision";

    public VisionSensorProto(double range, double accuracy, double batteryUsage) {
        super(VisionSensorProto.DEFAULT_VISION_SENSOR_MODEL, range, accuracy, batteryUsage);
    }

    public VisionSensorProto(String sensorModel, double range, double accuracy, double batteryUsage) {
        super(sensorModel, range, accuracy, batteryUsage);
    }

    public VisionSensorProto(JSONOptional optional) throws JSONException {
        super(optional);
    }

}
