package com.seat.rescuesim.common.sensor;

import java.util.HashMap;
import java.util.function.Function;

import com.seat.rescuesim.common.core.CommonException;
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

public class SensorRegistry {
    public static final String SENSOR_TYPE = "sensor_type";

    private static final HashMap<String, Function<JSONOption, Object>> REGISTRY =
            new HashMap<String, Function<JSONOption, Object>>(){{
        put(SensorConfig.class.getName(), (option) -> new SensorConfig(option));
        put(SensorProto.class.getName(), (option) -> new SensorProto(option));
        put(SensorState.class.getName(), (option) -> new SensorState(option));
        put(CommsSensorConfig.class.getName(), (option) -> new CommsSensorConfig(option));
        put(CommsSensorProto.class.getName(), (option) -> new CommsSensorProto(option));
        put(CommsSensorState.class.getName(), (option) -> new CommsSensorState(option));
        put(MonitorSensorConfig.class.getName(), (option) -> new MonitorSensorConfig(option));
        put(MonitorSensorProto.class.getName(), (option) -> new MonitorSensorProto(option));
        put(MonitorSensorState.class.getName(), (option) -> new MonitorSensorState(option));
        put(VisionSensorConfig.class.getName(), (option) -> new VisionSensorConfig(option));
        put(VisionSensorProto.class.getName(), (option) -> new VisionSensorProto(option));
        put(VisionSensorState.class.getName(), (option) -> new VisionSensorState(option));
    }};

    public static CommsSensorProto BluetoothComms(double range, double accuracy, double batteryUsage, double delay) {
        return new CommsSensorProto(range, accuracy, batteryUsage, delay);
    }

    public static VisionSensorProto CMOSCameraVision(double range, double accuracy, double batteryUsage) {
        return new VisionSensorProto(range, accuracy, batteryUsage);
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
        return new MonitorSensorProto(range, accuracy, batteryUsage);
    }

    public static CommsSensorProto LTERadioComms(double range, double accuracy, double batteryUsage, double delay) {
        return new CommsSensorProto(range, accuracy, batteryUsage, delay);
    }

    @SuppressWarnings("unchecked")
    public static <T> T decodeTo(JSONOption option, Class<? extends T> classType) throws CommonException, JSONException {
        String sensorType = SensorRegistry.decodeType(option);
        if (!SensorRegistry.isRegistered(sensorType)) {
            if (!SensorRegistry.isRegistered(classType)) {
                throw new CommonException(String.format("Cannot decode sensor %s", option.toString()));
            }
            return (T) SensorRegistry.REGISTRY.get(classType.getName()).apply(option);
        }
        return (T) SensorRegistry.REGISTRY.get(sensorType).apply(option);
    }

    public static String decodeType(JSONOption option) throws CommonException {
        if (!option.isSomeObject()) {
            throw new CommonException(String.format("Cannot decode sensor type of %s", option.toString()));
        }
        return option.someObject().getString(SensorRegistry.SENSOR_TYPE);
    }

    public static <T> boolean isRegistered(Class<T> classType) {
        return SensorRegistry.isRegistered(classType.getName());
    }

    public static boolean isRegistered(String className) {
        return SensorRegistry.REGISTRY.containsKey(className);
    }

}
