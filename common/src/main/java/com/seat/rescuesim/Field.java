package com.seat.rescuesim;

import org.json.JSONArray;
import org.json.JSONException;
public class Field extends Tuple<Vector, Vector> {

    public static Field decode(JSONArray json) throws JSONException {
        Vector direction = Vector.decode(json.getJSONArray(0));
        Vector acceleration = Vector.decode(json.getJSONArray(1));
        return new Field(direction, acceleration);
    }

    public static Field decode(String encoding) throws JSONException {
        return Field.decode(new JSONArray(encoding));
    }

    public Field() {
        super(new Vector(), new Vector());
    }

    public Field(Vector direction, Vector acceleration) {
        super(direction, acceleration);
    }

    public Field(Field field) {
        super(field.first, field.second);
    }

    public Vector getDirection() {
        return this.first;
    }

    public Vector getAcceleration() {
        return this.second;
    }

    public String encode() {
        return this.toJSON().toString();
    }

    @Override
    public JSONArray toJSON() {
        JSONArray json = new JSONArray();
        json.put(this.first.toJSON());
        json.put(this.second.toJSON());
        return json;
    }

}
