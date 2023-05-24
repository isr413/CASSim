package com.seat.sim.common.math;

public class RemotePhysics {

  private static double timeToReachDest(Vector loc, double speed, double maxVel, double maxAccel,
        Vector dest) {
    if (!Double.isFinite(maxAccel)) {
      return 0.;
    }
    double timeToBrake = (speed > 0.) ? speed / maxAccel : 0.;
    double distToBrake = speed * timeToBrake - 0.5 * maxAccel * timeToBrake * timeToBrake;
    double totalDist = loc.distTo(dest);
    if (distToBrake >= totalDist) {
      return timeToBrake;
    }
    if (!Double.isFinite(maxVel)) {
      double timeToCover =
          (Math.sqrt(4 * maxAccel * (totalDist - distToBrake) / 2.
              + speed * speed) - speed) / (2. * maxAccel);
      return 2 * timeToCover + timeToBrake;
    }
    double timeToTopSpeed = (speed < maxVel)
        ? (maxVel - speed) / maxAccel
        : 0.;
    double distToTopSpeed = speed * timeToTopSpeed
        + 0.5 * maxAccel * timeToTopSpeed * timeToTopSpeed;
    double maxTimeToBrake = maxVel / maxAccel;
    double maxDistToBrake = maxVel * maxTimeToBrake
        - 0.5 * maxAccel * maxTimeToBrake * maxTimeToBrake;
    if (distToTopSpeed + maxDistToBrake > totalDist) {
      double timeToCover =
          (Math.sqrt(4 * maxAccel * (totalDist - distToBrake) / 2.
              + speed * speed) - speed) / (2. * maxAccel);
      return 2 * timeToCover + timeToBrake;
    }
    double timeToCover = (totalDist - distToTopSpeed - maxDistToBrake) / maxVel;
    return timeToTopSpeed + timeToCover + maxTimeToBrake;
  }

  public static double timeToReachDest(PhysicsState state, Vector dest) {
    return RemotePhysics.timeToReachDest(
          state.getLocation(),
          state.getSpeed(),
          state.getMaxVelocity(),
          state.getMaxAcceleration(),
          dest
        );
  }

  public static double timeToReachDest(PhysicsState state, Vector s0, Vector v0, Vector dest) {
    return RemotePhysics.timeToReachDest(
          s0,
          v0.getMagnitude(),
          state.getMaxVelocity(),
          state.getMaxAcceleration(),
          dest
        );
  }

}
