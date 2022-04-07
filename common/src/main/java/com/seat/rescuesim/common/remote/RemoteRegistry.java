package com.seat.rescuesim.common.remote;

import java.util.Collection;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.remote.base.BaseConfig;
import com.seat.rescuesim.common.remote.base.BaseProto;
import com.seat.rescuesim.common.remote.base.BaseState;
import com.seat.rescuesim.common.remote.mobile.MobileRemoteConfig;
import com.seat.rescuesim.common.remote.mobile.MobileRemoteProto;
import com.seat.rescuesim.common.remote.mobile.MobileRemoteState;
import com.seat.rescuesim.common.remote.mobile.aerial.DroneConfig;
import com.seat.rescuesim.common.remote.mobile.aerial.DroneProto;
import com.seat.rescuesim.common.remote.mobile.aerial.DroneState;
import com.seat.rescuesim.common.remote.mobile.ground.VictimConfig;
import com.seat.rescuesim.common.remote.mobile.ground.VictimProto;
import com.seat.rescuesim.common.remote.mobile.ground.VictimState;
import com.seat.rescuesim.common.sensor.SensorConfig;
import com.seat.rescuesim.common.util.CoreException;

/** A registry of Remote prototypes and supporting serializable types. */
public class RemoteRegistry {

    public static BaseProto Base(Vector location, double maxBatteryPower, Collection<SensorConfig> sensors) {
        return new BaseProto(location, maxBatteryPower, sensors);
    }

    public static DroneProto Drone(Vector location, double maxBatteryPower, Collection<SensorConfig> sensors,
            Vector batteryUsage, double maxVelocity, double maxAcceleration, double maxJerk) {
        return new DroneProto(location, maxBatteryPower, sensors, batteryUsage, maxVelocity, maxAcceleration, maxJerk);
    }

    public static MobileRemoteProto GenericMobileRemote(Vector location, double maxBatteryPower,
            Collection<SensorConfig> sensors, double maxVelocity, double maxAcceleration, double maxJerk) {
        return new MobileRemoteProto(location, maxBatteryPower, sensors, maxVelocity, maxAcceleration, maxJerk);
    }

    public static RemoteProto GenericRemote(Vector location, double maxBatteryPower,
            Collection<SensorConfig> sensors) {
        return new RemoteProto(RemoteType.GENERIC, location, maxBatteryPower, sensors);
    }

    public static VictimProto Victim(Vector location, double maxBatteryPower, Collection<SensorConfig> sensors,
            double speedMean, double speedStdDev, double maxVelocity, double maxAcceleration, double maxJerk) {
        return new VictimProto(location, maxBatteryPower, sensors, speedMean, speedStdDev, maxVelocity, maxAcceleration,
            maxJerk);
    }

    @SuppressWarnings("unchecked")
    public static <T> T decodeAerialRemote(JSONOption option, Class<? extends T> classType, RemoteType remoteType)
            throws CoreException, JSONException {
        if (classType == RemoteConfig.class || classType == DroneConfig.class) {
            switch (remoteType) {
                default: return (T) new DroneConfig(option);
            }
        }
        if (classType == RemoteProto.class || classType == DroneProto.class) {
            switch (remoteType) {
                default: return (T) new DroneProto(option);
            }
        }
        if (classType == RemoteState.class || classType == DroneState.class) {
            switch (remoteType) {
                default: return (T) new DroneState(option);
            }
        }
        throw new CoreException(String.format("Cannot decode aerial remote %s", option.toString()));
    }

    @SuppressWarnings("unchecked")
    public static <T> T decodeBaseRemote(JSONOption option, Class<? extends T> classType, RemoteType remoteType)
            throws CoreException, JSONException {
        if (classType == RemoteConfig.class || classType == BaseConfig.class) {
            switch (remoteType) {
                default: return (T) new BaseConfig(option);
            }
        }
        if (classType == RemoteProto.class || classType == BaseProto.class) {
            switch (remoteType) {
                default: return (T) new BaseProto(option);
            }
        }
        if (classType == RemoteState.class || classType == BaseState.class) {
            switch (remoteType) {
                default: return (T) new BaseState(option);
            }
        }
        throw new CoreException(String.format("Cannot decode base remote %s", option.toString()));
    }

    @SuppressWarnings("unchecked")
    public static <T> T decodeGroundRemote(JSONOption option, Class<? extends T> classType, RemoteType remoteType)
            throws CoreException, JSONException {
        if (classType == RemoteConfig.class || classType == VictimConfig.class) {
            switch (remoteType) {
                default: return (T) new VictimConfig(option);
            }
        }
        if (classType == RemoteProto.class || classType == VictimProto.class) {
            switch (remoteType) {
                default: return (T) new VictimProto(option);
            }
        }
        if (classType == RemoteState.class || classType == VictimState.class) {
            switch (remoteType) {
                default: return (T) new VictimState(option);
            }
        }
        throw new CoreException(String.format("Cannot decode ground remote %s", option.toString()));
    }

    @SuppressWarnings("unchecked")
    public static <T> T decodeMobileRemote(JSONOption option, Class<? extends T> classType, RemoteType remoteType)
            throws CoreException, JSONException {
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
        throw new CoreException(String.format("Cannot decode mobile remote %s", option.toString()));
    }

    @SuppressWarnings("unchecked")
    public static <T> T decodeTo(JSONOption option, Class<? extends T> classType) throws CoreException, JSONException {
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
        throw new CoreException(String.format("Cannot decode remote %s", option.toString()));
    }

    public static RemoteType decodeType(JSONOption option) throws CoreException {
        if (!option.isSomeObject()) {
            throw new CoreException(String.format("Cannot decode remote type of %s", option.toString()));
        }
        return RemoteType.decodeType(option.someObject());
    }

    private static <T> boolean isRegisteredAerialRemote(Class<? extends T> classType) {
        return classType == DroneConfig.class || classType == DroneProto.class || classType == DroneState.class;
    }

    private static <T> boolean isRegisteredAerialRemote(Class<? extends T> classType, RemoteType remoteType) {
        return RemoteRegistry.isRegisteredAerialRemote(classType) || remoteType.equals(RemoteType.DRONE);
    }

    private static <T> boolean isRegisteredBaseRemote(Class<? extends T> classType) {
        return classType == BaseConfig.class || classType == BaseProto.class || classType == BaseState.class;
    }

    private static <T> boolean isRegisteredBaseRemote(Class<? extends T> classType, RemoteType remoteType) {
        return RemoteRegistry.isRegisteredBaseRemote(classType) || remoteType.equals(RemoteType.BASE);
    }

    private static <T> boolean isRegisteredGroundRemote(Class<? extends T> classType) {
        return classType == VictimConfig.class || classType == VictimProto.class || classType == VictimState.class;
    }

    private static <T> boolean isRegisteredGroundRemote(Class<? extends T> classType, RemoteType remoteType) {
        return RemoteRegistry.isRegisteredGroundRemote(classType) || remoteType.equals(RemoteType.VICTIM);
    }

    private static <T> boolean isRegisteredMobileRemote(Class<? extends T> classType) {
        return classType == MobileRemoteConfig.class || classType == MobileRemoteProto.class ||
            classType == MobileRemoteState.class || RemoteRegistry.isRegisteredAerialRemote(classType) ||
            RemoteRegistry.isRegisteredGroundRemote(classType);
    }

    private static <T> boolean isRegisteredMobileRemote(Class<? extends T> classType, RemoteType remoteType) {
        return RemoteRegistry.isRegisteredMobileRemote(classType) || remoteType.equals(RemoteType.MOBILE) ||
            RemoteRegistry.isRegisteredAerialRemote(classType, remoteType) ||
            RemoteRegistry.isRegisteredBaseRemote(classType, remoteType);
    }

    public static <T> boolean isRegistered(Class<? extends T> classType) {
        return classType == RemoteConfig.class || classType == RemoteProto.class ||
            classType == RemoteState.class || RemoteRegistry.isRegisteredBaseRemote(classType) ||
            RemoteRegistry.isRegisteredMobileRemote(classType);
    }

}
