package com.seat.sim.common.math;

import com.seat.sim.common.json.JSONAble;
import com.seat.sim.common.json.JSONArray;
import com.seat.sim.common.json.JSONArrayBuilder;
import com.seat.sim.common.json.JSONBuilder;
import com.seat.sim.common.json.JSONException;
import com.seat.sim.common.json.JSONOption;

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
 *  - the jerk vector represents a momentary jerk force applied per second
 *  - the jerk vector is used to accelerate and decelerate actors that move through the field
 *  - it cannot decelerate them to a velocity below 0 or a velocity greater than their max velocity
 *  - the point vector cannot be used to modify the actor's acceleration or velocity while in the field
 *  - the jerk vector is used to modify the actor's acceleration while in the field
 */
public class Field extends JSONAble {

    protected static final double DEFAULT_MAGNITUDE = 0.0;

    private boolean isPullForce;
    private Vector jerk;
    private double magnitude;
    private Vector point;

    public static Field Drag(Vector jerk) {
        return new Field(null, Field.DEFAULT_MAGNITUDE, false, Vector.scale(jerk, -1));
    }

    public static Field None() {
        return new Field(null, Field.DEFAULT_MAGNITUDE, false, null);
    }

    public static Field Propel(Vector jerk) {
        return new Field(null, Field.DEFAULT_MAGNITUDE, false, jerk);
    }

    public static Field Pull(Vector point, double magnitude) {
        return new Field(point, magnitude, true, null);
    }

    public static Field Pull(Vector point, double magnitude, Vector jerk) {
        return new Field(point, magnitude, true, jerk);
    }

    public static Field Push(Vector point, double magnitude) {
        return new Field(point, magnitude, false, null);
    }

    public static Field Push(Vector point, double magnitude, Vector jerk) {
        return new Field(point, magnitude, false, jerk);
    }

    private Field(Vector point, double magnitude, boolean isPullForce, Vector jerk) {
        this.point = (point != null) ? point : new Vector();
        this.magnitude = magnitude;
        this.isPullForce = isPullForce;
        this.jerk = (jerk != null) ? jerk : new Vector();
    }

    public Field(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected void decode(JSONArray json) throws JSONException {
        this.point = new Vector(json.getJSONOption(0));
        this.magnitude = json.getDouble(1);
        this.isPullForce = json.getBoolean(2);
        this.jerk = new Vector(json.getJSONOption(3));
    }

    public Vector getJerk() {
        return this.jerk;
    }

    public double getMagnitude() {
        return this.magnitude;
    }

    public Vector getPoint() {
        return this.point;
    }

    public boolean isNone() {
        return this.magnitude == 0 && this.jerk.getMagnitude() == 0;
    }

    public boolean isPullForce() {
        return this.isPullForce;
    }

    public boolean isPushForce() {
        return !this.isPullForce;
    }

    public JSONOption toJSON() throws JSONException {
        JSONArrayBuilder json = JSONBuilder.Array();
        json.put(this.point.toJSON());
        json.put(this.magnitude);
        json.put(this.isPullForce);
        json.put(this.jerk.toJSON());
        return json.toJSON();
    }

    public boolean equals(Field field) {
        if (field == null) return false;
        return this.point.equals(field.point) && this.magnitude == field.magnitude &&
            this.isPullForce == field.isPullForce && this.jerk.equals(field.jerk);
    }

}
