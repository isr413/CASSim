package com.seat.sim.common.math;

public class RemotePhysics {

  public static double timeToReachDest(PhysicsState state, Vector dest) {
    if (!Double.isFinite(state.getMaxAcceleration())) {
      return 0.;
    }
    double speed = state.getSpeed();
    double timeToBrake = (speed > 0.) ? speed / state.getMaxAcceleration() : 0.;
    double distToBrake = speed * timeToBrake - 0.5 * state.getMaxAcceleration() * timeToBrake * timeToBrake;
    double totalDist = state.getPosition().distTo(dest);
    if (distToBrake >= totalDist) {
      return timeToBrake;
    }
    if (!Double.isFinite(state.getMaxSpeed())) {
      double timeToCover = (Math.sqrt(4 * state.getMaxAcceleration() * (totalDist - distToBrake) / 2. + speed * speed) - speed) /
          (2. * state.getMaxAcceleration());
      return 2 * timeToCover + timeToBrake;
    }
    double timeToTopSpeed = (speed < state.getMaxSpeed()) ? (state.getMaxSpeed() - speed) / state.getMaxAcceleration() : 0.;
    double distToTopSpeed = speed * timeToTopSpeed + 0.5 * state.getMaxAcceleration() * timeToTopSpeed * timeToTopSpeed;
    double maxTimeToBrake = state.getMaxSpeed() / state.getMaxAcceleration();
    double maxDistToBrake = state.getMaxSpeed() * maxTimeToBrake - 0.5 * state.getMaxAcceleration() * maxTimeToBrake * maxTimeToBrake;
    if (distToTopSpeed + maxDistToBrake > totalDist) {
      double timeToCover = (Math.sqrt(4 * state.getMaxAcceleration() * (totalDist - distToBrake) / 2. + speed * speed) - speed) /
          (2. * state.getMaxAcceleration());
      return 2 * timeToCover + timeToBrake;
    }
    double timeToCover = (totalDist - distToTopSpeed - maxDistToBrake) / state.getMaxSpeed();
    return timeToTopSpeed + timeToCover + maxTimeToBrake;
  }
}
