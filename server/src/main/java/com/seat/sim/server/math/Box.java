package com.seat.sim.server.math;

import com.seat.sim.common.math.Grid;
import com.seat.sim.common.math.Vector;
import com.seat.sim.common.math.Zone;

public class Box {

  private double height;
  private double width;
  private double x;
  private double y;

  public Box(Grid grid) {
    this(0., 0., grid.getWidth(), grid.getHeight());
  }

  public Box(Zone zone) {
    this(zone.getLocation().getX() - zone.getSize() / 2., zone.getLocation().getY() - zone.getSize() / 2.,
        zone.getSize(), zone.getSize());
  }

  public Box(double x, double y, double width, double height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }

  public Vector getBotLeftCorner() {
    return new Vector(this.getLeftX(), this.getBotY());
  }

  public Vector getBotRightCorner() {
    return new Vector(this.getRightX(), this.getBotY());
  }

  public double getBotY() {
    return this.y + this.height;
  }

  public Vector getCenterPoint() {
    return new Vector(this.getCenterX(), this.getCenterY());
  }

  public double getCenterX() {
    return this.x + this.width / 2.;
  }

  public double getCenterY() {
    return this.y + this.height / 2.;
  }

  public double getHeight() {
    return this.height;
  }

  public double getLeftX() {
    return this.x;
  }

  public double getRightX() {
    return this.x + this.width;
  }

  public Vector getTopLeftCorner() {
    return new Vector(this.getLeftX(), this.getTopY());
  }

  public Vector getTopRightCorner() {
    return new Vector(this.getRightX(), this.getTopY());
  }

  public double getTopY() {
    return this.y;
  }

  public double getWidth() {
    return this.width;
  }

  public double getX() {
    return this.x;
  }

  public double getY() {
    return this.y;
  }

  public boolean isBoundaryCollision(Vector point) {
    return Vector.near(point.getX(), this.getLeftX()) || Vector.near(point.getX(), this.getRightX()) ||
        Vector.near(point.getY(), this.getTopY()) || Vector.near(point.getY(), this.getBotY());
  }

  public boolean isInbounds(Vector point) {
    if (this.isBoundaryCollision(point)) {
      return true;
    }
    return this.getLeftX() <= point.getX() && point.getX() <= this.getRightX() &&
        this.getTopY() <= point.getY() && point.getY() <= this.getBotY();
  }

  public String toString() {
    return String.format("[%.4f, %.4f, %.4f, %.4f]", this.x, this.y, this.width, this.height);
  }
}
