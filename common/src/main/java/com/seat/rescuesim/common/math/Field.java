package com.seat.rescuesim.common.math;

import com.seat.rescuesim.common.json.*;

/**
 * The Field class represents forces that can act on Victims or Drones. Things to remember:
 *  - the FieldType determines how the force should be applied (push, pull, etc.)
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

    private Vector jerk;
    private double magnitude;
    private Vector point;
    private FieldType type;

    public Field(JSONArray json) throws JSONException {
        super(json);
    }

    public Field(JSONOption option) throws JSONException {
        super(option);
    }

    public Field(String encoding) throws JSONException {
        super(encoding);
    }

    public Field() {
        this(FieldType.NONE, new Vector(), 0, new Vector());
    }

    public Field(Vector jerk) {
        this(FieldType.JERK, new Vector(), 0, jerk);
    }

    public Field(FieldType type, Vector point, double magnitude) {
        this(type, point, magnitude, new Vector());
    }

    public Field(FieldType type, Vector point, double magnitude, Vector jerk) {
        this.type = type;
        this.point = point;
        this.magnitude = magnitude;
        this.jerk = jerk;
    }

    @Override
    protected void decode(JSONArray json) throws JSONException {
        this.type = FieldType.values()[json.getInt(0)];
        this.point = new Vector(json.getJSONArray(1));
        this.magnitude = json.getDouble(2);
        this.jerk = new Vector(json.getJSONArray(3));
    }

    public FieldType getFieldType() {
        return this.type;
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

    public JSONOption toJSON() {
        JSONArrayBuilder json = JSONBuilder.Array();
        json.put(this.type.getType());
        json.put(this.point.toJSON());
        json.put(this.magnitude);
        json.put(this.jerk.toJSON());
        return json.toJSON();
    }

    public boolean equals(Field field) {
        return this.type.equals(field.type) && this.point.equals(field.point) && this.magnitude == field.magnitude &&
            this.jerk.equals(field.jerk);
    }

}
