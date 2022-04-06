package com.seat.rescuesim.common.remote;

import java.util.Collection;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.remote.base.BaseConfig;
import com.seat.rescuesim.common.remote.base.BaseProto;
import com.seat.rescuesim.common.remote.base.BaseState;
import com.seat.rescuesim.common.remote.kinetic.KineticRemoteConfig;
import com.seat.rescuesim.common.remote.kinetic.KineticRemoteProto;
import com.seat.rescuesim.common.remote.kinetic.KineticRemoteRegistry;
import com.seat.rescuesim.common.remote.kinetic.KineticRemoteState;
import com.seat.rescuesim.common.sensor.SensorConfig;
import com.seat.rescuesim.common.util.CoreException;

/** A registry of Remote prototypes and supporting serializable types. */
public class RemoteRegistry {

    public static BaseProto Base(Vector location, double maxBatteryPower, Collection<SensorConfig> sensors) {
        return new BaseProto(location, maxBatteryPower, sensors);
    }

    public static RemoteProto Generic(Vector location, double maxBatteryPower, Collection<SensorConfig> sensors) {
        return new RemoteProto(RemoteType.GENERIC, location, maxBatteryPower, sensors);
    }

    @SuppressWarnings("unchecked")
    public static <T> T decodeTo(JSONOption option, Class<? extends T> classType) throws CoreException, JSONException {
        if (KineticRemoteRegistry.isRegistered(classType)) {
            return KineticRemoteRegistry.decodeTo(option, classType);
        }
        RemoteType type = RemoteRegistry.decodeType(option);
        if (classType == RemoteConfig.class || classType == BaseConfig.class) {
            switch (type) {
                case BASE: return (T) new BaseConfig(option);
                case KINETIC: return (T) KineticRemoteRegistry.decodeTo(option, KineticRemoteConfig.class);
                default: return (T) new RemoteConfig(option);
            }
        }
        if (classType == RemoteProto.class || classType == BaseProto.class) {
            switch (type) {
                case BASE: return (T) new BaseProto(option);
                case KINETIC: return (T) KineticRemoteRegistry.decodeTo(option, KineticRemoteProto.class);
                default: return (T) new RemoteProto(option);
            }
        }
        if (classType == RemoteState.class || classType == BaseState.class) {
            switch (type) {
                case BASE: return (T) new BaseState(option);
                case KINETIC: return (T) KineticRemoteRegistry.decodeTo(option, KineticRemoteState.class);
                default: return (T) new RemoteState(option);
            }
        }
        throw new CoreException(String.format("Cannot decode remote %s", option.toString()));
    }

    public static RemoteType decodeType(JSONOption option) throws CoreException {
        if (!option.isSomeObject()) {
            throw new CoreException(String.format("Cannot decode remote type of %s", option.toString()));
        }
        return RemoteType.decodeType(option.someObject());
    }

    public static <T> boolean isRegistered(Class<? extends T> classType) {
        return classType == RemoteConfig.class || classType == RemoteProto.class ||
            classType == RemoteState.class || classType == BaseConfig.class || classType == BaseProto.class ||
            classType == BaseState.class || KineticRemoteRegistry.isRegistered(classType);
    }

}
