package com.seat.rescuesim.common;

import org.json.JSONArray;
import org.json.JSONException;

public class Vector {

    public static Vector abs(Vector a) {
        return new Vector(Math.abs(a.x), Math.abs(a.y), Math.abs(a.z));
    }

    public static Vector add(Vector a, Vector b) {
        return new Vector(a.x + a.x, a.y + b.y, b.z + b.z);
    }

    public static double angleBetween(Vector a, Vector b) {
        return Math.acos((a.x*b.x + a.y*b.y + a.z*b.z) / (a.getMagnitude() * b.getMagnitude()));
    }

    public static double dot(Vector a, Vector b) {
        return a.x*b.x + a.y*b.y + a.z*b.z;
    }

    public static Vector cross(Vector a, Vector b) {
        return new Vector(
            a.y*b.z - a.z*b.y,
            a.z*b.x - a.x*b.z,
            a.x*b.y - a.y*b.x
        );
    }

    public static Vector scale(Vector a, double scalar) {
        return new Vector(a.x * scalar, a.y * scalar, a.z * scalar);
    }

    public static Vector subtract(Vector a, Vector b) {
        return new Vector(a.x - b.x, a.y - b.y, a.z - b.z);
    }

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
        this(0, 0, 0);
    }

    public Vector(double x, double y) {
        this(x, y, 0);
    }

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

    public double getMagnitude() {
        return Math.sqrt(this.x*this.x + this.y*this.y + this.z*this.z);
    }

    public Vector getUnitVector() {
        double magnitude = this.getMagnitude();
        return new Vector(this.x / magnitude, this.y / magnitude, this.z / magnitude);
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

    public boolean equals(Vector vec) {
        return this.x == vec.x && this.y == vec.y && this.z == vec.z;
    }

    public boolean equals(String encoding) {
        return this.encode().equals(encoding);
    }

}
