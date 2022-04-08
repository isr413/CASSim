package com.seat.rescuesim.simserver.remote;

import com.seat.rescuesim.common.remote.base.BaseRemoteProto;
import com.seat.rescuesim.common.remote.base.BaseRemoteState;

public class BaseRemote extends Remote {

    public BaseRemote(BaseRemoteProto proto, String remoteID, boolean active) {
        super(proto, remoteID, active);
    }

    @Override
    public BaseRemoteProto getProto() {
        return (BaseRemoteProto) super.getProto();
    }

    @Override
    public BaseRemoteState getState() {
        return new BaseRemoteState(this.getRemoteID(), this.getLocation(), this.getBattery(), this.isActive());
    }

}
