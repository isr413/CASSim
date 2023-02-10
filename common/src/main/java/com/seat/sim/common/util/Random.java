package com.seat.sim.common.util;

import com.seat.sim.common.math.Vector;

public class Random {

  private java.util.Random rng;
  private long seed;

  public Random() {
    this(new java.util.Random().nextLong());
  }

  public Random(long seed) {
    this.seed = seed;
    this.rng = new java.util.Random(seed);
  }

  public boolean equals(Random rng) {
    if (rng == null) return false;
    return this.seed == rng.seed;
  }

  public Vector getRandomDirection2D() {
    return new Vector(this.getRandomPoint(-1, 1), this.getRandomPoint(-1, 1)).getUnitVector();
  }

  public Vector getRandomDirection3D() {
    return new Vector(this.getRandomPoint(-1, 1), this.getRandomPoint(-1, 1), this.getRandomPoint(-1, 1))
        .getUnitVector();
  }

  public Vector getRandomLocation2D(int width, int height) {
    return new Vector(this.getRandomPoint(width), this.getRandomPoint(height));
  }

  public Vector getRandomLocation3D(int width, int height, int elevation) {
    return new Vector(this.getRandomPoint(width), this.getRandomPoint(height), this.getRandomPoint(elevation));
  }

  public int getRandomNumber(int bound) {
    return this.rng.nextInt(bound);
  }

  public int getRandomNumber(int start, int bound) {
    return this.rng.nextInt(bound - start) + start;
  }

  public double getRandomPoint(int x) {
    return this.rng.nextDouble() * x;
  }

  public double getRandomPoint(double x) {
    return this.rng.nextDouble() * x;
  }

  public double getRandomPoint(double start, double end) {
    return start + (end - start) * this.rng.nextDouble();
  }

  public double getRandomProbability() {
    return this.rng.nextDouble();
  }

  public Vector getRandomSpeed2D(double maxSpeed) {
    if (!Double.isFinite(maxSpeed)) {
      return this.getRandomDirection2D();
    }
    return this.getRandomDirection2D().scale(this.getRandomPoint(maxSpeed));
  }

  public Vector getRandomSpeed2D(double mean, double stddev) {
    return this.getRandomSpeed2D(this.rng.nextGaussian() * stddev + mean);
  }

  public Vector getRandomSpeed3D(double maxSpeed) {
    if (!Double.isFinite(maxSpeed)) {
      return this.getRandomDirection3D();
    }
    return this.getRandomDirection3D().scale(this.getRandomPoint(maxSpeed));
  }

  public Vector getRandomSpeed3D(double mean, double stddev) {
    return this.getRandomSpeed3D(this.rng.nextGaussian() * stddev + mean);
  }

  public long getSeed() {
    return this.seed;
  }
}
