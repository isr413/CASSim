package com.seat.rescuesim.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * The Field class represents forces that can act on Victims or Drones. Things to remember:
 *  - the direction vector applies a uniform force to any actor within its field in a specific direction
 *  - the direction axis only uses its X and Y components to affect an actor's direction
 *  - the direction Z component represents acceleration and deceleration and is only used to affect an actor's velocity
 *  - the magnitude scales the direction vector
 *  - a direction vector of 0 with a positive magnitude represents a push force that originates at the center of
 *    the zone and pushes everything in the zone towards its boundaries
 *  - a direction vector of 0 with a negative magnitude represents a pull force that originates at the center of
 *    the zone and pulls everything in the zone towards its center
 */
public class Field {

    private Vector direction;
    private double magnitude;
    private HashMap<SensorType, Double> sensorInterference;

    public static Field decode(JSONArray json) throws JSONException {
        Vector direction = new Vector(json.getJSONArray(0));
        double magnitude = json.getDouble(1);
        JSONObject interferences = json.getJSONObject(2);
        HashMap<SensorType, Double> sensorInterferences = new HashMap<>();
        for (String key : interferences.keySet()) {
            SensorType sensor = SensorType.values()[Integer.parseInt(key)];
            double interference = interferences.getDouble(key);
            sensorInterferences.put(sensor, interference);
        }
        return new Field(direction, magnitude, sensorInterferences);
    }

    public static Field decode(String encoding) throws JSONException {
        return Field.decode(new JSONArray(encoding));
    }

    public Field() {
        this(new Vector(), 0, new HashMap<SensorType, Double>());
    }

    public Field(Vector direction, double magnitude) {
        this(direction, magnitude, new HashMap<SensorType, Double>());
    }

    public Field(Vector direction, double magnitude, Tuple<SensorType, Double>[] interference) {
        this(direction, magnitude, new ArrayList<Tuple<SensorType, Double>>(Arrays.asList(interference)));
    }

    public Field(Vector direction, double magnitude, ArrayList<Tuple<SensorType, Double>> interference) {
        this(direction, magnitude);
        for (Tuple<SensorType, Double> tuple : interference) {
            this.sensorInterference.put(tuple.getFirst(), tuple.getSecond());
        }
    }

    public Field(Vector direction, double magnitude, HashMap<SensorType, Double> interferences) {
        this.direction = direction;
        this.magnitude = magnitude;
        this.sensorInterference = interferences;
    }

    public Vector getDirection() {
        return this.direction;
    }

    public double getMagnitude() {
        return this.magnitude;
    }

    public HashMap<SensorType, Double> getSensorInterference() {
        return this.sensorInterference;
    }

    public double getSensorInterferenceWithType(SensorType sensor) {
        if (!this.hasSensorInterferenceWithType(sensor)) {
            return 0;
        }
        return this.sensorInterference.get(sensor);
    }

    public boolean hasSensorInterference() {
        return !this.sensorInterference.isEmpty();
    }

    public boolean hasSensorInterferenceWithType(SensorType sensor) {
        return this.sensorInterference.containsKey(sensor);
    }

    public String encode() {
        return this.toJSON().toString();
    }

    public JSONArray toJSON() {
        JSONArray json = new JSONArray();
        json.put(this.direction.toJSON());
        json.put(this.magnitude);
        JSONObject interferences = new JSONObject();
        for (SensorType key : this.sensorInterference.keySet()) {
            interferences.put(key.toString(), this.sensorInterference.get(key));
        }
        json.put(interferences);
        return json;
    }

    public String toString() {
        return this.encode();
    }

    public boolean equals(Field field) {
        return this.direction.equals(field.direction) && this.magnitude == field.magnitude &&
                this.sensorInterference.equals(field.sensorInterference);
    }

    public boolean equals(String encoding) {
        return this.encode().equals(encoding);
    }

}
