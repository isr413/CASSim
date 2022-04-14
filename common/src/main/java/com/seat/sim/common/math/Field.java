package com.seat.sim.common.math;

import com.seat.sim.common.json.JSONAble;
import com.seat.sim.common.json.JSONArray;
import com.seat.sim.common.json.JSONArrayBuilder;
import com.seat.sim.common.json.JSONBuilder;
import com.seat.sim.common.json.JSONException;
import com.seat.sim.common.json.JSONOptional;

/**
 * The Field class represents forces that can act on Victims or Drones. Things to remember:
 *  - the isPullForce determines how the force should be applied (pull for true or push for false)
 *  - the magnitude determines the size of the force
 *  - the point is a unit vector that determines the direction of the force
 *  - the point vector represents a momentary force applied per second to an actor's location while in the field
 *  - a push force with a non-zero point vector represents a uniform push in the same direction throughout the field
 *  - a push force with a zero point vector represents a non-uniform push away from the origin of the field
 *  - a pull force with a non-zero point vector represents a non-uniform pull towards the designated point
 *  - a pull force with a zero point vector represents a non-uniform pull towards the origin of the field
 *  - the resistance represents a momentary jerk force applied per second
 *  - the resistance is used to accelerate and decelerate actors that move through the field
 *  - it cannot decelerate them to a velocity below 0 or a velocity greater than their max velocity
 *  - the point vector cannot be used to modify the actor's acceleration or velocity while in the field
 *  - the resistance is used to modify the actor's acceleration while in the field
 */
public class Field extends JSONAble {

    protected static final double DEFAULT_MAGNITUDE = 0.0;

    private boolean isPullForce;
    private double magnitude;
    private Vector point;
    private double resistance;

    public static Field Drag(double resistance) {
        return new Field(null, Field.DEFAULT_MAGNITUDE, false, resistance);
    }

    public static Field None() {
        return new Field(null, Field.DEFAULT_MAGNITUDE, false, 0);
    }

    public static Field Propel(double propulsion) {
        return new Field(null, Field.DEFAULT_MAGNITUDE, false, -propulsion);
    }

    public static Field Pull(Vector point, double magnitude) {
        return new Field(point, magnitude, true, 0);
    }

    public static Field Pull(Vector point, double magnitude, double resistance) {
        return new Field(point, magnitude, true, resistance);
    }

    public static Field Push(Vector point, double magnitude) {
        return new Field(point, magnitude, false, 0);
    }

    public static Field Push(Vector point, double magnitude, double resistance) {
        return new Field(point, magnitude, false, resistance);
    }

    private Field(Vector point, double magnitude, boolean isPullForce, double resistance) {
        this.point = (point != null) ? point : new Vector();
        this.magnitude = magnitude;
        this.isPullForce = isPullForce;
        this.resistance = resistance;
    }

    public Field(JSONOptional optional) throws JSONException {
        super(optional);
    }

    @Override
    protected void decode(JSONArray json) throws JSONException {
        this.point = new Vector(json.getJSONOption(0));
        this.magnitude = json.getDouble(1);
        this.isPullForce = json.getBoolean(2);
        this.resistance = json.getDouble(3);
    }

    public double getMagnitude() {
        return this.magnitude;
    }

    public Vector getPoint() {
        return this.point;
    }

    public double getResistance() {
        return this.resistance;
    }

    public boolean isNone() {
        return this.magnitude == 0 && this.resistance == 0;
    }

    public boolean isPullForce() {
        return this.isPullForce;
    }

    public boolean isPushForce() {
        return !this.isPullForce;
    }

    public boolean hasForce() {
        return this.magnitude > 0;
    }

    public boolean hasPropulsion() {
        return this.resistance < 0;
    }

    public boolean hasResistance() {
        return this.resistance > 0;
    }

    public JSONOptional toJSON() throws JSONException {
        JSONArrayBuilder json = JSONBuilder.Array();
        json.put(this.point.toJSON());
        json.put(this.magnitude);
        json.put(this.isPullForce);
        json.put(this.resistance);
        return json.toJSON();
    }

    public boolean equals(Field field) {
        if (field == null) return false;
        return this.point.equals(field.point) && this.magnitude == field.magnitude &&
            this.isPullForce == field.isPullForce && this.resistance == field.resistance;
    }

}
