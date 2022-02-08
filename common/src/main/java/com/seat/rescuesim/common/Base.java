package com.seat.rescuesim.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Base {
    private static final String BASE_LOCATION = "location";
    private static final String BASE_SENSORS = "sensors";

    private Vector location;
    private HashMap<SensorType, Sensor> sensors;

    public static Base decode(JSONObject json) throws JSONException {
        Vector location = new Vector(json.getJSONArray(Base.BASE_LOCATION));
        JSONArray jsonSensors = json.getJSONArray(Base.BASE_SENSORS);
        ArrayList<Sensor> sensors = new ArrayList<>();
        for (int i = 0; i < jsonSensors.length(); i++) {
            sensors.add(Sensor.decode(jsonSensors.getJSONObject(i)));
        }
        return new Base(location, sensors);
    }

    public static Base decode(String encoding) throws JSONException {
        return Base.decode(new JSONObject(encoding));
    }

    public Base() {
        this(new Vector(), new HashMap<SensorType, Sensor>());
    }

    public Base(Vector location) {
        this(location, new HashMap<SensorType, Sensor>());
    }

    public Base(Vector location, Sensor[] sensors) {
        this(location, new ArrayList<Sensor>(Arrays.asList(sensors)));
    }

    public Base(Vector location, ArrayList<Sensor> sensors) {
        this(location);
        for (Sensor sensor : sensors) {
            this.sensors.put(sensor.getType(), sensor);
        }
    }

    public Base(Vector location, HashMap<SensorType, Sensor> sensors) {
        this.location = location;
        this.sensors = sensors;
    }

    public String getLabel() {
        return String.format("<%s>", this.location.toString());
    }

    public Vector getLocation() {
        return this.location;
    }

    public ArrayList<Sensor> getSensors() {
        return new ArrayList<>(this.sensors.values());
    }

    public Sensor getSensorWithType(SensorType type) {
        if (!this.hasSensorWithType(type)) {
            Debugger.logger.err(String.format("No sensor with type %s found on base %s",
                type.getLabel(), this.getLabel()));
            return null;
        }
        return this.sensors.get(type);
    }

    public boolean hasSensors() {
        return !this.sensors.isEmpty();
    }

    public boolean hasSensorWithType(SensorType type) {
        return this.sensors.containsKey(type);
    }

    public String encode() {
        return this.toJSON().toString();
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put(Base.BASE_LOCATION, this.location.toJSON());
        JSONArray baseSensors = new JSONArray();
        for (Sensor sensor : this.sensors.values()) {
            baseSensors.put(sensor.toJSON());
        }
        json.put(Base.BASE_SENSORS, baseSensors);
        return json;
    }

    public String toString() {
        return this.encode();
    }

    public boolean equals(Base base) {
        return this.location.equals(base.location) && this.sensors.equals(base.sensors);
    }

    public boolean equals(String encoding) {
        return this.encode().equals(encoding);
    }

}
