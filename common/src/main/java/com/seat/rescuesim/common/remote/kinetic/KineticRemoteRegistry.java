package com.seat.rescuesim.common.remote.kinetic;

import java.util.Collection;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.remote.kinetic.drone.DroneConfig;
import com.seat.rescuesim.common.remote.kinetic.drone.DroneProto;
import com.seat.rescuesim.common.remote.kinetic.drone.DroneState;
import com.seat.rescuesim.common.remote.kinetic.victim.VictimConfig;
import com.seat.rescuesim.common.remote.kinetic.victim.VictimProto;
import com.seat.rescuesim.common.remote.kinetic.victim.VictimState;
import com.seat.rescuesim.common.sensor.SensorConfig;
import com.seat.rescuesim.common.util.CoreException;

/** A registry of Kinetic Remote prototypes and supporting serializable types. */
public class KineticRemoteRegistry {

    public static DroneProto Drone(Vector location, double maxBatteryPower, Collection<SensorConfig> sensors,
            Vector batteryUsage, double maxVelocity, double maxAcceleration, double maxJerk) {
        return new DroneProto(location, maxBatteryPower, sensors, batteryUsage, maxVelocity, maxAcceleration, maxJerk);
    }

    public static KineticRemoteProto Generic(Vector location, double maxBatteryPower, Collection<SensorConfig> sensors,
            double maxVelocity, double maxAcceleration, double maxJerk) {
        return new KineticRemoteProto(KineticRemoteType.GENERIC, location, maxBatteryPower, sensors, maxVelocity,
            maxAcceleration, maxJerk);
    }

    public static VictimProto Victim(Vector location, double maxBatteryPower, Collection<SensorConfig> sensors,
            double speedMean, double speedStdDev, double maxVelocity, double maxAcceleration, double maxJerk) {
        return new VictimProto(location, maxBatteryPower, sensors, speedMean, speedStdDev, maxVelocity, maxAcceleration,
            maxJerk);
    }

    @SuppressWarnings("unchecked")
    public static <T> T decodeTo(JSONOption option, Class<? extends T> classType) throws CoreException, JSONException {
        KineticRemoteType type = KineticRemoteRegistry.decodeType(option);
        if (classType == KineticRemoteConfig.class || classType == DroneConfig.class ||
                classType == VictimConfig.class) {
            switch (type) {
                case DRONE: return (T) new DroneConfig(option);
                case VICTIM: return (T) new VictimConfig(option);
                default: return (T) new KineticRemoteConfig(option);
            }
        }
        if (classType == KineticRemoteProto.class || classType == DroneProto.class || classType == VictimProto.class) {
            switch (type) {
                case DRONE: return (T) new DroneProto(option);
                case VICTIM: return (T) new VictimProto(option);
                default: return (T) new KineticRemoteProto(option);
            }
        }
        if (classType == KineticRemoteState.class || classType == DroneState.class || classType == VictimState.class) {
            switch (type) {
                case DRONE: return (T) new DroneState(option);
                case VICTIM: return (T) new VictimState(option);
                default: return (T) new KineticRemoteState(option);
            }
        }
        throw new CoreException(String.format("Cannot decode kinetic remote %s", option.toString()));
    }

    public static KineticRemoteType decodeType(JSONOption option) throws CoreException {
        if (!option.isSomeObject()) {
            throw new CoreException(String.format("Cannot decode kinetic remote type of %s", option.toString()));
        }
        return KineticRemoteType.decodeType(option.someObject());
    }

    public static <T> boolean isRegistered(Class<? extends T> classType) {
        return classType == KineticRemoteConfig.class || classType == KineticRemoteProto.class ||
            classType == KineticRemoteState.class || classType == DroneConfig.class || classType == DroneProto.class ||
            classType == DroneState.class || classType == VictimConfig.class || classType == VictimProto.class ||
            classType == VictimState.class;
    }

}
