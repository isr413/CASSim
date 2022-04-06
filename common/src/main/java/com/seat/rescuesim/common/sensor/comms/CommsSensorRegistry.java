package com.seat.rescuesim.common.sensor.comms;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.util.CoreException;

/** A registry of Comms Sensor prototypes and supporting serializable types. */
public interface CommsSensorRegistry {

    public static CommsSensorProto Bluetooth(double range, double accuracy, double batteryUsage, double delay) {
        return new CommsSensorProto(CommsSensorType.BLUETOOTH, range, accuracy, batteryUsage, delay);
    }

    public static CommsSensorProto Generic(double range, double accuracy, double batteryUsage, double delay) {
        return new CommsSensorProto(CommsSensorType.GENERIC, range, accuracy, batteryUsage, delay);
    }

    public static CommsSensorProto LTERadio(double range, double accuracy, double batteryUsage, double delay) {
        return new CommsSensorProto(CommsSensorType.LTE_RADIO, range, accuracy, batteryUsage, delay);
    }

    @SuppressWarnings("unchecked")
    public static <T> T decodeTo(JSONOption option, Class<? extends T> classType) throws CoreException, JSONException {
        CommsSensorType type = CommsSensorRegistry.decodeType(option);
        if (classType == CommsSensorConfig.class) {
            switch (type) {
                default: return (T) new CommsSensorConfig(option);
            }
        }
        if (classType == CommsSensorProto.class) {
            switch (type) {
                default: return (T) new CommsSensorProto(option);
            }
        }
        if (classType == CommsSensorState.class) {
            switch (type) {
                default: return (T) new CommsSensorState(option);
            }
        }
        throw new CoreException(String.format("Cannot decode comms sensor %s", option.toString()));
    }

    public static CommsSensorType decodeType(JSONOption option) throws CoreException, JSONException {
        if (!option.isSomeObject()) {
            throw new CoreException(String.format("Cannot decode comms sensor type of %s", option.toString()));
        }
        return CommsSensorType.decodeType(option.someObject());
    }

    public static <T> boolean isRegistered(Class<? extends T> classType) {
        return classType == CommsSensorConfig.class || classType == CommsSensorProto.class ||
            classType == CommsSensorState.class;
    }

}
