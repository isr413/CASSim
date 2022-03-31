package com.seat.rescuesim.common.util;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.remote.RemoteConfig;
import com.seat.rescuesim.common.remote.RemoteConst;
import com.seat.rescuesim.common.remote.RemoteSpec;
import com.seat.rescuesim.common.remote.RemoteState;
import com.seat.rescuesim.common.remote.RemoteType;
import com.seat.rescuesim.common.remote.base.BaseConfig;
import com.seat.rescuesim.common.remote.base.BaseSpec;
import com.seat.rescuesim.common.remote.base.BaseState;
import com.seat.rescuesim.common.remote.drone.DroneConfig;
import com.seat.rescuesim.common.remote.drone.DroneSpec;
import com.seat.rescuesim.common.remote.drone.DroneState;
import com.seat.rescuesim.common.remote.victim.VictimConfig;
import com.seat.rescuesim.common.remote.victim.VictimSpec;
import com.seat.rescuesim.common.remote.victim.VictimState;

public class RemoteFactory {

    public static RemoteConfig decodeRemoteConfig(JSONOption option) throws JSONException {
        if (option.isSomeObject()) {
            RemoteType remoteType = RemoteFactory.decodeRemoteType(option);
            if (remoteType.equals(RemoteType.BASE)) {
                return new BaseConfig(option);
            }
            if (remoteType.equals(RemoteType.DRONE)) {
                return new DroneConfig(option);
            }
            if (remoteType.equals(RemoteType.VICTIM)) {
                return new VictimConfig(option);
            }
        }
        throw new JSONException(String.format("Cannot decode remote config of %s", option.toString()));
    }

    public static RemoteSpec decodeRemoteSpec(JSONOption option) throws JSONException {
        if (option.isSomeObject()) {
            RemoteType remoteType = RemoteFactory.decodeRemoteType(option);
            if (remoteType.equals(RemoteType.BASE)) {
                return new BaseSpec(option);
            }
            if (remoteType.equals(RemoteType.DRONE)) {
                return new DroneSpec(option);
            }
            if (remoteType.equals(RemoteType.VICTIM)) {
                return new VictimSpec(option);
            }
        }
        throw new JSONException(String.format("Cannot decode remote spec of %s", option.toString()));
    }

    public static RemoteState decodeRemoteState(JSONOption option) throws JSONException {
        if (option.isSomeObject()) {
            RemoteType remoteType = RemoteFactory.decodeRemoteType(option);
            if (remoteType.equals(RemoteType.BASE)) {
                return new BaseState(option);
            }
            if (remoteType.equals(RemoteType.DRONE)) {
                return new DroneState(option);
            }
            if (remoteType.equals(RemoteType.VICTIM)) {
                return new VictimState(option);
            }
        }
        throw new JSONException(String.format("Cannot decode remote state of %s", option.toString()));
    }

    public static RemoteType decodeRemoteType(JSONOption option) throws JSONException {
        if (option.isSomeObject()) {
            return RemoteType.values()[option.someObject().getInt(RemoteConst.REMOTE_TYPE)];
        }
        throw new JSONException(String.format("Cannot decode remote type of %s", option.toString()));
    }

}
