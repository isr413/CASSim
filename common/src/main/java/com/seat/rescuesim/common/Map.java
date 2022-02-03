package com.seat.rescuesim.common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Map {
    private static final String MAP_GRID = "map_grid";
    private static final String MAP_WIDTH = "map_width";
    private static final String MAP_HEIGHT = "map_height";
    private static final String ZONE_SIZE = "zone_size";

    private int width;
    private int height;
    private int zoneSize;
    private Zone[][] grid;

    public static Map decode(JSONObject json) throws JSONException {
        int width = json.getInt(Map.MAP_WIDTH);
        int height = json.getInt(Map.MAP_HEIGHT);
        int zoneSize = json.getInt(Map.ZONE_SIZE);
        Zone[][] gridZones = new Zone[height][width];
        JSONArray zones = json.getJSONArray(Map.MAP_GRID);
        for (int y = 0; y < height; y++) {
            JSONArray row = zones.getJSONArray(y);
            for (int x = 0; x < width; x++) {
                gridZones[y][x] = Zone.decode(row.getJSONObject(x));
            }
        }
        return new Map(width, height, zoneSize, gridZones);
    }

    public static Map decode(String encoding) throws JSONException {
        return Map.decode(new JSONObject(encoding));
    }

    public Map(int width, int height, int zoneSize) {
        this.width = width;
        this.height = height;
        this.zoneSize = zoneSize;
        this.grid = new Zone[this.height][this.width];
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                this.grid[y][x] = new Zone(new Vector(x, y), this.zoneSize);
            }
        }
    }

    public Map(int width, int height, int zoneSize, Zone[][] gridZones) {
        this.width = width;
        this.height = height;
        this.zoneSize = zoneSize;
        this.grid = gridZones;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getZoneSize() {
        return this.zoneSize;
    }

    public Zone[][] getGrid() {
        return this.grid;
    }

    public Zone getZone(int y, int x) {
        if (y < 0 || this.height <= y || x < 0 || this.width <= x) {
            return null;
        }
        return this.grid[y][x];
    }

    public String encode() {
        return this.toJSON().toString();
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put(Map.MAP_WIDTH, this.width);
        json.put(Map.MAP_HEIGHT, this.height);
        json.put(Map.ZONE_SIZE, this.zoneSize);
        JSONArray zones = new JSONArray();
        for (int y = 0; y < this.height; y++) {
            JSONArray row = new JSONArray();
            for (int x = 0; x < this.width; x++) {
                row.put(this.getZone(y, x).toJSON());
            }
            zones.put(row);
        }
        json.put(Map.MAP_GRID, zones);
        return json;
    }

    public String toString() {
        return this.encode();
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

    public boolean equals(String encoding) {
        return this.encode().equals(encoding);
    }

}
