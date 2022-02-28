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
    private static final String VICTIM_TYPE = "type";

    public static VictimSpecification None() {
        return new VictimSpecification();
    }

    private double maxBatteryPower;
    private double maxVelocity;
    private Double[] moveSpeedDistParams;
    private HashMap<String, SensorSpecification> sensors;
    private double staticBatteryUsage;
    private VictimType type;

    public VictimSpecification(JSONObject json) {
        super(json);
    }

    public VictimSpecification(JSONOption option) {
        super(option);
    }

    public VictimSpecification(String encoding) {
        super(encoding);
    }

    public VictimSpecification() {
        this(VictimType.NONE, 0, 0, new Double[]{0.0, 0.0}, 0, new HashMap<String, SensorSpecification>());
    }

    public VictimSpecification(VictimType type, double maxBatteryPower, double staticBatteryUsage) {
        this(type, maxBatteryPower, staticBatteryUsage, new Double[]{0.0, 0.0}, 0,
            new HashMap<String, SensorSpecification>());
    }

    public VictimSpecification(VictimType type, double maxBatteryPower, double staticBatteryUsage,
            SensorSpecification[] sensors) {
        this(type, maxBatteryPower, staticBatteryUsage, new Double[]{0.0, 0.0}, 0, sensors);
    }

    public VictimSpecification(VictimType type, double maxBatteryPower, double staticBatteryUsage,
            ArrayList<SensorSpecification> sensors) {
        this(type, maxBatteryPower, staticBatteryUsage, new Double[]{0.0, 0.0}, 0, sensors);
    }

    public VictimSpecification(VictimType type, double maxBatteryPower, double staticBatteryUsage,
            HashMap<String, SensorSpecification> sensors) {
        this(type, maxBatteryPower, staticBatteryUsage, new Double[]{0.0, 0.0}, 0, sensors);
    }

    public VictimSpecification(VictimType type, double maxBatteryPower, double staticBatteryUsage,
            double moveSpeedMean, double moveSpeedStdDev, double maxVelocity) {
        this(type, maxBatteryPower, staticBatteryUsage, new Double[]{moveSpeedMean, moveSpeedStdDev}, maxVelocity,
            new HashMap<String, SensorSpecification>());
    }

    public VictimSpecification(VictimType type, double maxBatteryPower, double staticBatteryUsage,
            Double[] moveSpeedDistParams, double maxVelocity) {
        this(type, maxBatteryPower, staticBatteryUsage, moveSpeedDistParams, maxVelocity,
            new HashMap<String, SensorSpecification>());
    }

    public VictimSpecification(VictimType type, double maxBatteryPower, double staticBatteryUsage,
            double moveSpeedMean, double moveSpeedStdDev, double maxVelocity, SensorSpecification[] sensors) {
        this(type, maxBatteryPower, staticBatteryUsage, new Double[]{moveSpeedMean, moveSpeedStdDev},
                maxVelocity, new ArrayList<SensorSpecification>(Arrays.asList(sensors)));
    }

    public VictimSpecification(VictimType type, double maxBatteryPower, double staticBatteryUsage,
            Double[] moveSpeedDistParams, double maxVelocity, SensorSpecification[] sensors) {
        this(type, maxBatteryPower, staticBatteryUsage, moveSpeedDistParams, maxVelocity,
            new ArrayList<SensorSpecification>(Arrays.asList(sensors)));
    }

    public VictimSpecification(VictimType type, double maxBatteryPower, double staticBatteryUsage,
            double moveSpeedMean, double moveSpeedStdDev, double maxVelocity,
            ArrayList<SensorSpecification> sensors) {
        this(type, maxBatteryPower, staticBatteryUsage, new Double[]{moveSpeedMean, moveSpeedStdDev}, maxVelocity,
            sensors);
    }

    public VictimSpecification(VictimType type, double maxBatteryPower, double staticBatteryUsage,
            Double[] moveSpeedDistParams, double maxVelocity, ArrayList<SensorSpecification> sensors) {
        this(type, maxBatteryPower, staticBatteryUsage, moveSpeedDistParams, maxVelocity,
            new HashMap<String, SensorSpecification>());
        for (SensorSpecification spec : sensors) {
            this.sensors.put(spec.getSpecID(), spec);
        }
    }

    public VictimSpecification(VictimType type, double maxBatteryPower, double staticBatteryUsage,
            double moveSpeedMean, double moveSpeedStdDev, double maxVelocity,
            HashMap<String, SensorSpecification> sensors) {
        this(type, maxBatteryPower, staticBatteryUsage, new Double[]{moveSpeedMean, moveSpeedStdDev}, maxVelocity,
            sensors);
    }

    public VictimSpecification(VictimType type, double maxBatteryPower, double staticBatteryUsage,
            Double[] moveSpeedDistParams, double maxVelocity, HashMap<String, SensorSpecification> sensors) {
        this.type = type;
        this.maxBatteryPower = maxBatteryPower;
        this.staticBatteryUsage = staticBatteryUsage;
        this.moveSpeedDistParams = moveSpeedDistParams;
        this.maxVelocity = maxVelocity;
        this.sensors = sensors;
    }

    @Override
    public void decode(JSONObject json) {
        this.type = VictimType.values()[json.getInt(VictimSpecification.VICTIM_TYPE)];
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
            this.sensors.put(spec.getSpecID(), spec);
        }
    }

    public String getLabel() {
        return this.type.getLabel();
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

    public SensorSpecification getSensor(String sensor) {
        if (!this.hasSensor(sensor)) {
            Debugger.logger.err(String.format("No sensor with type %s found on victim spec %s",
                sensor, this.type.getLabel()));
            return null;
        }
        return this.sensors.get(sensor);
    }

    public ArrayList<SensorSpecification> getSensors() {
        return new ArrayList<SensorSpecification>(this.sensors.values());
    }

    public ArrayList<SensorSpecification> getSensorsWithType(SensorType type) {
        ArrayList<SensorSpecification> specs = new ArrayList<>();
        for (SensorSpecification spec : this.sensors.values()) {
            if (spec.getSensorType() == type) {
                specs.add(spec);
            }
        }
        return specs;
    }

    public double getStaticBatteryUsage() {
        return this.staticBatteryUsage;
    }

    public VictimType getVictimType() {
        return this.type;
    }

    public boolean hasSensor(String sensor) {
        return this.sensors.containsKey(sensor);
    }

    public boolean hasSensors() {
        return !this.sensors.isEmpty();
    }

    public boolean hasSensorWithType(SensorType type) {
        for (SensorSpecification spec : this.sensors.values()) {
            if (spec.getSensorType() == type) {
                return true;
            }
        }
        return false;
    }

    public boolean isAlive() {
        return this.maxVelocity > 0;
    }

    public boolean isDead() {
        return !this.isAlive();
    }

    public JSONOption toJSON() {
        JSONObjectBuilder json = JSONBuilder.Object();
        json.put(VictimSpecification.VICTIM_TYPE, this.type.getType());
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
        return this.type.equals(spec.type) && this.maxBatteryPower == spec.maxBatteryPower &&
            this.staticBatteryUsage == spec.staticBatteryUsage &&
            Arrays.equals(this.moveSpeedDistParams, spec.moveSpeedDistParams) && this.sensors.equals(spec.sensors);
    }

}
