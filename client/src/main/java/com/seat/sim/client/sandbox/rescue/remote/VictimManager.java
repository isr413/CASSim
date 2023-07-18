package com.seat.sim.client.sandbox.rescue.remote;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.seat.sim.common.math.Grid;
import com.seat.sim.common.math.Vector;
import com.seat.sim.common.math.Zone;
import com.seat.sim.common.math.ZoneType;
import com.seat.sim.common.remote.RemoteState;
import com.seat.sim.common.remote.intent.IntentRegistry;
import com.seat.sim.common.remote.intent.IntentionSet;
import com.seat.sim.common.scenario.Snapshot;

public class VictimManager {

  private Set<String> rescues;
  private RescueScenario scenario;
  private int victimCount;

  public VictimManager(RescueScenario scenario, int victimCount) {
    this.scenario = scenario;
    this.victimCount = victimCount;
  }

  private Optional<Vector> randomWalk(Snapshot snap, RemoteState victim) {
    Grid grid = this.scenario.getGrid().get();
    if (!victim.hasLocation() || !grid.hasZoneAtLocation(victim.getLocation())) {
      return Optional.empty();
    }
    Zone zone = grid.getZoneAtLocation(victim.getLocation());
    List<Zone> neighborhood = grid.getNeighborhood(zone, false);
    Collections.shuffle(neighborhood, this.scenario.getRng().unwrap());
    Optional<Zone> nextZone = neighborhood
      .stream()
      .filter(neighbor -> !neighbor.hasZoneType(ZoneType.BLOCKED))
      .findFirst();
    if (nextZone.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(nextZone.get().getLocation());
  }

  public void close() {}

  public int getVictimCount() {
    return this.victimCount;
  }

  public void init() {
    this.rescues = new HashSet<>();
  }

  public boolean isDone(String victimID) {
    return this.rescues.contains(victimID);
  }

  public void reset() {
    this.init();
  }

  public void setDone(String victimID) {
    this.rescues.add(victimID);
  }

  public Collection<IntentionSet> update(Snapshot snap) {
    return this.update(
        snap,
        snap
          .getActiveRemoteStates()
          .stream()
          .filter(state -> state.hasTag(RescueScenario.VICTIM_TAG))
          .toList()
      );
  }

  public Collection<IntentionSet> update(Snapshot snap, Collection<RemoteState> victims) {
    if (victims.isEmpty()) {
      return Collections.emptyList();
    }
    snap
      .getActiveRemoteStates()
      .stream()
      .filter(state -> state.hasTag(RescueScenario.BASE_TAG))
      .flatMap(state -> {
          return state.getSensorStateWithModel(RescueScenario.HUMAN_VISION).get().getSubjects().stream();
        })
      .filter(subjectID -> snap.getRemoteStateWithID(subjectID).hasTag(RescueScenario.VICTIM_TAG))
      .filter(victimID -> !this.isDone(victimID))
      .forEach(victimID -> {
          this.setDone(victimID);
          this.scenario.report(
              snap.getTime(),
              ":: %s :: %s :: %s :: Rescued victim",
              RescueScenario.BASE_TAG,
              RescueScenario.GRID_CENTER.toString("%.0f"),
              victimID
            );
        });
    return victims
      .stream()
      .map(victim -> {
          IntentionSet intent = new IntentionSet(victim.getRemoteID());
          if (this.isDone(victim.getRemoteID())) {
            intent.addIntention(IntentRegistry.Done());
            return intent;
          }
          if (this.scenario.getRng().getRandomProbability() < this.scenario.getAlpha()) {
            intent.addIntention(IntentRegistry.Stop());
            return intent;
          }
          Optional<Vector> location = this.randomWalk(snap, victim);
          if (location.isPresent()) {
            intent.addIntention(IntentRegistry.GoTo(location.get()));
          } else {
            intent.addIntention(IntentRegistry.Stop());
          }
          return intent;
        })
      .toList();
  }
}
