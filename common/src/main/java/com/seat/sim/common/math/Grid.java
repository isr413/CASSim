package com.seat.sim.common.math;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Optional;

import com.seat.sim.common.core.CommonException;
import com.seat.sim.common.json.*;

/** Represents the map grid. */
public class Grid extends Jsonable {
  public static final String GRID_HEIGHT = "grid_height";
  public static final String GRID_WIDTH = "grid_width";
  public static final String ZONES = "zones";
  public static final String ZONE_SIZE = "zone_size";

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

  public Grid(BufferedImage img) {
    this(img, 1);
  }

  public Grid(BufferedImage img, int zoneSize) {
    this.width = img.getWidth();
    this.height= img.getHeight();
    this.zoneSize = zoneSize;
    this.isCustomGrid = true;
    this.zones = new Zone[img.getHeight()][img.getWidth()];
    for (int y = 0; y < img.getHeight(); y++) {
      for (int x = 0; x < img.getWidth(); x++) {
        if (img.getRGB(x, y) == Color.BLACK.getRGB()) {
          this.zones[y][x] = new Zone(ZoneType.BLOCKED, new Vector(x, y), zoneSize);
        } else {
          this.zones[y][x] = new Zone(new Vector(x, y), zoneSize);
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

  public Optional<Zone> checkZoneAtLocation(Vector location) {
    return this.checkZoneAtLocation(location.getX(), location.getY());
  }

  public Optional<Zone> checkZoneAtLocation(double x, double y) {
    if (this.hasZoneAtLocation(x, y)) {
      return Optional.of(this.getZoneAtLocation(x, y));
    }
    return Optional.empty();
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

  public boolean hasZoneAtLocation(Vector location) {
    return this.hasZoneAtLocation(location.getX(), location.getY());
  }

  public boolean hasZoneAtLocation(double x, double y) {
    return 0 <= x && x < this.getWidth() && 0 <= y && y < this.getHeight();
  }

  public boolean hasZones() {
    return this.isCustomGrid;
  }

  public Json toJson() throws JsonException {
    return this.getJsonBuilder().toJson();
  }
}
