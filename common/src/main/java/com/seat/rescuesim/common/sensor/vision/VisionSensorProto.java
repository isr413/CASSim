package com.seat.rescuesim.common.sensor.vision;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.sensor.SensorProto;
import com.seat.rescuesim.common.sensor.SensorType;

/** A serializable prototype of a Vision Sensor. */
public class VisionSensorProto extends SensorProto {
    public static final SensorType DEFAULT_VISION_SENSOR_TYPE = SensorType.VISION;

    public VisionSensorProto() {
        this(VisionSensorProto.DEFAULT_VISION_SENSOR_TYPE, SensorProto.DEFAULT_RANGE, SensorProto.DEFAULT_ACCURACY,
            SensorProto.DEFAULT_BATTERY_USAGE);
    }

    public VisionSensorProto(double range) {
        this(VisionSensorProto.DEFAULT_VISION_SENSOR_TYPE, range, SensorProto.DEFAULT_ACCURACY,
            SensorProto.DEFAULT_BATTERY_USAGE);
    }

    public VisionSensorProto(double range, double accuracy) {
        this(VisionSensorProto.DEFAULT_VISION_SENSOR_TYPE, range, accuracy, SensorProto.DEFAULT_BATTERY_USAGE);
    }

    public VisionSensorProto(double range, double accuracy, double batteryUsage) {
        this(VisionSensorProto.DEFAULT_VISION_SENSOR_TYPE, range, accuracy, batteryUsage);
    }

    public VisionSensorProto(SensorType sensorType) {
        this(sensorType, SensorProto.DEFAULT_RANGE, SensorProto.DEFAULT_ACCURACY, SensorProto.DEFAULT_BATTERY_USAGE);
    }

    public VisionSensorProto(SensorType sensorType, double range) {
        this(sensorType, range, SensorProto.DEFAULT_ACCURACY, SensorProto.DEFAULT_BATTERY_USAGE);
    }

    public VisionSensorProto(SensorType sensorType, double range, double accuracy) {
        this(sensorType, range, accuracy, SensorProto.DEFAULT_BATTERY_USAGE);
    }

    public VisionSensorProto(SensorType sensorType, double range, double accuracy, double batteryUsage) {
        super(sensorType, range, accuracy, batteryUsage);
    }

    public VisionSensorProto(JSONOption option) throws JSONException {
        super(option);
    }

}
