package com.seat.sim.client.sandbox.rescue.util;

import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Stream;

import com.seat.sim.client.sandbox.rescue.remote.RescueScenario;
import com.seat.sim.common.math.Grid;
import com.seat.sim.common.math.Zone;
import com.seat.sim.common.remote.RemoteState;
import com.seat.sim.common.scenario.Snapshot;

public abstract class HeatmapTaskManager implements TaskManager {

  private static final double DIAGONAL_WEIGHT = 0.075;
  private static final double ORDINAL_WEIGHT = 0.175;

  protected HashMap<Integer, Integer> defer;
  protected RescueScenario scenario;
  protected boolean useDefer;
  protected boolean useMax;
  protected boolean useMovement;
  protected double[][] zones;

  public HeatmapTaskManager(RescueScenario scenario) {
    this(scenario, true, true, true);
  }

  public HeatmapTaskManager(RescueScenario scenario, boolean useDefer, boolean useMovement, boolean useMax) {
    this.scenario = scenario;
    this.useDefer = useDefer;
    this.useMovement = useMovement;
    this.useMax = useMax;
  }

  protected void deferZone(Zone zone) {
    int key = this.getZoneKey(zone);
    if (!this.defer.containsKey(key)) {
      this.defer.put(key, 0);
    }
    this.defer.put(key, this.defer.get(key) + 1);
  }

  protected int getColls(Zone zone) {
    int key = this.getZoneKey(zone);
    return (this.defer.containsKey(key)) ? this.defer.get(key) : 0;
  }

  protected int getZoneKey(Zone zone) {
    int gridSize = this.scenario.getGrid().get().getWidthInZones(), size = zone.getSize();
    double x = zone.getLocation().getX(), y = zone.getLocation().getY();
    return ((int) (y / size)) * gridSize + ((int) (x / size));
  }

  protected double getZoneWeight(Zone zone) {
    double x = zone.getLocation().getX(), y = zone.getLocation().getY(), size = zone.getSize();
    return this.zones[(int) (y / size)][(int) (x / size)];
  }

  private void reportScore() {
    double weight = 0.;
    for (int i = 0; i < this.zones.length; i++) {
      for (int j = 0; j < this.zones[i].length; j++) {
        weight += this.zones[i][j];
      }
    }
    double count = this.zones.length * this.zones[0].length;
    this.scenario.report(RescueScenario.MISSION_LENGTH, "Score: %.4f", (count - weight) / count);
  }

  private void scanForBase(double x, double y, int size) {
    this.scanZone((x - size / 2.) / size, (y - size / 2.) / size, size, true);
    this.scanZone((x - size / 2.) / size, (y + size / 2.) / size, size, true);
    this.scanZone((x + size / 2.) / size, (y - size / 2.) / size, size, true);
    this.scanZone((x + size / 2.) / size, (y + size / 2.) / size, size, true);
  }

  private void scanZone(double x, double y, int size) {
    this.scanZone(x, y, size, false);
  }

  private void scanZone(double x, double y, int size, boolean base) {
    if (base) {
      this.zones[(int) (y / size)][(int) (x / size)] = 0;
      return;
    }
    this.zones[(int) (y / size)][(int) (x / size)] *= (1. - this.scenario.getPhi());
  }

  private void setZoneWeights() {
    for (int i = 0; i < this.zones.length; i++) {
      for (int j = 0; j < this.zones[i].length; j++) {
        this.zones[i][j] = 1.;
      }
    }
  }

  private void updateZones(Stream<RemoteState> drones) {
    int size = this.scenario.getGrid().get().getZoneSize();
    drones.forEach(drone -> this.scanZone(drone.getLocation().getX(), drone.getLocation().getY(), size));
    if (this.scenario.getBaseCount() > 0) {
      this.scanForBase(RescueScenario.GRID_CENTER.getX(), RescueScenario.GRID_CENTER.getY(), size);
    }
    if (!this.useMovement) {
      return;
    }
    double[][] weights = new double[this.zones.length][this.zones[0].length];
    for (int i = 0; i < this.zones.length; i++) {
      for (int j = 0; j < this.zones[i].length; j++) {
        double updateWeight = this.zones[i][j] * (1 - this.scenario.getAlpha());
        int prevI = (i > 0) ? (i - 1) : (this.zones.length - 1);
        int nextI = (i < this.zones.length - 1) ? (i + 1) : 0;
        int prevJ = (j > 0) ? (j - 1) : (this.zones[i].length - 1);
        int nextJ = (j < this.zones[i].length - 1) ? (j + 1) : 0;
        weights[prevI][prevJ] += (updateWeight * HeatmapTaskManager.DIAGONAL_WEIGHT);
        weights[nextI][prevJ] += (updateWeight * HeatmapTaskManager.DIAGONAL_WEIGHT);
        weights[nextI][nextJ] += (updateWeight * HeatmapTaskManager.DIAGONAL_WEIGHT);
        weights[prevI][nextJ] += (updateWeight * HeatmapTaskManager.DIAGONAL_WEIGHT);
        weights[i][j] += this.zones[i][j] * this.scenario.getAlpha();
        weights[prevI][j] += (updateWeight * HeatmapTaskManager.ORDINAL_WEIGHT);
        weights[i][nextJ] += (updateWeight * HeatmapTaskManager.ORDINAL_WEIGHT);
        weights[nextI][j] += (updateWeight * HeatmapTaskManager.ORDINAL_WEIGHT);
        weights[i][prevJ] += (updateWeight * HeatmapTaskManager.ORDINAL_WEIGHT);
      }
    }
    this.zones = weights;
  }

  public void addTask(Zone task) {}

  public void close() {
    this.reportScore();
  }

  public abstract Optional<Zone> getNextTask(Snapshot snap, RemoteState state);

  public boolean hasNextTask() {
    return true;
  }

  public void init() {
    Grid grid = this.scenario.getGrid().get();
    this.zones = new double[grid.getHeightInZones()][grid.getWidthInZones()];
    this.setZoneWeights();
    this.defer = new HashMap<>();
  }

  public void reset() {
    this.reportScore();
    this.setZoneWeights();
  }

  public void update(Snapshot snap) {
    Stream<RemoteState> drones = snap
      .getActiveRemoteStates()
      .stream()
      .filter(state -> state.hasTag(RescueScenario.DRONE_TAG))
      .filter(state -> this.scenario.getCooldown(state.getRemoteID()) == 0);
    this.updateZones(drones);
    this.defer.clear();
  }
}

