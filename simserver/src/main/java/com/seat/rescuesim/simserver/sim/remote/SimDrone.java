package com.seat.rescuesim.simserver.sim.remote;

import com.seat.rescuesim.common.drone.DroneSpec;
import com.seat.rescuesim.common.drone.DroneState;
import com.seat.rescuesim.common.drone.DroneType;
import com.seat.rescuesim.common.remote.RemoteController;
import com.seat.rescuesim.simserver.sim.SimScenario;
import com.seat.rescuesim.simserver.sim.util.SimException;

public class SimDrone extends KineticSimRemote {

    public SimDrone(DroneSpec spec, String label) {
        super(spec, label);
    }

    public DroneSpec getSpec() {
        return (DroneSpec) this.spec;
    }

    public DroneType getSpecType() {
        return this.getSpec().getSpecType();
    }

    public DroneState getState() {
        return new DroneState(
            this.getSpecType(),
            this.label,
            this.location,
            this.battery,
            this.getSensorState(),
            this.velocity,
            this.acceleration
        );
    }

    @Override
    public void update(SimScenario scenario, RemoteController controller, double stepSize) throws SimException {
        super.update(scenario, controller, stepSize);
        if (this.isInactive() || this.isDone()) {
            return;
        }
        double batteryUsage = this.getSpec().getStaticBatteryUsage();
        batteryUsage += this.getAcceleration().getProjectionXY().getMagnitude() *
            this.getSpec().getHorizontalKineticBatteryUsage();
        batteryUsage += this.getAcceleration().getZ() * this.getSpec().getVerticalKineticBatteryUsage();
        this.updateBattery(batteryUsage, stepSize);
    }

}
