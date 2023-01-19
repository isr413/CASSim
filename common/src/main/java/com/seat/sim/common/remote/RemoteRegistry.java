package com.seat.sim.common.remote;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.seat.sim.common.core.CommonException;
import com.seat.sim.common.json.JsonException;
import com.seat.sim.common.json.Json;
import com.seat.sim.common.remote.kinematics.Kinematics;
import com.seat.sim.common.sensor.SensorConfig;

/** A registry of Remote prototypes and supporting serializable types. */
public class RemoteRegistry {
    public static final String REMOTE_TYPE = "remote_type";

    private static final Map<String, Function<Json, Object>> REGISTRY =
            new HashMap<String, Function<Json, Object>>(){{
        put(RemoteConfig.class.getName(), (json) -> new RemoteConfig(json));
        put(RemoteProto.class.getName(), (json) -> new RemoteProto(json));
        put(RemoteState.class.getName(), (json) -> new RemoteState(json));
    }};

    public static RemoteProto Remote(Kinematics kinematics, Collection<SensorConfig> sensors) {
        return new RemoteProto(kinematics, sensors);
    }

    @SuppressWarnings("unchecked")
    public static <T> T decodeTo(Class<T> cls, Json json) throws CommonException, JsonException {
        String remoteType = RemoteRegistry.decodeType(json);
        if (!RemoteRegistry.isRegistered(remoteType)) {
            if (!RemoteRegistry.isRegistered(cls)) {
                throw new CommonException(String.format("Cannot decode remote %s", json.toString()));
            }
            return (T) RemoteRegistry.REGISTRY.get(cls.getName()).apply(json);
        }
        return (T) RemoteRegistry.REGISTRY.get(remoteType).apply(json);
    }

    public static String decodeType(Json json) throws CommonException {
        if (!json.isJsonObject()) {
            throw new CommonException(String.format("Cannot decode remote type of %s", json.toString()));
        }
        return json.getJsonObject().getString(RemoteRegistry.REMOTE_TYPE);
    }

    public static <T> boolean isRegistered(Class<T> cls) {
        return RemoteRegistry.isRegistered(cls.getName());
    }

    public static boolean isRegistered(String className) {
        return RemoteRegistry.REGISTRY.containsKey(className);
    }
}
