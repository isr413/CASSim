package com.seat.rescuesim.simserver.remote;

import com.seat.rescuesim.common.remote.intent.IntentionSet;
import com.seat.rescuesim.common.remote.mobile.aerial.DroneRemoteProto;
import com.seat.rescuesim.common.remote.mobile.aerial.DroneRemoteState;
import com.seat.rescuesim.simserver.core.SimException;
import com.seat.rescuesim.simserver.scenario.SimScenario;

public class DroneRemote extends MobileRemote {

    public DroneRemote(DroneRemoteProto proto, String remoteID, boolean active) {
        super(proto, remoteID, active);
    }

    @Override
    public DroneRemoteProto getProto() {
        return (DroneRemoteProto) super.getProto();
    }

    @Override
    public DroneRemoteState getState() {
        return (DroneRemoteState) super.getState();
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
