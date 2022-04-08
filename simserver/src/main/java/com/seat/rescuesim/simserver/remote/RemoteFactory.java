package com.seat.rescuesim.simserver.remote;

import com.seat.rescuesim.common.remote.RemoteProto;
import com.seat.rescuesim.common.remote.base.BaseRemoteProto;
import com.seat.rescuesim.common.remote.mobile.MobileRemoteProto;
import com.seat.rescuesim.common.remote.mobile.aerial.DroneRemoteProto;
import com.seat.rescuesim.common.remote.mobile.ground.VictimRemoteProto;
import com.seat.rescuesim.simserver.core.SimException;

public class RemoteFactory {

    public static Remote getRemote(RemoteProto proto, String remoteID, boolean active) throws SimException {
        switch (proto.getRemoteType()) {
            case BASE: return new BaseRemote((BaseRemoteProto) proto, remoteID, active);
            case DRONE: return new DroneRemote((DroneRemoteProto) proto, remoteID, active);
            case MOBILE: return new MobileRemote((MobileRemoteProto) proto, remoteID, active);
            case VICTIM: return new VictimRemote((VictimRemoteProto) proto, remoteID, active);
            default: return new Remote(proto, remoteID, active);
        }
    }

}
