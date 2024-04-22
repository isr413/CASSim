package com.seat.sim.client.sandbox.recon.remote;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.seat.sim.client.core.DroneManager;
import com.seat.sim.client.core.DroneScenario;
import com.seat.sim.client.negotiation.Contract;
import com.seat.sim.common.math.Vector;
import com.seat.sim.common.math.Zone;
import com.seat.sim.common.remote.RemoteState;
import com.seat.sim.common.remote.intent.IntentRegistry;
import com.seat.sim.common.remote.intent.IntentionSet;
import com.seat.sim.common.scenario.Snapshot;

public class ReconDroneManager implements DroneManager {

  private Map<String, Zone> assignments;
  private Map<String, List<Contract>> contracts;
  private Map<String, Integer> cooldown;
  private int cooldownTime;
  private Set<String> detectedIntel;
  private Set<String> destroyedDrones;
  private int droneCount;
  private Set<String> goingHome;
  private DroneScenario scenario;

  public ReconDroneManager(DroneScenario scenario, int droneCount, int cooldownTime) {
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

  public void addContract(String droneID, Contract contract) {
    if (!this.contracts.containsKey(droneID)) {
      this.contracts.put(droneID, new LinkedList<>());
    }
    this.contracts.get(droneID).add(contract);
  }

  public void close() {}

  public Optional<Zone> getAssignment(String droneID) {
    return (this.hasAssignment(droneID)) ? Optional.of(this.assignments.get(droneID)) : Optional.empty();
  }

  public List<Contract> getContracts(String droneID) {
    return (this.hasContracts(droneID)) ? this.contracts.get(droneID) : List.of();
  }

  public int getCooldown(String droneID) {
    return (this.isOnCooldown(droneID)) ? this.cooldown.get(droneID) : 0;
  }

  public int getCooldownTime() {
    return this.cooldownTime;
  }

  public Set<String> getDetectedAssets() {
    return this.getDetectedIntel();
  }

  public Set<String> getDetectedIntel() {
    return this.detectedIntel;
  }

  public int getDroneCount() {
    return this.droneCount;
  }

  public boolean hasAssignment(String droneID) {
    return this.assignments.containsKey(droneID);
  }

  public boolean hasContracts(String droneID) {
    return this.contracts.containsKey(droneID) && !this.contracts.get(droneID).isEmpty();
  }

  public void init() {
    this.detectedIntel = new HashSet<>();
    this.assignments = new HashMap<>();
    this.contracts = new HashMap<>();
    this.cooldown = new HashMap<>();
    this.goingHome = new HashSet<>();
    this.destroyedDrones = new HashSet<>();
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
          .filter(state -> state.hasTag(ReconScenario.DRONE_TAG))
          .collect(Collectors.toList())
      );
  }

  public Collection<IntentionSet> update(Snapshot snap, Collection<RemoteState> drones) {
    if (drones.isEmpty()) {
      return Collections.emptyList();
    }
    Set<String> surveyedIntel = new HashSet<>();
    return snap
      .getActiveRemoteStates()
      .stream()
      .filter(state -> state.hasTag(ReconScenario.DRONE_TAG))
      .map(drone -> {
          IntentionSet intent = new IntentionSet(drone.getRemoteID());
          if (this.isDone(drone.getRemoteID())) {
            if (drone.getLocation().near(ReconScenario.GRID_CENTER)) {
              intent.addIntention(IntentRegistry.Done());
              this.scenario.report(
                  snap.getTime(),
                  ":: %s :: %s :: Drone done",
                  drone.getRemoteID(),
                  drone.getLocation().toString("%.0f")
                );
              return intent;
            }
            intent.addIntention(IntentRegistry.GoHome());
            this.scenario.report(
                snap.getTime(),
                ":: %s :: %s :: Drone going home",
                drone.getRemoteID(),
                drone.getLocation().toString("%.0f")
              );
            return intent;
          }
          if (ReconRemoteUtil.shouldReturnHome(
                snap,
                drone,
                this.scenario.getGrid().get(),
                this.getAssignment(drone.getRemoteID())) && !this.hasContracts(drone.getRemoteID())
              ) {
            intent.addIntention(IntentRegistry.DeactivateAllSensors());
            intent.addIntention(IntentRegistry.GoHome());
            this.setDone(drone.getRemoteID());
            if (this.hasAssignment(drone.getRemoteID())) {
              this.removeAssignment(drone.getRemoteID());
            }
            this.scenario.report(
                snap.getTime(),
                ":: %s :: %s :: Drone returning home",
                drone.getRemoteID(),
                drone.getLocation().toString("%.0f")
              );
            return intent;
          }
          if (this.hasContracts(drone.getRemoteID())) {
            this.updateContracts(snap, drone.getRemoteID());
          }
          if (this.destroyedDrones.contains(drone.getRemoteID())) {
            intent.addIntention(IntentRegistry.DeactivateAllSensors());
            intent.addIntention(IntentRegistry.Done());
            this.setDone(drone.getRemoteID());
            if (this.hasAssignment(drone.getRemoteID())) {
              this.removeAssignment(drone.getRemoteID());
            }
            this.scenario.report(
                snap.getTime(),
                ":: %s :: %s :: Drone destroyed",
                drone.getRemoteID(),
                drone.getLocation().toString("%.0f")
              );
            return intent;
          }
          if (this.isOnCooldown(drone.getRemoteID())) {
            this.setCooldown(drone.getRemoteID(), this.getCooldown(drone.getRemoteID()) - 1);
            if (this.getCooldown(drone.getRemoteID()) > 0) {
              intent.addIntention(IntentRegistry.Stop());
              this.scenario.report(
                  snap.getTime(),
                  ":: %s :: %s :: Performing task",
                  drone.getRemoteID(),
                  drone.getLocation().toString("%.0f")
                );
              return intent;
            } else {
              intent.addIntention(IntentRegistry.ActivateAllSensors());
              this.scenario.report(
                  snap.getTime(),
                  ":: %s :: %s :: Completes task",
                  drone.getRemoteID(),
                  drone.getLocation().toString("%.0f")
                );
              if (this.hasAssignment(drone.getRemoteID()) &&
                    drone.getLocation().near(this.getAssignment(drone.getRemoteID()).get().getLocation())) {
                this.removeAssignment(drone.getRemoteID());
              }
            }
          }
          if (this.hasAssignment(drone.getRemoteID()) &&
                drone.getLocation().near(this.getAssignment(drone.getRemoteID()).get().getLocation())) {
            Set<String> sensorDetections = this.detectionsBy(
                snap,
                drone,
                ReconScenario.SENSOR_A_COMMS,
                ReconScenario.INTEL_TAG
              );
            sensorDetections.addAll(this.detectionsBy(
                snap,
                drone,
                ReconScenario.SENSOR_A_COMMS,
                ReconScenario.INTEL_POPUP_TAG
              ));
            Set<String> allDetections = this.detectionsBy(
                snap,
                drone,
                ReconScenario.DRONE_CAMERA,
                ReconScenario.INTEL_TAG
              );
            allDetections.addAll(this.detectionsBy(
                snap,
                drone,
                ReconScenario.DRONE_CAMERA,
                ReconScenario.INTEL_POPUP_TAG
              ));
            allDetections.addAll(sensorDetections);
            for (String intelID : allDetections) {
              if (this.detectedIntel.contains(intelID)) {
                continue;
              }
              RemoteState state = snap.getRemoteStateWithID(intelID);
              if (state.hasTag(ReconScenario.INTEL_POPUP_TAG) &&
                  ((IntelManager) this.scenario.getManager().getAssetManager()).isHidden(snap, state)) {
                continue;
              }
              this.scenario.report(
                  snap.getTime(),
                  ":: %s :: %s :: %s :: %d :: Detected intel",
                  drone.getRemoteID(),
                  drone.getLocation().toString("%.0f"),
                  intelID,
                  ((IntelManager) this.scenario.getManager().getAssetManager()).getAdvAssignment(intelID)
                );
              this.detectedIntel.add(intelID);
            }
            sensorDetections.removeAll(surveyedIntel);
            if (this.scenario.hasNegotiations() && !sensorDetections.isEmpty()) {
              for (String intelID : sensorDetections) {
                List<RemoteState> otherDrones = snap
                    .getActiveRemoteStates()
                    .stream()
                    .filter(state -> state.hasTag(ReconScenario.DRONE_TAG))
                    .filter(state -> !drone.equals(state))
                    .filter(state -> !this.hasContracts(state.getRemoteID()))
                    .collect(Collectors.toList());
                Collections.sort(otherDrones, new Comparator<RemoteState>() {
                  @Override
                  public int compare(RemoteState a, RemoteState b) {
                    return Double.compare(
                        Vector.dist(drone.getLocation(), a.getLocation()),
                        Vector.dist(drone.getLocation(), b.getLocation())
                      );
                  }
                });
                StringBuilder receiverID = new StringBuilder(drone.getRemoteID());
                int adv = ((IntelManager) this.scenario.getManager().getAssetManager()).getAdvAssignment(intelID);
                if (otherDrones.size() < adv) continue;
                for (int i = 0; i < adv; i++) {
                  receiverID.append("::");
                  receiverID.append(otherDrones.get(i).getRemoteID());
                }
                Optional<Contract> contract = this.scenario.negotiate(
                    snap,
                    drone,
                    intelID,
                    receiverID.toString()
                  );
                if (contract.isPresent()) {
                  this.scenario.report(
                    snap.getTime(),
                    ":: %s :: %s :: %s :: Accepted task",
                    drone.getRemoteID(),
                    drone.getLocation().toString("%.0f"),
                    intelID
                  );
                  surveyedIntel.add(intelID);
                  this.scenario.setDone(intelID, false);
                  for (int i = 0; i < adv; i++) {
                    this.addContract(otherDrones.get(i).getRemoteID(), contract.get().clone());
                  }
                  this.addContract(drone.getRemoteID(), contract.get());
                } else {
                  this.scenario.report(
                    snap.getTime(),
                    ":: %s :: %s :: %s :: Rejected task",
                    drone.getRemoteID(),
                    drone.getLocation().toString("%.0f"),
                    intelID
                  );
                }
              }
              if (this.hasContracts(drone.getRemoteID())) {
                intent.addIntention(IntentRegistry.Stop());
                intent.addIntention(IntentRegistry.DeactivateAllSensors());
                this.scenario.report(
                    snap.getTime(),
                    ":: %s :: %s :: Initiating task",
                    drone.getRemoteID(),
                    drone.getLocation().toString("%.0f")
                  );
                return intent;
              }
            } else if (this.getCooldownTime() > 0 && !sensorDetections.isEmpty()) {
              this.setCooldown(drone.getRemoteID(), this.getCooldownTime());
              intent.addIntention(IntentRegistry.Stop());
              intent.addIntention(IntentRegistry.DeactivateAllSensors());
              surveyedIntel.addAll(sensorDetections);
              this.scenario.report(
                  snap.getTime(),
                  ":: %s :: %s :: Initiating task",
                  drone.getRemoteID(),
                  drone.getLocation().toString("%.0f")
                );
              return intent;
            }
            this.removeAssignment(drone.getRemoteID());
            this.scenario.report(
                snap.getTime(),
                ":: %s :: %s :: Completes assignment",
                drone.getRemoteID(),
                drone.getLocation().toString("%.0f")
              );
          }
          if (!this.hasAssignment(drone.getRemoteID())) {
            Optional<Zone> task = this.scenario.nextTask(snap, drone);
            if (task.isEmpty()) {
              intent.addIntention(IntentRegistry.Stop());
              this.scenario.report(
                  snap.getTime(),
                  ":: %s :: %s :: Drone on standby",
                  drone.getRemoteID(),
                  drone.getLocation().toString("%.0f")
                );
              return intent;
            }
            this.setAssignment(drone.getRemoteID(), task.get());
            this.scenario.report(
                snap.getTime(),
                ":: %s :: %s :: %s :: Drone assigned",
                drone.getRemoteID(),
                drone.getLocation().toString("%.0f"),
                task.get().getLocation().toString("%.0f")
              );
          }
          intent.addIntention(
              IntentRegistry.GoTo(
                this.getAssignment(drone.getRemoteID()).get().getLocation(),
                ReconScenario.DRONE_SCAN_VELOCITY,
                ReconScenario.DRONE_SCAN_ACCELERATION
              )
            );
          this.scenario.report(
              snap.getTime(),
              ":: %s :: %s :: Drone exploring",
              drone.getRemoteID(),
              drone.getLocation().toString("%.0f")
            );
          return intent;
        })
      .toList();
  }

  public void updateContracts(Snapshot snap, String droneID) {
    if (!this.hasContracts(droneID)) {
      return;
    }
    List<Contract> contracts = this.getContracts(droneID)
      .stream()
      .map(c -> c.update())
      .filter(c -> {
        if (Vector.near(c.getProposal().getDeadline(), 0.)) {
          if (!this.scenario.scoreContract(snap, c)) {
            String droneIDs = (c.getReceiverID().contains(droneID))
                ? c.getReceiverID()
                : c.getSenderID();
            for (String eachID : droneIDs.split("::")) {
              this.destroyedDrones.add(eachID);
            }
          }
          this.scenario.terminateContract(c);
          return false;
        }
        return true;
      })
      .collect(Collectors.toList());
    if (contracts.isEmpty()) {
      this.contracts.remove(droneID);
      this.setCooldown(droneID, 0);
      return;
    }
    Collections.sort(contracts);
    this.contracts.put(droneID, contracts);
    this.setCooldown(droneID, (int) Math.ceil(contracts.get(0).getProposal().getDeadline()));
  }
}
