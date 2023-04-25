package com.seat.sim.client.sandbox.rescue;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import com.seat.sim.client.sandbox.rescue.util.RescueScenario;
import com.seat.sim.client.sandbox.rescue.util.TaskManager;
import com.seat.sim.common.math.Grid;
import com.seat.sim.common.remote.intent.IntentionSet;
import com.seat.sim.common.scenario.Snapshot;
import com.seat.sim.common.util.ArgsParser;
import com.seat.sim.common.util.Range;

public class Heatmap extends RescueScenario {

  private int[][] heatmap;

  public Heatmap(ArgsParser args, int threadID, long seed) throws IOException {
    super("Heatmap", 1, 0, 1024, 0, new Range(0., .1, 1., true), 0., 0., 1, threadID, 4, seed);
  }

  public Optional<TaskManager> getTaskManager() {
    return Optional.empty();
  }

  private void report() {
    int sum = 0;
    for (int i = 0; i < this.heatmap.length; i++) {
      for (int j = 0; j < this.heatmap[i].length; j++) {
        sum += this.heatmap[i][j];
      }
    }
    for (int i = 0; i < this.heatmap.length; i++) {
      for (int j = 0; j < this.heatmap[i].length; j++) {
        System.out.printf("%.4f ", ((double) this.heatmap[i][j]) / sum);
      }
      System.out.println();
    }
  }

  @Override
  public void close() {
    this.report();
    super.close();
  }

  @Override
  public void init() {
    super.init();
    Grid grid = this.getGrid().get();
    this.heatmap = new int[grid.getHeightInZones()][grid.getWidthInZones()];
  }

  @Override
  public void reset() {
    this.report();
    super.reset();
    this.init();
  }

  @Override
  public Collection<IntentionSet> update(Snapshot snap) {
    Grid grid = this.getGrid().get();
    snap
      .getActiveRemoteStates()
      .stream()
      .filter(state -> state.hasTag(RescueScenario.VICTIM_TAG))
      .map(state -> state.getLocation())
      .map(loc -> grid.getZoneAtLocation(loc))
      .forEach(zone -> {
        int size = zone.getSize();
        double x = zone.getLocation().getX(), y = zone.getLocation().getY();
        this.heatmap[(int) (y / size)][(int) (x / size)]++;
      });
    return super.update(snap);
  }
}
