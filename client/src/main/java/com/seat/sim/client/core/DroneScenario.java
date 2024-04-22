package com.seat.sim.client.core;

import java.util.Optional;

import com.seat.sim.client.negotiation.Contract;
import com.seat.sim.client.sandbox.rescue.remote.RemoteManager;
import com.seat.sim.common.core.Application;
import com.seat.sim.common.math.Vector;
import com.seat.sim.common.math.Zone;
import com.seat.sim.common.remote.RemoteState;
import com.seat.sim.common.scenario.Snapshot;
import com.seat.sim.common.util.Random;

public interface DroneScenario extends Application {

  public void addTask(Zone task);

  public double getAlpha();

  public int getBaseCount();

  public int getCooldown(String droneID);

  public int getDroneCount();

  public String getDroneTag();

  public Vector getGridCenter();

  public RemoteManager getManager();

  public int getMissionLength();

  public double getPhi();

  public Random getRng();

  public boolean hasNegotiations();

  public Optional<Contract> negotiate(Snapshot snap, RemoteState state, String senderID, String receiverID);

  public Optional<Zone> nextTask(Snapshot snap, RemoteState state);

  public void report(double simTime, String fmt, Object... args);

  public default boolean scoreContract(Snapshot snap, Contract contract) {
    return scoreContract(snap, contract, false);
  }

  public boolean scoreContract(Snapshot snap, Contract contract, boolean earlyCompletion);

  public void setDone(String remoteID, boolean droneNotOther);

  public boolean terminateContract(Contract contract);
}
