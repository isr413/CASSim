package com.seat.sim.common.sensor;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.seat.sim.common.core.CommonException;
import com.seat.sim.common.json.JSONException;
import com.seat.sim.common.json.JSONOptional;
import com.seat.sim.common.sensor.comms.CommsSensorConfig;
import com.seat.sim.common.sensor.comms.CommsSensorProto;
import com.seat.sim.common.sensor.comms.CommsSensorState;
import com.seat.sim.common.sensor.monitor.MonitorSensorConfig;
import com.seat.sim.common.sensor.monitor.MonitorSensorProto;
import com.seat.sim.common.sensor.monitor.MonitorSensorState;
import com.seat.sim.common.sensor.vision.VisionSensorConfig;
import com.seat.sim.common.sensor.vision.VisionSensorProto;
import com.seat.sim.common.sensor.vision.VisionSensorState;

public class SensorRegistry {
    public static final String BLUETOOTH_COMMS = "bluetooth";
    public static final String CMOS_CAMERA = "cmos_camera";
    public static final String GENERIC = "generic";
    public static final String HRVM = "hrvm";
    public static final String LTE_RADIO_COMMS = "lte_radio";
    public static final String SENSOR_TYPE = "sensor_type";

    private static final Map<String, Function<JSONOptional, Object>> REGISTRY =
            new HashMap<String, Function<JSONOptional, Object>>(){{
        put(SensorConfig.class.getName(), (optional) -> new SensorConfig(optional));
        put(SensorProto.class.getName(), (optional) -> new SensorProto(optional));
        put(SensorState.class.getName(), (optional) -> new SensorState(optional));
        put(CommsSensorConfig.class.getName(), (optional) -> new CommsSensorConfig(optional));
        put(CommsSensorProto.class.getName(), (optional) -> new CommsSensorProto(optional));
        put(CommsSensorState.class.getName(), (optional) -> new CommsSensorState(optional));
        put(MonitorSensorConfig.class.getName(), (optional) -> new MonitorSensorConfig(optional));
        put(MonitorSensorProto.class.getName(), (optional) -> new MonitorSensorProto(optional));
        put(MonitorSensorState.class.getName(), (optional) -> new MonitorSensorState(optional));
        put(VisionSensorConfig.class.getName(), (optional) -> new VisionSensorConfig(optional));
        put(VisionSensorProto.class.getName(), (optional) -> new VisionSensorProto(optional));
        put(VisionSensorState.class.getName(), (optional) -> new VisionSensorState(optional));
    }};

    public static CommsSensorProto BluetoothSensor(double range, double accuracy, double batteryUsage, double delay) {
        return new CommsSensorProto(SensorRegistry.BLUETOOTH_COMMS, range, accuracy, batteryUsage, delay);
    }

    public static VisionSensorProto CMOSCameraSensor(double range, double accuracy, double batteryUsage) {
        return new VisionSensorProto(SensorRegistry.CMOS_CAMERA, range, accuracy, batteryUsage);
    }

    public static CommsSensorProto CommsSensor(double range, double accuracy, double batteryUsage, double delay) {
        return new CommsSensorProto(range, accuracy, batteryUsage, delay);
    }

    public static MonitorSensorProto HRVMSensor(double range, double accuracy, double batteryUsage) {
        return new MonitorSensorProto(SensorRegistry.HRVM, range, accuracy, batteryUsage);
    }

    public static CommsSensorProto LTERadioSensor(double range, double accuracy, double batteryUsage, double delay) {
        return new CommsSensorProto(SensorRegistry.LTE_RADIO_COMMS, range, accuracy, batteryUsage, delay);
    }

    public static MonitorSensorProto MonitorSensor(double range, double accuracy, double batteryUsage) {
        return new MonitorSensorProto(range, accuracy, batteryUsage);
    }

    public static SensorProto Sensor(double range, double accuracy, double batteryUsage) {
        return new SensorProto(SensorRegistry.GENERIC, range, accuracy, batteryUsage);
    }

    public static VisionSensorProto VisionSensor(double range, double accuracy, double batteryUsage) {
        return new VisionSensorProto(range, accuracy, batteryUsage);
    }

    @SuppressWarnings("unchecked")
    public static <T> T decodeTo(JSONOptional optional, Class<? extends T> classType) throws CommonException, JSONException {
        String sensorType = SensorRegistry.decodeType(optional);
        if (!SensorRegistry.isRegistered(sensorType)) {
            if (!SensorRegistry.isRegistered(classType)) {
                throw new CommonException(String.format("Cannot decode sensor %s", optional.toString()));
            }
            return (T) SensorRegistry.REGISTRY.get(classType.getName()).apply(optional);
        }
        return (T) SensorRegistry.REGISTRY.get(sensorType).apply(optional);
    }

    public static String decodeType(JSONOptional optional) throws CommonException {
        if (!optional.isSomeObject()) {
            throw new CommonException(String.format("Cannot decode sensor type of %s", optional.toString()));
        }
        return optional.someObject().getString(SensorRegistry.SENSOR_TYPE);
    }

    public static <T> boolean isRegistered(Class<T> classType) {
        return SensorRegistry.isRegistered(classType.getName());
    }

    public static boolean isRegistered(String className) {
        return SensorRegistry.REGISTRY.containsKey(className);
    }

}
