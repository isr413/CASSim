package com.seat.rescuesim.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.seat.rescuesim.common.json.*;
import com.seat.rescuesim.common.util.Debugger;

public class VictimSpecification extends JSONAble {
    private static final String VICTIM_MAX_BATTERY = "max_battery";
    private static final String VICTIM_MAX_VELOCITY = "max_velocity";
    private static final String VICTIM_MOVE_SPEED_PARAMS = "move_speed_params";
    private static final String VICTIM_SENSORS = "sensors";
    private static final String VICTIM_STATIC_BATTERY_USAGE = "static_battery_usage";

    private double maxBatteryPower;
    private double maxVelocity;
    private Double[] moveSpeedDistParams;
    private HashMap<SensorType, SensorSpecification> sensors;
    private double staticBatteryUsage;

    public VictimSpecification(JSONObject json) {
        super(json);
    }

    public VictimSpecification(JSONOption option) {
        super(option);
    }

    public VictimSpecification(String encoding) {
        super(encoding);
    }

    public VictimSpecification(double batteryPower, double staticBatteryUsage) {
        this(batteryPower, staticBatteryUsage, new Double[]{0.0, 0.0}, 0,
            new HashMap<SensorType, SensorSpecification>());
    }

    public VictimSpecification(double batteryPower, double staticBatteryUsage, SensorSpecification[] sensors) {
        this(batteryPower, staticBatteryUsage, new Double[]{0.0, 0.0}, 0, sensors);
    }

    public VictimSpecification(double batteryPower, double staticBatteryUsage,
            ArrayList<SensorSpecification> sensors) {
        this(batteryPower, staticBatteryUsage, new Double[]{0.0, 0.0}, 0, sensors);
    }

    public VictimSpecification(double batteryPower, double staticBatteryUsage,
            HashMap<SensorType, SensorSpecification> sensors) {
        this(batteryPower, staticBatteryUsage, new Double[]{0.0, 0.0}, 0, sensors);
    }

    public VictimSpecification(double batteryPower, double staticBatteryUsage,
            double moveSpeedMean, double moveSpeedStdDev, double maxVelocity) {
        this(batteryPower, staticBatteryUsage, new Double[]{moveSpeedMean, moveSpeedStdDev}, maxVelocity,
            new HashMap<SensorType, SensorSpecification>());
    }

    public VictimSpecification(double batteryPower, double staticBatteryUsage,
            Double[] moveSpeedDistParams, double maxVelocity) {
        this(batteryPower, staticBatteryUsage, moveSpeedDistParams, maxVelocity,
            new HashMap<SensorType, SensorSpecification>());
    }

    public VictimSpecification(double batteryPower, double staticBatteryUsage,
            double moveSpeedMean, double moveSpeedStdDev, double maxVelocity, SensorSpecification[] sensors) {
        this(batteryPower, staticBatteryUsage, new Double[]{moveSpeedMean, moveSpeedStdDev},
                maxVelocity, new ArrayList<SensorSpecification>(Arrays.asList(sensors)));
    }

    public VictimSpecification(double batteryPower, double staticBatteryUsage,
            Double[] moveSpeedDistParams, double maxVelocity, SensorSpecification[] sensors) {
        this(batteryPower, staticBatteryUsage, moveSpeedDistParams, maxVelocity,
            new ArrayList<SensorSpecification>(Arrays.asList(sensors)));
    }

    public VictimSpecification(double batteryPower, double staticBatteryUsage,
            double moveSpeedMean, double moveSpeedStdDev, double maxVelocity,
            ArrayList<SensorSpecification> sensors) {
        this(batteryPower, staticBatteryUsage, new Double[]{moveSpeedMean, moveSpeedStdDev}, maxVelocity, sensors);
    }

    public VictimSpecification(double batteryPower, double staticBatteryUsage,
            Double[] moveSpeedDistParams, double maxVelocity, ArrayList<SensorSpecification> sensors) {
        this(batteryPower, staticBatteryUsage, moveSpeedDistParams, maxVelocity,
            new HashMap<SensorType, SensorSpecification>());
        for (SensorSpecification spec : sensors) {
            this.sensors.put(spec.getSensorType(), spec);
        }
    }

    public VictimSpecification(double batteryPower, double staticBatteryUsage,
            double moveSpeedMean, double moveSpeedStdDev, double maxVelocity,
            HashMap<SensorType, SensorSpecification> sensors) {
        this(batteryPower, staticBatteryUsage, new Double[]{moveSpeedMean, moveSpeedStdDev}, maxVelocity, sensors);
    }

    public VictimSpecification(double batteryPower, double staticBatteryUsage,
            Double[] moveSpeedDistParams, double maxVelocity, HashMap<SensorType, SensorSpecification> sensors) {
        this.maxBatteryPower = batteryPower;
        this.staticBatteryUsage = staticBatteryUsage;
        this.moveSpeedDistParams = moveSpeedDistParams;
        this.maxVelocity = maxVelocity;
        this.sensors = sensors;
    }

    @Override
    public void decode(JSONObject json) {
        this.maxBatteryPower = json.getDouble(VictimSpecification.VICTIM_MAX_BATTERY);
        this.staticBatteryUsage = json.getDouble(VictimSpecification.VICTIM_STATIC_BATTERY_USAGE);
        JSONArray jsonParams = json.getJSONArray(VictimSpecification.VICTIM_MOVE_SPEED_PARAMS);
        this.moveSpeedDistParams = new Double[jsonParams.length()];
        for (int i = 0; i < this.moveSpeedDistParams.length; i++) {
            this.moveSpeedDistParams[i] = jsonParams.getDouble(i);
        }
        this.maxVelocity = json.getDouble(VictimSpecification.VICTIM_MAX_VELOCITY);
        this.sensors = new HashMap<>();
        JSONArray jsonSensors = json.getJSONArray(VictimSpecification.VICTIM_SENSORS);
        for (int i = 0; i < jsonSensors.length(); i++) {
            SensorSpecification spec = new SensorSpecification(jsonSensors.getJSONObject(i));
            this.sensors.put(spec.getSensorType(), spec);
        }
    }

    public double getMaxBatteryPower() {
        return this.maxBatteryPower;
    }

    public double getMaxVelocity() {
        return this.maxVelocity;
    }

    public Double[] getMoveSpeedDistributionParameters() {
        return this.moveSpeedDistParams;
    }

    public ArrayList<SensorSpecification> getSensors() {
        return new ArrayList<SensorSpecification>(this.sensors.values());
    }

    public SensorSpecification getSensorWithType(SensorType type) {
        if (!this.hasSensorWithType(type)) {
            Debugger.logger.err(String.format("No sensor with type %s found on victim spec", type.getLabel()));
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

    public boolean isDead() {
        return this.maxVelocity > 0;
    }

    public JSONOption toJSON() {
        JSONObjectBuilder json = JSONBuilder.Object();
        json.put(VictimSpecification.VICTIM_MAX_BATTERY, this.maxBatteryPower);
        json.put(VictimSpecification.VICTIM_STATIC_BATTERY_USAGE, this.staticBatteryUsage);
        JSONArrayBuilder jsonParams = JSONBuilder.Array();
        for (int i = 0; i < this.moveSpeedDistParams.length; i++) {
            jsonParams.put(this.moveSpeedDistParams[i]);
        }
        json.put(VictimSpecification.VICTIM_MOVE_SPEED_PARAMS, jsonParams.toJSON());
        json.put(VictimSpecification.VICTIM_MAX_VELOCITY, this.maxVelocity);
        JSONArrayBuilder jsonSensors = JSONBuilder.Array();
        for (SensorSpecification spec : this.sensors.values()) {
            jsonSensors.put(spec.toJSON());
        }
        json.put(VictimSpecification.VICTIM_SENSORS, jsonSensors.toJSON());
        return json.toJSON();
    }

    public boolean equals(VictimSpecification spec) {
        return this.maxBatteryPower == spec.maxBatteryPower && this.staticBatteryUsage == spec.staticBatteryUsage &&
                Arrays.equals(this.moveSpeedDistParams, spec.moveSpeedDistParams) && this.sensors.equals(spec.sensors);
    }

}
