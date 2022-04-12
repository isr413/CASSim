package com.seat.sim.common.map;

import com.seat.sim.common.core.CommonException;
import com.seat.sim.common.json.JSONAble;
import com.seat.sim.common.json.JSONArray;
import com.seat.sim.common.json.JSONArrayBuilder;
import com.seat.sim.common.json.JSONBuilder;
import com.seat.sim.common.json.JSONException;
import com.seat.sim.common.json.JSONObject;
import com.seat.sim.common.json.JSONObjectBuilder;
import com.seat.sim.common.json.JSONOption;
import com.seat.sim.common.math.Vector;

/** Represents the map grid. */
public class Map extends JSONAble {
    public static final String MAP_GRID = "map_grid";
    public static final String MAP_HEIGHT = "map_height";
    public static final String MAP_WIDTH = "map_width";
    public static final String ZONE_SIZE = "zone_size";

    private boolean defaultGrid;
    private Zone[][] grid;
    private int height;     // number of zones in each column of the grid
    private int width;      // number of zones in each row of the grid
    private int zoneSize;   // zones in the same map grid must have a uniform size

    public Map(int zoneSize, int width, int height) {
        this(null, zoneSize, width, height);
    }

    public Map(Zone[][] grid, int zoneSize, int width, int height) {
        this.defaultGrid = (grid == null);
        if (this.defaultGrid) {
            this.grid = new Zone[height][width];
            for (int y = 0; y < this.height; y++) {
                for (int x = 0; x < this.width; x++) {
                    this.grid[y][x] = new Zone(
                        new Vector(this.zoneSize*(x + 0.5), this.zoneSize*(y + 0.5)),
                        this.zoneSize
                    );
                }
            }
        } else {
            this.grid = grid;
        }
        this.zoneSize = zoneSize;
        this.width = width;
        this.height = height;
    }

    public Map(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        this.width = json.getInt(Map.MAP_WIDTH);
        this.height = json.getInt(Map.MAP_HEIGHT);
        this.zoneSize = json.getInt(Map.ZONE_SIZE);
        this.grid = new Zone[this.height][this.width];
        if (json.hasKey(Map.MAP_GRID)) {
            JSONArray jsonGrid = json.getJSONArray(Map.MAP_GRID);
            for (int y = 0; y < this.height; y++) {
                JSONArray jsonRow = jsonGrid.getJSONArray(y);
                for (int x = 0; x < this.width; x++) {
                    this.grid[y][x] = new Zone(jsonRow.getJSONOption(x));
                }
            }
        } else {
            for (int y = 0; y < this.height; y++) {
                for (int x = 0; x < this.width; x++) {
                    this.grid[y][x] = new Zone(
                        new Vector(this.zoneSize*(x + 0.5), this.zoneSize*(y + 0.5)),
                        this.zoneSize
                    );
                }
            }
        }
    }

    public Vector bounceLocation(Vector location, Vector nextLocation) {
        Vector edgePoint = this.edgePointBetween(location, nextLocation);
        if (edgePoint == null) {
            return null;
        }
        double deltaX = nextLocation.getX() - edgePoint.getX();
        double deltaY = nextLocation.getY() - edgePoint.getY();
        if ((edgePoint.getX() == 0 || edgePoint.getX() == this.getWidth()) &&
                (edgePoint.getY() == 0 || edgePoint.getY() == this.getHeight())) {
            return new Vector(edgePoint.getX() - deltaX, edgePoint.getY() - deltaY);
        } else if (edgePoint.getY() == 0 || edgePoint.getY() == this.getHeight()) {
            return new Vector(edgePoint.getX() + deltaX, edgePoint.getY() - deltaY);
        } else {
            return new Vector(edgePoint.getX() - deltaX, edgePoint.getY() + deltaY);
        }
    }

    public Vector edgePointBetween(Vector location, Vector nextLocation) {
        if (this.isInbounds(nextLocation)) {
            return null;
        }
        double slope = Vector.slope(location, nextLocation);
        if (nextLocation.getX() < 0 && nextLocation.getY() < 0) {
            return new Vector(0, 0);
        } else if (this.getWidth() < nextLocation.getX() && nextLocation.getY() < 0) {
            return new Vector(this.getWidth(), 0);
        } else if (nextLocation.getY() < 0) {
            double deltaX = (Double.isFinite(slope) && slope != 0) ? (0 - location.getY()) / slope : 0;
            return new Vector(location.getX() + deltaX, 0);
        } else if (this.getWidth() < nextLocation.getX() && this.getHeight() < nextLocation.getY()) {
            return new Vector(this.getWidth(), this.getHeight());
        } else if (this.getWidth() < nextLocation.getX()) {
            double deltaY = (Double.isFinite(slope)) ? (this.getWidth() - location.getX()) * slope : 0;
            return new Vector(this.getWidth(), location.getY() + deltaY);
        } else if (nextLocation.getX() < 0 && this.getHeight() < nextLocation.getY()) {
            return new Vector(0, this.getWidth());
        } else if (this.getHeight() < nextLocation.getY()) {
            double deltaX = (Double.isFinite(slope) && slope != 0) ? (this.getHeight()-location.getY()) / slope : 0;
            return new Vector(location.getX() + deltaX, this.getHeight());
        } else {
            double deltaY = (Double.isFinite(slope)) ? (0 - location.getX()) * slope : 0;
            return new Vector(0, location.getY() + deltaY);
        }
    }

    public Zone[][] getGrid() {
        return this.grid;
    }

    public int getHeight() {
        return this.height * this.zoneSize;
    }

    public int getHeightUnits() {
        return this.height;
    }

    public String getLabel() {
        return String.format("<(%d, %d)>", this.width, this.height);
    }

    public int getWidth() {
        return this.width * this.zoneSize;
    }

    public int getWidthUnits() {
        return this.width;
    }

    /** Returns the Zone located at row y, column x.
     * @throws CommonException if the either y or x are out of bounds
     */
    public Zone getZone(int y, int x) throws CommonException {
        if (y < 0 || this.height <= y) {
            throw new CommonException(new IndexOutOfBoundsException(y).toString());
        }
        if (x < 0 || this.width <= x) {
            throw new CommonException(new IndexOutOfBoundsException(x).toString());
        }
        return this.grid[y][x];
    }

    /** Returns the Zone that contains the location with the specified x and y coordinates.
     * @throws CommonException if the either y or x are out of bounds
     */
    public Zone getZoneAtLocation(double x, double y) throws CommonException {
        return this.getZone((int) (y / this.zoneSize), (int) (x / this.zoneSize));
    }

    /** Returns the Zone that contains the location with the specified coordinates.
     * @throws CommonException if the vector is out of bounds
     */
    public Zone getZoneAtLocation(Vector location) throws CommonException {
        return this.getZoneAtLocation(location.getX(), location.getY());
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

    public JSONOption toJSON() throws JSONException {
        JSONObjectBuilder json = JSONBuilder.Object();
        json.put(Map.MAP_WIDTH, this.width);
        json.put(Map.MAP_HEIGHT, this.height);
        json.put(Map.ZONE_SIZE, this.zoneSize);
        if (!this.defaultGrid) {
            JSONArrayBuilder jsonGrid = JSONBuilder.Array();
            for (int y = 0; y < this.height; y++) {
                JSONArrayBuilder jsonRow = JSONBuilder.Array();
                for (int x = 0; x < this.width; x++) {
                    jsonRow.put(this.getZone(y, x).toJSON());
                }
                jsonGrid.put(jsonRow.toJSON());
            }
            json.put(Map.MAP_GRID, jsonGrid.toJSON());
        }
        return json.toJSON();
    }

    public boolean equals(Map map) {
        if (map == null) return false;
        if (!(this.width == map.width && this.height == map.height && this.zoneSize == map.zoneSize)) return false;
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