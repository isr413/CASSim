package com.seat.rescuesim.common.remote;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.remote.kinetic.KineticRemoteConfig;
import com.seat.rescuesim.common.remote.kinetic.KineticRemoteProto;
import com.seat.rescuesim.common.remote.kinetic.KineticRemoteState;
import com.seat.rescuesim.common.remote.kinetic.KineticRemoteType;
import com.seat.rescuesim.common.remote.kinetic.drone.DroneConfig;
import com.seat.rescuesim.common.remote.kinetic.drone.DroneProto;
import com.seat.rescuesim.common.remote.kinetic.drone.DroneState;
import com.seat.rescuesim.common.remote.kinetic.victim.VictimConfig;
import com.seat.rescuesim.common.remote.kinetic.victim.VictimProto;
import com.seat.rescuesim.common.remote.kinetic.victim.VictimState;

public class RemoteFactory {

    public static KineticRemoteConfig decodeKineticRemoteConfig(JSONOption option) throws JSONException {
        if (option.isSomeObject()) {
            KineticRemoteType specType = RemoteFactory.decodeKineticRemoteType(option);
            if (specType.equals(KineticRemoteType.GENERIC)) {
                return new KineticRemoteConfig(option);
            }
            if (specType.equals(KineticRemoteType.DRONE)) {
                return new DroneConfig(option);
            }
            if (specType.equals(KineticRemoteType.VICTIM)) {
                return new VictimConfig(option);
            }
        }
        throw new JSONException(String.format("Cannot decode remote config of %s", option.toString()));
    }

    public static KineticRemoteProto decodeKineticRemoteSpec(JSONOption option) throws JSONException {
        if (option.isSomeObject()) {
            KineticRemoteType specType = RemoteFactory.decodeKineticRemoteType(option);
            if (specType.equals(KineticRemoteType.GENERIC)) {
                return new KineticRemoteProto(option);
            }
            if (specType.equals(KineticRemoteType.DRONE)) {
                return new DroneProto(option);
            }
            if (specType.equals(KineticRemoteType.VICTIM)) {
                return new VictimProto(option);
            }
        }
        throw new JSONException(String.format("Cannot decode remote spec of %s", option.toString()));
    }

    public static KineticRemoteState decodeKineticRemoteState(JSONOption option) throws JSONException {
        if (option.isSomeObject()) {
            KineticRemoteType specType = RemoteFactory.decodeKineticRemoteType(option);
            if (specType.equals(KineticRemoteType.GENERIC)) {
                return new KineticRemoteState(option);
            }
            if (specType.equals(KineticRemoteType.DRONE)) {
                return new DroneState(option);
            }
            if (specType.equals(KineticRemoteType.VICTIM)) {
                return new VictimState(option);
            }
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
            if (remoteType.equals(RemoteType.GENERIC)) {
                return new RemoteConfig(option);
            }
            if (remoteType.equals(RemoteType.KINETIC)) {
                return RemoteFactory.decodeKineticRemoteConfig(option);
            }
        }
        throw new JSONException(String.format("Cannot decode remote config of %s", option.toString()));
    }

    public static RemoteProto decodeRemoteSpec(JSONOption option) throws JSONException {
        if (option.isSomeObject()) {
            RemoteType remoteType = RemoteFactory.decodeRemoteType(option);
            if (remoteType.equals(RemoteType.GENERIC)) {
                return new RemoteProto(option);
            }
            if (remoteType.equals(RemoteType.KINETIC)) {
                return RemoteFactory.decodeKineticRemoteSpec(option);
            }
        }
        throw new JSONException(String.format("Cannot decode remote spec of %s", option.toString()));
    }

    public static RemoteState decodeRemoteState(JSONOption option) throws JSONException {
        if (option.isSomeObject()) {
            RemoteType remoteType = RemoteFactory.decodeRemoteType(option);
            if (remoteType.equals(RemoteType.GENERIC)) {
                return new RemoteState(option);
            }
            if (remoteType.equals(RemoteType.KINETIC)) {
                return RemoteFactory.decodeKineticRemoteState(option);
            }
        }
        throw new JSONException(String.format("Cannot decode remote state of %s", option.toString()));
    }

    public static RemoteType decodeRemoteType(JSONOption option) throws JSONException {
        if (option.isSomeObject()) {
            return RemoteType.decodeType(option.someObject());
        }
        throw new JSONException(String.format("Cannot decode remote type of %s", option.toString()));
    }

}
