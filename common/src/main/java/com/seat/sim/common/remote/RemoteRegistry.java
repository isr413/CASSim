package com.seat.sim.common.remote;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.seat.sim.common.core.CommonException;
import com.seat.sim.common.json.JSONException;
import com.seat.sim.common.json.JSONOptional;
import com.seat.sim.common.remote.kinematics.Kinematics;
import com.seat.sim.common.sensor.SensorConfig;

/** A registry of Remote prototypes and supporting serializable types. */
public class RemoteRegistry {
    public static final String REMOTE_TYPE = "remote_type";

    private static final Map<String, Function<JSONOptional, Object>> REGISTRY =
            new HashMap<String, Function<JSONOptional, Object>>(){{
        put(RemoteConfig.class.getName(), (optional) -> new RemoteConfig(optional));
        put(RemoteProto.class.getName(), (optional) -> new RemoteProto(optional));
        put(RemoteState.class.getName(), (optional) -> new RemoteState(optional));
    }};

    public static RemoteProto Remote(Kinematics kinematics, Collection<SensorConfig> sensors) {
        return new RemoteProto(kinematics, sensors);
    }

    @SuppressWarnings("unchecked")
    public static <T> T decodeTo(Class<T> cls, JSONOptional optional) throws CommonException, JSONException {
        String remoteType = RemoteRegistry.decodeType(optional);
        if (!RemoteRegistry.isRegistered(remoteType)) {
            if (!RemoteRegistry.isRegistered(cls)) {
                throw new CommonException(String.format("Cannot decode remote %s", optional.toString()));
            }
            return (T) RemoteRegistry.REGISTRY.get(cls.getName()).apply(optional);
        }
        return (T) RemoteRegistry.REGISTRY.get(remoteType).apply(optional);
    }

    public static String decodeType(JSONOptional optional) throws CommonException {
        if (!optional.isPresentObject()) {
            throw new CommonException(String.format("Cannot decode remote type of %s", optional.toString()));
        }
        return optional.getObject().getString(RemoteRegistry.REMOTE_TYPE);
    }

    public static <T> boolean isRegistered(Class<T> cls) {
        return RemoteRegistry.isRegistered(cls.getName());
    }

    public static boolean isRegistered(String className) {
        return RemoteRegistry.REGISTRY.containsKey(className);
    }

}
