package com.seat.sim.common.math;

import com.seat.sim.common.core.CommonException;
import com.seat.sim.common.json.JSONAble;
import com.seat.sim.common.json.JSONArray;
import com.seat.sim.common.json.JSONArrayBuilder;
import com.seat.sim.common.json.JSONBuilder;
import com.seat.sim.common.json.JSONException;
import com.seat.sim.common.json.JSONObject;
import com.seat.sim.common.json.JSONObjectBuilder;
import com.seat.sim.common.json.JSONOption;

/** Represents the map grid. */
public class Grid extends JSONAble {
    public static final String GRID_HEIGHT = "grid_height";
    public static final String GRID_WIDTH = "grid_width";
    public static final String ZONES = "zones";
    public static final String ZONE_SIZE = "zone_size";

    private int height;     // number of zones in each column of the grid
    private boolean isCustomGrid;
    private int width;      // number of zones in each row of the grid
    private Zone[][] zones;
    private int zoneSize;   // zones in the same map grid must have a uniform size

    public Grid(int zoneSize, int width, int height) {
        this(null, zoneSize, width, height);
    }

    public Grid(Zone[][] zones, int zoneSize, int width, int height) {
        if (zones != null) {
            this.isCustomGrid = true;
            this.zones = zones;
        } else {
            this.isCustomGrid = false;
            this.zones = new Zone[height][width];
            for (int y = 0; y < this.height; y++) {
                for (int x = 0; x < this.width; x++) {
                    this.zones[y][x] = new Zone(
                        new Vector(this.zoneSize*(x + 0.5), this.zoneSize*(y + 0.5)),
                        this.zoneSize
                    );
                }
            }
        }
        this.zoneSize = zoneSize;
        this.width = width;
        this.height = height;
    }

    public Grid(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        this.width = json.getInt(Grid.GRID_WIDTH);
        this.height = json.getInt(Grid.GRID_HEIGHT);
        this.zoneSize = json.getInt(Grid.ZONE_SIZE);
        this.zones = new Zone[this.height][this.width];
        if (json.hasKey(Grid.ZONES)) {
            this.isCustomGrid = true;
            JSONArray jsonZones = json.getJSONArray(Grid.ZONES);
            for (int y = 0; y < this.height; y++) {
                JSONArray jsonRow = jsonZones.getJSONArray(y);
                for (int x = 0; x < this.width; x++) {
                    this.zones[y][x] = new Zone(jsonRow.getJSONOption(x));
                }
            }
        } else {
            this.isCustomGrid = false;
            for (int y = 0; y < this.height; y++) {
                for (int x = 0; x < this.width; x++) {
                    this.zones[y][x] = new Zone(
                        new Vector(
                            this.zoneSize * (x + 0.5),
                            this.zoneSize * (y + 0.5)
                        ),
                        this.zoneSize
                    );
                }
            }
        }
    }

    public Vector getBoundsCollisionLocation(Vector location, Vector velocity) {
        Vector nextLocation = Vector.add(location, velocity);
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

    public int getHeight() {
        return this.height * this.zoneSize;
    }

    public int getHeightInZones() {
        return this.height;
    }

    public String getLabel() {
        return String.format("<g: (%d, %d)>", this.width, this.height);
    }

    public Vector getLocationAfterBounce(Vector location, Vector velocity) {
        Vector nextLocation = Vector.add(location, velocity);
        Vector boundsCollision = this.getBoundsCollisionLocation(location, nextLocation);
        if (boundsCollision == null) {
            return null;
        }
        double deltaX = nextLocation.getX() - boundsCollision.getX();
        double deltaY = nextLocation.getY() - boundsCollision.getY();
        if ((boundsCollision.getX() == 0 || boundsCollision.getX() == this.getWidth()) &&
                (boundsCollision.getY() == 0 || boundsCollision.getY() == this.getHeight())) {
            return new Vector(boundsCollision.getX() - deltaX, boundsCollision.getY() - deltaY);
        } else if (boundsCollision.getY() == 0 || boundsCollision.getY() == this.getHeight()) {
            return new Vector(boundsCollision.getX() + deltaX, boundsCollision.getY() - deltaY);
        } else {
            return new Vector(boundsCollision.getX() - deltaX, boundsCollision.getY() + deltaY);
        }
    }

    public int getWidth() {
        return this.width * this.zoneSize;
    }

    public int getWidthInZones() {
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
        return this.zones[y][x];
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

    public JSONOption toJSON() throws JSONException {
        JSONObjectBuilder json = JSONBuilder.Object();
        json.put(Grid.GRID_WIDTH, this.width);
        json.put(Grid.GRID_HEIGHT, this.height);
        json.put(Grid.ZONE_SIZE, this.zoneSize);
        if (this.isCustomGrid) {
            JSONArrayBuilder jsonZones = JSONBuilder.Array();
            for (int y = 0; y < this.height; y++) {
                JSONArrayBuilder jsonRow = JSONBuilder.Array();
                for (int x = 0; x < this.width; x++) {
                    jsonRow.put(this.getZone(y, x).toJSON());
                }
                jsonZones.put(jsonRow.toJSON());
            }
            json.put(Grid.ZONES, jsonZones.toJSON());
        }
        return json.toJSON();
    }

    public boolean equals(Grid grid) {
        if (grid == null) return false;
        if (!(this.width == grid.width && this.height == grid.height && this.zoneSize == grid.zoneSize)) return false;
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                if (!this.zones[y][x].equals(grid.zones[y][x])) {
                    return false;
                }
            }
        }
        return true;
    }

}
