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

public class QuadTaskManager implements TaskManager {

  private List<Zone> quadZones;
  private RescueScenario scenario;
  private HeatmapTaskManager smgWalk;

  public QuadTaskManager(RescueScenario scenario) {
    this.scenario = scenario;
    this.smgWalk = new StochasticHeatmap(scenario);
    this.quadZones = List.of();
  }

  public QuadTaskManager(RescueScenario scenario, boolean useDefer, boolean useMovement, boolean useMax) {
    this.scenario = scenario;
    this.smgWalk = new StochasticHeatmap(scenario, useDefer, useMovement, useMax);
    this.quadZones = List.of();
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

  public void addTask(Zone task) {
    this.smgWalk.addTask(task);
  }

  public void close() {
    this.smgWalk.close();
  }

  public Optional<Zone> getNextTask(Snapshot snap, RemoteState state) {
    if (this.hasQuadZones()) {
      return Optional.of(this.quadZones.remove(this.quadZones.size() - 1));
    }

    return this.smgWalk.getNextTask(snap, state);
  }

  public boolean hasNextTask() {
    return this.hasQuadZones() || this.smgWalk.hasNextTask();
  }

  public void init() {
    this.smgWalk.init();
    this.quadZones = this.getQuadZones();
  }

  public void reset() {
    this.smgWalk.reset();
    this.quadZones = this.getQuadZones();
  }

  public void update(Snapshot snap) {
    this.smgWalk.update(snap);
  }
}
