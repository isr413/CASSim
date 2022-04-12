package com.seat.sim.server.remote;

import com.seat.sim.common.core.TeamColor;
import com.seat.sim.common.remote.intent.IntentionSet;
import com.seat.sim.common.remote.mobile.aerial.DroneRemoteProto;
import com.seat.sim.common.remote.mobile.aerial.DroneRemoteState;
import com.seat.sim.server.core.SimException;
import com.seat.sim.server.scenario.SimScenario;

public class DroneRemote extends MobileRemote {

    public DroneRemote(DroneRemoteProto proto, String remoteID, TeamColor team, boolean active) {
        super(proto, remoteID, team, active);
    }

    @Override
    public DroneRemoteProto getProto() {
        return (DroneRemoteProto) super.getProto();
    }

    @Override
    public DroneRemoteState getState() {
        return new DroneRemoteState(this.getRemoteID(), this.getTeam(), this.getLocation(), this.getBattery(),
            this.isActive(), this.getVelocity(), this.getAcceleration());
    }

    @Override
    public void update(SimScenario scenario, IntentionSet intentions, double stepSize) throws SimException {
        super.update(scenario, intentions, stepSize);
        if (this.isDisabled() || this.isInactive() || this.isDone()) {
            return;
        }
        double batteryUsage = this.getProto().getStaticBatteryUsage();
        batteryUsage += this.getAcceleration().getProjectionXY().getMagnitude() *
            this.getProto().getHorizontalKineticBatteryUsage();
        batteryUsage += this.getAcceleration().getZ() * this.getProto().getVerticalKineticBatteryUsage();
        this.updateBattery(batteryUsage, stepSize);
    }

}
