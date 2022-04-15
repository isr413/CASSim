package com.seat.sim.server.remote;

import com.seat.sim.common.core.TeamColor;
import com.seat.sim.common.remote.base.BaseRemoteProto;
import com.seat.sim.common.remote.base.BaseRemoteState;

public class BaseRemote extends Remote {

    public BaseRemote(BaseRemoteProto proto, String remoteID, TeamColor team, boolean active) {
        super(proto, remoteID, team, active);
    }

    @Override
    public BaseRemoteProto getProto() {
        return (BaseRemoteProto) super.getProto();
    }

    @Override
    public BaseRemoteState getRemoteState() {
        return new BaseRemoteState(this.getRemoteID(), this.getTeam(), this.getLocation(), this.getBattery(),
            this.isActive(), this.getSensorStates());
    }

}
