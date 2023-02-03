package com.seat.sim.common.math;

public class SimMath {
  public static final double PRECISION = 0.0001;

  public static boolean near(double d1, double d2) {
    return (d1 >= d2) ? (d1 - d2) <= SimMath.PRECISION : (d2 - d1) <= SimMath.PRECISION;
  }
}
