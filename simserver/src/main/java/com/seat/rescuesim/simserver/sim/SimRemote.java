package com.seat.rescuesim.simserver.sim;

import java.util.ArrayList;

import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.remote.RemoteSpec;
import com.seat.rescuesim.common.remote.RemoteState;
import com.seat.rescuesim.common.remote.intent.Intention;
import com.seat.rescuesim.common.util.Debugger;

public abstract class SimRemote {

    protected double battery;
    protected String label;
    protected Vector location;
    protected RemoteSpec spec;

    public SimRemote(String label, RemoteSpec spec, Vector location) {
        this(label, spec, location, spec.getMaxBatteryPower());
    }

    public SimRemote(String label, RemoteSpec spec, Vector location, double battery) {
        this.label = label;
        this.spec = spec;
        this.location = location;
        this.battery = battery;
    }

    public double getBattery() {
        return this.battery;
    }

    public String getLabel() {
        return this.label;
    }

    public Vector getLocation() {
        return this.location;
    }

    public String getRemoteID() {
        return this.getLabel();
    }

    public RemoteSpec getSpec() {
        return this.spec;
    }

    public abstract RemoteState getState();

    public boolean isKinetic() {
        return false;
    }

    public void setBattery(double battery) {
        if (battery < 0) {
            Debugger.logger.err(String.format("Battery cannot be negative on remote %s", this.getRemoteID()));
            battery = 0;
        }
        if (this.spec.getMaxBatteryPower() < battery) {
            Debugger.logger.err(String.format("Battery exceeds max battery power of remote %s", this.getRemoteID()));
        }
        this.battery = battery;
    }

    public void setLocation(Vector location) {
        this.location = location;
    }

    public void update(double stepSize) throws SimException {
        this.update(null, stepSize);
    }

    public void update(ArrayList<Intention> intentions, double stepSize) throws SimException {}

    protected void updateBattery(double usage, double stepSize) {
        this.battery += usage * stepSize;
    }

    protected void updateLocation(Vector delta, double stepSize) {
        this.location = Vector.add(this.location, Vector.scale(delta, stepSize));
    }

}
