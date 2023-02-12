package com.seat.sim.server.math;

import java.util.Optional;

import com.seat.sim.common.math.Grid;
import com.seat.sim.common.math.Vector;
import com.seat.sim.common.math.Zone;
import com.seat.sim.common.math.ZoneType;

public class Physics {

  private static Vector getBotRightCorner(Grid grid) {
    return new Vector(grid.getWidth(), grid.getHeight());
  }

  private static Vector getBotRightCorner(Zone zone) {
    return new Vector(
          zone.getLocation().getX() + zone.getSize() / 2,
          zone.getLocation().getY() + zone.getSize() / 2
        );
  }

  private static Vector getTopLeftCorner(Grid grid) {
    return Vector.ZERO;
  }

  private static Vector getTopLeftCorner(Zone zone) {
    return new Vector(
          zone.getLocation().getX() - zone.getSize() / 2,
          zone.getLocation().getY() - zone.getSize() / 2
        );
  }

  public static Optional<Vector> getBounce(Vector tail, Vector tip, Grid grid) {
    return Physics.getBounce(tail, tip, Physics.getTopLeftCorner(grid), Physics.getBotRightCorner(grid));
  }

  public static Optional<Vector> getBounce(Vector tail, Vector tip, Zone zone) {
    return Physics.getBounce(tail, tip, Physics.getTopLeftCorner(zone), Physics.getBotRightCorner(zone));
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
    return Physics.getBoundaryCollision(tail, tip, Physics.getTopLeftCorner(grid), Physics.getBotRightCorner(grid));
  }

  public static Optional<Vector> getBoundaryCollision(Vector tail, Vector tip, Zone zone) {
    return Physics.getBoundaryCollision(tail, tip, Physics.getTopLeftCorner(zone), Physics.getBotRightCorner(zone));
  }

  public static Optional<Vector> getBoundaryCollision(Vector tail, Vector tip, Vector topLeft, Vector botRight) {
    if (Physics.isInbounds(tip, topLeft, botRight)) {
      return Optional.empty();
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

  public static Optional<Vector> getRayTrace(Vector tail, Vector tip, Grid grid) {
    if (!grid.hasZoneAtLocation(tail)) {
      return Optional.empty();
    }
    if (grid.hasZoneAtLocation(tip) && grid.getZoneAtLocation(tail) == grid.getZoneAtLocation(tip)) {
      return Optional.empty();
    }
    Vector ray = tail;
    Optional<Zone> zone = Optional.of(grid.getZoneAtLocation(ray));
    while (zone.isPresent() && Vector.dist(tail, ray) < Vector.dist(tail, tip)) {
      Optional<Vector> coll = Physics.getBoundaryCollision(ray, tip, zone.get());
      if (coll.isEmpty()) {
        break;
      }
      ray = coll.get();
      double size = zone.get().getSize();
      double cx = zone.get().getLocation().getX(), cy = zone.get().getLocation().getY();
      double x = cx - size / 2, y = cy - size / 2;
      if (coll.get().getX() == x && coll.get().getY() == y) {
        if (grid.hasZoneAtLocation(cx - size, cy) &&
            grid.getZoneAtLocation(cx - size, cy).hasZoneType(ZoneType.BLOCKED)) {
          return coll;
        } else if (grid.hasZoneAtLocation(cx, cy - size) &&
            grid.getZoneAtLocation(cx, cy - size).hasZoneType(ZoneType.BLOCKED)) {
          return coll;
        }
        zone = grid.checkZoneAtLocation(cx - size, cy - size);
      } else if (coll.get().getX() == x + size && coll.get().getY() == y) {
        if (grid.hasZoneAtLocation(cx + size, cy) &&
            grid.getZoneAtLocation(cx + size, cy).hasZoneType(ZoneType.BLOCKED)) {
          return coll;
        } else if (grid.hasZoneAtLocation(cx, cy - size) &&
            grid.getZoneAtLocation(cx, cy - size).hasZoneType(ZoneType.BLOCKED)) {
          return coll;
        }
        zone = grid.checkZoneAtLocation(cx + size, cy - size);
      } else if (coll.get().getX() == x + size && coll.get().getY() == y + size) {
        if (grid.hasZoneAtLocation(cx + size, cy) &&
            grid.getZoneAtLocation(cx + size, cy).hasZoneType(ZoneType.BLOCKED)) {
          return coll;
        } else if (grid.hasZoneAtLocation(cx, cy + size) &&
            grid.getZoneAtLocation(cx, cy + size).hasZoneType(ZoneType.BLOCKED)) {
          return coll;
        }
        zone = grid.checkZoneAtLocation(cx + size, cy + size);
      } else if (coll.get().getX() == x && coll.get().getY() == y + size) {
        if (grid.hasZoneAtLocation(cx - size, cy) &&
            grid.getZoneAtLocation(cx - size, cy).hasZoneType(ZoneType.BLOCKED)) {
          return coll;
        } else if (grid.hasZoneAtLocation(cx, cy + size) &&
            grid.getZoneAtLocation(cx, cy + size).hasZoneType(ZoneType.BLOCKED)) {
          return coll;
        }
        zone = grid.checkZoneAtLocation(cx - size, cy + size);
      } else if (coll.get().getX() == x) {
        zone = grid.checkZoneAtLocation(cx - size, cy);
      } else if (coll.get().getY() == y) {
        zone = grid.checkZoneAtLocation(cx, cy - size);
      } else if (coll.get().getX() == x + size) {
        zone = grid.checkZoneAtLocation(cx + size, cy);
      } else {
        zone = grid.checkZoneAtLocation(cx, cy + size);
      }
      if (zone.isPresent() && zone.get().hasZoneType(ZoneType.BLOCKED)) {
        return coll;
      }
    }
    if (grid.hasZoneAtLocation(tip) && grid.getZoneAtLocation(tip).hasZoneType(ZoneType.BLOCKED)) {
      return Physics.getBoundaryCollision(tip, tail, grid.getZoneAtLocation(tip));
    }
    return Physics.getBoundaryCollision(tail, tip, grid);
  }

  public static boolean isInbounds(Vector pos, Grid grid) {
    return Physics.isInbounds(pos, 0., 0., grid.getWidth(), grid.getHeight());
  }

  public static boolean isInbounds(Vector pos, Zone zone) {
    return Physics.isInbounds(pos, Physics.getTopLeftCorner(zone), Physics.getBotRightCorner(zone));
  }

  public static boolean isInbounds(Vector pos, Vector topLeft, Vector botRight) {
    return Physics.isInbounds(pos, topLeft.getX(), topLeft.getY(),
        botRight.getX() - topLeft.getX(), botRight.getY() - topLeft.getY());
  }

  public static boolean isInbounds(Vector pos, double x, double y, double width, double height) {
    return x <= pos.getX() && pos.getX() < x + width && y <= pos.getY() && pos.getY() < y + height;
  }
}
