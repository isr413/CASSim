package com.seat.rescuesim.common;

import org.json.JSONObject;

public class Sensor {
    private static final String SENSOR_ACCURACY = "accuracy";
    private static final String SENSOR_DELAY = "delay";
    private static final String SENSOR_FUEL_COST = "fuel_cost";
    private static final String SENSOR_RANGE = "range";
    private static final String SENSOR_TYPE = "type";

    private double accuracy;
    private double cost;
    private double delay;
    private double range;
    private SensorType type;

    public static Sensor decode(JSONObject json) {
        int type = json.getInt(Sensor.SENSOR_TYPE);
        double range = json.getDouble(Sensor.SENSOR_RANGE);
        double fuelCost = json.getDouble(Sensor.SENSOR_FUEL_COST);
        double accuracy = json.getDouble(Sensor.SENSOR_ACCURACY);
        double delay = json.getDouble(Sensor.SENSOR_DELAY);
        return new Sensor(SensorType.values()[type], range, fuelCost, accuracy, delay);
    }

    public static Sensor decode(String encoding) {
        return Sensor.decode(new JSONObject(encoding));
    }

    public Sensor(SensorType type, double range, double fuelCost, double accuracy, double delay) {
        this.type = type;
        this.range = range;
        this.cost = fuelCost;
        this.accuracy = accuracy;
        this.delay = delay;
    }

    public SensorType getType() {
        return this.type;
    }

    public double getSensorRange() {
        return this.range;
    }

    public double getFuelCost() {
        return this.cost;
    }

    public double getSensorAccuracy() {
        return this.accuracy;
    }

    public double getSensorDelay() {
        return this.delay;
    }

    public String encode() {
        return this.toJSON().toString();
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put(Sensor.SENSOR_TYPE, this.type.getType());
        json.put(Sensor.SENSOR_RANGE, this.range);
        json.put(Sensor.SENSOR_FUEL_COST, this.cost);
        json.put(Sensor.SENSOR_ACCURACY, this.accuracy);
        json.put(Sensor.SENSOR_DELAY, this.delay);
        return json;
    }

    public String toString() {
        return this.encode();
    }

}
