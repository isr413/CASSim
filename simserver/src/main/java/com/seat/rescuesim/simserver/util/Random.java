package com.seat.rescuesim.simserver.util;

import com.seat.rescuesim.common.math.Vector;

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

    public Vector getRandomDirection2D() {
        return this.getRandomLocation2D(1, 1).getUnitVector();
    }

    public Vector getRandomDirection3D() {
        return this.getRandomLocation3D(1, 1, 1).getUnitVector();
    }

    public Vector getRandomLocation2D(int width, int height) {
        return new Vector(this.getRandomPoint(width), this.getRandomPoint(height));
    }

    public Vector getRandomLocation3D(int width, int height, int elevation) {
        return new Vector(this.getRandomPoint(width), this.getRandomPoint(height), this.getRandomPoint(elevation));
    }

    public double getRandomPoint(int x) {
        return this.rng.nextDouble() * x;
    }

    public Vector getRandomSpeed2D(double speed) {
        return Vector.scale(this.getRandomDirection2D(), speed);
    }

    public Vector getRandomSpeed2D(double mean, double stddev) {
        return this.getRandomSpeed2D(this.rng.nextGaussian() * stddev + mean);
    }

    public Vector getRandomSpeed3D(double speed) {
        return Vector.scale(this.getRandomDirection3D(), speed);
    }

    public Vector getRandomSpeed3D(double mean, double stddev) {
        return this.getRandomSpeed3D(this.rng.nextGaussian() * stddev + mean);
    }

    public long getSeed() {
        return this.seed;
    }

}