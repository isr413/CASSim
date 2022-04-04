package com.seat.rescuesim.common.util;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.sensor.SensorConfig;
import com.seat.rescuesim.common.sensor.SensorSpec;
import com.seat.rescuesim.common.sensor.SensorState;
import com.seat.rescuesim.common.sensor.SensorType;
import com.seat.rescuesim.common.sensor.comms.CommsSensorConfig;
import com.seat.rescuesim.common.sensor.comms.CommsSensorSpec;
import com.seat.rescuesim.common.sensor.comms.CommsSensorState;
import com.seat.rescuesim.common.sensor.comms.CommsSensorType;
import com.seat.rescuesim.common.sensor.monitor.MonitorSensorConfig;
import com.seat.rescuesim.common.sensor.monitor.MonitorSensorSpec;
import com.seat.rescuesim.common.sensor.monitor.MonitorSensorState;
import com.seat.rescuesim.common.sensor.monitor.MonitorSensorType;
import com.seat.rescuesim.common.sensor.vision.VisionSensorConfig;
import com.seat.rescuesim.common.sensor.vision.VisionSensorSpec;
import com.seat.rescuesim.common.sensor.vision.VisionSensorState;
import com.seat.rescuesim.common.sensor.vision.VisionSensorType;

public class SensorFactory {

    public static CommsSensorConfig decodeCommsSensorConfig(JSONOption option) throws JSONException {
        if (option.isSomeObject()) {
            return new CommsSensorConfig(option);
        }
        throw new JSONException(String.format("Cannot decode sensor config of %s", option.toString()));
    }

    public static CommsSensorSpec decodeCommsSensorSpec(JSONOption option) throws JSONException {
        if (option.isSomeObject()) {
            return new CommsSensorSpec(option);
        }
        throw new JSONException(String.format("Cannot decode sensor spec of %s", option.toString()));
    }

    public static CommsSensorState decodeCommsSensorState(JSONOption option) throws JSONException {
        if (option.isSomeObject()) {
            return new CommsSensorState(option);
        }
        throw new JSONException(String.format("Cannot decode sensor state of %s", option.toString()));
    }

    public static CommsSensorType decodeCommsSensorType(JSONOption option) throws JSONException {
        if (option.isSomeObject()) {
            return CommsSensorType.decodeType(option.someObject());
        }
        throw new JSONException(String.format("Cannot decode sensor type of %s", option.toString()));
    }

    public static MonitorSensorConfig decodeMonitorSensorConfig(JSONOption option) throws JSONException {
        if (option.isSomeObject()) {
            return new MonitorSensorConfig(option);
        }
        throw new JSONException(String.format("Cannot decode sensor config of %s", option.toString()));
    }

    public static MonitorSensorSpec decodeMonitorSensorSpec(JSONOption option) throws JSONException {
        if (option.isSomeObject()) {
            return new MonitorSensorSpec(option);
        }
        throw new JSONException(String.format("Cannot decode sensor spec of %s", option.toString()));
    }

    public static MonitorSensorState decodeMonitorSensorState(JSONOption option) throws JSONException {
        if (option.isSomeObject()) {
            return new MonitorSensorState(option);
        }
        throw new JSONException(String.format("Cannot decode sensor state of %s", option.toString()));
    }

    public static MonitorSensorType decodeMonitorSensorType(JSONOption option) throws JSONException {
        if (option.isSomeObject()) {
            return MonitorSensorType.decodeType(option.someObject());
        }
        throw new JSONException(String.format("Cannot decode sensor type of %s", option.toString()));
    }

    public static SensorConfig decodeSensorConfig(JSONOption option) throws JSONException {
        if (option.isSomeObject()) {
            SensorType sensorType = SensorFactory.decodeSensorType(option);
            if (sensorType.equals(SensorType.COMMS)) {
                return SensorFactory.decodeCommsSensorConfig(option);
            }
            if (sensorType.equals(SensorType.MONITOR)) {
                return SensorFactory.decodeMonitorSensorConfig(option);
            }
            if (sensorType.equals(SensorType.VISION)) {
                return SensorFactory.decodeVisionSensorConfig(option);
            }
            return new SensorConfig(option);
        }
        throw new JSONException(String.format("Cannot decode remote config of %s", option.toString()));
    }

    public static SensorSpec decodeSensorSpec(JSONOption option) throws JSONException {
        if (option.isSomeObject()) {
            SensorType sensorType = SensorFactory.decodeSensorType(option);
            if (sensorType.equals(SensorType.COMMS)) {
                return SensorFactory.decodeCommsSensorSpec(option);
            }
            if (sensorType.equals(SensorType.MONITOR)) {
                return SensorFactory.decodeMonitorSensorSpec(option);
            }
            if (sensorType.equals(SensorType.VISION)) {
                return SensorFactory.decodeVisionSensorSpec(option);
            }
            return new SensorSpec(option);
        }
        throw new JSONException(String.format("Cannot decode remote spec of %s", option.toString()));
    }

    public static SensorState decodeSensorState(JSONOption option) throws JSONException {
        if (option.isSomeObject()) {
            SensorType sensorType = SensorFactory.decodeSensorType(option);
            if (sensorType.equals(SensorType.COMMS)) {
                return SensorFactory.decodeCommsSensorState(option);
            }
            if (sensorType.equals(SensorType.MONITOR)) {
                return SensorFactory.decodeMonitorSensorState(option);
            }
            if (sensorType.equals(SensorType.VISION)) {
                return SensorFactory.decodeVisionSensorState(option);
            }
            return new SensorState(option);
        }
        throw new JSONException(String.format("Cannot decode remote state of %s", option.toString()));
    }

    public static SensorType decodeSensorType(JSONOption option) throws JSONException {
        if (option.isSomeObject()) {
            return SensorType.decodeType(option.someObject());
        }
        throw new JSONException(String.format("Cannot decode remote type of %s", option.toString()));
    }

    public static VisionSensorConfig decodeVisionSensorConfig(JSONOption option) throws JSONException {
        if (option.isSomeObject()) {
            return new VisionSensorConfig(option);
        }
        throw new JSONException(String.format("Cannot decode sensor config of %s", option.toString()));
    }

    public static VisionSensorSpec decodeVisionSensorSpec(JSONOption option) throws JSONException {
        if (option.isSomeObject()) {
            return new VisionSensorSpec(option);
        }
        throw new JSONException(String.format("Cannot decode sensor spec of %s", option.toString()));
    }

    public static VisionSensorState decodeVisionSensorState(JSONOption option) throws JSONException {
        if (option.isSomeObject()) {
            return new VisionSensorState(option);
        }
        throw new JSONException(String.format("Cannot decode sensor state of %s", option.toString()));
    }

    public static VisionSensorType decodeVisionSensorType(JSONOption option) throws JSONException {
        if (option.isSomeObject()) {
            return VisionSensorType.decodeType(option.someObject());
        }
        throw new JSONException(String.format("Cannot decode sensor type of %s", option.toString()));
    }

}
