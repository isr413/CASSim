package com.seat.rescuesim.common.remote;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.remote.kinetic.KineticRemoteConfig;
import com.seat.rescuesim.common.remote.kinetic.KineticRemoteSpec;
import com.seat.rescuesim.common.remote.kinetic.KineticRemoteState;
import com.seat.rescuesim.common.remote.kinetic.KineticRemoteType;
import com.seat.rescuesim.common.remote.kinetic.drone.DroneConfig;
import com.seat.rescuesim.common.remote.kinetic.drone.DroneSpec;
import com.seat.rescuesim.common.remote.kinetic.drone.DroneState;
import com.seat.rescuesim.common.remote.kinetic.victim.VictimConfig;
import com.seat.rescuesim.common.remote.kinetic.victim.VictimSpec;
import com.seat.rescuesim.common.remote.kinetic.victim.VictimState;
import com.seat.rescuesim.common.remote.stat.StaticRemoteConfig;
import com.seat.rescuesim.common.remote.stat.StaticRemoteSpec;
import com.seat.rescuesim.common.remote.stat.StaticRemoteState;
import com.seat.rescuesim.common.remote.stat.StaticRemoteType;
import com.seat.rescuesim.common.remote.stat.base.BaseConfig;
import com.seat.rescuesim.common.remote.stat.base.BaseSpec;
import com.seat.rescuesim.common.remote.stat.base.BaseState;

public class RemoteFactory {

    public static KineticRemoteConfig decodeKineticRemoteConfig(JSONOption option) throws JSONException {
        if (option.isSomeObject()) {
            KineticRemoteType specType = RemoteFactory.decodeKineticRemoteType(option);
            if (specType.equals(KineticRemoteType.DRONE)) {
                return new DroneConfig(option);
            }
            if (specType.equals(KineticRemoteType.VICTIM)) {
                return new VictimConfig(option);
            }
            return new KineticRemoteConfig(option);
        }
        throw new JSONException(String.format("Cannot decode remote config of %s", option.toString()));
    }

    public static KineticRemoteSpec decodeKineticRemoteSpec(JSONOption option) throws JSONException {
        if (option.isSomeObject()) {
            KineticRemoteType specType = RemoteFactory.decodeKineticRemoteType(option);
            if (specType.equals(KineticRemoteType.DRONE)) {
                return new DroneSpec(option);
            }
            if (specType.equals(KineticRemoteType.VICTIM)) {
                return new VictimSpec(option);
            }
            return new KineticRemoteSpec(option);
        }
        throw new JSONException(String.format("Cannot decode remote spec of %s", option.toString()));
    }

    public static KineticRemoteState decodeKineticRemoteState(JSONOption option) throws JSONException {
        if (option.isSomeObject()) {
            KineticRemoteType specType = RemoteFactory.decodeKineticRemoteType(option);
            if (specType.equals(KineticRemoteType.DRONE)) {
                return new DroneState(option);
            }
            if (specType.equals(KineticRemoteType.VICTIM)) {
                return new VictimState(option);
            }
            return new KineticRemoteState(option);
        }
        throw new JSONException(String.format("Cannot decode remote state of %s", option.toString()));
    }

    public static KineticRemoteType decodeKineticRemoteType(JSONOption option) throws JSONException {
        if (option.isSomeObject()) {
            return KineticRemoteType.decodeType(option.someObject());
        }
        throw new JSONException(String.format("Cannot decode remote type of %s", option.toString()));
    }

    public static RemoteConfig decodeRemoteConfig(JSONOption option) throws JSONException {
        if (option.isSomeObject()) {
            RemoteType remoteType = RemoteFactory.decodeRemoteType(option);
            if (remoteType.equals(RemoteType.KINETIC)) {
                return RemoteFactory.decodeKineticRemoteConfig(option);
            }
            if (remoteType.equals(RemoteType.STATIC)) {
                return RemoteFactory.decodeStaticRemoteConfig(option);
            }
            return new RemoteConfig(option);
        }
        throw new JSONException(String.format("Cannot decode remote config of %s", option.toString()));
    }

    public static RemoteSpec decodeRemoteSpec(JSONOption option) throws JSONException {
        if (option.isSomeObject()) {
            RemoteType remoteType = RemoteFactory.decodeRemoteType(option);
            if (remoteType.equals(RemoteType.KINETIC)) {
                return RemoteFactory.decodeKineticRemoteSpec(option);
            }
            if (remoteType.equals(RemoteType.STATIC)) {
                return RemoteFactory.decodeStaticRemoteSpec(option);
            }
            return new RemoteSpec(option);
        }
        throw new JSONException(String.format("Cannot decode remote spec of %s", option.toString()));
    }

    public static RemoteState decodeRemoteState(JSONOption option) throws JSONException {
        if (option.isSomeObject()) {
            RemoteType remoteType = RemoteFactory.decodeRemoteType(option);
            if (remoteType.equals(RemoteType.KINETIC)) {
                return RemoteFactory.decodeKineticRemoteState(option);
            }
            if (remoteType.equals(RemoteType.STATIC)) {
                return RemoteFactory.decodeStaticRemoteState(option);
            }
            return new RemoteState(option);
        }
        throw new JSONException(String.format("Cannot decode remote state of %s", option.toString()));
    }

    public static RemoteType decodeRemoteType(JSONOption option) throws JSONException {
        if (option.isSomeObject()) {
            return RemoteType.decodeType(option.someObject());
        }
        throw new JSONException(String.format("Cannot decode remote type of %s", option.toString()));
    }

    public static StaticRemoteConfig decodeStaticRemoteConfig(JSONOption option) throws JSONException {
        if (option.isSomeObject()) {
            StaticRemoteType specType = RemoteFactory.decodeStaticRemoteType(option);
            if (specType.equals(StaticRemoteType.BASE)) {
                return new BaseConfig(option);
            }
            return new StaticRemoteConfig(option);
        }
        throw new JSONException(String.format("Cannot decode remote config of %s", option.toString()));
    }

    public static StaticRemoteSpec decodeStaticRemoteSpec(JSONOption option) throws JSONException {
        if (option.isSomeObject()) {
            StaticRemoteType specType = RemoteFactory.decodeStaticRemoteType(option);
            if (specType.equals(StaticRemoteType.BASE)) {
                return new BaseSpec(option);
            }
            return new StaticRemoteSpec(option);
        }
        throw new JSONException(String.format("Cannot decode remote spec of %s", option.toString()));
    }

    public static StaticRemoteState decodeStaticRemoteState(JSONOption option) throws JSONException {
        if (option.isSomeObject()) {
            StaticRemoteType specType = RemoteFactory.decodeStaticRemoteType(option);
            if (specType.equals(StaticRemoteType.BASE)) {
                return new BaseState(option);
            }
            return new StaticRemoteState(option);
        }
        throw new JSONException(String.format("Cannot decode remote state of %s", option.toString()));
    }

    public static StaticRemoteType decodeStaticRemoteType(JSONOption option) throws JSONException {
        if (option.isSomeObject()) {
            return StaticRemoteType.decodeType(option.someObject());
        }
        throw new JSONException(String.format("Cannot decode remote type of %s", option.toString()));
    }

}
