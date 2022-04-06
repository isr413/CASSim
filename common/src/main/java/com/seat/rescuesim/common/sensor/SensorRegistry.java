package com.seat.rescuesim.common.sensor;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.sensor.comms.CommsSensorConfig;
import com.seat.rescuesim.common.sensor.comms.CommsSensorProto;
import com.seat.rescuesim.common.sensor.comms.CommsSensorRegistry;
import com.seat.rescuesim.common.sensor.comms.CommsSensorState;
import com.seat.rescuesim.common.sensor.monitor.MonitorSensorConfig;
import com.seat.rescuesim.common.sensor.monitor.MonitorSensorProto;
import com.seat.rescuesim.common.sensor.monitor.MonitorSensorRegistry;
import com.seat.rescuesim.common.sensor.monitor.MonitorSensorState;
import com.seat.rescuesim.common.sensor.vision.VisionSensorConfig;
import com.seat.rescuesim.common.sensor.vision.VisionSensorProto;
import com.seat.rescuesim.common.sensor.vision.VisionSensorRegistry;
import com.seat.rescuesim.common.sensor.vision.VisionSensorState;
import com.seat.rescuesim.common.util.CoreException;

public class SensorRegistry {

    public static CommsSensorProto Comms(double range, double accuracy, double batteryUsage, double delay) {
        return CommsSensorRegistry.Generic(range, accuracy, batteryUsage, delay);
    }

    public static SensorProto Generic(double range, double accuracy, double batteryUsage) {
        return new SensorProto(SensorType.GENERIC, range, accuracy, batteryUsage);
    }

    public static MonitorSensorProto Monitor(double range, double accuracy, double batteryUsage) {
        return MonitorSensorRegistry.Generic(range, accuracy, batteryUsage);
    }

    public static VisionSensorProto Vision(double range, double accuracy, double batteryUsage) {
        return VisionSensorRegistry.Generic(range, accuracy, batteryUsage);
    }

    @SuppressWarnings("unchecked")
    public static <T> T decodeTo(JSONOption option, Class<? extends T> classType) throws CoreException, JSONException {
        if (CommsSensorRegistry.isRegistered(classType)) {
            return CommsSensorRegistry.decodeTo(option, classType);
        }
        if (MonitorSensorRegistry.isRegistered(classType)) {
            return MonitorSensorRegistry.decodeTo(option, classType);
        }
        if (VisionSensorRegistry.isRegistered(classType)) {
            return VisionSensorRegistry.decodeTo(option, classType);
        }
        SensorType type = SensorRegistry.decodeType(option);
        if (classType == SensorConfig.class) {
            switch (type) {
                case COMMS: return (T) CommsSensorRegistry.decodeTo(option, CommsSensorConfig.class);
                case MONITOR: return (T) MonitorSensorRegistry.decodeTo(option, MonitorSensorConfig.class);
                case VISION: return (T) VisionSensorRegistry.decodeTo(option, VisionSensorConfig.class);
                default: return (T) new SensorConfig(option);
            }
        }
        if (classType == SensorProto.class) {
            switch (type) {
                case COMMS: return (T) CommsSensorRegistry.decodeTo(option, CommsSensorProto.class);
                case MONITOR: return (T) MonitorSensorRegistry.decodeTo(option, MonitorSensorProto.class);
                case VISION: return (T) VisionSensorRegistry.decodeTo(option, VisionSensorProto.class);
                default: return (T) new SensorProto(option);
            }
        }
        if (classType == SensorState.class) {
            switch (type) {
                case COMMS: return (T) CommsSensorRegistry.decodeTo(option, CommsSensorState.class);
                case MONITOR: return (T) MonitorSensorRegistry.decodeTo(option, MonitorSensorState.class);
                case VISION: return (T) VisionSensorRegistry.decodeTo(option, VisionSensorState.class);
                default: return (T) new SensorState(option);
            }
        }
        throw new CoreException(String.format("Cannot decode sensor %s", option.toString()));
    }

    public static SensorType decodeType(JSONOption option) throws CoreException, JSONException {
        if (!option.isSomeObject()) {
            throw new CoreException(String.format("Cannot decode sensor type of %s", option.toString()));
        }
        return SensorType.decodeType(option.someObject());
    }

    public static <T> boolean isRegistered(Class<? extends T> classType) {
        return classType == SensorConfig.class || classType == SensorProto.class || classType == SensorState.class ||
            CommsSensorRegistry.isRegistered(classType) || MonitorSensorRegistry.isRegistered(classType) ||
            VisionSensorRegistry.isRegistered(classType);
    }

}
