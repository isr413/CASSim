package com.seat.sim.client.sandbox.rescue.util;

import java.util.Optional;

import com.seat.sim.common.math.Grid;
import com.seat.sim.common.math.Vector;
import com.seat.sim.common.math.Zone;
import com.seat.sim.common.remote.RemoteState;
import com.seat.sim.common.scenario.Snapshot;

public class RemoteUtil {

  private static double timeToCrossDistance(double dist, double speed, double maxSpeed, double acceleration) {
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
      ? !RemoteUtil.validChoice(snap, drone, task.get())
      : !RemoteUtil.validChoice(snap, drone, grid.getZoneAtLocation(drone.getLocation()));
  }

  public static boolean validChoice(Snapshot snap, RemoteState drone, Zone zone) {
    double timeToMove = Math.max(
        RemoteUtil.timeToCrossDistance(
            Vector.dist(drone.getLocation(), zone.getLocation()),
            drone.getSpeed(),
            RescueScenario.DRONE_MAX_VELOCITY,
            RescueScenario.DRONE_MAX_ACCELERATION
          ),
        RescueScenario.TURN_LENGTH
      );
    double timeToGoHome = Math.max(
        RemoteUtil.timeToCrossDistance(
          Vector.dist(zone.getLocation(), RescueScenario.GRID_CENTER),
          0.,
          RescueScenario.DRONE_MAX_VELOCITY,
          RescueScenario.DRONE_MAX_ACCELERATION
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
    return snap.getTime() + 2 * RescueScenario.TURN_LENGTH < RescueScenario.MISSION_LENGTH;
  }
}
