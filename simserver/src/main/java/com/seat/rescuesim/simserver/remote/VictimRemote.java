package com.seat.rescuesim.simserver.remote;

import com.seat.rescuesim.common.remote.mobile.ground.VictimRemoteProto;
import com.seat.rescuesim.common.remote.mobile.ground.VictimRemoteState;

public class VictimRemote extends MobileRemote {

    public VictimRemote(VictimRemoteProto proto, String remoteID, boolean active) {
        super(proto, remoteID, active);
    }

    @Override
    public VictimRemoteProto getProto() {
        return (VictimRemoteProto) super.getProto();
    }

    @Override
    public VictimRemoteState getState() {
        return new VictimRemoteState(this.getRemoteID(), this.getLocation(), this.getBattery(), this.isActive(),
            this.getVelocity(), this.getAcceleration());
    }

}
