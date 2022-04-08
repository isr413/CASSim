package com.seat.rescuesim.simserver.remote;

import com.seat.rescuesim.common.core.TeamColor;
import com.seat.rescuesim.common.remote.RemoteProto;
import com.seat.rescuesim.common.remote.base.BaseRemoteProto;
import com.seat.rescuesim.common.remote.mobile.MobileRemoteProto;
import com.seat.rescuesim.common.remote.mobile.aerial.DroneRemoteProto;
import com.seat.rescuesim.common.remote.mobile.ground.VictimRemoteProto;
import com.seat.rescuesim.simserver.core.SimException;

public class RemoteFactory {

    public static Remote getRemote(RemoteProto proto, String remoteID, TeamColor team, boolean active)
            throws SimException {
        switch (proto.getRemoteType()) {
            case BASE: return new BaseRemote((BaseRemoteProto) proto, remoteID, team, active);
            case DRONE: return new DroneRemote((DroneRemoteProto) proto, remoteID, team, active);
            case MOBILE: return new MobileRemote((MobileRemoteProto) proto, remoteID, team, active);
            case VICTIM: return new VictimRemote((VictimRemoteProto) proto, remoteID, team, active);
            default: return new Remote(proto, remoteID, team, active);
        }
    }

}
