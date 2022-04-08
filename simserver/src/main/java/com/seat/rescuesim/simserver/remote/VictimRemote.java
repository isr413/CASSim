package com.seat.rescuesim.simserver.remote;

import com.seat.rescuesim.common.core.TeamColor;
import com.seat.rescuesim.common.remote.mobile.ground.VictimRemoteProto;
import com.seat.rescuesim.common.remote.mobile.ground.VictimRemoteState;

public class VictimRemote extends MobileRemote {

    public VictimRemote(VictimRemoteProto proto, String remoteID, TeamColor team, boolean active) {
        super(proto, remoteID, team, active);
    }

    @Override
    public VictimRemoteProto getProto() {
        return (VictimRemoteProto) super.getProto();
    }

    @Override
    public VictimRemoteState getState() {
        return new VictimRemoteState(this.getRemoteID(), this.getTeam(), this.getLocation(), this.getBattery(),
            this.isActive(), this.getVelocity(), this.getAcceleration());
    }

}
