package com.seat.sim.client.sandbox.rescue.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;

import com.seat.sim.client.sandbox.rescue.remote.RescueScenario;
import com.seat.sim.common.math.Vector;
import com.seat.sim.common.math.Zone;
import com.seat.sim.common.remote.RemoteState;
import com.seat.sim.common.scenario.Snapshot;

public class StochasticHeatmap extends HeatmapTaskManager {

  public StochasticHeatmap(RescueScenario scenario) {
    super(scenario);
  }

  public StochasticHeatmap(RescueScenario scenario, boolean useDefer, boolean useMovement, boolean useMax) {
    super(scenario, useDefer, useMovement, useMax);
  }

  @Override
  public Optional<Zone> getNextTask(Snapshot snap, RemoteState state) {
    Zone zone = this.scenario.getGrid().get().getZoneAtLocation(state.getLocation());
    List<Zone> neighborhood = this.scenario.getGrid().get().getNeighborhood(zone, false);
    Collections.shuffle(neighborhood, this.scenario.getRng().unwrap());

    if (neighborhood.isEmpty()) {
      return Optional.empty();
    }

    if (this.useDefer) {
      OptionalInt minColls = neighborhood
          .stream()
          .mapToInt(neighbor -> this.getColls(neighbor))
          .min();
      double normalSum = neighborhood
          .stream()
          .filter(neighbor -> this.getColls(neighbor) == minColls.getAsInt())
          .mapToDouble(neighbor -> this.getZoneWeight(neighbor))
          .sum();
      if (!Vector.near(normalSum, 0.)) {
        neighborhood = neighborhood
            .stream()
            .filter(neighbor -> this.getColls(neighbor) == minColls.getAsInt())
            .toList();
      }
    }
    if (this.useMax) {
      OptionalDouble maxWeight = neighborhood
          .stream()
          .mapToDouble(neighbor -> this.getZoneWeight(neighbor))
          .max();
      if (maxWeight.isPresent()) {
        neighborhood = neighborhood
            .stream()
            .filter(neighbor -> Vector.near(this.getZoneWeight(neighbor), maxWeight.getAsDouble()))
            .toList();
      }
      Zone choice = neighborhood.get(this.scenario.getRng().getRandomNumber(neighborhood.size()));
      if (this.useDefer) {
        this.deferZone(choice);
      }
      return Optional.of(choice);
    }
    double[] weights = neighborhood
        .stream()
        .mapToDouble(neighbor -> this.getZoneWeight(neighbor))
        .toArray();
    double normalSum = Arrays.stream(weights).sum();
    if (Vector.near(normalSum, 0.)) {
      Zone choice = neighborhood.get(this.scenario.getRng().getRandomNumber(neighborhood.size()));
      if (this.useDefer) {
        this.deferZone(choice);
      }
      return Optional.of(choice);
    }
    weights[0] /= normalSum;
    for (int i = 1; i < weights.length; i++) {
      weights[i] /= normalSum;
      weights[i] += weights[i - 1];
    }
    double roll = this.scenario.getRng().getRandomProbability();
    int idx = 0;
    while (idx < weights.length - 1 && weights[idx] < roll) {
      idx++;
    }
    Zone choice = neighborhood.get(idx);
    if (this.useDefer) {
      this.deferZone(choice);
    }
    return Optional.of(choice);
  }
}
