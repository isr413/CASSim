package com.seat.sim.server.remote;

import com.seat.sim.common.core.TeamColor;
import com.seat.sim.common.remote.mobile.victim.VictimRemoteProto;
import com.seat.sim.common.remote.mobile.victim.VictimRemoteState;

public class VictimRemote extends MobileRemote {

    public VictimRemote(VictimRemoteProto proto, String remoteID, TeamColor team, boolean active) {
        super(proto, remoteID, team, active);
    }

    @Override
    public VictimRemoteProto getProto() {
        return (VictimRemoteProto) super.getProto();
    }

    @Override
    public VictimRemoteState getRemoteState() {
        return new VictimRemoteState(this.getRemoteID(), this.getTeam(), this.getLocation(), this.getBattery(),
            this.isActive(), this.getSensorStates(), this.getVelocity(), this.getAcceleration());
    }

}
