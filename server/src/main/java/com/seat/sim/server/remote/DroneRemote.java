package com.seat.sim.server.remote;

import com.seat.sim.common.core.TeamColor;
import com.seat.sim.common.remote.mobile.aerial.drone.DroneRemoteProto;
import com.seat.sim.common.remote.mobile.aerial.drone.DroneRemoteState;

public class DroneRemote extends AerialRemote {

    public DroneRemote(DroneRemoteProto proto, String remoteID, TeamColor team, boolean active) {
        super(proto, remoteID, team, active);
    }

    @Override
    public DroneRemoteProto getProto() {
        return (DroneRemoteProto) super.getProto();
    }

    @Override
    public DroneRemoteState getRemoteState() {
        return new DroneRemoteState(this.getRemoteID(), this.getTeam(), this.getLocation(), this.getBattery(),
            this.isActive(), this.getVelocity(), this.getAcceleration());
    }

}
