package com.seat.sim.server.math;

import java.util.Optional;

import com.seat.sim.common.math.Grid;
import com.seat.sim.common.math.Vector;
import com.seat.sim.common.math.Zone;
import com.seat.sim.common.math.ZoneType;

public class Physics {

  public static Optional<Vector> getBounce(Vector tail, Vector tip, Grid grid) {
    return Physics.getBounce(tail, tip, new Box(grid));
  }

  public static Optional<Vector> getBounce(Vector tail, Vector tip, Zone zone) {
    return Physics.getBounce(tail, tip, new Box(zone));
  }

  public static Optional<Vector> getBounce(Vector tail, Vector tip, Box box) {
    Optional<Collision> coll = Physics.getBoundaryCollision(tail, tip, box);
    if (coll.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(coll.get().reflect(tip));
  }

  public static Optional<Collision> getBoundaryCollision(Vector tail, Vector tip, Grid grid) {
    return Physics.getBoundaryCollision(tail, tip, new Box(grid));
  }

  public static Optional<Collision> getBoundaryCollision(Vector tail, Vector tip, Zone zone) {
    return Physics.getBoundaryCollision(tail, tip, new Box(zone));
  }

  public static Optional<Collision> getBoundaryCollision(Vector tail, Vector tip, Box box) {
    if (!box.isInbounds(tail) && !box.isInbounds(tip)) {
      return Optional.empty();
    }
    if (box.isBoundaryCollision(tip)) {
      return Optional.of(new Collision(tip, box));
    }
    if (box.isInbounds(tail) && box.isInbounds(tip)) {
      return Optional.empty();
    }
    if (!box.isInbounds(tail)) {
      Vector tmp = tail;
      tail = tip;
      tip = tmp;
    }
    double slope = Vector.slope(tail, tip);
    if (slope == Double.POSITIVE_INFINITY) {
      return Optional.of(new Collision(new Vector(tip.getX(), box.getBotY()), box));
    }
    if (slope == Double.NEGATIVE_INFINITY) {
      return Optional.of(new Collision(new Vector(tip.getX(), box.getTopY()), box));
    }
    if (Physics.getDirection(tail, tip) > 0) {
      if (Vector.near(slope, 0.)) {
        return Optional.of(new Collision(new Vector(box.getRightX(), tip.getY()), box));
      }
      Vector topRight = box.getTopRightCorner();
      if (slope < Vector.slope(tail, topRight)) {
        return Optional.of(new Collision(
              new Vector(tail.getX() + (topRight.getY() - tail.getY()) / slope, topRight.getY()),
              box
            ));
      }
      if (Vector.near(slope, Vector.slope(tail, topRight))) {
        return Optional.of(new Collision(topRight, box));
      }
      Vector botRight = box.getBotRightCorner();
      if (slope < Vector.slope(tail, botRight)) {
        return Optional.of(new Collision(
              new Vector(botRight.getX(), tail.getY() + (botRight.getX() - tail.getX()) * slope),
              box
            ));
      }
      if (Vector.near(slope, Vector.slope(tail, botRight))) {
        return Optional.of(new Collision(botRight, box));
      }
      return Optional.of(new Collision(
            new Vector(tail.getX() + (botRight.getY() - tail.getY()) / slope, botRight.getY()),
            box
          ));
    } else {
      if (Vector.near(slope, 0.)) {
        return Optional.of(new Collision(new Vector(box.getLeftX(), tip.getY()), box));
      }
      Vector botLeft = box.getBotLeftCorner();
      if (slope < Vector.slope(tail, botLeft)) {
        return Optional.of(new Collision(
              new Vector(tail.getX() + (botLeft.getY() - tail.getY()) / slope, botLeft.getY()),
              box
            ));
      }
      if (Vector.near(slope, Vector.slope(tail, botLeft))) {
        return Optional.of(new Collision(botLeft, box));
      }
      Vector topLeft = box.getTopLeftCorner();
      if (slope < Vector.slope(tail, topLeft)) {
        return Optional.of(new Collision(
              new Vector(topLeft.getX(), tail.getY() + (topLeft.getX() - tail.getX()) * slope),
              box
            ));
      }
      if (Vector.near(slope, Vector.slope(tail, topLeft))) {
        return Optional.of(new Collision(topLeft, box));
      }
      return Optional.of(new Collision(
            new Vector(tail.getX() + (topLeft.getY() - tail.getY()) / slope, topLeft.getY()),
            box
          ));
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

  public static Optional<Collision> getRayTrace(Vector tail, Vector tip, Grid grid) {
    if (!grid.hasZoneAtLocation(tail)) {
      return Optional.empty();
    }
    if (grid.hasZoneAtLocation(tip) && grid.getZoneAtLocation(tail) == grid.getZoneAtLocation(tip)) {
      return Optional.empty();
    }
    Vector ray = tail;
    Optional<Zone> zone = grid.checkZoneAtLocation(tail);
    while (zone.isPresent() && Vector.dist(tail, ray) < Vector.dist(tail, tip)) {
      Optional<Collision> coll = Physics.getBoundaryCollision(ray, tip, zone.get());
      if (coll.isEmpty()) {
        break;
      }
      ray = coll.get().getPoint();
      double size = zone.get().getSize();
      double cx = zone.get().getLocation().getX(), cy = zone.get().getLocation().getY();
      double x = cx - size / 2, y = cy - size / 2;
      if (Vector.near(ray.getX(), x) && Vector.near(ray.getY(), y)) {
        zone = grid.checkZoneAtLocation(cx - size, cy - size);
        if (zone.isPresent() && grid.hasZoneAtLocation(cx - size, cy) && grid.hasZoneAtLocation(cx, cy - size) &&
            grid.getZoneAtLocation(cx - size, cy).hasZoneType(ZoneType.BLOCKED) &&
            grid.getZoneAtLocation(cx, cy - size).hasZoneType(ZoneType.BLOCKED)) {
          return Optional.of(new Collision(ray, new Box(zone.get())));
        }
      } else if (Vector.near(ray.getX(), x + size) && Vector.near(ray.getY(), y)) {
        zone = grid.checkZoneAtLocation(cx + size, cy - size);
        if (zone.isPresent() && grid.hasZoneAtLocation(cx + size, cy) && grid.hasZoneAtLocation(cx, cy - size) &&
            grid.getZoneAtLocation(cx + size, cy).hasZoneType(ZoneType.BLOCKED) &&
            grid.getZoneAtLocation(cx, cy - size).hasZoneType(ZoneType.BLOCKED)) {
          return Optional.of(new Collision(ray, new Box(zone.get())));
        }
      } else if (Vector.near(ray.getX(), x + size) && Vector.near(ray.getY(), y + size)) {
        zone = grid.checkZoneAtLocation(cx + size, cy + size);
        if (zone.isPresent() && grid.hasZoneAtLocation(cx + size, cy) && grid.hasZoneAtLocation(cx, cy + size) &&
            grid.getZoneAtLocation(cx + size, cy).hasZoneType(ZoneType.BLOCKED) &&
            grid.getZoneAtLocation(cx, cy + size).hasZoneType(ZoneType.BLOCKED)) {
          return Optional.of(new Collision(ray, new Box(zone.get())));
        }
      } else if (Vector.near(ray.getX(), x) && Vector.near(ray.getY(), y + size)) {
        zone = grid.checkZoneAtLocation(cx - size, cy + size);
        if (zone.isPresent() && grid.hasZoneAtLocation(cx - size, cy) && grid.hasZoneAtLocation(cx, cy + size) &&
            grid.getZoneAtLocation(cx - size, cy).hasZoneType(ZoneType.BLOCKED) &&
            grid.getZoneAtLocation(cx, cy + size).hasZoneType(ZoneType.BLOCKED)) {
          return Optional.of(new Collision(ray, new Box(zone.get())));
        }
      } else if (Vector.near(ray.getX(), x)) {
        zone = grid.checkZoneAtLocation(cx - size, cy);
      } else if (Vector.near(ray.getY(), y)) {
        zone = grid.checkZoneAtLocation(cx, cy - size);
      } else if (Vector.near(ray.getX(), x + size)) {
        zone = grid.checkZoneAtLocation(cx + size, cy);
      } else {
        zone = grid.checkZoneAtLocation(cx, cy + size);
      }
      if (zone.isPresent() && zone.get().hasZoneType(ZoneType.BLOCKED)) {
        return Optional.of(new Collision(ray, new Box(zone.get())));
      }
    }
    if (grid.hasZoneAtLocation(tip) && grid.getZoneAtLocation(tip).hasZoneType(ZoneType.BLOCKED)) {
      return Physics.getBoundaryCollision(tip, tail, grid.getZoneAtLocation(tip));
    }
    return Physics.getBoundaryCollision(tail, tip, grid);
  }

  public static boolean isInbounds(Vector point, Grid grid) {
    return new Box(grid).isInbounds(point);
  }

  public static boolean isInbounds(Vector point, Zone zone) {
    return new Box(zone).isInbounds(point);
  }

  public static boolean isInbounds(Vector point, double x, double y, double width, double height) {
    return new Box(x, y, width, height).isInbounds(point);
  }
}
