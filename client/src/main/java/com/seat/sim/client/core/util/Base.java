package com.seat.sim.client.core.util;

import com.seat.sim.common.remote.base.BaseRemoteState;

public class Base extends Remote {

    public Base(String remoteID) {
        super(remoteID);
    }

    @Override
    public BaseRemoteState getRemoteState() {
        return (BaseRemoteState) super.getRemoteState();
    }

}
