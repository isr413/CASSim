package com.seat.sim.client.sandbox.rescue.remote;

import java.util.Optional;

import com.seat.sim.common.math.Grid;
import com.seat.sim.common.math.RemotePhysics;
import com.seat.sim.common.math.Zone;
import com.seat.sim.common.remote.RemoteState;
import com.seat.sim.common.scenario.Snapshot;

public class RemoteUtil {

  public static boolean shouldReturnHome(Snapshot snap, RemoteState drone, Grid grid, Optional<Zone> task) {
    System.out.println("here");
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
      ? !RemoteUtil.validChoice(snap, drone, task.get())
      : !RemoteUtil.validChoice(snap, drone, grid.getZoneAtLocation(drone.getLocation()));
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
      System.out.println("timeToMove: " + timeToMove);
      System.out.println("timeToGoHome: " + timeToGoHome);
      System.out.println("snap.getTime(): " + snap.getTime());
      return false;
    }
    double avgBattUsage = (1. - drone.getFuelAmount()) / snap.getTime();
    if ((timeToMove + timeToGoHome) * avgBattUsage >= drone.getFuelAmount()) {
      System.out.println("out of battery");
      return false;
    }
    System.out.println("should be valid");
    return snap.getTime() + 2 * RescueScenario.TURN_LENGTH <= RescueScenario.MISSION_LENGTH;
  }
}
