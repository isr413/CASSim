package com.seat.sim.client.sandbox.recon.remote;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.seat.sim.client.core.AssetManager;
import com.seat.sim.client.core.DroneScenario;
import com.seat.sim.common.math.Vector;
import com.seat.sim.common.remote.RemoteState;
import com.seat.sim.common.remote.intent.IntentRegistry;
import com.seat.sim.common.remote.intent.IntentionSet;
import com.seat.sim.common.scenario.Snapshot;
import com.seat.sim.common.util.Range;

public class IntelManager implements AssetManager {

  private Map<String, Integer> advAssignment;
  private int advCount;
  private Map<String, Double> popupTimes;
  private int intelCount;
  private int popupCount;
  private Range popupTime;
  private DroneScenario scenario;
  private Set<String> surveys;

  public IntelManager(DroneScenario scenario, int intelCount, int popupCount, int advCount, Range popupTime) {
    this.scenario = scenario;
    this.intelCount = intelCount;
    this.popupCount = popupCount;
    this.advCount = advCount;
    this.advAssignment = new HashMap<>();
    this.popupTime = popupTime;
    this.popupTimes = new HashMap<>();
  }

  public void close() {}

  public int getAdvAssignment(String intelID) {
    if (!this.advAssignment.containsKey(intelID)) {
      int remAdv = advCount - this.advAssignment.values().stream().mapToInt(x -> x).sum();
      int remAssets = this.getAssetCount() - this.surveys.size();
      if (remAdv == 0 || remAssets == 0) {
        this.advAssignment.put(intelID, 0);
      } else if (remAssets == 1) {
        this.advAssignment.put(intelID, remAdv);
      } else {
        double avgAdv = (double) remAdv / remAssets;
        int roll = Math.max(0, (int) (this.scenario.getRng().getRng().nextGaussian() + avgAdv));
        this.advAssignment.put(intelID, roll);
      }
    }
    return this.advAssignment.get(intelID);
  }

  public int getAdvCount() {
    return this.advCount;
  }

  public int getAssetCount() {
    return this.getIntelCount() + this.getPopupCount();
  }

  public int getIntelCount() {
    return this.intelCount;
  }

  public int getPopupCount() {
    return this.popupCount;
  }

  public void init() {
    this.surveys = new HashSet<>();
    this.advAssignment = new HashMap<>();
    this.popupTimes = new HashMap<>();
  }

  public boolean isDone(String intelID) {
    return this.surveys.contains(intelID);
  }

  public boolean isHidden(Snapshot snap, RemoteState state) {
    if (!this.popupTimes.containsKey(state.getRemoteID())) {
      this.popupTimes.put(state.getRemoteID(), this.popupTime.uniform(this.scenario.getRng()));
    }
    double time = this.popupTimes.get(state.getRemoteID());
    return time < snap.getTime() && !Vector.near(time, snap.getTime());
  }

  public void reset() {
    this.init();
  }

  public void setDone(String intelID) {
    this.surveys.add(intelID);
  }

  public Collection<IntentionSet> update(Snapshot snap) {
    return this.update(
        snap,
        snap
          .getActiveRemoteStates()
          .stream()
          .filter(state -> state.hasTag(ReconScenario.INTEL_TAG) || state.hasTag(ReconScenario.INTEL_POPUP_TAG))
          .toList()
      );
  }

  public Collection<IntentionSet> update(Snapshot snap, Collection<RemoteState> intel) {
    if (intel.isEmpty()) {
      return Collections.emptyList();
    }
    return intel
      .stream()
      .map(intelCache -> {
          IntentionSet intent = new IntentionSet(intelCache.getRemoteID());
          if (this.isDone(intelCache.getRemoteID())) {
            intent.addIntention(IntentRegistry.Done());
            return intent;
          }
          intent.addIntention(IntentRegistry.Stop());
          return intent;
        })
      .toList();
  }
}
