package com.seat.rescuesim;

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
        Vector direction = Vector.decode(json.getJSONArray(0));
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
        this.direction = new Vector();
        this.magnitude = 0.0;
        this.sensorInterference = new HashMap<>();
    }

    public Field(Vector direction, double magnitude) {
        this.direction = direction;
        this.magnitude = magnitude;
        this.sensorInterference = new HashMap<>();
    }

    public Field(Vector direction, double magnitude, Tuple<SensorType, Double>[] interferences) {
        this.direction = direction;
        this.magnitude = magnitude;
        this.sensorInterference = new HashMap<>();
        for (int i = 0; i < interferences.length; i++) {
            this.sensorInterference.put(interferences[i].getFirst(), interferences[i].getSecond());
        }
    }

    public Field(Vector direction, double magnitude, HashMap<SensorType, Double> interferences) {
        this.direction = direction;
        this.magnitude = magnitude;
        this.sensorInterference = interferences;
    }

    public Field(Field field) {
        this.direction = field.direction;
        this.magnitude = field.magnitude;
        this.sensorInterference = field.sensorInterference;
    }

    public Vector getDirection() {
        return this.direction;
    }

    public double getMagnitude() {
        return this.magnitude;
    }

    public HashMap<SensorType, Double> getSensorInterferences() {
        return this.sensorInterference;
    }

    public boolean hasSensorInterference(SensorType sensor) {
        return this.sensorInterference.containsKey(sensor);
    }

    public double getSensorInterference(SensorType sensor) {
        if (!this.hasSensorInterference(sensor)) {
            return 0.0;
        }
        return this.sensorInterference.get(sensor);
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

}
