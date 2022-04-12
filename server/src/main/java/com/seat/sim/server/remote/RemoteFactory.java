package com.seat.sim.server.remote;

import com.seat.sim.common.core.TeamColor;
import com.seat.sim.common.remote.RemoteProto;
import com.seat.sim.common.remote.base.BaseRemoteProto;
import com.seat.sim.common.remote.mobile.MobileRemoteProto;
import com.seat.sim.common.remote.mobile.aerial.DroneRemoteProto;
import com.seat.sim.common.remote.mobile.ground.VictimRemoteProto;
import com.seat.sim.server.core.SimException;

public class RemoteFactory {

    public static Remote getRemote(RemoteProto proto, String remoteID, TeamColor team, boolean active)
            throws SimException {
        if (DroneRemoteProto.class.isAssignableFrom(proto.getClass()))
            return new DroneRemote((DroneRemoteProto) proto, remoteID, team, active);
        if (VictimRemoteProto.class.isAssignableFrom(proto.getClass()))
            return new VictimRemote((VictimRemoteProto) proto, remoteID, team, active);
        if (BaseRemoteProto.class.isAssignableFrom(proto.getClass()))
            return new BaseRemote((BaseRemoteProto) proto, remoteID, team, active);
        if (MobileRemoteProto.class.isAssignableFrom(proto.getClass()))
            return new MobileRemote((MobileRemoteProto) proto, remoteID, team, active);
        return new Remote(proto, remoteID, team, active);
    }

}
