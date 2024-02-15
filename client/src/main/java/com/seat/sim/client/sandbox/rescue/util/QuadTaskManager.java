package com.seat.sim.client.sandbox.rescue.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import com.seat.sim.client.sandbox.rescue.remote.RescueScenario;
import com.seat.sim.common.math.Grid;
import com.seat.sim.common.math.Vector;
import com.seat.sim.common.math.Zone;
import com.seat.sim.common.remote.RemoteState;
import com.seat.sim.common.scenario.Snapshot;

public class QuadTaskManager extends StochasticHeatmap {

  private List<Zone> quadZones;

  public QuadTaskManager(RescueScenario scenario) {
    super(scenario, true, true, true);
    this.quadZones = List.of();
  }

  public QuadTaskManager(RescueScenario scenario, boolean useDefer, boolean useMovement, boolean useMax) {
    super(scenario, useDefer, useMovement, useMax);
  }

  private List<Zone> getQuadZones() {
    Grid grid = this.scenario.getGrid().get();
    List<Zone> quadZones = new ArrayList<>();
    int gridWidth = grid.getWidthInZones(), gridHeight = grid.getHeightInZones();
    int numDrones = this.scenario.getDroneCount();
    int ranks = gridHeight / (int) Math.sqrt(numDrones);
    int rowsRemaining = gridHeight, dronesRemaining = numDrones;
    for (int i = 0; i < ranks; i++) {
      int rowsInRank = (int) Math.ceil(rowsRemaining / (ranks - i));
      int dronesInRank = (int) Math.ceil(dronesRemaining / (ranks - i));
      int colsRemaining = gridWidth;
      for (int j = 0; j < dronesInRank; j++) {
        int colsForDrone = (int) Math.ceil(colsRemaining / (dronesInRank - j));
        int x = gridWidth - colsRemaining + colsForDrone / 2;
        int y = gridHeight - rowsRemaining + rowsInRank / 2;
        quadZones.add(grid.getZone(x, y));
        colsRemaining -= colsForDrone;
      }
      dronesRemaining -= dronesInRank;
      rowsRemaining -= rowsInRank;
    }
    if (quadZones.isEmpty()) {
      return quadZones;
    }
    Collections.sort(quadZones, new Comparator<Zone>() {
      @Override public int compare(Zone z1, Zone z2) {
        return -Double.compare(
            Vector.dist(RescueScenario.GRID_CENTER, z1.getLocation()),
            Vector.dist(RescueScenario.GRID_CENTER, z2.getLocation())
          );
      }
    });
    return quadZones;
  }

  private boolean hasQuadZones() {
    return !this.quadZones.isEmpty();
  }

  @Override
  public Optional<Zone> getNextTask(Snapshot snap, RemoteState state) {
    if (this.hasQuadZones()) {
      return Optional.of(this.quadZones.remove(this.quadZones.size() - 1));
    }

    return super.getNextTask(snap, state);
  }

  @Override
  public boolean hasNextTask() {
    return this.hasQuadZones() || super.hasNextTask();
  }

  @Override
  public void init() {
    super.init();
    this.quadZones = this.getQuadZones();
  }

  @Override
  public void reset() {
    super.reset();
    this.quadZones = this.getQuadZones();
  }
}
