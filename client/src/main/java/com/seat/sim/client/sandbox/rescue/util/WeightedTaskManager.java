package com.seat.sim.client.sandbox.rescue.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

import com.seat.sim.client.sandbox.rescue.remote.RescueScenario;
import com.seat.sim.common.math.Vector;
import com.seat.sim.common.math.Zone;
import com.seat.sim.common.remote.RemoteState;
import com.seat.sim.common.scenario.Snapshot;

public class WeightedTaskManager extends HeatmapTaskManager {

  public WeightedTaskManager(RescueScenario scenario) {
    super(scenario);
  }

  public WeightedTaskManager(RescueScenario scenario, boolean useMovement) {
    super(scenario, true, useMovement, true);
  }

  @Override
  public Optional<Zone> getNextTask(Snapshot snap, RemoteState state) {
    Collection<Zone> neighborhood = this.scenario.getGrid().get().getNeighborhood(state.getLocation());

    if (neighborhood.isEmpty()) {
      return Optional.empty();
    }

    OptionalDouble maxWeight = neighborhood
      .stream()
      .filter(zone -> !this.defer.containsKey(this.getZoneKey(zone)))
      .mapToDouble(neighbor -> this.getZoneWeight(neighbor))
      .max();
    if (maxWeight.isEmpty()) {
      List<Zone> choices = new ArrayList<>(neighborhood);
      Collections.sort(choices, new Comparator<Zone>() {
        @Override
        public int compare(Zone z1, Zone z2) {
          return -Double.compare(
              Vector.dist(RescueScenario.GRID_CENTER, z1.getLocation()),
              Vector.dist(RescueScenario.GRID_CENTER, z2.getLocation())
            );
        }
      });
      Zone choice = choices.get(this.scenario.getRng().getRandomNumber(choices.size()));
      this.deferZone(choice);
      return Optional.of(choice);
    }
    List<Zone> choices = neighborhood
      .stream()
      .filter(neighbor -> Vector.near(this.getZoneWeight(neighbor), maxWeight.getAsDouble()))
      .toList();
    Zone choice = choices.get(this.scenario.getRng().getRandomNumber(choices.size()));
    this.deferZone(choice);
    return Optional.of(choice);
  }
}