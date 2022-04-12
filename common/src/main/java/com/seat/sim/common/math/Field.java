package com.seat.sim.common.math;

import com.seat.sim.common.json.JSONAble;
import com.seat.sim.common.json.JSONArray;
import com.seat.sim.common.json.JSONArrayBuilder;
import com.seat.sim.common.json.JSONBuilder;
import com.seat.sim.common.json.JSONException;
import com.seat.sim.common.json.JSONOption;

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

    protected static final FieldType DEFAULT_FIELD_TYPE = FieldType.NONE;
    protected static final double DEFAULT_MAGNITUDE = 0.0;

    private Vector jerk;
    private double magnitude;
    private Vector point;
    private FieldType type;

    public Field() {
        this(Field.DEFAULT_FIELD_TYPE, null, Field.DEFAULT_MAGNITUDE, null);
    }

    public Field(Vector jerk) {
        this(FieldType.JERK, null, Field.DEFAULT_MAGNITUDE, jerk);
    }

    public Field(FieldType type, Vector point, double magnitude) {
        this(type, point, magnitude, null);
    }

    public Field(FieldType type, Vector point, double magnitude, Vector jerk) {
        this.type = type;
        this.point = (point != null) ? point : new Vector();
        this.magnitude = magnitude;
        this.jerk = (jerk != null) ? jerk : new Vector();
    }

    public Field(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected void decode(JSONArray json) throws JSONException {
        this.type = FieldType.decodeType(json);
        this.point = new Vector(json.getJSONOption(1));
        this.magnitude = json.getDouble(2);
        this.jerk = new Vector(json.getJSONOption(3));
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

    public JSONOption toJSON() throws JSONException {
        JSONArrayBuilder json = JSONBuilder.Array();
        json.put(this.type.getType());
        json.put(this.point.toJSON());
        json.put(this.magnitude);
        json.put(this.jerk.toJSON());
        return json.toJSON();
    }

    public boolean equals(Field field) {
        if (field == null) return false;
        return this.type.equals(field.type) && this.point.equals(field.point) && this.magnitude == field.magnitude &&
            this.jerk.equals(field.jerk);
    }

}