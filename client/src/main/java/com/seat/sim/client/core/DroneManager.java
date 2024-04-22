package com.seat.sim.client.core;

import java.util.Collection;
import java.util.Set;

import com.seat.sim.common.remote.intent.IntentionSet;
import com.seat.sim.common.scenario.Snapshot;

public interface DroneManager {

  public void close();

  public int getCooldown(String droneID);

  public Set<String> getDetectedAssets();

  public int getDroneCount();

  public void init();

  public boolean isDone(String droneID);

  public boolean isOnCooldown(String droneID);

  public void reset();

  public void setDone(String droneID);

  public Collection<IntentionSet> update(Snapshot snap);
}
