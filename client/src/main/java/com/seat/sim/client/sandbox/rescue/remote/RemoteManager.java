package com.seat.sim.client.sandbox.rescue.remote;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import com.seat.sim.common.remote.RemoteConfig;
import com.seat.sim.common.remote.intent.IntentRegistry;
import com.seat.sim.common.remote.intent.IntentionSet;
import com.seat.sim.common.scenario.Snapshot;

public class RemoteManager {

  private int baseCount;
  private DroneManager droneManager;
  private Collection<RemoteConfig> remotes;
  private RescueScenario scenario;
  private VictimManager victimManager;

  public RemoteManager(RescueScenario scenario, int baseCount, int droneCount, int victimCount, int cooldown) {
    this.scenario = scenario;
    this.baseCount = baseCount;
    this.droneManager = new DroneManager(scenario, droneCount, cooldown);
    this.victimManager = new VictimManager(scenario, victimCount);
  }

  public void close() {
    this.droneManager.close();
    this.victimManager.close();
  }

  public int getBaseCount() {
    return this.baseCount;
  }

  public int getCooldown(String droneID) {
    return this.droneManager.getCooldown(droneID);
  }

  public int getDroneCount() {
    return this.droneManager.getDroneCount();
  }

  public DroneManager getDroneManager() {
    return this.droneManager;
  }

  public Collection<RemoteConfig> getRemoteConfigs() {
    return this.remotes;
  }

  public int getVictimCount() {
    return this.victimManager.getVictimCount();
  }

  public VictimManager getVictimManager() {
    return this.victimManager;
  }

  public boolean hasRemoteConfigs() {
    return this.remotes != null && !this.remotes.isEmpty();
  }

  public void init() {
    this.droneManager.init();
    this.victimManager.init();
  }

  public boolean isDone(String remoteID) {
    return this.droneManager.isDone(remoteID) || this.victimManager.isDone(remoteID);
  }

  public boolean isOnCooldown(String remoteID) {
    return this.droneManager.isOnCooldown(remoteID);
  }

  public void reset() {
    this.droneManager.reset();
    this.victimManager.reset();
  }

  public void setDone(String remoteID, boolean droneNotVictim) {
    if (droneNotVictim) {
      this.droneManager.setDone(remoteID);
    } else {
      this.victimManager.setDone(remoteID);
    }
  }

  public void setDroneManager(DroneManager manager) {
    this.droneManager = manager;
  }

  public void setRemoteConfigs(Collection<RemoteConfig> remotes) {
    this.remotes = remotes;
  }

  public void setVictimManager(VictimManager manager) {
    this.victimManager = manager;
  }

  public Collection<IntentionSet> update(Snapshot snap) {
    Collection<IntentionSet> droneIntentions = this.droneManager.update(snap);
    Collection<IntentionSet> victimIntentions = this.victimManager.update(snap);
    if (victimIntentions.isEmpty() && droneIntentions.isEmpty()) {
      IntentionSet intentions = new IntentionSet(this.scenario.getScenarioID());
      intentions.addIntention(IntentRegistry.Done());
      return List.of(intentions);
    }
    if (victimIntentions.isEmpty()) {
      return droneIntentions;
    }
    if (droneIntentions.isEmpty()) {
      return victimIntentions;
    }
    return Stream.concat(droneIntentions.stream(), victimIntentions.stream()).toList();
  }
}
