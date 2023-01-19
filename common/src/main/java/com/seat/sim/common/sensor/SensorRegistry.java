package com.seat.sim.common.sensor;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.seat.sim.common.core.CommonException;
import com.seat.sim.common.json.Json;
import com.seat.sim.common.json.JsonException;

public class SensorRegistry {
    public static final String BLUETOOTH_COMMS = "bluetooth";
    public static final String CMOS_CAMERA = "cmos_camera";
    public static final String GENERIC = "generic";
    public static final String HRVM = "hrvm";
    public static final String LTE_RADIO_COMMS = "lte_radio";
    public static final String NATURAL_VISION = "natural_vision";
    public static final String SENSOR_TYPE = "sensor_type";

    private static final Map<String, Function<Json, Object>> REGISTRY =
            new HashMap<String, Function<Json, Object>>(){{
        put(SensorConfig.class.getName(), (json) -> new SensorConfig(json));
        put(SensorProto.class.getName(), (json) -> new SensorProto(json));
        put(SensorState.class.getName(), (json) -> new SensorState(json));
    }};

    public static SensorProto Sensor(double range, double accuracy, double delay, double batteryUsage) {
        return new SensorProto(SensorRegistry.GENERIC, range, accuracy, delay, batteryUsage);
    }

    @SuppressWarnings("unchecked")
    public static <T> T decodeTo(Class<T> cls, Json json) throws CommonException, JsonException {
        String sensorType = SensorRegistry.decodeType(json);
        if (!SensorRegistry.isRegistered(sensorType)) {
            if (!SensorRegistry.isRegistered(cls)) {
                throw new CommonException(String.format("Cannot decode sensor %s", json.toString()));
            }
            return (T) SensorRegistry.REGISTRY.get(cls.getName()).apply(json);
        }
        return (T) SensorRegistry.REGISTRY.get(sensorType).apply(json);
    }

    public static String decodeType(Json json) throws CommonException {
        if (!json.isJsonObject()) {
            throw new CommonException(String.format("Cannot decode sensor type of %s", json.toString()));
        }
        return json.getJsonObject().getString(SensorRegistry.SENSOR_TYPE);
    }

    public static <T> boolean isRegistered(Class<T> cls) {
        return SensorRegistry.isRegistered(cls.getName());
    }

    public static boolean isRegistered(String className) {
        return SensorRegistry.REGISTRY.containsKey(className);
    }
}
