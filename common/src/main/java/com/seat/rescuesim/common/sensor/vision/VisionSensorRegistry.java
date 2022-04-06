package com.seat.rescuesim.common.sensor.vision;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.util.CoreException;

/** A registry of Vision Sensor prototypes and supporting serializable types. */
public class VisionSensorRegistry {

    public static VisionSensorProto CMOSCamera(double range, double accuracy, double batteryUsage) {
        return new VisionSensorProto(VisionSensorType.CMOS_CAMERA, range, accuracy, batteryUsage);
    }

    public static VisionSensorProto Generic(double range, double accuracy, double batteryUsage) {
        return new VisionSensorProto(VisionSensorType.GENERIC, range, accuracy, batteryUsage);
    }

    @SuppressWarnings("unchecked")
    public static <T> T decodeTo(JSONOption option, Class<? extends T> classType) throws CoreException, JSONException {
        VisionSensorType type = VisionSensorRegistry.decodeType(option);
        if (classType == VisionSensorConfig.class) {
            switch (type) {
                default: return (T) new VisionSensorConfig(option);
            }
        }
        if (classType == VisionSensorProto.class) {
            switch (type) {
                default: return (T) new VisionSensorProto(option);
            }
        }
        if (classType == VisionSensorState.class) {
            switch (type) {
                default: return (T) new VisionSensorState(option);
            }
        }
        throw new CoreException(String.format("Cannot decode vision sensor %s", option.toString()));
    }

    public static VisionSensorType decodeType(JSONOption option) throws CoreException, JSONException {
        if (!option.isSomeObject()) {
            throw new CoreException(String.format("Cannot decode vision sensor type of %s", option.toString()));
        }
        return VisionSensorType.decodeType(option.someObject());
    }

    public static <T> boolean isRegistered(Class<? extends T> classType) {
        return classType == VisionSensorConfig.class || classType == VisionSensorProto.class ||
            classType == VisionSensorState.class;
    }

}
