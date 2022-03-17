package com.seat.rescuesim.simserver.sim;

import java.util.ArrayList;

import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.remote.RemoteSpec;
import com.seat.rescuesim.common.remote.RemoteState;
import com.seat.rescuesim.common.remote.intent.Intention;

public abstract class SimAsset {

    protected double battery;
    protected String label;
    protected Vector location;
    protected RemoteSpec spec;

    public SimAsset(String label, RemoteSpec spec, Vector location) {
        this(label, spec, location, spec.getMaxBatteryPower());
    }

    public SimAsset(String label, RemoteSpec spec, Vector location, double battery) {
        this.label = label;
        this.spec = spec;
        this.location = location;
        this.battery = battery;
    }

    public String getAssetID() {
        return this.getLabel();
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

    public RemoteSpec getSpec() {
        return this.spec;
    }

    public abstract RemoteState getState();

    public void setBattery(double battery) {
        this.battery = battery;
    }

    public void setLocation(Vector location) {
        this.location = location;
    }

    public RemoteState update(double stepSize) throws SimException {
        return this.update(null, stepSize);
    }

    public abstract RemoteState update(ArrayList<Intention> intentions, double stepSize) throws SimException;

}
