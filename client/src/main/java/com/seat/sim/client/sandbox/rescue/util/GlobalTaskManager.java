package com.seat.sim.client.sandbox.rescue.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import com.seat.sim.client.sandbox.rescue.remote.RescueScenario;
import com.seat.sim.common.math.Grid;
import com.seat.sim.common.math.Vector;
import com.seat.sim.common.math.Zone;
import com.seat.sim.common.remote.RemoteState;
import com.seat.sim.common.scenario.Snapshot;

public class GlobalTaskManager implements TaskManager {

  private HashMap<Integer, Integer> defer;
  private RescueScenario scenario;
  private boolean[][] zones;
  private int zonesRemaining;

  public GlobalTaskManager(RescueScenario scenario) {
    this.scenario = scenario;
  }

  private void assignZone(Zone zone) {
    int key = this.getZoneKey(zone);
    if (!this.defer.containsKey(key)) {
      this.defer.put(key, 0);
    }
    this.defer.put(key, this.defer.get(key) + 1);
    if (this.hasAssignedZone(zone)) {
      return;
    }
    double x = zone.getLocation().getX(), y = zone.getLocation().getY();
    int size = zone.getSize();
    this.zones[(int) (y / size)][(int) (x / size)] = true;
    this.zonesRemaining--;
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

  private boolean hasAssignedZone(Zone zone) {
    double x = zone.getLocation().getX(), y = zone.getLocation().getY();
    int size = zone.getSize();
    return this.zones[(int) (y / size)][(int) (x / size)];
  }

  public void addTask(Zone task) {}

  public void close() {}

  public Optional<Zone> getNextTask(Snapshot snap, RemoteState state) {
    Zone zone = this.scenario.getGrid().get().getZoneAtLocation(state.getLocation());
    List<Zone> neighborhood = this.scenario.getGrid().get().getNeighborhood(zone, false);
    Collections.shuffle(neighborhood, this.scenario.getRng().unwrap());
    if (neighborhood.isEmpty()) {
      return Optional.empty();
    }
    List<Zone> choices = neighborhood
      .stream()
      .filter(neighbor -> !this.hasAssignedZone(neighbor))
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
      choices = new ArrayList<>(choices);
      Collections.sort(choices, new Comparator<Zone>() {
        @Override
        public int compare(Zone z1, Zone z2) {
          return -Double.compare(
              Vector.dist(RescueScenario.GRID_CENTER, z1.getLocation()),
              Vector.dist(RescueScenario.GRID_CENTER, z2.getLocation())
            );
        }
      });
      Zone choice = choices.get(0);
      this.assignZone(choice);
      return Optional.of(choice);
    }
    Zone choice = choices.get(this.scenario.getRng().getRandomNumber(choices.size()));
    this.assignZone(choice);
    return Optional.of(choice);
  }

  public boolean hasNextTask() {
    return this.zonesRemaining > 0;
  }

  public void init() {
    Grid grid = this.scenario.getGrid().get();
    this.zones = new boolean[grid.getHeightInZones()][grid.getWidthInZones()];
    this.zonesRemaining = grid.getHeightInZones() * grid.getWidthInZones();
    this.defer = new HashMap<>();
    this.assignZone(grid.getZoneAtLocation(RescueScenario.GRID_CENTER));
  }

  public void reset() {
    this.init();
  }

  public void update(Snapshot snap) {
    this.defer.clear();
  }
}

