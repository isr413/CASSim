package com.seat.sim.client.sandbox.recon.remote;

import java.util.Optional;

import com.seat.sim.common.math.Grid;
import com.seat.sim.common.math.RemotePhysics;
import com.seat.sim.common.math.Zone;
import com.seat.sim.common.remote.RemoteState;
import com.seat.sim.common.scenario.Snapshot;

public class ReconRemoteUtil {

  public static boolean shouldReturnHome(Snapshot snap, RemoteState drone, Grid grid, Optional<Zone> task) {
    if (!drone.hasTag(ReconScenario.DRONE_TAG)) {
      return false;
    }
    if (snap.getTime() + 2 * ReconScenario.TURN_LENGTH >= ReconScenario.MISSION_LENGTH) {
      return true;
    }
    if (!drone.hasLocation() || !grid.hasZoneAtLocation(drone.getLocation())) {
      return false;
    }
    return (task.isPresent())
      ? !ReconRemoteUtil.validChoice(snap, drone, task.get())
      : !ReconRemoteUtil.validChoice(snap, drone, grid.getZoneAtLocation(drone.getLocation()));
  }

  public static boolean validChoice(Snapshot snap, RemoteState drone, Zone zone) {
    double timeToMove = Math.max(
        RemotePhysics.timeToReachDest(
          drone.getPhysicsState().getLocation(),
          0.,
          ReconScenario.DRONE_SCAN_VELOCITY,
          ReconScenario.DRONE_SCAN_ACCELERATION,
          zone.getLocation()
        ),
        ReconScenario.TURN_LENGTH
      );
    double timeToGoHome = Math.max(
        RemotePhysics.timeToReachDest(
          drone.getPhysicsState().getLocation(),
          0.,
          ReconScenario.DRONE_MAX_VELOCITY,
          ReconScenario.DRONE_MAX_ACCELERATION,
          ReconScenario.GRID_CENTER
        ),
        ReconScenario.TURN_LENGTH
      );
    if ((timeToMove + timeToGoHome + snap.getTime() + ReconScenario.TURN_LENGTH) >= ReconScenario.MISSION_LENGTH) {
      return false;
    }
    double avgBattUsage = (1. - drone.getFuelAmount()) / snap.getTime();
    if ((timeToMove + timeToGoHome) * avgBattUsage >= drone.getFuelAmount()) {
      return false;
    }
    return snap.getTime() + 2 * ReconScenario.TURN_LENGTH <= ReconScenario.MISSION_LENGTH;
  }
}
