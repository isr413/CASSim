package com.seat.rescuesim.common.remote;

import java.util.Collection;
import com.seat.rescuesim.common.core.CommonException;
import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.remote.base.BaseRemoteConfig;
import com.seat.rescuesim.common.remote.base.BaseRemoteProto;
import com.seat.rescuesim.common.remote.base.BaseRemoteState;
import com.seat.rescuesim.common.remote.mobile.MobileRemoteConfig;
import com.seat.rescuesim.common.remote.mobile.MobileRemoteProto;
import com.seat.rescuesim.common.remote.mobile.MobileRemoteState;
import com.seat.rescuesim.common.remote.mobile.aerial.DroneRemoteConfig;
import com.seat.rescuesim.common.remote.mobile.aerial.DroneRemoteProto;
import com.seat.rescuesim.common.remote.mobile.aerial.DroneRemoteState;
import com.seat.rescuesim.common.remote.mobile.ground.VictimRemoteConfig;
import com.seat.rescuesim.common.remote.mobile.ground.VictimRemoteProto;
import com.seat.rescuesim.common.remote.mobile.ground.VictimRemoteState;
import com.seat.rescuesim.common.sensor.SensorConfig;

/** A registry of Remote prototypes and supporting serializable types. */
public class RemoteRegistry {

    public static BaseRemoteProto Base(Vector location, double maxBatteryPower, Collection<SensorConfig> sensors) {
        return new BaseRemoteProto(location, maxBatteryPower, sensors);
    }

    public static DroneRemoteProto Drone(Vector location, double maxBatteryPower, Collection<SensorConfig> sensors,
            Vector batteryUsage, double maxVelocity, double maxAcceleration, double maxJerk) {
        return new DroneRemoteProto(location, maxBatteryPower, sensors, batteryUsage, maxVelocity, maxAcceleration,
            maxJerk);
    }

    public static MobileRemoteProto GenericMobileRemote(Vector location, double maxBatteryPower,
            Collection<SensorConfig> sensors, double maxVelocity, double maxAcceleration, double maxJerk) {
        return new MobileRemoteProto(location, maxBatteryPower, sensors, maxVelocity, maxAcceleration, maxJerk);
    }

    public static RemoteProto GenericRemote(Vector location, double maxBatteryPower,
            Collection<SensorConfig> sensors) {
        return new RemoteProto(location, maxBatteryPower, sensors);
    }

    public static VictimRemoteProto Victim(Vector location, double maxBatteryPower, Collection<SensorConfig> sensors,
            double speedMean, double speedStdDev, double maxVelocity, double maxAcceleration, double maxJerk) {
        return new VictimRemoteProto(location, maxBatteryPower, sensors, speedMean, speedStdDev, maxVelocity,
            maxAcceleration, maxJerk);
    }

    @SuppressWarnings("unchecked")
    public static <T> T decodeAerialRemote(JSONOption option, Class<T> classType, RemoteType remoteType)
            throws CommonException, JSONException {
        if (classType == RemoteConfig.class || classType == MobileRemoteConfig.class ||
                classType == DroneRemoteConfig.class) {
            switch (remoteType) {
                default: return (T) new DroneRemoteConfig(option);
            }
        }
        if (classType == RemoteProto.class || classType == MobileRemoteProto.class ||
                classType == DroneRemoteProto.class) {
            switch (remoteType) {
                default: return (T) new DroneRemoteProto(option);
            }
        }
        if (classType == RemoteState.class || classType == MobileRemoteState.class ||
                classType == DroneRemoteState.class) {
            switch (remoteType) {
                default: return (T) new DroneRemoteState(option);
            }
        }
        throw new CommonException(String.format("Cannot decode aerial remote %s", option.toString()));
    }

    @SuppressWarnings("unchecked")
    public static <T> T decodeBaseRemote(JSONOption option, Class<T> classType, RemoteType remoteType)
            throws CommonException, JSONException {
        if (classType == RemoteConfig.class || classType == BaseRemoteConfig.class) {
            switch (remoteType) {
                default: return (T) new BaseRemoteConfig(option);
            }
        }
        if (classType == RemoteProto.class || classType == BaseRemoteProto.class) {
            switch (remoteType) {
                default: return (T) new BaseRemoteProto(option);
            }
        }
        if (classType == RemoteState.class || classType == BaseRemoteState.class) {
            switch (remoteType) {
                default: return (T) new BaseRemoteState(option);
            }
        }
        throw new CommonException(String.format("Cannot decode base remote %s", option.toString()));
    }

    @SuppressWarnings("unchecked")
    public static <T> T decodeGroundRemote(JSONOption option, Class<T> classType, RemoteType remoteType)
            throws CommonException, JSONException {
        if (classType == RemoteConfig.class || classType == MobileRemoteConfig.class ||
                classType == VictimRemoteConfig.class) {
            switch (remoteType) {
                default: return (T) new VictimRemoteConfig(option);
            }
        }
        if (classType == RemoteProto.class || classType == MobileRemoteProto.class ||
                classType == VictimRemoteProto.class) {
            switch (remoteType) {
                default: return (T) new VictimRemoteProto(option);
            }
        }
        if (classType == RemoteState.class || classType == MobileRemoteState.class ||
                classType == VictimRemoteState.class) {
            switch (remoteType) {
                default: return (T) new VictimRemoteState(option);
            }
        }
        throw new CommonException(String.format("Cannot decode ground remote %s", option.toString()));
    }

    @SuppressWarnings("unchecked")
    public static <T> T decodeMobileRemote(JSONOption option, Class<T> classType, RemoteType remoteType)
            throws CommonException, JSONException {
        if (RemoteRegistry.isRegisteredAerialRemote(classType, remoteType)) {
            return RemoteRegistry.decodeAerialRemote(option, classType, remoteType);
        }
        if (RemoteRegistry.isRegisteredGroundRemote(classType, remoteType)) {
            return RemoteRegistry.decodeGroundRemote(option, classType, remoteType);
        }
        if (classType == RemoteConfig.class || classType == MobileRemoteConfig.class) {
            switch (remoteType) {
                default: return (T) new MobileRemoteConfig(option);
            }
        }
        if (classType == RemoteProto.class || classType == MobileRemoteProto.class) {
            switch (remoteType) {
                default: return (T) new MobileRemoteProto(option);
            }
        }
        if (classType == RemoteState.class || classType == MobileRemoteState.class) {
            switch (remoteType) {
                default: return (T) new MobileRemoteState(option);
            }
        }
        throw new CommonException(String.format("Cannot decode mobile remote %s", option.toString()));
    }

    @SuppressWarnings("unchecked")
    public static <T> T decodeTo(JSONOption option, Class<T> classType) throws CommonException, JSONException {
        RemoteType remoteType = RemoteRegistry.decodeType(option);
        if (RemoteRegistry.isRegisteredBaseRemote(classType, remoteType)) {
            return RemoteRegistry.decodeBaseRemote(option, classType, remoteType);
        }
        if (RemoteRegistry.isRegisteredMobileRemote(classType, remoteType)) {
            return RemoteRegistry.decodeMobileRemote(option, classType, remoteType);
        }
        if (classType == RemoteConfig.class) {
            switch (remoteType) {
                default: return (T) new RemoteConfig(option);
            }
        }
        if (classType == RemoteProto.class) {
            switch (remoteType) {
                default: return (T) new RemoteProto(option);
            }
        }
        if (classType == RemoteState.class) {
            switch (remoteType) {
                default: return (T) new RemoteState(option);
            }
        }
        throw new CommonException(String.format("Cannot decode remote %s", option.toString()));
    }

    public static RemoteType decodeType(JSONOption option) throws CommonException {
        if (!option.isSomeObject()) {
            throw new CommonException(String.format("Cannot decode remote type of %s", option.toString()));
        }
        return RemoteType.decodeType(option.someObject());
    }

    private static <T> boolean isRegisteredAerialRemote(Class<T> classType) {
        return classType == DroneRemoteConfig.class || classType == DroneRemoteProto.class ||
            classType == DroneRemoteState.class;
    }

    private static <T> boolean isRegisteredAerialRemote(Class<T> classType, RemoteType remoteType) {
        return RemoteRegistry.isRegisteredAerialRemote(classType) || remoteType.equals(RemoteType.DRONE);
    }

    private static <T> boolean isRegisteredBaseRemote(Class<T> classType) {
        return classType == BaseRemoteConfig.class || classType == BaseRemoteProto.class ||
            classType == BaseRemoteState.class;
    }

    private static <T> boolean isRegisteredBaseRemote(Class<T> classType, RemoteType remoteType) {
        return RemoteRegistry.isRegisteredBaseRemote(classType) || remoteType.equals(RemoteType.BASE);
    }

    private static <T> boolean isRegisteredGroundRemote(Class<T> classType) {
        return classType == VictimRemoteConfig.class || classType == VictimRemoteProto.class ||
            classType == VictimRemoteState.class;
    }

    private static <T> boolean isRegisteredGroundRemote(Class<T> classType, RemoteType remoteType) {
        return RemoteRegistry.isRegisteredGroundRemote(classType) || remoteType.equals(RemoteType.VICTIM);
    }

    private static <T> boolean isRegisteredMobileRemote(Class<T> classType) {
        return classType == MobileRemoteConfig.class || classType == MobileRemoteProto.class ||
            classType == MobileRemoteState.class || RemoteRegistry.isRegisteredAerialRemote(classType) ||
            RemoteRegistry.isRegisteredGroundRemote(classType);
    }

    private static <T> boolean isRegisteredMobileRemote(Class<T> classType, RemoteType remoteType) {
        return RemoteRegistry.isRegisteredMobileRemote(classType) || remoteType.equals(RemoteType.MOBILE) ||
            RemoteRegistry.isRegisteredAerialRemote(classType, remoteType) ||
            RemoteRegistry.isRegisteredGroundRemote(classType, remoteType);
    }

    public static <T> boolean isRegistered(Class<T> classType) {
        return classType == RemoteConfig.class || classType == RemoteProto.class ||
            classType == RemoteState.class || RemoteRegistry.isRegisteredBaseRemote(classType) ||
            RemoteRegistry.isRegisteredMobileRemote(classType);
    }

}
