package com.seat.sim.client.sandbox.rescue.remote;

import java.util.Optional;

import com.seat.sim.common.math.Grid;
import com.seat.sim.common.math.RemotePhysics;
import com.seat.sim.common.math.Zone;
import com.seat.sim.common.remote.RemoteState;
import com.seat.sim.common.scenario.Snapshot;

public class RescueRemoteUtil {

  public static boolean shouldReturnHome(Snapshot snap, RemoteState drone, Grid grid, Optional<Zone> task) {
    if (!drone.hasTag(RescueScenario.DRONE_TAG)) {
      return false;
    }
    if (snap.getTime() + 2 * RescueScenario.TURN_LENGTH >= RescueScenario.MISSION_LENGTH) {
      return true;
    }
    if (!drone.hasLocation() || !grid.hasZoneAtLocation(drone.getLocation())) {
      return false;
    }
    return (task.isPresent())
      ? !RescueRemoteUtil.validChoice(snap, drone, task.get())
      : !RescueRemoteUtil.validChoice(snap, drone, grid.getZoneAtLocation(drone.getLocation()));
  }

  public static boolean validChoice(Snapshot snap, RemoteState drone, Zone zone) {
    double timeToMove = Math.max(
        RemotePhysics.timeToReachDest(
          drone.getPhysicsState().getLocation(),
          0.,
          RescueScenario.DRONE_SCAN_VELOCITY,
          RescueScenario.DRONE_SCAN_ACCELERATION,
          zone.getLocation()
        ),
        RescueScenario.TURN_LENGTH
      );
    double timeToGoHome = Math.max(
        RemotePhysics.timeToReachDest(
          drone.getPhysicsState().getLocation(),
          0.,
          RescueScenario.DRONE_MAX_VELOCITY,
          RescueScenario.DRONE_MAX_ACCELERATION,
          RescueScenario.GRID_CENTER
        ),
        RescueScenario.TURN_LENGTH
      );
    if ((timeToMove + timeToGoHome + snap.getTime() + RescueScenario.TURN_LENGTH) >= RescueScenario.MISSION_LENGTH) {
      return false;
    }
    double avgBattUsage = (1. - drone.getFuelAmount()) / snap.getTime();
    if ((timeToMove + timeToGoHome) * avgBattUsage >= drone.getFuelAmount()) {
      return false;
    }
    return snap.getTime() + 2 * RescueScenario.TURN_LENGTH <= RescueScenario.MISSION_LENGTH;
  }
}
