package com.seat.rescuesim.common.sensor;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.sensor.comms.CommsSensorConfig;
import com.seat.rescuesim.common.sensor.comms.CommsSensorProto;
import com.seat.rescuesim.common.sensor.comms.CommsSensorState;
import com.seat.rescuesim.common.sensor.monitor.MonitorSensorConfig;
import com.seat.rescuesim.common.sensor.monitor.MonitorSensorProto;
import com.seat.rescuesim.common.sensor.monitor.MonitorSensorState;
import com.seat.rescuesim.common.sensor.vision.VisionSensorConfig;
import com.seat.rescuesim.common.sensor.vision.VisionSensorProto;
import com.seat.rescuesim.common.sensor.vision.VisionSensorState;
import com.seat.rescuesim.common.util.CoreException;

public class SensorRegistry {

    public static CommsSensorProto BluetoothComms(double range, double accuracy, double batteryUsage, double delay) {
        return new CommsSensorProto(SensorType.BLUETOOTH_COMMS, range, accuracy, batteryUsage, delay);
    }

    public static VisionSensorProto CMOSCameraVision(double range, double accuracy, double batteryUsage) {
        return new VisionSensorProto(SensorType.CMOS_CAMERA_VISION, range, accuracy, batteryUsage);
    }

    public static CommsSensorProto GenericComms(double range, double accuracy, double batteryUsage, double delay) {
        return new CommsSensorProto(range, accuracy, batteryUsage, delay);
    }

    public static MonitorSensorProto GenericMonitor(double range, double accuracy, double batteryUsage) {
        return new MonitorSensorProto(range, accuracy, batteryUsage);
    }

    public static SensorProto GenericSensor(double range, double accuracy, double batteryUsage) {
        return new SensorProto(range, accuracy, batteryUsage);
    }

    public static VisionSensorProto GenericVision(double range, double accuracy, double batteryUsage) {
        return new VisionSensorProto(range, accuracy, batteryUsage);
    }

    public static MonitorSensorProto HRVMMonitor(double range, double accuracy, double batteryUsage) {
        return new MonitorSensorProto(SensorType.HRVM_MONITOR, range, accuracy, batteryUsage);
    }

    public static CommsSensorProto LTERadioComms(double range, double accuracy, double batteryUsage, double delay) {
        return new CommsSensorProto(SensorType.LTE_RADIO_COMMS, range, accuracy, batteryUsage, delay);
    }

    @SuppressWarnings("unchecked")
    private static <T> T decodeComms(JSONOption option, Class<? extends T> classType, SensorType sensorType)
            throws CoreException, JSONException {
        if (classType == SensorConfig.class || classType == CommsSensorConfig.class) {
            switch (sensorType) {
                default: return (T) new CommsSensorConfig(option);
            }
        }
        if (classType == SensorProto.class || classType == CommsSensorProto.class) {
            switch (sensorType) {
                default: return (T) new CommsSensorProto(option);
            }
        }
        if (classType == SensorState.class || classType == CommsSensorState.class) {
            switch (sensorType) {
                default: return (T) new CommsSensorState(option);
            }
        }
        throw new CoreException(String.format("Cannot decode comms sensor %s", option.toString()));
    }

    @SuppressWarnings("unchecked")
    private static <T> T decodeMonitor(JSONOption option, Class<? extends T> classType, SensorType sensorType)
            throws CoreException, JSONException {
        if (classType == SensorConfig.class || classType == MonitorSensorConfig.class) {
            switch (sensorType) {
                default: return (T) new MonitorSensorConfig(option);
            }
        }
        if (classType == SensorProto.class || classType == MonitorSensorProto.class) {
            switch (sensorType) {
                default: return (T) new MonitorSensorProto(option);
            }
        }
        if (classType == SensorState.class || classType == MonitorSensorState.class) {
            switch (sensorType) {
                default: return (T) new MonitorSensorState(option);
            }
        }
        throw new CoreException(String.format("Cannot decode monitor sensor %s", option.toString()));
    }

    @SuppressWarnings("unchecked")
    private static <T> T decodeVision(JSONOption option, Class<? extends T> classType, SensorType sensorType)
            throws CoreException, JSONException {
        if (classType == SensorConfig.class || classType == VisionSensorConfig.class) {
            switch (sensorType) {
                default: return (T) new VisionSensorConfig(option);
            }
        }
        if (classType == SensorProto.class || classType == VisionSensorProto.class) {
            switch (sensorType) {
                default: return (T) new VisionSensorProto(option);
            }
        }
        if (classType == SensorState.class || classType == VisionSensorState.class) {
            switch (sensorType) {
                default: return (T) new VisionSensorState(option);
            }
        }
        throw new CoreException(String.format("Cannot decode vision sensor %s", option.toString()));
    }

    @SuppressWarnings("unchecked")
    public static <T> T decodeTo(JSONOption option, Class<? extends T> classType) throws CoreException, JSONException {
        SensorType sensorType = SensorRegistry.decodeType(option);
        if (SensorRegistry.isRegisteredComms(classType, sensorType)) {
            return SensorRegistry.decodeComms(option, classType, sensorType);
        }
        if (SensorRegistry.isRegisteredMonitor(classType, sensorType)) {
            return SensorRegistry.decodeMonitor(option, classType, sensorType);
        }
        if (SensorRegistry.isRegisteredVision(classType, sensorType)) {
            return SensorRegistry.decodeVision(option, classType, sensorType);
        }
        if (classType == SensorConfig.class) {
            switch (sensorType) {
                default: return (T) new SensorConfig(option);
            }
        }
        if (classType == SensorProto.class) {
            switch (sensorType) {
                default: return (T) new SensorProto(option);
            }
        }
        if (classType == SensorState.class) {
            switch (sensorType) {
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

    private static <T> boolean isRegisteredComms(Class<? extends T> classType) {
        return classType == CommsSensorConfig.class || classType == CommsSensorProto.class ||
            classType == CommsSensorState.class;
    }

    private static <T> boolean isRegisteredComms(Class<? extends T> classType, SensorType sensorType) {
        return SensorRegistry.isRegisteredComms(classType) || sensorType.equals(SensorType.COMMS) ||
            sensorType.equals(SensorType.BLUETOOTH_COMMS) || sensorType.equals(SensorType.LTE_RADIO_COMMS);
    }

    private static <T> boolean isRegisteredMonitor(Class<? extends T> classType) {
        return classType == MonitorSensorConfig.class || classType == MonitorSensorProto.class ||
            classType == MonitorSensorState.class;
    }

    private static <T> boolean isRegisteredMonitor(Class<? extends T> classType, SensorType sensorType) {
        return SensorRegistry.isRegisteredMonitor(classType) || sensorType.equals(SensorType.MONITOR) ||
            sensorType.equals(SensorType.HRVM_MONITOR);
    }

    private static <T> boolean isRegisteredVision(Class<? extends T> classType) {
        return classType == VisionSensorConfig.class || classType == VisionSensorProto.class ||
            classType == VisionSensorState.class;
    }

    private static <T> boolean isRegisteredVision(Class<? extends T> classType, SensorType sensorType) {
        return SensorRegistry.isRegisteredVision(classType) || sensorType.equals(SensorType.VISION) ||
            sensorType.equals(SensorType.CMOS_CAMERA_VISION);
    }

    public static <T> boolean isRegistered(Class<? extends T> classType) {
        return classType == SensorConfig.class || classType == SensorProto.class || classType == SensorState.class ||
            SensorRegistry.isRegisteredComms(classType) || SensorRegistry.isRegisteredMonitor(classType) ||
            SensorRegistry.isRegisteredVision(classType);
    }

}
