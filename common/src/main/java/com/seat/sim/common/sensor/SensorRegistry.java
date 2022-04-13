package com.seat.sim.common.sensor;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.seat.sim.common.core.CommonException;
import com.seat.sim.common.json.JSONException;
import com.seat.sim.common.json.JSONOption;
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

    private static final Map<String, Function<JSONOption, Object>> REGISTRY =
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

    public static CommsSensorProto BluetoothSensor(double range, double accuracy, double batteryUsage, double delay) {
        return new CommsSensorProto(SensorRegistry.BLUETOOTH_COMMS, range, accuracy, batteryUsage, delay);
    }

    public static VisionSensorProto CMOSCameraSensor(double range, double accuracy, double batteryUsage) {
        return new VisionSensorProto(SensorRegistry.CMOS_CAMERA, range, accuracy, batteryUsage);
    }

    public static CommsSensorProto CommsSensor(double range, double accuracy, double batteryUsage, double delay) {
        return new CommsSensorProto(range, accuracy, batteryUsage, delay);
    }

    public static CommsSensorConfig ConfigureCommsSensor(String sensorModel, double range, double accuracy,
            double batteryUsage, double delay, int count, boolean active) {
        return new CommsSensorConfig(
            new CommsSensorProto(sensorModel, range, accuracy, batteryUsage, delay),
            count,
            active
        );
    }

    public static CommsSensorConfig ConfigureCommsSensor(String sensorModel, double range, double accuracy,
            double batteryUsage, double delay, Collection<String> sensorIDs, boolean active) {
        return new CommsSensorConfig(
            new CommsSensorProto(sensorModel, range, accuracy, batteryUsage, delay),
            sensorIDs,
            active
        );
    }

    public static MonitorSensorConfig ConfigureMonitorSensor(String sensorModel, double range, double accuracy,
            double batteryUsage, int count, boolean active) {
        return new MonitorSensorConfig(
            new MonitorSensorProto(sensorModel, range, accuracy, batteryUsage),
            count,
            active
        );
    }

    public static MonitorSensorConfig ConfigureMonitorSensor(String sensorModel, double range, double accuracy,
            double batteryUsage, Collection<String> sensorIDs, boolean active) {
        return new MonitorSensorConfig(
            new MonitorSensorProto(sensorModel, range, accuracy, batteryUsage),
            sensorIDs,
            active
        );
    }

    public static VisionSensorConfig ConfigureVisionSensor(String sensorModel, double range, double accuracy,
            double batteryUsage, int count, boolean active) {
        return new VisionSensorConfig(
            new VisionSensorProto(sensorModel, range, accuracy, batteryUsage),
            count,
            active
        );
    }

    public static VisionSensorConfig ConfigureVisionSensor(String sensorModel, double range, double accuracy,
            double batteryUsage, Collection<String> sensorIDs, boolean active) {
        return new VisionSensorConfig(
            new VisionSensorProto(sensorModel, range, accuracy, batteryUsage),
            sensorIDs,
            active
        );
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
