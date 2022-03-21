package com.seat.rescuesim.common.util;

import com.seat.rescuesim.common.base.BaseState;
import com.seat.rescuesim.common.drone.DroneState;
import com.seat.rescuesim.common.json.*;
import com.seat.rescuesim.common.remote.RemoteConst;
import com.seat.rescuesim.common.remote.RemoteState;
import com.seat.rescuesim.common.remote.RemoteType;
import com.seat.rescuesim.common.victim.VictimState;

public class RemoteFactory {

    public static RemoteType decodeRemoteType(JSONObject json) throws JSONException {
        return RemoteType.values()[json.getInt(RemoteConst.REMOTE_TYPE)];
    }

    public static RemoteType decodeRemoteType(JSONOption option) throws JSONException {
        if (option.isSomeObject()) {
            return RemoteFactory.decodeRemoteType(option.someObject());
        }
        throw new JSONException(String.format("Cannot decode remote type of %s", option.toString()));
    }

    public static RemoteType decodeRemoteType(String encoding) throws JSONException {
        return RemoteFactory.decodeRemoteType(JSONOption.String(encoding));
    }

    public static RemoteState decodeRemoteState(JSONObject json) throws JSONException {
        RemoteType remoteType = RemoteFactory.decodeRemoteType(json);
        if (remoteType.equals(RemoteType.BASE)) {
            return new BaseState(json);
        } else if (remoteType.equals(RemoteType.DRONE)) {
            return new DroneState(json);
        } else if (remoteType.equals(RemoteType.VICTIM)) {
            return new VictimState(json);
        }
        throw new JSONException(String.format("Cannot decode remote state of %s", json.toString()));
    }

    public static RemoteState decodeRemoteState(JSONOption option) throws JSONException {
        if (option.isSomeObject()) {
            return RemoteFactory.decodeRemoteState(option.someObject());
        }
        throw new JSONException(String.format("Cannot decode remote state of %s", option.toString()));
    }

    public static RemoteState decodeRemoteState(String encoding) throws JSONException {
        return RemoteFactory.decodeRemoteState(JSONOption.String(encoding));
    }

}
