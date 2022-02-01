package com.seat.rescuesim;

public class Map {
    private int mapWidth;
    private int mapHeight;
    private int zoneSize;
    private Integer[][] zoneTypes;
    private Field[][] groundFields;
    private Field[][] aerialFields;

    public Map(int width, int height, int zoneSize, Integer[][] zoneTypes, Field[][] ground, Field[][] aerial) {
        this.mapWidth = width;
        this.mapHeight = height;
        this.zoneSize = zoneSize;
        this.zoneTypes = zoneTypes;
        this.groundFields = ground;
        this.aerialFields = aerial;
    }

    public int getWidth() {
        return this.mapWidth;
    }

    public int getHeight() {
        return this.mapHeight;
    }

    public int getZoneSize() {
        return this.zoneSize;
    }

    public Integer[][] getZoneTypes() {
        return this.zoneTypes;
    }

    public Integer getZoneType(int x, int y) {
        if (x < 0 || this.mapWidth <= x || y < 0 || this.mapHeight <= y) {
            return null;
        }
        return this.zoneTypes[x][y];
    }

    public Field[][] getGroundFields() {
        return this.groundFields;
    }

    public Field getGroundField(int x, int y) {
        if (x < 0 || this.mapWidth <= x || y < 0 || this.mapHeight <= y) {
            return null;
        }
        return this.groundFields[x][y];
    }

    public Field[][] getAerialFields() {
        return this.aerialFields;
    }

    public Field getAerialField(int x, int y) {
        if (x < 0 || this.mapWidth <= x || y < 0 || this.mapHeight <= y) {
            return null;
        }
        return this.aerialFields[x][y];
    }

}
