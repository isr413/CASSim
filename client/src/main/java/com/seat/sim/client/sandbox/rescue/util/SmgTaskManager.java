package com.seat.sim.client.sandbox.rescue.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.stream.Stream;

import com.seat.sim.client.sandbox.rescue.remote.RescueScenario;
import com.seat.sim.common.math.Grid;
import com.seat.sim.common.math.Vector;
import com.seat.sim.common.math.Zone;
import com.seat.sim.common.remote.RemoteState;
import com.seat.sim.common.scenario.Snapshot;

public class SmgTaskManager implements TaskManager {

  private HashMap<Integer, Integer> defer;
  private RescueScenario scenario;
  private boolean useDefer;
  private boolean useMax;
  private boolean useMovement;
  private double[][] zones;

  public SmgTaskManager(RescueScenario scenario) {
    this(scenario, true, true, true);
  }

  public SmgTaskManager(RescueScenario scenario, boolean useDefer, boolean useMovement, boolean useMax) {
    this.scenario = scenario;
    this.useDefer = useDefer;
    this.useMovement = useMovement;
    this.useMax = useMax;
  }

  private void deferZone(Zone zone) {
    int key = this.getZoneKey(zone);
    if (!this.defer.containsKey(key)) {
      this.defer.put(key, 0);
    }
    this.defer.put(key, this.defer.get(key) + 1);
  }

  private int getColls(Zone zone) {
    int key = this.getZoneKey(zone);
    return (this.defer.containsKey(key)) ? this.defer.get(key) : 0;
  }

  private int getZoneKey(Zone zone) {
    int gridSize = this.scenario.getGrid().get().getWidthInZones(), size = zone.getSize();
    double x = zone.getLocation().getX(), y = zone.getLocation().getY();
    return ((int) (y / size)) * gridSize + ((int) (x / size));
  }

  private double getZoneWeight(Zone zone) {
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
    double updateWeight = (1. - this.scenario.getBeta()) * (1. - this.scenario.getGamma());
    this.zones[(int) (y / size)][(int) (x / size)] *= updateWeight;
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
        int neighbors = 8;
        if ((i == 0 || i == this.zones.length - 1) && (j == 0 || j == this.zones[i].length - 1)) {
          neighbors = 3;
        } else if (i == 0 || i == this.zones.length - 1 || j == 0 || j == this.zones[i].length - 1) {
          neighbors = 5;
        }
        double updateWeight = this.zones[i][j] * (1 - this.scenario.getAlpha()) / neighbors;
        if (i > 0) {
          if (j > 0) weights[i - 1][j - 1] += updateWeight;
          weights[i - 1][j] += updateWeight;
          if (j < this.zones[i].length - 1) weights[i - 1][j + 1] += updateWeight;
        }
        if (j > 0) weights[i][j - 1] += updateWeight;
        weights[i][j] += this.zones[i][j] * this.scenario.getAlpha();
        if (j < this.zones[i].length - 1) weights[i][j + 1] += updateWeight;
        if (i < this.zones.length - 1) {
          if (j > 0) weights[i + 1][j - 1] += updateWeight;
          weights[i + 1][j] += updateWeight;
          if (j < this.zones[i].length - 1) weights[i + 1][j + 1] += updateWeight;
        }
      }
    }
    this.zones = weights;
  }

  public void addTask(Zone task) {}

  public void close() {
    this.reportScore();
  }

  public Optional<Zone> getNextTask(Snapshot snap, RemoteState state) {
    Zone zone = this.scenario.getGrid().get().getZoneAtLocation(state.getLocation());
    List<Zone> neighborhood = this.scenario.getGrid().get().getNeighborhood(zone, false);
    Collections.shuffle(neighborhood, this.scenario.getRng().unwrap());
    if (neighborhood.isEmpty()) {
      return Optional.empty();
    }
    List<Zone> choices = neighborhood
      .stream()
      .filter(neighbor -> !this.useDefer || !this.defer.containsKey(this.getZoneKey(neighbor)))
      .toList();
    if (choices.isEmpty()) {
      OptionalInt minColls = neighborhood
        .stream()
        .mapToInt(neighbor -> this.getColls(neighbor))
        .min();
      if (minColls.isPresent()) {
        choices = neighborhood
          .stream()
          .filter(neighbor -> this.getColls(neighbor) == minColls.getAsInt())
          .toList();
      } else {
        choices = neighborhood;
      }
    }
    if (this.useMax) {
      OptionalDouble maxWeight = choices
        .stream()
        .mapToDouble(neighbor -> this.getZoneWeight(neighbor))
        .max();
      if (maxWeight.isPresent()) {
        choices = choices
          .stream()
          .filter(neighbor -> Vector.near(this.getZoneWeight(neighbor), maxWeight.getAsDouble()))
          .toList();
      }
      // choices = new ArrayList<>(choices);
      // Collections.sort(choices, new Comparator<Zone>() {
      //   @Override
      //   public int compare(Zone z1, Zone z2) {
      //     return -Double.compare(
      //         Vector.dist(RescueScenario.GRID_CENTER, z1.getLocation()),
      //         Vector.dist(RescueScenario.GRID_CENTER, z2.getLocation())
      //       );
      //   }
      // });
      // Zone choice = choices.get(0);
      Zone choice = choices.get(this.scenario.getRng().getRandomNumber(choices.size()));
      if (this.useDefer) {
        this.deferZone(choice);
      }
      return Optional.of(choice);
    } else {
      double[] weights = choices
        .stream()
        .mapToDouble(neighbor -> this.getZoneWeight(neighbor))
        .toArray();
      double normalSum = Arrays.stream(weights).sum();
      weights[0] /= normalSum;
      for (int i = 1; i < weights.length; i++) {
        weights[i] /= normalSum;
        weights[i] += weights[i - 1];
      }
      weights[weights.length - 1] = 1.;
      double roll = this.scenario.getRng().getRandomProbability();
      int idx = 0;
      while (weights[idx] < roll) {
        idx++;
      }
      Zone choice = choices.get(idx);
      if (this.useDefer) {
        this.deferZone(choice);
      }
      return Optional.of(choice);
    }
  }

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

