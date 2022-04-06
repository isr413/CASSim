package com.seat.rescuesim.common.sensor.monitor;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.util.CoreException;

/** A registry of Monitor Sensor prototypes and supporting serializable types. */
public class MonitorSensorRegistry {

    public static MonitorSensorProto Generic(double range, double accuracy, double batteryUsage) {
        return new MonitorSensorProto(MonitorSensorType.GENERIC, range, accuracy, batteryUsage);
    }

    public static MonitorSensorProto HRVM(double range, double accuracy, double batteryUsage) {
        return new MonitorSensorProto(MonitorSensorType.HRVM, range, accuracy, batteryUsage);
    }

    @SuppressWarnings("unchecked")
    public static <T> T decodeTo(JSONOption option, Class<? extends T> classType) throws CoreException, JSONException {
        MonitorSensorType type = MonitorSensorRegistry.decodeType(option);
        if (classType == MonitorSensorConfig.class) {
            switch (type) {
                default: return (T) new MonitorSensorConfig(option);
            }
        }
        if (classType == MonitorSensorProto.class) {
            switch (type) {
                default: return (T) new MonitorSensorProto(option);
            }
        }
        if (classType == MonitorSensorState.class) {
            switch (type) {
                default: return (T) new MonitorSensorState(option);
            }
        }
        throw new CoreException(String.format("Cannot decode monitor sensor %s", option.toString()));
    }

    public static MonitorSensorType decodeType(JSONOption option) throws CoreException {
        if (!option.isSomeObject()) {
            throw new CoreException(String.format("Cannot decode monitor sensor type of %s", option.toString()));
        }
        return MonitorSensorType.decodeType(option.someObject());
    }

    public static <T> boolean isRegistered(Class<? extends T> classType) {
        return classType == MonitorSensorConfig.class || classType == MonitorSensorProto.class ||
            classType == MonitorSensorState.class;
    }

}
