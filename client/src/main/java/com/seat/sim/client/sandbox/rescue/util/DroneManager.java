package com.seat.sim.client.sandbox.rescue.util;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.seat.sim.common.math.Grid;
import com.seat.sim.common.math.Vector;
import com.seat.sim.common.math.Zone;
import com.seat.sim.common.math.ZoneType;
import com.seat.sim.common.remote.RemoteState;
import com.seat.sim.common.remote.intent.IntentRegistry;
import com.seat.sim.common.remote.intent.IntentionSet;
import com.seat.sim.common.scenario.Snapshot;

public class DroneManager {

  private Map<String, Zone> assignments;
  private Map<String, Integer> cooldown;
  private int cooldownTime;
  private int droneCount;
  private Set<String> goingHome;
  private RescueScenario scenario;

  public DroneManager(RescueScenario scenario, int droneCount, int cooldownTime) {
    this.scenario = scenario;
    this.droneCount = droneCount;
    this.assignments = new HashMap<>();
    this.cooldown = new HashMap<>();
    this.cooldownTime = cooldownTime;
    this.goingHome = new HashSet<>();
  }

  private int countDetectionsBy(Snapshot snap, RemoteState drone, String model, String tag) {
    if (!drone.hasSensorStateWithModel(model)) {
      return 0;
    }
    return (int) drone
      .getSensorStateWithModel(model)
      .get()
      .getSubjects()
      .stream()
      .filter(remoteID -> snap.getRemoteStateWithID(remoteID).hasTag(tag))
      .count();
  }

  private Optional<Vector> randomWalk(Snapshot snap, RemoteState state) {
    Grid grid = this.scenario.getGrid().get();
    if (!state.hasLocation() || !grid.hasZoneAtLocation(state.getLocation())) {
      return Optional.empty();
    }
    Zone zone = grid.getZoneAtLocation(state.getLocation());
    List<Zone> neighborhood = grid.getNeighborhood(zone);
    Collections.shuffle(neighborhood, this.scenario.getRng().unwrap());
    Optional<Zone> nextZone = neighborhood
      .stream()
      .filter(neighbor -> !neighbor.getLocation().equals(zone.getLocation()))
      .filter(neighbor -> !neighbor.hasZoneType(ZoneType.BLOCKED))
      .filter(neighbor -> this.validChoice(snap, state, zone))
      .findFirst();
    if (nextZone.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(nextZone.get().getLocation());
  }

  private boolean shouldReturnHome(Snapshot snap, RemoteState drone) {
    if (!drone.hasTag(RescueScenario.DRONE_TAG)) {
      return false;
    }
    if (!drone.hasLocation() || !this.scenario.getGrid().get().hasZoneAtLocation(drone.getLocation())) {
      return snap.getTime() + RescueScenario.TURN_LENGTH >= RescueScenario.MISSION_LENGTH;
    }
    return !this.validChoice(snap, drone, this.scenario.getGrid().get().getZoneAtLocation(drone.getLocation()));
  }

  private double timeToCrossDistance(double dist, double speed, double maxSpeed, double acceleration) {
    double timeToBrake = (speed > 0.) ? speed / acceleration : 0.;
    double distToBrake = speed * timeToBrake - 0.5 * acceleration * timeToBrake * timeToBrake;
    if (distToBrake >= dist) {
      return timeToBrake;
    }
    double timeToTopSpeed = (speed < maxSpeed) ? (maxSpeed - speed) / acceleration : 0.;
    double distToTopSpeed = speed * timeToTopSpeed + 0.5 * acceleration * timeToTopSpeed * timeToTopSpeed;
    double maxTimeToBrake = maxSpeed / acceleration;
    double maxDistToBrake = maxSpeed * maxTimeToBrake - 0.5 * acceleration * maxTimeToBrake * maxTimeToBrake;
    if (distToTopSpeed + maxDistToBrake > dist) {
      double timeToCover = (Math.sqrt(4 * acceleration * (dist - distToBrake) / 2. + speed * speed) - speed) /
          (2. * acceleration);
      return 2 * timeToCover + timeToBrake;
    }
    double timeToCover = (dist - distToTopSpeed - maxDistToBrake) / maxSpeed;
    return timeToTopSpeed + timeToCover + maxTimeToBrake;
  }

  private boolean validChoice(Snapshot snap, RemoteState drone, Zone zone) {
    double timeToMove = Math.max(
        this.timeToCrossDistance(
            Vector.dist(drone.getLocation(), zone.getLocation()),
            drone.getSpeed(),
            RescueScenario.DRONE_MAX_VELOCITY,
            RescueScenario.DRONE_MAX_ACCELERATION
          ),
        RescueScenario.TURN_LENGTH
      );
    double timeToGoHome = Math.max(
        this.timeToCrossDistance(
          Vector.dist(zone.getLocation(), RescueScenario.GRID_CENTER),
          0.,
          RescueScenario.DRONE_MAX_VELOCITY,
          RescueScenario.DRONE_MAX_ACCELERATION
        ),
        RescueScenario.TURN_LENGTH
      );
    if ((timeToMove + timeToGoHome) >= (RescueScenario.MISSION_LENGTH - snap.getTime())) {
      return false;
    }
    double avgBattUsage = (1. - drone.getFuelAmount()) / snap.getTime();
    if ((timeToMove + timeToGoHome) * avgBattUsage >= drone.getFuelAmount()) {
      return false;
    }
    return snap.getTime() + RescueScenario.TURN_LENGTH < RescueScenario.MISSION_LENGTH;
  }

  public Optional<Zone> getAssignment(String droneID) {
    return (this.hasAssignment(droneID)) ? Optional.of(this.assignments.get(droneID)) : Optional.empty();
  }

  public int getCooldown(String droneID) {
    return (this.isOnCooldown(droneID)) ? this.cooldown.get(droneID) : 0;
  }

  public int getCooldownTime() {
    return this.cooldownTime;
  }

  public int getDroneCount() {
    return this.droneCount;
  }

  public boolean hasAssignment(String droneID) {
    return this.assignments.containsKey(droneID);
  }

  public boolean isDone(String droneID) {
    return this.goingHome.contains(droneID);
  }

  public boolean isOnCooldown(String droneID) {
    return this.cooldown.containsKey(droneID) && this.cooldown.get(droneID) > 0;
  }

  public void reset() {
    this.assignments = new HashMap<>();
    this.cooldown = new HashMap<>();
    this.goingHome = new HashSet<>();
  }

  public void removeAssignment(String droneID) {
    if (!this.hasAssignment(droneID)) {
      return;
    }
    this.scenario.addTask(this.getAssignment(droneID).get());
    this.assignments.remove(droneID);
  }

  public void setAssignment(String droneID, Zone task) {
    if (this.hasAssignment(droneID)) {
      this.removeAssignment(droneID);
    }
    this.assignments.put(droneID, task);
  }

  public void setCooldown(String droneID, int length) {
    this.cooldown.put(droneID, length);
  }

  public void setDone(String droneID) {
    this.goingHome.add(droneID);
  }

  public Collection<IntentionSet> update(Snapshot snap) {
    return this.update(
        snap,
        snap
          .getActiveRemoteStates()
          .stream()
          .filter(state -> state.hasTag(RescueScenario.DRONE_TAG))
          .collect(Collectors.toList())
      );
  }

  public Collection<IntentionSet> update(Snapshot snap, Collection<RemoteState> drones) {
    if (drones.isEmpty()) {
      return Collections.emptyList();
    }
    return snap
      .getActiveRemoteStates()
      .stream()
      .filter(state -> state.hasTag(RescueScenario.DRONE_TAG))
      .map(drone -> {
          IntentionSet intent = new IntentionSet(drone.getRemoteID());
          if (this.isDone(drone.getRemoteID())) {
            if (drone.getLocation().near(RescueScenario.GRID_CENTER)) {
              intent.addIntention(IntentRegistry.Done());
              this.scenario.report(snap.getTime(), "Drone done %s", drone.getRemoteID());
              return intent;
            }
            intent.addIntention(IntentRegistry.GoHome());
            return intent;
          }
          if (this.shouldReturnHome(snap, drone)) {
            intent.addIntention(IntentRegistry.DeactivateAllSensors());
            intent.addIntention(IntentRegistry.GoHome());
            this.setDone(drone.getRemoteID());
            if (this.hasAssignment(drone.getRemoteID())) {
              this.removeAssignment(drone.getRemoteID());
            }
            this.scenario.report(snap.getTime(), "Drone returning home %s", drone.getRemoteID());
            return intent;
          }
          if (this.isOnCooldown(drone.getRemoteID())) {
            intent.addIntention(IntentRegistry.Stop());
            this.setCooldown(drone.getRemoteID(), this.getCooldown(drone.getRemoteID()) - 1);
            return intent;
          }
          if (this.cooldown.containsKey(drone.getRemoteID())) {
            this.cooldown.remove(drone.getRemoteID());
            intent.addIntention(IntentRegistry.ActivateAllSensors());
          }
          if (!this.hasAssignment(drone.getRemoteID())) {
            Optional<Zone> task = this.scenario.nextTask(snap, drone);
            if (task.isEmpty()) {
              intent.addIntention(IntentRegistry.Stop());
              return intent;
            }
            this.setAssignment(drone.getRemoteID(), task.get());
          }
          if (drone.getLocation().near(this.getAssignment(drone.getRemoteID()).get().getLocation())) {
            intent.addIntention(IntentRegistry.Stop());
            if (this.countDetectionsBy(snap, drone, RescueScenario.BLE_COMMS, RescueScenario.VICTIM_TAG) > 0) {
              this.setCooldown(drone.getRemoteID(), this.getCooldownTime());
              intent.addIntention(IntentRegistry.DeactivateAllSensors());
              return intent;
            }
            this.removeAssignment(drone.getRemoteID());
            return intent;
          }
          intent.addIntention(
              IntentRegistry.GoTo(
                this.getAssignment(drone.getRemoteID()).get().getLocation(),
                RescueScenario.DRONE_SCAN_VELOCITY,
                RescueScenario.DRONE_SCAN_ACCELERATION
              )
            );
          return intent;
        })
      .toList();
  }
}
