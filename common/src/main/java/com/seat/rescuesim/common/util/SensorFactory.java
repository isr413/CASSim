package com.seat.rescuesim.common.util;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.sensor.CommsSensorState;
import com.seat.rescuesim.common.sensor.MonitorSensorState;
import com.seat.rescuesim.common.sensor.SensorConst;
import com.seat.rescuesim.common.sensor.SensorState;
import com.seat.rescuesim.common.sensor.SensorType;
import com.seat.rescuesim.common.sensor.VisionSensorState;

public class SensorFactory {

    public static SensorState decodeSensorState(JSONOption option) throws JSONException {
        if (option.isSomeObject()) {
            SensorType sensorType = SensorFactory.decodeSensorType(option);
            if (sensorType.equals(SensorType.Comms)) {
                return new CommsSensorState(option);
            }
            if (sensorType.equals(SensorType.Monitor)) {
                return new MonitorSensorState(option);
            }
            if (sensorType.equals(SensorType.Vision)) {
                return new VisionSensorState(option);
            }
        }
        throw new JSONException(String.format("Cannot decode sensor state of %s", option.toString()));
    }

    public static SensorType decodeSensorType(JSONOption option) throws JSONException {
        if (option.isSomeObject()) {
            return SensorType.values()[option.someObject().getInt(SensorConst.SENSOR_TYPE)];
        }
        throw new JSONException(String.format("Cannot decode sensor type of %s", option.toString()));
    }

}
