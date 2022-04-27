package com.seat.sim.client.core.util;

import com.seat.sim.common.remote.mobile.victim.VictimRemoteState;

public class Victim extends Remote {

    public Victim(String remoteID) {
        super(remoteID);
    }

    @Override
    public VictimRemoteState getRemoteState() {
        return (VictimRemoteState) super.getRemoteState();
    }

}
