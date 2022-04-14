package com.seat.sim.common.remote;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.seat.sim.common.core.CommonException;
import com.seat.sim.common.json.JSONException;
import com.seat.sim.common.json.JSONOptional;
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

    private static final Map<String, Function<JSONOptional, Object>> REGISTRY =
            new HashMap<String, Function<JSONOptional, Object>>(){{
        put(RemoteConfig.class.getName(), (optional) -> new RemoteConfig(optional));
        put(RemoteProto.class.getName(), (optional) -> new RemoteProto(optional));
        put(RemoteState.class.getName(), (optional) -> new RemoteState(optional));
        put(BaseRemoteConfig.class.getName(), (optional) -> new BaseRemoteConfig(optional));
        put(BaseRemoteProto.class.getName(), (optional) -> new BaseRemoteProto(optional));
        put(BaseRemoteState.class.getName(), (optional) -> new BaseRemoteState(optional));
        put(MobileRemoteConfig.class.getName(), (optional) -> new MobileRemoteConfig(optional));
        put(MobileRemoteProto.class.getName(), (optional) -> new MobileRemoteProto(optional));
        put(MobileRemoteState.class.getName(), (optional) -> new MobileRemoteState(optional));
        put(AerialRemoteConfig.class.getName(), (optional) -> new AerialRemoteConfig(optional));
        put(AerialRemoteProto.class.getName(), (optional) -> new AerialRemoteProto(optional));
        put(AerialRemoteState.class.getName(), (optional) -> new AerialRemoteState(optional));
        put(DroneRemoteConfig.class.getName(), (optional) -> new DroneRemoteConfig(optional));
        put(DroneRemoteProto.class.getName(), (optional) -> new DroneRemoteProto(optional));
        put(DroneRemoteState.class.getName(), (optional) -> new DroneRemoteState(optional));
        put(VictimRemoteConfig.class.getName(), (optional) -> new VictimRemoteConfig(optional));
        put(VictimRemoteProto.class.getName(), (optional) -> new VictimRemoteProto(optional));
        put(VictimRemoteState.class.getName(), (optional) -> new VictimRemoteState(optional));
    }};

    public static AerialRemoteProto AerialRemote(Vector location, double maxBatteryPower,
            Collection<SensorConfig> sensors, double maxVelocity, double maxAcceleration, Vector batteryUsage) {
        return new AerialRemoteProto(location, maxBatteryPower, sensors, maxVelocity, maxAcceleration, batteryUsage);
    }

    public static BaseRemoteProto BaseRemote(Vector location, double maxBatteryPower,
            Collection<SensorConfig> sensors) {
        return new BaseRemoteProto(location, maxBatteryPower, sensors);
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
    public static <T> T decodeTo(JSONOptional optional, Class<T> classType) throws CommonException, JSONException {
        String remoteType = RemoteRegistry.decodeType(optional);
        if (!RemoteRegistry.isRegistered(remoteType)) {
            if (!RemoteRegistry.isRegistered(classType)) {
                throw new CommonException(String.format("Cannot decode remote %s", optional.toString()));
            }
            return (T) RemoteRegistry.REGISTRY.get(classType.getName()).apply(optional);
        }
        return (T) RemoteRegistry.REGISTRY.get(remoteType).apply(optional);
    }

    public static String decodeType(JSONOptional optional) throws CommonException {
        if (!optional.isSomeObject()) {
            throw new CommonException(String.format("Cannot decode remote type of %s", optional.toString()));
        }
        return optional.someObject().getString(RemoteRegistry.REMOTE_TYPE);
    }

    public static <T> boolean isRegistered(Class<T> classType) {
        return RemoteRegistry.isRegistered(classType.getName());
    }

    public static boolean isRegistered(String className) {
        return RemoteRegistry.REGISTRY.containsKey(className);
    }

}
