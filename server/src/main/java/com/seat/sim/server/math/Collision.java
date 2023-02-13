package com.seat.sim.server.math;

import com.seat.sim.common.math.Vector;

public class Collision {

  private Box box;
  private Vector point;

  public Collision(Vector point, Box box) {
    this.point = point;
    this.box = box;
  }

  public Box getBoundaryBox() {
    return this.box;
  }

  public Vector getPoint() {
    return this.point;
  }

  public boolean hasCornerCollision() {
    return this.hasHorizontalCollision() && this.hasVerticalCollision();
  }

  public boolean hasHorizontalCollision() {
    return Vector.near(this.point.getX(), this.box.getLeftX()) || Vector.near(this.point.getX(), this.box.getRightX());
  }

  public boolean hasSideCollision() {
    return !this.hasCornerCollision() && (this.hasHorizontalCollision() || this.hasVerticalCollision());
  }

  public boolean hasVerticalCollision() {
    return Vector.near(this.point.getY(), this.box.getTopY()) || Vector.near(this.point.getY(), this.box.getBotY());
  }

  public Vector reflect(Vector ray) {
    Vector delta = Vector.sub(ray, this.point);
    if (this.hasCornerCollision()) {
      return new Vector(this.point.getX() - delta.getX(), this.point.getY() - delta.getY());
    } else if (this.hasHorizontalCollision()) {
      return new Vector(this.point.getX() - delta.getX(), this.point.getY() + delta.getY());
    } else {
      return new Vector(this.point.getX() + delta.getX(), this.point.getY() - delta.getY());
    }
  }
}
