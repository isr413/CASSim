package com.seat.sim.common.remote;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.seat.sim.common.core.CommonException;
import com.seat.sim.common.core.TeamColor;
import com.seat.sim.common.json.JSONException;
import com.seat.sim.common.json.JSONOption;
import com.seat.sim.common.math.Vector;
import com.seat.sim.common.remote.base.BaseRemoteConfig;
import com.seat.sim.common.remote.base.BaseRemoteProto;
import com.seat.sim.common.remote.base.BaseRemoteState;
import com.seat.sim.common.remote.mobile.MobileRemoteConfig;
import com.seat.sim.common.remote.mobile.MobileRemoteProto;
import com.seat.sim.common.remote.mobile.MobileRemoteState;
import com.seat.sim.common.remote.mobile.aerial.AerialRemoteConfig;
import com.seat.sim.common.remote.mobile.aerial.AerialRemoteProto;
import com.seat.sim.common.remote.mobile.aerial.AerialRemoteState;
import com.seat.sim.common.remote.mobile.aerial.drone.DroneRemoteConfig;
import com.seat.sim.common.remote.mobile.aerial.drone.DroneRemoteProto;
import com.seat.sim.common.remote.mobile.aerial.drone.DroneRemoteState;
import com.seat.sim.common.remote.mobile.victim.VictimRemoteConfig;
import com.seat.sim.common.remote.mobile.victim.VictimRemoteProto;
import com.seat.sim.common.remote.mobile.victim.VictimRemoteState;
import com.seat.sim.common.sensor.SensorConfig;

/** A registry of Remote prototypes and supporting serializable types. */
public class RemoteRegistry {
    public static final String REMOTE_TYPE = "remote_type";

    private static final Map<String, Function<JSONOption, Object>> REGISTRY =
            new HashMap<String, Function<JSONOption, Object>>(){{
        put(RemoteConfig.class.getName(), (option) -> new RemoteConfig(option));
        put(RemoteProto.class.getName(), (option) -> new RemoteProto(option));
        put(RemoteState.class.getName(), (option) -> new RemoteState(option));
        put(BaseRemoteConfig.class.getName(), (option) -> new BaseRemoteConfig(option));
        put(BaseRemoteProto.class.getName(), (option) -> new BaseRemoteProto(option));
        put(BaseRemoteState.class.getName(), (option) -> new BaseRemoteState(option));
        put(MobileRemoteConfig.class.getName(), (option) -> new MobileRemoteConfig(option));
        put(MobileRemoteProto.class.getName(), (option) -> new MobileRemoteProto(option));
        put(MobileRemoteState.class.getName(), (option) -> new MobileRemoteState(option));
        put(AerialRemoteConfig.class.getName(), (option) -> new AerialRemoteConfig(option));
        put(AerialRemoteProto.class.getName(), (option) -> new AerialRemoteProto(option));
        put(AerialRemoteState.class.getName(), (option) -> new AerialRemoteState(option));
        put(DroneRemoteConfig.class.getName(), (option) -> new DroneRemoteConfig(option));
        put(DroneRemoteProto.class.getName(), (option) -> new DroneRemoteProto(option));
        put(DroneRemoteState.class.getName(), (option) -> new DroneRemoteState(option));
        put(VictimRemoteConfig.class.getName(), (option) -> new VictimRemoteConfig(option));
        put(VictimRemoteProto.class.getName(), (option) -> new VictimRemoteProto(option));
        put(VictimRemoteState.class.getName(), (option) -> new VictimRemoteState(option));
    }};

    public static AerialRemoteProto AerialRemote(Vector location, double maxBatteryPower,
            Collection<SensorConfig> sensors, double maxVelocity, double maxAcceleration, Vector batteryUsage) {
        return new AerialRemoteProto(location, maxBatteryPower, sensors, maxVelocity, maxAcceleration, batteryUsage);
    }

    public static BaseRemoteProto BaseRemote(Vector location, double maxBatteryPower,
            Collection<SensorConfig> sensors) {
        return new BaseRemoteProto(location, maxBatteryPower, sensors);
    }

    public static AerialRemoteConfig ConfigureAerialRemote(Vector location, double maxBatteryPower,
            Collection<SensorConfig> sensors, double maxVelocity, double maxAcceleration, Vector batteryUsage,
            TeamColor team, int count, boolean active, boolean dynamic, double speedMean, double speedStdDev) {
        return new AerialRemoteConfig(
            new AerialRemoteProto(location, maxBatteryPower, sensors, maxVelocity, maxAcceleration, batteryUsage),
            team,
            count,
            active,
            dynamic,
            speedMean,
            speedStdDev
        );
    }

    public static AerialRemoteConfig ConfigureAerialRemote(Vector location, double maxBatteryPower,
            Collection<SensorConfig> sensors, double maxVelocity, double maxAcceleration, Vector batteryUsage,
            TeamColor team, Collection<String> remoteIDs, boolean active, boolean dynamic, double speedMean,
            double speedStdDev) {
        return new AerialRemoteConfig(
            new AerialRemoteProto(location, maxBatteryPower, sensors, maxVelocity, maxAcceleration, batteryUsage),
            team,
            remoteIDs,
            active,
            dynamic,
            speedMean,
            speedStdDev
        );
    }

    public static BaseRemoteConfig ConfigureBaseRemote(Vector location, double maxBatteryPower,
            Collection<SensorConfig> sensors, TeamColor team, int count, boolean active, boolean dynamic) {
        return new BaseRemoteConfig(new BaseRemoteProto(location, maxBatteryPower, sensors),
            team,
            count,
            active,
            dynamic
        );
    }

    public static RemoteConfig ConfigureBaseRemote(Vector location, double maxBatteryPower,
            Collection<SensorConfig> sensors, TeamColor team, Collection<String> remoteIDs, boolean active,
            boolean dynamic) {
        return new BaseRemoteConfig(new BaseRemoteProto(location, maxBatteryPower, sensors),
            team,
            remoteIDs,
            active,
            dynamic
        );
    }

    public static DroneRemoteConfig ConfigureDroneRemote(Vector location, double maxBatteryPower,
            Collection<SensorConfig> sensors, double maxVelocity, double maxAcceleration, Vector batteryUsage,
            TeamColor team, int count, boolean active, boolean dynamic, double speedMean, double speedStdDev) {
        return new DroneRemoteConfig(
            new DroneRemoteProto(location, maxBatteryPower, sensors, maxVelocity, maxAcceleration, batteryUsage),
            team,
            count,
            active,
            dynamic,
            speedMean,
            speedStdDev
        );
    }

    public static DroneRemoteConfig ConfigureDroneRemote(Vector location, double maxBatteryPower,
            Collection<SensorConfig> sensors, double maxVelocity, double maxAcceleration, Vector batteryUsage,
            TeamColor team, Collection<String> remoteIDs, boolean active, boolean dynamic, double speedMean,
            double speedStdDev) {
        return new DroneRemoteConfig(
            new DroneRemoteProto(location, maxBatteryPower, sensors, maxVelocity, maxAcceleration, batteryUsage),
            team,
            remoteIDs,
            active,
            dynamic,
            speedMean,
            speedStdDev
        );
    }

    public static RemoteConfig ConfigureMobileRemote(Vector location, double maxBatteryPower,
            Collection<SensorConfig> sensors, double maxVelocity, double maxAcceleration, TeamColor team, int count,
            boolean active, boolean dynamic, double speedMean, double speedStdDev) {
        return new MobileRemoteConfig(
            new MobileRemoteProto(location, maxBatteryPower, sensors, maxVelocity, maxAcceleration),
            team,
            count,
            active,
            dynamic,
            speedMean,
            speedStdDev
        );
    }

    public static MobileRemoteConfig ConfigureMobileRemote(Vector location, double maxBatteryPower,
            Collection<SensorConfig> sensors, double maxVelocity, double maxAcceleration, TeamColor team,
            Collection<String> remoteIDs, boolean active, boolean dynamic, double speedMean, double speedStdDev) {
        return new MobileRemoteConfig(
            new MobileRemoteProto(location, maxBatteryPower, sensors, maxVelocity, maxAcceleration),
            team,
            remoteIDs,
            active,
            dynamic,
            speedMean,
            speedStdDev
        );
    }

    public static VictimRemoteConfig ConfigureVictimRemote(Vector location, double maxBatteryPower,
            Collection<SensorConfig> sensors, double maxVelocity, double maxAcceleration, TeamColor team, int count,
            boolean active, boolean dynamic, double speedMean, double speedStdDev) {
        return new VictimRemoteConfig(
            new VictimRemoteProto(location, maxBatteryPower, sensors, maxVelocity, maxAcceleration),
            team,
            count,
            active,
            dynamic,
            speedMean,
            speedStdDev
        );
    }

    public static VictimRemoteConfig ConfigureVictimRemote(Vector location, double maxBatteryPower,
            Collection<SensorConfig> sensors, double maxVelocity, double maxAcceleration, TeamColor team,
            Collection<String> remoteIDs, boolean active, boolean dynamic, double speedMean, double speedStdDev) {
        return new VictimRemoteConfig(
            new VictimRemoteProto(location, maxBatteryPower, sensors, maxVelocity, maxAcceleration),
            team,
            remoteIDs,
            active,
            dynamic,
            speedMean,
            speedStdDev
        );
    }

    public static RemoteConfig ConfigureRemote(Vector location, double maxBatteryPower,
            Collection<SensorConfig> sensors, TeamColor team, int count, boolean active, boolean dynamic) {
        return new RemoteConfig(new RemoteProto(location, maxBatteryPower, sensors), team, count, active, dynamic);
    }

    public static RemoteConfig ConfigureRemote(Vector location, double maxBatteryPower,
            Collection<SensorConfig> sensors, TeamColor team, Collection<String> remoteIDs, boolean active,
            boolean dynamic) {
        return new RemoteConfig(new RemoteProto(location, maxBatteryPower, sensors), team, remoteIDs, active, dynamic);
    }

    public static DroneRemoteProto DroneRemote(Vector location, double maxBatteryPower,
            Collection<SensorConfig> sensors, double maxVelocity, double maxAcceleration, Vector batteryUsage) {
        return new DroneRemoteProto(location, maxBatteryPower, sensors, maxVelocity, maxAcceleration, batteryUsage);
    }

    public static MobileRemoteProto MobileRemote(Vector location, double maxBatteryPower,
            Collection<SensorConfig> sensors, double maxVelocity, double maxAcceleration) {
        return new MobileRemoteProto(location, maxBatteryPower, sensors, maxVelocity, maxAcceleration);
    }

    public static RemoteProto Remote(Vector location, double maxBatteryPower, Collection<SensorConfig> sensors) {
        return new RemoteProto(location, maxBatteryPower, sensors);
    }

    public static VictimRemoteProto VictimRemote(Vector location, double maxBatteryPower,
            Collection<SensorConfig> sensors, double maxVelocity, double maxAcceleration) {
        return new VictimRemoteProto(location, maxBatteryPower, sensors, maxVelocity, maxAcceleration);
    }

    @SuppressWarnings("unchecked")
    public static <T> T decodeTo(JSONOption option, Class<T> classType) throws CommonException, JSONException {
        String remoteType = RemoteRegistry.decodeType(option);
        if (!RemoteRegistry.isRegistered(remoteType)) {
            if (!RemoteRegistry.isRegistered(classType)) {
                throw new CommonException(String.format("Cannot decode remote %s", option.toString()));
            }
            return (T) RemoteRegistry.REGISTRY.get(classType.getName()).apply(option);
        }
        return (T) RemoteRegistry.REGISTRY.get(remoteType).apply(option);
    }

    public static String decodeType(JSONOption option) throws CommonException {
        if (!option.isSomeObject()) {
            throw new CommonException(String.format("Cannot decode remote type of %s", option.toString()));
        }
        return option.someObject().getString(RemoteRegistry.REMOTE_TYPE);
    }

    public static <T> boolean isRegistered(Class<T> classType) {
        return RemoteRegistry.isRegistered(classType.getName());
    }

    public static boolean isRegistered(String className) {
        return RemoteRegistry.REGISTRY.containsKey(className);
    }

}
