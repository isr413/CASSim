package com.seat.sim.client.core.util;

import com.seat.sim.common.remote.mobile.aerial.drone.DroneRemoteState;

public class Drone extends Remote {

    public Drone(String remoteID) {
        super(remoteID);
    }

    @Override
    public DroneRemoteState getRemoteState() {
        return (DroneRemoteState) super.getRemoteState();
    }

}
