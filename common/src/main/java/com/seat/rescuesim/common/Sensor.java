package com.seat.rescuesim.common;

import org.json.JSONObject;

public class Sensor {
    private static final String SENSOR_ACCURACY = "accuracy";
    private static final String SENSOR_BATTERY_USAGE = "battery_usage";
    private static final String SENSOR_DELAY = "delay";
    private static final String SENSOR_RANGE = "range";
    private static final String SENSOR_TYPE = "type";

    private double accuracy;
    private double batteryUsage;
    private double delay;
    private double range;
    private SensorType type;

    public static Sensor decode(JSONObject json) {
        int type = json.getInt(Sensor.SENSOR_TYPE);
        double range = json.getDouble(Sensor.SENSOR_RANGE);
        double batteryUsage = json.getDouble(Sensor.SENSOR_BATTERY_USAGE);
        double accuracy = json.getDouble(Sensor.SENSOR_ACCURACY);
        double delay = json.getDouble(Sensor.SENSOR_DELAY);
        return new Sensor(SensorType.values()[type], range, batteryUsage, accuracy, delay);
    }

    public static Sensor decode(String encoding) {
        return Sensor.decode(new JSONObject(encoding));
    }

    public Sensor(SensorType type, double range, double batteryUsage, double accuracy, double delay) {
        this.type = type;
        this.range = range;
        this.batteryUsage = batteryUsage;
        this.accuracy = accuracy;
        this.delay = delay;
    }

    public double getBatteryUsage() {
        return this.batteryUsage;
    }

    public double getSensorAccuracy() {
        return this.accuracy;
    }

    public double getSensorDelay() {
        return this.delay;
    }

    public double getSensorRange() {
        return this.range;
    }

    public SensorType getType() {
        return this.type;
    }

    public String encode() {
        return this.toJSON().toString();
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put(Sensor.SENSOR_TYPE, this.type.getType());
        json.put(Sensor.SENSOR_RANGE, this.range);
        json.put(Sensor.SENSOR_BATTERY_USAGE, this.batteryUsage);
        json.put(Sensor.SENSOR_ACCURACY, this.accuracy);
        json.put(Sensor.SENSOR_DELAY, this.delay);
        return json;
    }

    public String toString() {
        return this.encode();
    }

    public boolean equals(Sensor sensor) {
        return this.type.equals(sensor.type);
    }

    public boolean equals(String encoding) {
        return this.encode().equals(encoding);
    }

}
