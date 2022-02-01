package com.seat.rescuesim;

import org.json.JSONArray;

public class Vector {
    private double x;
    private double y;
    private double z;

    public Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public String toString() {
        JSONArray json = new JSONArray();
        json.put(this.x);
        json.put(this.y);
        json.put(this.z);
        return json.toString();
    }

}
