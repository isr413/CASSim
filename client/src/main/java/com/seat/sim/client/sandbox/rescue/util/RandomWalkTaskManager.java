package com.seat.sim.client.sandbox.rescue.util;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.seat.sim.client.core.DroneScenario;
import com.seat.sim.client.sandbox.rescue.remote.RemoteUtil;
import com.seat.sim.common.math.Grid;
import com.seat.sim.common.math.Vector;
import com.seat.sim.common.math.Zone;
import com.seat.sim.common.math.ZoneType;
import com.seat.sim.common.remote.RemoteState;
import com.seat.sim.common.scenario.Snapshot;

public class RandomWalkTaskManager implements TaskManager {
    private DroneScenario scenario;
    private boolean useMiddle;

    public RandomWalkTaskManager(DroneScenario scenario) {
      this(scenario, false);
    }

    public RandomWalkTaskManager(DroneScenario scenario, boolean useMiddle) {
      this.scenario = scenario;
      this.useMiddle = useMiddle;
    }

    private Optional<Zone> randomWalk(Snapshot snap, RemoteState state) {
      Grid grid = this.scenario.getGrid().get();
      if (!state.hasLocation() || !grid.hasZoneAtLocation(state.getLocation())) {
        return Optional.empty();
      }
      Zone zone = grid.getZoneAtLocation(state.getLocation());
      List<Zone> neighborhood = grid.getNeighborhood(zone, false)
        .stream()
        .filter(neighbor -> !neighbor.hasZoneType(ZoneType.BLOCKED))
        .filter(neighbor -> RemoteUtil.validChoice(snap, state, zone))
        .toList();
      if (neighborhood.isEmpty()) {
        return Optional.empty();
      }
      double diagonalWeight = Math.PI / (16. * (Math.PI - 0.5));
      double orthogonalWeight = (1. - 4 * diagonalWeight) / 4.;
      double[] weights = neighborhood
        .stream()
        .mapToDouble(z -> {
          if (Vector.near(zone.getLocation().getX() - z.getLocation().getX(), 0.) ||
              Vector.near(zone.getLocation().getY() - z.getLocation().getY(), 0.)) {
            return orthogonalWeight;
          } else {
            return diagonalWeight;
          }
        })
        .toArray();
      double norm = Arrays.stream(weights).sum();
      double roll = this.scenario.getRng().getRandomProbability();
      for (int i = 0; i < weights.length; i++) {
        roll -= (weights[i] / norm);
        if (roll <= 0. || Vector.near(roll, 0.)) {
          return Optional.of(neighborhood.get(i));
        }
      }
      return Optional.of(neighborhood.get(neighborhood.size() - 1));
      // Zone zone = grid.getZoneAtLocation(state.getLocation());
      // List<Zone> neighborhood = grid.getNeighborhood(zone, this.useMiddle);
      // Collections.shuffle(neighborhood, this.scenario.getRng().unwrap());
      // return neighborhood
      //   .stream()
      //   .filter(neighbor -> !neighbor.hasZoneType(ZoneType.BLOCKED))
      //   .filter(neighbor -> RemoteUtil.validChoice(snap, state, zone))
      //   .findFirst();
    }

    public void addTask(Zone task) {}

    public Optional<Zone> getNextTask(Snapshot snap, RemoteState state) {
      return this.randomWalk(snap, state);
    }

    public boolean hasNextTask() {
      return true;
    }

    public double predict(Snapshot snap, RemoteState state, double eDeadline, double eSuccess,
        double deadline, Optional<Double> mass) {
      return 0.;
    }
}
