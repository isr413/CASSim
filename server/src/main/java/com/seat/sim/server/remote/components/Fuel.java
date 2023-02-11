package com.seat.sim.server.remote.components;

import com.seat.sim.common.math.Vector;
import com.seat.sim.common.remote.kinematics.FuelProto;
import com.seat.sim.server.core.SimException;

public class Fuel {

  private double fuelAmount;
  private FuelProto proto;

  public Fuel(FuelProto proto) {
    this.proto = (proto != null) ? proto : new FuelProto();
    this.fuelAmount = (proto.hasInitialFuel()) ? proto.getInitialFuel() : 0;
  }

  public double getFuelAmount() {
    return this.fuelAmount;
  }

  public Vector getFuelUsage() {
    return this.proto.getFuelUsage();
  }

  public double getMaxFuel() {
    return this.proto.getMaxFuel();
  }

  public FuelProto getProto() {
    return this.proto;
  }

  public boolean hasFuelUsage() {
    return this.proto.hasFuelUsage();
  }

  public boolean hasMaxFuel() {
    return this.proto.hasMaxFuel();
  }

  public boolean isEmpty() {
    return this.fuelAmount == 0;
  }

  public boolean isFull() {
    return this.hasMaxFuel() && this.fuelAmount == this.getMaxFuel();
  }

  public void updateFuel(double stepSize) throws SimException {
    if (!this.hasFuelUsage()) {
      return;
    }
    this.updateFuelBy(-this.getFuelUsage().getX(), stepSize);
  }

  public void updateFuelBy(double fuelUsage, double stepSize) throws SimException {
    if (this.hasMaxFuel()) {
      this.fuelAmount = Math.min(Math.max(this.fuelAmount + fuelUsage * stepSize, 0), this.getMaxFuel());
    } else {
      this.fuelAmount = Math.max(this.fuelAmount + fuelUsage * stepSize, 0);
    }
  }
}
