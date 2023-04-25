package com.seat.sim.client.sandbox.rescue.util;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.seat.sim.common.math.Zone;
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
    this.cooldownTime = cooldownTime;
  }

  private Set<String> detectionsBy(Snapshot snap, RemoteState drone, String model, String tag) {
    if (!drone.hasSensorStateWithModel(model)) {
      return Set.of();
    }
    return drone
      .getSensorStateWithModel(model)
      .get()
      .getSubjects()
      .stream()
      .filter(remoteID -> snap.getRemoteStateWithID(remoteID).hasTag(tag))
      .collect(Collectors.toSet());
  }

  public void close() {}

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

  public void init() {
    this.assignments = new HashMap<>();
    this.cooldown = new HashMap<>();
    this.goingHome = new HashSet<>();
  }

  public boolean isDone(String droneID) {
    return this.goingHome.contains(droneID);
  }

  public boolean isOnCooldown(String droneID) {
    return this.cooldown.containsKey(droneID) && this.cooldown.get(droneID) > 0;
  }

  public void reset() {
    this.init();
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
    Set<String> assistedVictims = new HashSet<>();
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
          if (RemoteUtil.shouldReturnHome(
                snap,
                drone,
                this.scenario.getGrid().get(),
                this.getAssignment(drone.getRemoteID()))
              ) {
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
            if (this.getCooldown(drone.getRemoteID()) == 0) {
              intent.addIntention(IntentRegistry.ActivateAllSensors());
            }
            return intent;
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
            Set<String> uniqueVictims = this.detectionsBy(
                snap,
                drone,
                RescueScenario.BLE_COMMS,
                RescueScenario.VICTIM_TAG
              );
            for (String victimID : uniqueVictims) {
              if (!this.scenario.isDone(victimID)) {
                this.scenario.report(snap.getTime(), "Rescued victim %s", victimID);
                this.scenario.setDone(victimID, false);
              }
            }
            uniqueVictims.removeAll(assistedVictims);
            if (!uniqueVictims.isEmpty()) {
              this.setCooldown(drone.getRemoteID(), this.getCooldownTime());
              intent.addIntention(IntentRegistry.DeactivateAllSensors());
              assistedVictims.addAll(uniqueVictims);
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
