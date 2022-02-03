package com.seat.rescuesim.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VictimSpecification {
    private static final String VICTIM_BATTERY = "battery";
    private static final String VICTIM_MOVE_SPEED = "move_speed";
    private static final String VICTIM_SENSORS = "sensors";
    private static final String VICTIM_STATIC_BATTERY_USAGE = "static_battery_usage";

    private double maxBatteryPower;
    private Tuple<Double, Double> moveSpeedDistParams;
    private HashMap<SensorType, Sensor> sensors;
    private double staticBatteryUsage;

    public static VictimSpecification decode(JSONObject json) throws JSONException {
        double batteryPower = json.getDouble(VictimSpecification.VICTIM_BATTERY);
        double staticBatteryUsage = json.getDouble(VictimSpecification.VICTIM_STATIC_BATTERY_USAGE);
        JSONArray distParams = json.getJSONArray(VictimSpecification.VICTIM_MOVE_SPEED);
        double moveSpeedMean = distParams.getDouble(0);
        double moveSpeedStdDev = distParams.getDouble(1);
        JSONArray jsonSensors = json.getJSONArray(VictimSpecification.VICTIM_SENSORS);
        ArrayList<Sensor> sensors = new ArrayList<>();
        for (int i = 0; i < jsonSensors.length(); i++) {
            sensors.add(Sensor.decode(jsonSensors.getJSONObject(i)));
        }
        return new VictimSpecification(batteryPower, staticBatteryUsage, moveSpeedMean, moveSpeedStdDev, sensors);
    }

    public static VictimSpecification decode(String encoding) throws JSONException {
        return VictimSpecification.decode(new JSONObject(encoding));
    }

    public VictimSpecification(double batteryPower, double staticBatteryUsage) {
        this(batteryPower, staticBatteryUsage, new Tuple<Double, Double>(0.0, 0.0), new HashMap<SensorType, Sensor>());
    }

    public VictimSpecification(double batteryPower, double staticBatteryUsage, Sensor[] sensors) {
        this(batteryPower, staticBatteryUsage, new Tuple<Double, Double>(0.0, 0.0), sensors);
    }

    public VictimSpecification(double batteryPower, double staticBatteryUsage, ArrayList<Sensor> sensors) {
        this(batteryPower, staticBatteryUsage, new Tuple<Double, Double>(0.0, 0.0), sensors);
    }

    public VictimSpecification(double batteryPower, double staticBatteryUsage, HashMap<SensorType, Sensor> sensors) {
        this(batteryPower, staticBatteryUsage, new Tuple<Double, Double>(0.0, 0.0), sensors);
    }

    public VictimSpecification(double batteryPower, double staticBatteryUsage,
            double moveSpeedMean, double moveSpeedStdDev) {
        this(batteryPower, staticBatteryUsage, new Tuple<Double, Double>(moveSpeedMean, moveSpeedStdDev),
                new HashMap<SensorType, Sensor>());
    }

    public VictimSpecification(double batteryPower, double staticBatteryUsage,
            Tuple<Double, Double> moveSpeedDistParams) {
                this(batteryPower, staticBatteryUsage, moveSpeedDistParams, new HashMap<SensorType, Sensor>());
    }

    public VictimSpecification(double batteryPower, double staticBatteryUsage,
            double moveSpeedMean, double moveSpeedStdDev, Sensor[] sensors) {
        this(batteryPower, staticBatteryUsage, new Tuple<Double, Double>(moveSpeedMean, moveSpeedStdDev),
                new ArrayList<Sensor>(Arrays.asList(sensors)));
    }

    public VictimSpecification(double batteryPower, double staticBatteryUsage,
            Tuple<Double, Double> moveSpeedDistParams, Sensor[] sensors) {
        this(batteryPower, staticBatteryUsage, moveSpeedDistParams, new ArrayList<Sensor>(Arrays.asList(sensors)));
    }

    public VictimSpecification(double batteryPower, double staticBatteryUsage,
            double moveSpeedMean, double moveSpeedStdDev, ArrayList<Sensor> sensors) {
        this(batteryPower, staticBatteryUsage, new Tuple<Double, Double>(moveSpeedMean, moveSpeedStdDev), sensors);
    }

    public VictimSpecification(double batteryPower, double staticBatteryUsage,
            Tuple<Double, Double> moveSpeedDistParams, ArrayList<Sensor> sensors) {
        this(batteryPower, staticBatteryUsage, moveSpeedDistParams);
        for (Sensor sensor : sensors) {
            this.sensors.put(sensor.getType(), sensor);
        }
    }

    public VictimSpecification(double batteryPower, double staticBatteryUsage,
            double moveSpeedMean, double moveSpeedStdDev, HashMap<SensorType, Sensor> sensors) {
        this(batteryPower, staticBatteryUsage, new Tuple<Double, Double>(moveSpeedMean, moveSpeedStdDev), sensors);
    }

    public VictimSpecification(double batteryPower, double staticBatteryUsage,
            Tuple<Double, Double> moveSpeedDistParams, HashMap<SensorType, Sensor> sensors) {
        this.maxBatteryPower = batteryPower;
        this.staticBatteryUsage = staticBatteryUsage;
        this.moveSpeedDistParams = moveSpeedDistParams;
        this.sensors = sensors;
    }

    public double getMaxBatteryPower() {
        return this.maxBatteryPower;
    }

    public Tuple<Double, Double> getMoveSpeedDistributionParameters() {
        return this.moveSpeedDistParams;
    }

    public ArrayList<Sensor> getSensors() {
        return new ArrayList<Sensor>(this.sensors.values());
    }

    public Sensor getSensorWithType(SensorType type) {
        if (!this.hasSensorWithType(type)) {
            Debugger.logger.err("No sensor with type (" + type.toString() + ") found on victim spec");
            return null;
        }
        return this.sensors.get(type);
    }

    public double getStaticBatteryUsage() {
        return this.staticBatteryUsage;
    }

    public boolean hasSensors() {
        return !this.sensors.isEmpty();
    }

    public boolean hasSensorWithType(SensorType type) {
        return this.sensors.containsKey(type);
    }

    public boolean isKinetic() {
        return this.moveSpeedDistParams.first > 0.0 || this.moveSpeedDistParams.second > 0.0;
    }

    public String encode() {
        return this.toJSON().toString();
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put(VictimSpecification.VICTIM_BATTERY, this.maxBatteryPower);
        json.put(VictimSpecification.VICTIM_STATIC_BATTERY_USAGE, this.staticBatteryUsage);
        JSONArray distParams = new JSONArray();
        distParams.put(this.moveSpeedDistParams.getFirst());
        distParams.put(this.moveSpeedDistParams.getSecond());
        json.put(VictimSpecification.VICTIM_MOVE_SPEED, distParams);
        JSONArray victimSensors = new JSONArray();
        for (Sensor sensor : this.sensors.values()) {
            victimSensors.put(sensor.toJSON());
        }
        json.put(VictimSpecification.VICTIM_SENSORS, victimSensors);
        return json;
    }

    public String toString() {
        return this.encode();
    }

    public boolean equals(VictimSpecification spec) {
        return this.maxBatteryPower == spec.maxBatteryPower && this.staticBatteryUsage == spec.staticBatteryUsage &&
                this.moveSpeedDistParams.equals(spec.moveSpeedDistParams) && this.sensors.equals(spec.sensors);
    }

    public boolean equals(String encoding) {
        return this.encode().equals(encoding);
    }

}
