package com.seat.rescuesim.common.util;

import com.seat.rescuesim.common.json.*;
import com.seat.rescuesim.common.sensor.CommsSensorState;
import com.seat.rescuesim.common.sensor.MonitorSensorState;
import com.seat.rescuesim.common.sensor.SensorConst;
import com.seat.rescuesim.common.sensor.SensorState;
import com.seat.rescuesim.common.sensor.SensorType;
import com.seat.rescuesim.common.sensor.VisionSensorState;
import org.json.JSONException;

public class SensorFactory {

    public static SensorType decodeSensorType(JSONObject json) throws JSONException {
        return SensorType.values()[json.getInt(SensorConst.TYPE)];
    }

    public static SensorType decodeSensorType(JSONOption option) throws JSONException {
        if (option.isSomeObject()) {
            return SensorFactory.decodeSensorType(option.someObject());
        }
        throw new JSONException(String.format("Cannot decode sensor type of %s", option.toString()));
    }

    public static SensorType decodeSensorType(String encoding) throws JSONException {
        return SensorFactory.decodeSensorType(JSONOption.String(encoding));
    }

    public static SensorState decodeSensorState(JSONObject json) throws JSONException {
        SensorType sensorType = SensorFactory.decodeSensorType(json);
        if (sensorType.equals(SensorType.Comms)) {
            return new CommsSensorState(json);
        } else if (sensorType.equals(SensorType.Monitor)) {
            return new MonitorSensorState(json);
        } else if (sensorType.equals(SensorType.Vision)) {
            return new VisionSensorState(json);
        } else {
            throw new JSONException(String.format("Cannot decode sensor state of %s", json.toString()));
        }
    }

    public static SensorState decodeSensorState(JSONOption option) throws JSONException {
        if (option.isSomeObject()) {
            return SensorFactory.decodeSensorState(option.someObject());
        }
        throw new JSONException(String.format("Cannot decode sensor state of %s", option.toString()));
    }

    public static SensorState decodeSensorState(String encoding) throws JSONException {
        return SensorFactory.decodeSensorState(JSONOption.String(encoding));
    }

}
