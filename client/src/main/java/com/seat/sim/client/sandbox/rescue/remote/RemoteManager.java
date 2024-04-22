package com.seat.sim.client.sandbox.rescue.remote;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import com.seat.sim.client.core.AssetManager;
import com.seat.sim.client.core.DroneScenario;
import com.seat.sim.common.remote.RemoteConfig;
import com.seat.sim.common.remote.intent.IntentRegistry;
import com.seat.sim.common.remote.intent.IntentionSet;
import com.seat.sim.common.scenario.Snapshot;

public class RemoteManager {

  private AssetManager assetManager;
  private int baseCount;
  private DroneManager droneManager;
  private Collection<RemoteConfig> remotes;
  private DroneScenario scenario;

  public RemoteManager(DroneScenario scenario, AssetManager assetManager, int baseCount, int droneCount, int cooldown) {
    this.scenario = scenario;
    this.baseCount = baseCount;
    this.droneManager = new DroneManager(scenario, droneCount, cooldown);
    this.assetManager = assetManager;
  }

  public void close() {
    this.droneManager.close();
    this.assetManager.close();
  }

  public int getAssetCount() {
    return this.assetManager.getAssetCount();
  }

  public AssetManager getAssetManager() {
    return this.assetManager;
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

  public boolean hasRemoteConfigs() {
    return this.remotes != null && !this.remotes.isEmpty();
  }

  public void init() {
    this.droneManager.init();
    this.assetManager.init();
  }

  public boolean isDone(String remoteID) {
    return this.droneManager.isDone(remoteID) || this.assetManager.isDone(remoteID);
  }

  public boolean isOnCooldown(String remoteID) {
    return this.droneManager.isOnCooldown(remoteID);
  }

  public void reset() {
    this.droneManager.reset();
    this.assetManager.reset();
  }

  public void setAssetManager(AssetManager manager) {
    this.assetManager = manager;
  }

  public void setDone(String remoteID, boolean droneNotVictim) {
    if (droneNotVictim) {
      this.droneManager.setDone(remoteID);
    } else {
      this.assetManager.setDone(remoteID);
    }
  }

  public void setDroneManager(DroneManager manager) {
    this.droneManager = manager;
  }

  public void setRemoteConfigs(Collection<RemoteConfig> remotes) {
    this.remotes = remotes;
  }

  public Collection<IntentionSet> update(Snapshot snap) {
    Collection<IntentionSet> droneIntentions = this.droneManager.update(snap);
    Collection<IntentionSet> victimIntentions = this.assetManager.update(snap);
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
