package com.seat.sim.server.math;

import java.util.Optional;

import com.seat.sim.common.math.Grid;
import com.seat.sim.common.math.Vector;

public class Physics {

  public static Optional<Vector> getBounce(Vector tail, Vector tip, Grid grid) {
    return Physics.getBounce(tail, tip, Vector.ZERO, new Vector(grid.getWidth(), grid.getHeight()));
  }

  public static Optional<Vector> getBounce(Vector tail, Vector tip, Vector topLeft, Vector botRight) {
    Optional<Vector> coll = Physics.getBoundaryCollision(tail, tip, topLeft, botRight);
    if (coll.isEmpty()) {
      return Optional.empty();
    }
    Vector ray = Vector.sub(tip, coll.get());
    if ((coll.get().getX() == topLeft.getX() || coll.get().getX() == botRight.getX()) &&
        (coll.get().getY() == topLeft.getY() || coll.get().getY() == botRight.getY())) {
      return Optional.of(new Vector(coll.get().getX() - ray.getX(), coll.get().getY() - ray.getY()));
    }
    if ((coll.get().getX() == topLeft.getX() || coll.get().getX() == botRight.getX())) {
      return Optional.of(new Vector(coll.get().getX() - ray.getX(), coll.get().getY() + ray.getY()));
    }
    return Optional.of(new Vector(coll.get().getX() + ray.getX(), coll.get().getY() - ray.getY()));
  }

  public static Optional<Vector> getBoundaryCollision(Vector tail, Vector tip, Grid grid) {
    return Physics.getBoundaryCollision(tail, tip, Vector.ZERO, new Vector(grid.getWidth(), grid.getHeight()));
  }

  public static Optional<Vector> getBoundaryCollision(Vector tail, Vector tip, Vector topLeft, Vector botRight) {
    if (Physics.isInbounds(tip, topLeft, botRight)) {
      return Optional.empty();
    }
    if (tail.getX() == topLeft.getX() || tail.getX() == botRight.getX() ||
        tail.getY() == topLeft.getY() || tail.getY() == botRight.getY()) {
      return Optional.of(tail);
    }
    if (tip.getX() == topLeft.getX() || tip.getX() == botRight.getX() ||
        tip.getY() == topLeft.getY() || tip.getY() == botRight.getY()) {
      return Optional.of(tip);
    }
    double slope = Vector.slope(tail, tip);
    if (slope == Double.POSITIVE_INFINITY) {
      return Optional.of(new Vector(tip.getX(), botRight.getY()));
    }
    if (slope == Double.NEGATIVE_INFINITY) {
      return Optional.of(new Vector(tip.getX(), topLeft.getY()));
    }
    if (Physics.getDirection(tail, tip) > 0) {
      if (Vector.near(slope, 0.)) {
        return Optional.of(new Vector(botRight.getX(), tip.getY()));
      }
      Vector topRight = new Vector(botRight.getX(), topLeft.getY());
      if (slope < Vector.slope(tail, topRight)) {
        return Optional.of(new Vector(tail.getX() + (topRight.getY() - tail.getY()) / slope, topRight.getY()));
      }
      if (Vector.near(slope, Vector.slope(tail, topRight))) {
        return Optional.of(topRight);
      }
      if (slope < Vector.slope(tail, botRight)) {
        return Optional.of(new Vector(botRight.getX(), tail.getY() + (botRight.getX() - tail.getX()) * slope));
      }
      if (Vector.near(slope, Vector.slope(tail, botRight))) {
        return Optional.of(botRight);
      }
      return Optional.of(new Vector(tail.getX() + (botRight.getY() - tail.getY()) / slope, botRight.getY()));
    } else {
      if (Vector.near(slope, 0.)) {
        return Optional.of(new Vector(topLeft.getX(), tip.getY()));
      }
      Vector botLeft = new Vector(topLeft.getX(), botRight.getY());
      if (slope < Vector.slope(tail, botLeft)) {
        return Optional.of(new Vector(tail.getX() + (botLeft.getY() - tail.getY()) / slope, botLeft.getY()));
      }
      if (Vector.near(slope, Vector.slope(tail, botLeft))) {
        return Optional.of(botLeft);
      }
      if (slope < Vector.slope(tail, topLeft)) {
        return Optional.of(new Vector(topLeft.getX(), tail.getY() + (topLeft.getX() - tail.getX()) * slope));
      }
      if (Vector.near(slope, Vector.slope(tail, topLeft))) {
        return Optional.of(topLeft);
      }
      return Optional.of(new Vector(tail.getX() + (topLeft.getY() - tail.getY()) / slope, topLeft.getY()));
    }
  }

  public static int getDirection(Vector tail, Vector tip) {
    return (Vector.near(tail.getX(), tip.getX()))
        ? (Vector.near(tail.getY(), tip.getY()))
            ? 0
            : (tip.getY() > tail.getY())
                ? 1
                : -1
        : (tip.getX() > tail.getX())
            ? 1
            : -1;
  }

  public static boolean isInbounds(Vector pos, Vector topLeft, Vector botRight) {
    return topLeft.getX() < pos.getX() && pos.getX() < botRight.getX() &&
        topLeft.getY() < pos.getY() && pos.getY() < botRight.getY();
  }
}
