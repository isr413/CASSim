package com.seat.rescuesim.common;

import java.util.Arrays;
import java.util.HashSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VictimSpecification {
    private static final String VICTIM_FUEL = "fuel";
    private static final String VICTIM_MIN_FUEL_COST = "min_fuel_cost";
    private static final String VICTIM_MOVE_SPEED = "move_speed";
    private static final String VICTIM_SENSORS = "sensors";

    private double maxBatteryPower;
    private double minBatteryUsage;
    private Tuple<Double, Double> moveSpeedDistParams;
    private Sensor[] sensors;

    public static VictimSpecification decode(JSONObject json) throws JSONException {
        double batteryPower = json.getDouble(VictimSpecification.VICTIM_FUEL);
        double batteryUsage = json.getDouble(VictimSpecification.VICTIM_MIN_FUEL_COST);
        JSONArray distParams = json.getJSONArray(VictimSpecification.VICTIM_MOVE_SPEED);
        double moveSpeedMean = distParams.getDouble(0);
        double moveSpeedStdDev = distParams.getDouble(1);
        JSONArray victimSensors = json.getJSONArray(VictimSpecification.VICTIM_SENSORS);
        Sensor[] sensors = new Sensor[victimSensors.length()];
        for (int i = 0; i < sensors.length; i++) {
            sensors[i] = Sensor.decode(victimSensors.getJSONObject(i));
        }
        return new VictimSpecification(batteryPower, batteryUsage, moveSpeedMean, moveSpeedStdDev, sensors);
    }

    public static VictimSpecification decode(String encoding) throws JSONException {
        return VictimSpecification.decode(new JSONObject(encoding));
    }

    public VictimSpecification(double batteryPower, double batteryUsage, double moveSpeedMean, double moveSpeedStdDev,
            Sensor[] sensors) {
        this.maxBatteryPower = batteryPower;
        this.minBatteryUsage = batteryUsage;
        this.moveSpeedDistParams = new Tuple<>(moveSpeedMean, moveSpeedStdDev);
        this.sensors = sensors;
    }

    public double getMaxBatteryPower() {
        return this.maxBatteryPower;
    }

    public double getMinBatteryUsage() {
        return this.minBatteryUsage;
    }

    public Tuple<Double, Double> getMoveSpeedDistributionParameters() {
        return this.moveSpeedDistParams;
    }

    public Sensor[] getSensors() {
        return this.sensors;
    }

    public Sensor getSensorWithType(SensorType type) {
        for (int i = 0; i < this.sensors.length; i++) {
            if (this.sensors[i].getType() == type) {
                return this.sensors[i];
            }
        }
        return null;
    }

    public boolean hasSensorWithType(SensorType type) {
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
        json.put(VictimSpecification.VICTIM_FUEL, this.maxBatteryPower);
        json.put(VictimSpecification.VICTIM_MIN_FUEL_COST, this.minBatteryUsage);
        JSONArray distParams = new JSONArray();
        distParams.put(this.moveSpeedDistParams.getFirst());
        distParams.put(this.moveSpeedDistParams.getSecond());
        json.put(VictimSpecification.VICTIM_MOVE_SPEED, distParams);
        JSONArray victimSensors = new JSONArray();
        for (int i = 0; i < this.sensors.length; i++) {
            victimSensors.put(this.sensors[i].toJSON());
        }
        json.put(VictimSpecification.VICTIM_SENSORS, victimSensors);
        return json;
    }

    public String toString() {
        return this.encode();
    }

    public boolean equals(VictimSpecification spec) {
        return this.maxBatteryPower == spec.maxBatteryPower && this.minBatteryUsage == spec.minBatteryUsage &&
                this.moveSpeedDistParams.equals(spec.moveSpeedDistParams) &&
                this.sensors.length == spec.sensors.length &&
                new HashSet<>(Arrays.asList(this.sensors)).equals(new HashSet<>(Arrays.asList(spec.sensors)));
    }

}
