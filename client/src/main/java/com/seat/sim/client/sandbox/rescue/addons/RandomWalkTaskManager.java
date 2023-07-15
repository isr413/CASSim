package com.seat.sim.client.sandbox.rescue.addons;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.seat.sim.client.sandbox.rescue.util.RemoteUtil;
import com.seat.sim.client.sandbox.rescue.util.RescueScenario;
import com.seat.sim.client.sandbox.rescue.util.TaskManager;
import com.seat.sim.common.math.Grid;
import com.seat.sim.common.math.Zone;
import com.seat.sim.common.math.ZoneType;
import com.seat.sim.common.remote.RemoteState;
import com.seat.sim.common.scenario.Snapshot;

public class RandomWalkTaskManager implements TaskManager {
    private RescueScenario scenario;

    public RandomWalkTaskManager(RescueScenario scenario) {
      this.scenario = scenario;
    }

    private Optional<Zone> randomWalk(Snapshot snap, RemoteState state) {
      Grid grid = this.scenario.getGrid().get();
      if (!state.hasLocation() || !grid.hasZoneAtLocation(state.getLocation())) {
        return Optional.empty();
      }
      Zone zone = grid.getZoneAtLocation(state.getLocation());
      List<Zone> neighborhood = grid.getNeighborhood(zone);
      Collections.shuffle(neighborhood, this.scenario.getRng().unwrap());
      return neighborhood
        .stream()
        .filter(neighbor -> !neighbor.getLocation().equals(zone.getLocation()))
        .filter(neighbor -> !neighbor.hasZoneType(ZoneType.BLOCKED))
        .filter(neighbor -> RemoteUtil.validChoice(snap, state, zone))
        .findFirst();
    }

    public void addTask(Zone task) {}

    public Optional<Zone> getNextTask(Snapshot snap, RemoteState state) {
      return this.randomWalk(snap, state);
    }

    public boolean hasNextTask() {
      return true;
    }
}