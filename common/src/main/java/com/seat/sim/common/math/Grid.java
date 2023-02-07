package com.seat.sim.common.math;

import java.util.Optional;

import com.seat.sim.common.core.CommonException;
import com.seat.sim.common.json.*;

/** Represents the map grid. */
public class Grid extends Jsonable {
  public static final String GRID_HEIGHT = "grid_height";
  public static final String GRID_WIDTH = "grid_width";
  public static final String ZONES = "zones";
  public static final String ZONE_SIZE = "zone_size";

  public static Grid fromBitmap() {
    // TODO: implement
    throw new UnsupportedOperationException("Not yet implemented");
  }

  private int height; // number of zones in each column of the grid
  private boolean isCustomGrid;
  private int width; // number of zones in each row of the grid
  private Zone[][] zones;
  private int zoneSize; // zones in the same map grid must have a uniform size

  public Grid(int width, int height, int zoneSize) {
    this(width, height, zoneSize, null);
  }

  public Grid(int width, int height, int zoneSize, Zone[][] zones) {
    this.width = width;
    this.height = height;
    this.zoneSize = zoneSize;
    if (zones != null) {
      this.isCustomGrid = true;
      this.zones = zones;
    } else {
      this.isCustomGrid = false;
      this.zones = new Zone[this.height][this.width];
      for (int y = 0; y < this.height; y++) {
        for (int x = 0; x < this.width; x++) {
          this.zones[y][x] = new Zone(
              new Vector(this.zoneSize * (x + 0.5), this.zoneSize * (y + 0.5)),
              this.zoneSize);
        }
      }
    }
  }

  public Grid(Json json) throws JsonException {
    super(json);
  }

  @Override
  protected void decode(JsonObject json) throws JsonException {
    this.width = json.getInt(Grid.GRID_WIDTH);
    this.height = json.getInt(Grid.GRID_HEIGHT);
    this.zoneSize = json.getInt(Grid.ZONE_SIZE);
    this.zones = new Zone[this.height][this.width];
    if (json.hasKey(Grid.ZONES)) {
      this.isCustomGrid = true;
      JsonArray jsonZones = json.getJsonArray(Grid.ZONES);
      for (int y = 0; y < this.height; y++) {
        JsonArray jsonRow = jsonZones.getJsonArray(y);
        for (int x = 0; x < this.width; x++) {
          this.zones[y][x] = new Zone(jsonRow.getJson(x));
        }
      }
    } else {
      this.isCustomGrid = false;
      for (int y = 0; y < this.height; y++) {
        for (int x = 0; x < this.width; x++) {
          this.zones[y][x] = new Zone(
              new Vector(
                  this.zoneSize * (x + 0.5),
                  this.zoneSize * (y + 0.5)),
              this.zoneSize);
        }
      }
    }
  }

  protected JsonObjectBuilder getJsonBuilder() throws JsonException {
    JsonObjectBuilder json = JsonBuilder.Object();
    json.put(Grid.GRID_WIDTH, this.width);
    json.put(Grid.GRID_HEIGHT, this.height);
    json.put(Grid.ZONE_SIZE, this.zoneSize);
    if (this.isCustomGrid) {
      JsonArrayBuilder jsonZones = JsonBuilder.Array();
      for (int row = 0; row < this.height; row++) {
        JsonArrayBuilder jsonRow = JsonBuilder.Array();
        for (int col = 0; col < this.width; col++) {
          jsonRow.put(this.getZone(row, col).toJson());
        }
        jsonZones.put(jsonRow.toJson());
      }
      json.put(Grid.ZONES, jsonZones.toJson());
    }
    return json;
  }

  public Optional<Vector> getBoundsCollisionLocation(Vector location, Vector nextLocation) {
    if (this.isInbounds(nextLocation)) {
      return Optional.empty();
    }
    double slope = Vector.slope(location, nextLocation);
    Vector topLeft = new Vector(0, 0);
    Vector topRight = new Vector(this.getWidth(), 0);
    Vector botRight = new Vector(this.getWidth(), this.getHeight());
    Vector botLeft = new Vector(0, this.getHeight());
    if (nextLocation.getX() < 0 && nextLocation.getY() < 0
        && SimMath.near(Vector.slope(location, topLeft), slope)) {
      return Optional.of(topLeft);
    } else if (this.getWidth() < nextLocation.getX() && nextLocation.getY() < 0
        && SimMath.near(Vector.slope(location, topRight), slope)) {
      return Optional.of(topRight);
    } else if (this.getWidth() < nextLocation.getX() && this.getHeight() < nextLocation.getY()
        && SimMath.near(Vector.slope(location, botRight), slope)) {
      return Optional.of(botRight);
    } else if (nextLocation.getX() < 0 && this.getHeight() < nextLocation.getY()
        && SimMath.near(Vector.slope(location, botLeft), slope)) {
      return Optional.of(botLeft);
    } else if (nextLocation.getY() < 0
        && (Math.abs(Vector.slope(location, topLeft)) < Math.abs(slope)
            || Math.abs(Vector.slope(location, topRight)) < Math.abs(slope))) {
      double deltaX = (Double.isFinite(slope) && slope != 0) ? (0 - location.getY()) / slope : 0;
      return Optional.of(new Vector(location.getX() + deltaX, 0));
    } else if (this.getHeight() < nextLocation.getY()
        && (Math.abs(Vector.slope(location, botLeft)) < Math.abs(slope)
            || Math.abs(Vector.slope(location, botRight)) < Math.abs(slope))) {
      double deltaX = (Double.isFinite(slope) && slope != 0) ? (this.getHeight() - location.getY()) / slope : 0;
      return Optional.of(new Vector(location.getX() + deltaX, this.getHeight()));
    } else if (this.getWidth() < nextLocation.getX()) {
      double deltaY = (Double.isFinite(slope)) ? (this.getWidth() - location.getX()) * slope : 0;
      return Optional.of(new Vector(this.getWidth(), location.getY() + deltaY));
    } else {
      double deltaY = (Double.isFinite(slope)) ? (0 - location.getX()) * slope : 0;
      return Optional.of(new Vector(0, location.getY() + deltaY));
    }
  }

  public int getHeight() {
    return this.height * this.zoneSize;
  }

  public int getHeightInZones() {
    return this.height;
  }

  public String getLabel() {
    return String.format("<g: (%d, %d)>", this.width, this.height);
  }

  public Optional<Vector> getLocationAfterBounce(Vector location, Vector nextLocation) {
    Optional<Vector> optional = this.getBoundsCollisionLocation(location, nextLocation);
    if (optional.isEmpty()) {
      return Optional.empty();
    }
    Vector boundsCollision = optional.get();
    double deltaX = nextLocation.getX() - boundsCollision.getX();
    double deltaY = nextLocation.getY() - boundsCollision.getY();
    Vector locationAfter = null;
    if ((boundsCollision.getX() == 0 || boundsCollision.getX() == this.getWidth()) &&
        (boundsCollision.getY() == 0 || boundsCollision.getY() == this.getHeight())) {
      locationAfter = new Vector(boundsCollision.getX() - deltaX, boundsCollision.getY() - deltaY);
    } else if (boundsCollision.getY() == 0 || boundsCollision.getY() == this.getHeight()) {
      locationAfter = new Vector(boundsCollision.getX() + deltaX, boundsCollision.getY() - deltaY);
    } else {
      locationAfter = new Vector(boundsCollision.getX() - deltaX, boundsCollision.getY() + deltaY);
    }
    if (!this.isInbounds(locationAfter)) {
      return this.getLocationAfterBounce(boundsCollision, locationAfter);
    }
    return Optional.of(locationAfter);
  }

  public int getWidth() {
    return this.width * this.zoneSize;
  }

  public int getWidthInZones() {
    return this.width;
  }

  /**
   * Returns the Zone located at row, col.
   * 
   * @throws CommonException if the either y or x are out of bounds
   */
  public Zone getZone(int row, int col) throws CommonException {
    if (row < 0 || this.height <= row) {
      throw new CommonException(new IndexOutOfBoundsException(row).toString());
    }
    if (col < 0 || this.width <= col) {
      throw new CommonException(new IndexOutOfBoundsException(col).toString());
    }
    return this.zones[row][col];
  }

  /**
   * Returns the Zone that contains the location with the specified x and y
   * coordinates.
   * 
   * @throws CommonException if the either y or x are out of bounds
   */
  public Zone getZoneAtLocation(double x, double y) throws CommonException {
    return this.getZone((int) (y / this.zoneSize), (int) (x / this.zoneSize));
  }

  /**
   * Returns the Zone that contains the location with the specified coordinates.
   * 
   * @throws CommonException if the vector is out of bounds
   */
  public Zone getZoneAtLocation(Vector location) throws CommonException {
    return this.getZoneAtLocation(location.getX(), location.getY());
  }

  public Zone[][] getZones() {
    return this.zones;
  }

  public int getZoneSize() {
    return this.zoneSize;
  }

  public boolean isInbounds(Vector location) {
    return 0 <= location.getX() && location.getX() <= this.getWidth() && 0 <= location.getY() &&
        location.getY() <= this.getHeight();
  }

  public boolean isOutOfBounds(Vector location) {
    return !this.isInbounds(location);
  }

  public Json toJson() throws JsonException {
    return this.getJsonBuilder().toJson();
  }
}
