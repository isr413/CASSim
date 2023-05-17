package com.seat.sim.common.math;

public class PhysicsState {

    private double maxAcceleration;
    private double maxSpeed;
    private Vector position;
    private Vector velocity;

    public PhysicsState(Vector position) {
        this(position, Vector.ZERO, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    public PhysicsState(Vector position, double maxAcceleration) {
        this(position, Vector.ZERO, Double.POSITIVE_INFINITY, maxAcceleration);
    }

    public PhysicsState(Vector position, Vector velocity) {
        this(position, velocity, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    public PhysicsState(Vector position, Vector velocity, double maxAcceleration) {
        this(position, velocity, Double.POSITIVE_INFINITY, maxAcceleration);
    }
    
    public PhysicsState(Vector position, Vector velocity, double maxSpeed, double maxAcceleration) {
        this.position = position;
        this.velocity = velocity;
        this.maxSpeed = maxSpeed;
        this.maxAcceleration = maxAcceleration;
    }

    public double getMaxAcceleration() {
        return this.maxAcceleration;
    }

    public double getMaxSpeed() {
        return this.maxSpeed;
    }

    public Vector getPosition() {
        return this.position;
    }

    public double getSpeed() {
        return this.velocity.getMagnitude();
    }

    public Vector getVelocity() {
        return this.velocity;
    }

}
