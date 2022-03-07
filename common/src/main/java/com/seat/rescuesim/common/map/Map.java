package com.seat.rescuesim.common.map;

import com.seat.rescuesim.common.json.*;
import com.seat.rescuesim.common.math.*;

/** Represents the map grid. */
public class Map extends JSONAble {
    private static final String MAP_GRID = "map_grid";
    private static final String MAP_HEIGHT = "map_height";
    private static final String MAP_WIDTH = "map_width";
    private static final String ZONE_SIZE = "zone_size";

    private Zone[][] grid;
    private int height;     // number of zones in each column of the grid
    private int width;      // number of zones in each row of the grid
    private int zoneSize;   // zones in the same map grid must have a uniform size

    public Map(JSONObject json) {
        super(json);
    }

    public Map(JSONOption option) {
        super(option);
    }

    public Map(String encoding) {
        super(encoding);
    }

    public Map(int zoneSize, int width, int height) {
        this(new Zone[height][width], zoneSize, width, height);
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                this.grid[y][x] = new Zone(new Vector(this.zoneSize*(x + 0.5), this.zoneSize*(y + 0.5)), this.zoneSize);
            }
        }
    }

    public Map(Zone[][] grid, int zoneSize, int width, int height) {
        this.grid = grid;
        this.zoneSize = zoneSize;
        this.width = width;
        this.height = height;
    }

    @Override
    protected void decode(JSONObject json) {
        this.width = json.getInt(Map.MAP_WIDTH);
        this.height = json.getInt(Map.MAP_HEIGHT);
        this.zoneSize = json.getInt(Map.ZONE_SIZE);
        this.grid = new Zone[this.height][this.width];
        JSONArray jsonGrid = json.getJSONArray(Map.MAP_GRID);
        for (int y = 0; y < this.height; y++) {
            JSONArray jsonRow = jsonGrid.getJSONArray(y);
            for (int x = 0; x < this.width; x++) {
                this.grid[y][x] = new Zone(jsonRow.getJSONObject(x));
            }
        }
    }

    public Zone[][] getGrid() {
        return this.grid;
    }

    public int getHeight() {
        return this.height;
    }

    public String getLabel() {
        return String.format("<(%d, %d)>", this.width, this.height);
    }

    public int getWidth() {
        return this.width;
    }

    /** Returns the Zone located at row y, column x.
     * @throws IndexOutOfBoundsException if the either y or x are out of bounds
     */
    public Zone getZone(int y, int x) throws IndexOutOfBoundsException {
        if (y < 0 || this.height <= y) {
            throw new IndexOutOfBoundsException(y);
        }
        if (x < 0 || this.width <= x) {
            throw new IndexOutOfBoundsException(x);
        }
        return this.grid[y][x];
    }

    /** Returns the Zone that contains the location with the specified x and y coordinates.
     * @throws IndexOutOfBoundsException if the either y or x are out of bounds
     */
    public Zone getZoneAtLocation(double x, double y) {
        return this.getZone((int) (y / this.zoneSize), (int) (x / this.zoneSize));
    }

    /** Returns the Zone that contains the location with the specified coordinates.
     * @throws IndexOutOfBoundsException if the vector is out of bounds
     */
    public Zone getZoneAtLocation(Vector location) {
        return this.getZoneAtLocation(location.getX(), location.getY());
    }

    public int getZoneSize() {
        return this.zoneSize;
    }

    public JSONOption toJSON() {
        JSONObjectBuilder json = JSONBuilder.Object();
        json.put(Map.MAP_WIDTH, this.width);
        json.put(Map.MAP_HEIGHT, this.height);
        json.put(Map.ZONE_SIZE, this.zoneSize);
        JSONArrayBuilder jsonGrid = JSONBuilder.Array();
        for (int y = 0; y < this.height; y++) {
            JSONArrayBuilder jsonRow = JSONBuilder.Array();
            for (int x = 0; x < this.width; x++) {
                jsonRow.put(this.getZone(y, x).toJSON());
            }
            jsonGrid.put(jsonRow.toJSON());
        }
        json.put(Map.MAP_GRID, jsonGrid.toJSON());
        return json.toJSON();
    }

    public boolean equals(Map map) {
        boolean flag = this.width == map.width && this.height == map.height && this.zoneSize == map.zoneSize;
        if (!flag) {
            return false;
        }
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                if (!this.grid[y][x].equals(map.grid[y][x])) {
                    return false;
                }
            }
        }
        return true;
    }

}
