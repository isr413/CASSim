package com.seat.sim.client.sandbox.recon.remote;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.seat.sim.client.core.AssetManager;
import com.seat.sim.client.core.DroneScenario;
import com.seat.sim.common.remote.RemoteState;
import com.seat.sim.common.remote.intent.IntentRegistry;
import com.seat.sim.common.remote.intent.IntentionSet;
import com.seat.sim.common.scenario.Snapshot;

public class IntelManager implements AssetManager {

  private Set<String> rescues;
  private DroneScenario scenario;
  private int intelCount;

  public IntelManager(DroneScenario scenario, int intelCount) {
    this.scenario = scenario;
    this.intelCount = intelCount;
  }

  public void close() {}

  public int getAssetCount() {
    return this.getintelCount();
  }

  public int getintelCount() {
    return this.intelCount;
  }

  public void init() {
    this.rescues = new HashSet<>();
  }

  public boolean isDone(String intelID) {
    return this.rescues.contains(intelID);
  }

  public void reset() {
    this.init();
  }

  public void setDone(String intelID) {
    this.rescues.add(intelID);
  }

  public Collection<IntentionSet> update(Snapshot snap) {
    return this.update(
        snap,
        snap
          .getActiveRemoteStates()
          .stream()
          .filter(state -> state.hasTag(ReconScenario.INTEL_TAG))
          .toList()
      );
  }

  public Collection<IntentionSet> update(Snapshot snap, Collection<RemoteState> intels) {
    if (intels.isEmpty()) {
      return Collections.emptyList();
    }
    snap
      .getActiveRemoteStates()
      .stream()
      .filter(state -> state.hasTag(ReconScenario.BASE_TAG))
      .flatMap(state -> {
          return state.getSensorStateWithModel(ReconScenario.SENSOR_A_COMMS).get().getSubjects().stream();
        })
      .filter(subjectID -> snap.getRemoteStateWithID(subjectID).hasTag(ReconScenario.INTEL_TAG))
      .filter(intelID -> !this.isDone(intelID))
      .forEach(intelID -> {
          this.setDone(intelID);
          this.scenario.report(
              snap.getTime(),
              ":: %s :: %s :: %s :: Rescued intel",
              ReconScenario.BASE_TAG,
              ReconScenario.GRID_CENTER.toString("%.0f"),
              intelID
            );
        });
    return intels
      .stream()
      .map(intel -> {
          IntentionSet intent = new IntentionSet(intel.getRemoteID());
          if (this.isDone(intel.getRemoteID())) {
            intent.addIntention(IntentRegistry.Done());
            return intent;
          }
          intent.addIntention(IntentRegistry.Stop());
          return intent;
        })
      .toList();
  }
}
