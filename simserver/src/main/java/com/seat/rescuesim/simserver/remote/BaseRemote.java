package com.seat.rescuesim.simserver.remote;

import com.seat.rescuesim.common.core.TeamColor;
import com.seat.rescuesim.common.remote.base.BaseRemoteProto;
import com.seat.rescuesim.common.remote.base.BaseRemoteState;

public class BaseRemote extends Remote {

    public BaseRemote(BaseRemoteProto proto, String remoteID, TeamColor team, boolean active) {
        super(proto, remoteID, team, active);
    }

    @Override
    public BaseRemoteProto getProto() {
        return (BaseRemoteProto) super.getProto();
    }

    @Override
    public BaseRemoteState getState() {
        return new BaseRemoteState(this.getRemoteID(), this.getTeam(), this.getLocation(), this.getBattery(),
            this.isActive());
    }

}
