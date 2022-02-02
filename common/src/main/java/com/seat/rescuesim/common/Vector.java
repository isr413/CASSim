package com.seat.rescuesim.common;

import org.json.JSONArray;
import org.json.JSONException;

public class Vector {
    private double x;
    private double y;
    private double z;

    public static Vector decode(JSONArray json) throws JSONException {
        double x = json.getDouble(0);
        double y = json.getDouble(1);
        double z = json.getDouble(2);
        return new Vector(x, y, z);
    }

    public static Vector decode(String encoding) throws JSONException {
        return Vector.decode(new JSONArray(encoding));
    }

    public Vector() {
        this.x = 0.0;
        this.y = 0.0;
        this.z = 0.0;
    }

    public Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector(Vector v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
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

    public String encode() {
        return this.toJSON().toString();
    }

    public JSONArray toJSON() {
        JSONArray json = new JSONArray();
        json.put(this.x);
        json.put(this.y);
        json.put(this.z);
        return json;
    }

    public String toString() {
        return this.encode();
    }

}
