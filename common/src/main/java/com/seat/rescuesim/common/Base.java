package com.seat.rescuesim.common;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Base {
    private static final String BASE_LOCATION = "location";
    private static final String BASE_SENSORS = "sensors";

    private Vector loc;
    private Sensor[] sensors;

    public static Base decode(JSONObject json) throws JSONException {
        Vector location = Vector.decode(json.getJSONArray(Base.BASE_LOCATION));
        JSONArray baseSensors = json.getJSONArray(Base.BASE_SENSORS);
        Sensor[] sensors = new Sensor[baseSensors.length()];
        for (int i = 0; i < sensors.length; i++) {
            sensors[i] = Sensor.decode(baseSensors.getJSONObject(i));
        }
        return new Base(location, sensors);
    }

    public static Base decode(String encoding) throws JSONException {
        return Base.decode(new JSONObject(encoding));
    }

    public Base(Vector location, Sensor[] sensors) {
        this.loc = location;
        this.sensors = sensors;
    }

    public Vector getLocation() {
        return this.loc;
    }

    public Sensor[] getSensors() {
        return this.sensors;
    }

    public Sensor[] getSensorsWithType(SensorType type) {
        ArrayList<Sensor> sensorsWithType = new ArrayList<>();
        for (int i = 0; i < this.sensors.length; i++) {
            if (this.sensors[i].getType() == type) {
                sensorsWithType.add(this.sensors[i]);
            }
        }
        return (Sensor[]) sensorsWithType.toArray();
    }

    public boolean hasSensorType(SensorType type) {
        for (int i = 0; i < this.sensors.length; i++) {
            if (this.sensors[i].getType() == type) {
                return true;
            }
        }
        return false;
    }

    public String encode() {
        return this.toJSON().toString();
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put(Base.BASE_LOCATION, this.loc.toJSON());
        JSONArray baseSensors = new JSONArray();
        for (int i = 0; i < this.sensors.length; i++) {
            baseSensors.put(this.sensors[i].toJSON());
        }
        json.put(Base.BASE_SENSORS, baseSensors);
        return json;
    }

    public String toString() {
        return this.encode();
    }

}
