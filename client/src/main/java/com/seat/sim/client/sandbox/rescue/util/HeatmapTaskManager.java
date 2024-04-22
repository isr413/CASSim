package com.seat.sim.client.sandbox.rescue.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.seat.sim.client.core.DroneScenario;
import com.seat.sim.common.math.Grid;
import com.seat.sim.common.math.Vector;
import com.seat.sim.common.math.Zone;
import com.seat.sim.common.remote.RemoteState;
import com.seat.sim.common.scenario.Snapshot;

public abstract class HeatmapTaskManager implements TaskManager {

  private static final double DIAGONAL_WEIGHT = 0.075;
  private static final double ORDINAL_WEIGHT = 0.175;

  protected HashMap<Integer, Integer> defer;
  protected DroneScenario scenario;
  protected boolean useDefer;
  protected boolean useMax;
  protected boolean useMovement;
  protected double[][] zones;

  public HeatmapTaskManager(DroneScenario scenario) {
    this(scenario, true, true, true);
  }

  public HeatmapTaskManager(DroneScenario scenario, boolean useDefer, boolean useMovement, boolean useMax) {
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

  protected double getHeat() {
    return this.getHeat(this.zones);
  }

  protected double getHeat(double[][] zones) {
    double weight = 0.;
    for (int i = 0; i < this.zones.length; i++) {
      for (int j = 0; j < this.zones[i].length; j++) {
        weight += this.zones[i][j];
      }
    }
    double count = this.zones.length * this.zones[0].length;
    return (count - weight) / count;
  }

  protected int getZoneKey(Zone zone) {
    int gridSize = this.scenario.getGrid().get().getWidthInZones(), size = zone.getSize();
    double x = zone.getLocation().getX(), y = zone.getLocation().getY();
    return ((int) (y / size)) * gridSize + ((int) (x / size));
  }

  protected double getZoneWeight(Zone zone) {
    return this.getZoneWeight(zones, zone);
  }

  private double[][] computeWeights(double[][] zones) {
    double[][] weights = new double[zones.length][zones[0].length];
    for (int i = 0; i < zones.length; i++) {
      for (int j = 0; j < zones[i].length; j++) {
        double updateWeight = zones[i][j] * (1 - this.scenario.getAlpha());
        int prevI = (i > 0) ? (i - 1) : (zones.length - 1);
        int nextI = (i < zones.length - 1) ? (i + 1) : 0;
        int prevJ = (j > 0) ? (j - 1) : (zones[i].length - 1);
        int nextJ = (j < zones[i].length - 1) ? (j + 1) : 0;
        weights[prevI][prevJ] += (updateWeight * HeatmapTaskManager.DIAGONAL_WEIGHT);
        weights[nextI][prevJ] += (updateWeight * HeatmapTaskManager.DIAGONAL_WEIGHT);
        weights[nextI][nextJ] += (updateWeight * HeatmapTaskManager.DIAGONAL_WEIGHT);
        weights[prevI][nextJ] += (updateWeight * HeatmapTaskManager.DIAGONAL_WEIGHT);
        weights[i][j] += zones[i][j] * this.scenario.getAlpha();
        weights[prevI][j] += (updateWeight * HeatmapTaskManager.ORDINAL_WEIGHT);
        weights[i][nextJ] += (updateWeight * HeatmapTaskManager.ORDINAL_WEIGHT);
        weights[nextI][j] += (updateWeight * HeatmapTaskManager.ORDINAL_WEIGHT);
        weights[i][prevJ] += (updateWeight * HeatmapTaskManager.ORDINAL_WEIGHT);
      }
    }
    return weights;
  }

  private double getZoneWeight(double[][] zones, Zone zone) {
    double x = zone.getLocation().getX(), y = zone.getLocation().getY(), size = zone.getSize();
    return zones[(int) (y / size)][(int) (x / size)];
  }

  private Vector predict(Vector location, double[][] zones) {
    List<Zone> neighbors = this.scenario.getGrid().get().getNeighborhood(location);
    if (neighbors.isEmpty()) {
      return location;
    }
    Collections.sort(neighbors, new Comparator<Zone>() {
      @Override
      public int compare(Zone a, Zone b) {
        double aWeight = getZoneWeight(zones, a);
        double bWeight = getZoneWeight(zones, b);
        if (Vector.near(aWeight, bWeight)) {
          double aDist = Vector.dist(a.getLocation(), scenario.getGridCenter());
          double bDist = Vector.dist(b.getLocation(), scenario.getGridCenter());
          return Double.compare(aDist, bDist);
        }
        return Double.compare(bWeight, aWeight);
      }
    });
    return neighbors.get(0).getLocation();
  }

  private void reportHeat() {
    this.scenario.report(scenario.getMissionLength(), "Heat: %.4f", this.getHeat());
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

  private void updateWeights(double[][] zones, double[][] drones) {
    for (int i = 0; i < zones.length; i++) {
      for (int j = 0; j < zones[i].length; j++) {
        zones[i][j] *= ((1. - drones[i][j]) + drones[i][j] * (1. - this.scenario.getPhi()));
      }
    }
  }

  private void updateZones(Stream<RemoteState> drones) {
    int size = this.scenario.getGrid().get().getZoneSize();
    drones.forEach(drone -> this.scanZone(drone.getLocation().getX(), drone.getLocation().getY(), size));
    if (this.scenario.getBaseCount() > 0) {
      this.scanForBase(scenario.getGridCenter().getX(), scenario.getGridCenter().getY(), size);
    }
    if (!this.useMovement) {
      return;
    }
    this.zones = this.computeWeights(this.zones);
  }

  public void addTask(Zone task) {}

  public void close() {
    this.reportHeat();
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

  public double predict(Snapshot snap, List<RemoteState> states, double eDeadline, double eSuccess,
      double deadline, Optional<Double> mass) {
    double[][] simZones = new double[this.zones.length][];
    double[][] droneZones = new double[this.zones.length][];
    for (int i = 0; i < this.zones.length; i++) {
      simZones[i] = new double[this.zones[i].length];
      System.arraycopy(this.zones[i], 0, simZones[i], 0, this.zones[i].length);
      droneZones[i] = new double[this.zones[i].length];
    }

    List<RemoteState> drones = snap
      .getActiveRemoteStates()
      .stream()
      .filter(s -> s.hasTag(scenario.getDroneTag()))
      .filter(s -> !states.contains(s))
      .collect(Collectors.toList());
    for (RemoteState s : drones) {
      double x = s.getLocation().getX(), y = s.getLocation().getY();
      int size = this.scenario.getGrid().get().getZoneSize();
      droneZones[(int) (y / size)][(int) (x / size)] = 1.;
    }

    List<Vector> locations = states.stream().map(state -> state.getLocation()).collect(Collectors.toList());

    double earlyLoss = 0.;
    for (int t = 0; t < (Vector.near(eDeadline, 0.) ? 0 : (int) Math.ceil(eDeadline)); t++) {
      simZones = this.computeWeights(simZones);

      final double[][] currZones = simZones;
      locations = locations.stream().map(location -> this.predict(location, currZones)).collect(Collectors.toList());
      for (Vector location : locations) {
        Zone choice = this.scenario.getGrid().get().getZoneAtLocation(location);
        earlyLoss += this.getZoneWeight(simZones, choice);
        double x = choice.getLocation().getX(), y = choice.getLocation().getY(), size = choice.getSize();
        simZones[(int) (y / size)][(int) (x / size)] *= (1. - this.scenario.getPhi());
      }

      droneZones = this.computeWeights(droneZones);
      this.updateWeights(simZones, droneZones);
    }
    earlyLoss *= this.scenario.getPhi();

    double lateLoss = 0.;
    for (int t = 0; t < (Vector.near(deadline, 0.) ? 0 : (int) Math.ceil(deadline)); t++) {
      simZones = this.computeWeights(simZones);

      final double[][] currZones = simZones;
      locations = locations.stream().map(location -> this.predict(location, currZones)).collect(Collectors.toList());
      for (Vector location : locations) {
        Zone choice = this.scenario.getGrid().get().getZoneAtLocation(location);
        lateLoss += this.getZoneWeight(simZones, choice);
        double x = choice.getLocation().getX(), y = choice.getLocation().getY(), size = choice.getSize();
        simZones[(int) (y / size)][(int) (x / size)] *= (1. - this.scenario.getPhi());
      }

      droneZones = this.computeWeights(droneZones);
      this.updateWeights(simZones, droneZones);
    }
    lateLoss *= this.scenario.getPhi();

    double loss = (earlyLoss + (1. - eSuccess) * lateLoss) / (this.zones.length * this.zones[0].length);
    if (mass.isEmpty()) {
      return loss;
    }

    return mass.get() / this.getHeat() * loss;
  }

  public void reset() {
    this.reportHeat();
    this.setZoneWeights();
  }

  public void update(Snapshot snap) {
    Stream<RemoteState> drones = snap
      .getActiveRemoteStates()
      .stream()
      .filter(state -> state.hasTag(scenario.getDroneTag()))
      .filter(state -> this.scenario.getCooldown(state.getRemoteID()) == 0);
    this.updateZones(drones);
    this.defer.clear();
  }
}

