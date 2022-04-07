package com.seat.rescuesim.common.sensor.vision;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.sensor.SensorProto;
import com.seat.rescuesim.common.sensor.SensorType;

/** A serializable prototype of a Vision Sensor. */
public class VisionSensorProto extends SensorProto {
    public static final SensorType DEFAULT_VISION_SENSOR_TYPE = SensorType.VISION;

    public VisionSensorProto() {
        super(VisionSensorProto.DEFAULT_VISION_SENSOR_TYPE);
    }

    public VisionSensorProto(double range) {
        super(VisionSensorProto.DEFAULT_VISION_SENSOR_TYPE, range);
    }

    public VisionSensorProto(double range, double accuracy) {
        super(VisionSensorProto.DEFAULT_VISION_SENSOR_TYPE, range, accuracy);
    }

    public VisionSensorProto(double range, double accuracy, double batteryUsage) {
        super(VisionSensorProto.DEFAULT_VISION_SENSOR_TYPE, range, accuracy, batteryUsage);
    }

    public VisionSensorProto(SensorType sensorType) {
        super(sensorType);
    }

    public VisionSensorProto(SensorType sensorType, double range) {
        super(sensorType, range);
    }

    public VisionSensorProto(SensorType sensorType, double range, double accuracy) {
        super(sensorType, range, accuracy);
    }

    public VisionSensorProto(SensorType sensorType, double range, double accuracy, double batteryUsage) {
        super(sensorType, range, accuracy, batteryUsage);
    }

    public VisionSensorProto(JSONOption option) throws JSONException {
        super(option);
    }

}
