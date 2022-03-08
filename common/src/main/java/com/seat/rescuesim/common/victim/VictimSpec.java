package com.seat.rescuesim.common.victim;

import java.util.ArrayList;
import java.util.Arrays;

import com.seat.rescuesim.common.json.*;
import com.seat.rescuesim.common.remote.RemoteSpecification;
import com.seat.rescuesim.common.sensor.SensorConf;
import com.seat.rescuesim.common.sensor.SensorType;
import com.seat.rescuesim.common.util.Debugger;

/** A prototype for Victim remotes. */
public class VictimSpec extends JSONAble implements RemoteSpecification {
    private static final String VICTIM_MAX_BATTERY = "max_battery";
    private static final String VICTIM_MAX_VELOCITY = "max_velocity";
    private static final String VICTIM_MOVE_SPEED_PARAMS = "move_speed_params";
    private static final String VICTIM_SENSORS = "sensors";
    private static final String VICTIM_STATIC_BATTERY_USAGE = "static_battery_usage";
    private static final String VICTIM_TYPE = "victim_type";

    public static VictimSpec None() {
        return new VictimSpec(VictimType.NONE, new Double[]{0.0, 0.0}, 0, 1, 0, new ArrayList<SensorConf>());
    }

    private double maxBatteryPower;
    private double maxVelocity;
    private Double[] moveSpeedDistParams;
    private ArrayList<SensorConf> sensors;
    private double staticBatteryUsage;
    private VictimType type;

    public VictimSpec(JSONObject json) {
        super(json);
    }

    public VictimSpec(JSONOption option) {
        super(option);
    }

    public VictimSpec(String encoding) {
        super(encoding);
    }

    public VictimSpec() {
        this(VictimType.DEFAULT, new Double[]{0.0, 0.0}, 0, 1, 0, new ArrayList<SensorConf>());
    }

    public VictimSpec(VictimType type, double moveSpeedMean, double moveSpeedStdDev, double maxVelocity) {
        this(type, new Double[]{moveSpeedMean, moveSpeedStdDev}, maxVelocity, 1, 0, new ArrayList<SensorConf>());
    }

    public VictimSpec(double moveSpeedMean, double moveSpeedStdDev, double maxVelocity) {
        this(VictimType.DEFAULT, new Double[]{moveSpeedMean, moveSpeedStdDev}, maxVelocity, 1, 0,
            new ArrayList<SensorConf>());
    }

    public VictimSpec(VictimType type, double moveSpeedMean, double moveSpeedStdDev, double maxVelocity,
            SensorConf[] sensors) {
        this(type, new Double[]{moveSpeedMean, moveSpeedStdDev}, maxVelocity, 1, 0, sensors);
    }

    public VictimSpec(double moveSpeedMean, double moveSpeedStdDev, double maxVelocity, SensorConf[] sensors) {
        this(VictimType.DEFAULT, new Double[]{moveSpeedMean, moveSpeedStdDev}, maxVelocity, 1, 0, sensors);
    }

    public VictimSpec(VictimType type, double moveSpeedMean, double moveSpeedStdDev, double maxVelocity,
            ArrayList<SensorConf> sensors) {
        this(type, new Double[]{moveSpeedMean, moveSpeedStdDev}, maxVelocity, 1, 0, sensors);
    }

    public VictimSpec(double moveSpeedMean, double moveSpeedStdDev, double maxVelocity, ArrayList<SensorConf> sensors) {
        this(VictimType.DEFAULT, new Double[]{moveSpeedMean, moveSpeedStdDev}, maxVelocity, 1, 0, sensors);
    }

    public VictimSpec(VictimType type, Double[] moveSpeedDistParams, double maxVelocity) {
        this(type, moveSpeedDistParams, maxVelocity, 1, 0, new ArrayList<SensorConf>());
    }

    public VictimSpec(Double[] moveSpeedDistParams, double maxVelocity) {
        this(VictimType.DEFAULT, moveSpeedDistParams, maxVelocity, 1, 0, new ArrayList<SensorConf>());
    }

    public VictimSpec(VictimType type, Double[] moveSpeedDistParams, double maxVelocity, SensorConf[] sensors) {
        this(type, moveSpeedDistParams, maxVelocity, 1, 0, sensors);
    }

    public VictimSpec(Double[] moveSpeedDistParams, double maxVelocity, SensorConf[] sensors) {
        this(VictimType.DEFAULT, moveSpeedDistParams, maxVelocity, 1, 0, sensors);
    }

    public VictimSpec(VictimType type, Double[] moveSpeedDistParams, double maxVelocity,
            ArrayList<SensorConf> sensors) {
        this(type, moveSpeedDistParams, maxVelocity, 1, 0, sensors);
    }

    public VictimSpec(Double[] moveSpeedDistParams, double maxVelocity, ArrayList<SensorConf> sensors) {
        this(VictimType.DEFAULT, moveSpeedDistParams, maxVelocity, 1, 0, sensors);
    }

    public VictimSpec(VictimType type, double maxBatteryPower, double staticBatteryUsage) {
        this(type, new Double[]{0.0, 0.0}, 0, maxBatteryPower, staticBatteryUsage, new ArrayList<SensorConf>());
    }

    public VictimSpec(double maxBatteryPower, double staticBatteryUsage) {
        this(VictimType.DEFAULT, new Double[]{0.0, 0.0}, 0, maxBatteryPower, staticBatteryUsage,
            new ArrayList<SensorConf>());
    }

    public VictimSpec(VictimType type, double maxBatteryPower, double staticBatteryUsage, SensorConf[] sensors) {
        this(type, new Double[]{0.0, 0.0}, 0, maxBatteryPower, staticBatteryUsage, sensors);
    }

    public VictimSpec(double maxBatteryPower, double staticBatteryUsage, SensorConf[] sensors) {
        this(VictimType.DEFAULT, new Double[]{0.0, 0.0}, 0, maxBatteryPower, staticBatteryUsage, sensors);
    }

    public VictimSpec(VictimType type, double maxBatteryPower, double staticBatteryUsage,
            ArrayList<SensorConf> sensors) {
        this(type, new Double[]{0.0, 0.0}, 0, maxBatteryPower, staticBatteryUsage, sensors);
    }

    public VictimSpec(double maxBatteryPower, double staticBatteryUsage, ArrayList<SensorConf> sensors) {
        this(VictimType.DEFAULT, new Double[]{0.0, 0.0}, 0, maxBatteryPower, staticBatteryUsage, sensors);
    }

    public VictimSpec(VictimType type, double moveSpeedMean, double moveSpeedStdDev, double maxVelocity,
            double maxBatteryPower, double staticBatteryUsage) {
        this(type, new Double[]{moveSpeedMean, moveSpeedStdDev}, maxVelocity, maxBatteryPower, staticBatteryUsage,
            new ArrayList<SensorConf>());
    }

    public VictimSpec(double moveSpeedMean, double moveSpeedStdDev, double maxVelocity, double maxBatteryPower,
            double staticBatteryUsage) {
        this(VictimType.DEFAULT, new Double[]{moveSpeedMean, moveSpeedStdDev}, maxVelocity, maxBatteryPower,
            staticBatteryUsage, new ArrayList<SensorConf>());
    }

    public VictimSpec(VictimType type, Double[] moveSpeedDistParams, double maxVelocity, double maxBatteryPower,
            double staticBatteryUsage) {
        this(type, moveSpeedDistParams, maxVelocity, maxBatteryPower, staticBatteryUsage, new ArrayList<SensorConf>());
    }

    public VictimSpec(Double[] moveSpeedDistParams, double maxVelocity, double maxBatteryPower,
            double staticBatteryUsage) {
        this(VictimType.DEFAULT, moveSpeedDistParams, maxVelocity, maxBatteryPower, staticBatteryUsage,
            new ArrayList<SensorConf>());
    }

    public VictimSpec(VictimType type, double moveSpeedMean, double moveSpeedStdDev, double maxVelocity,
            double maxBatteryPower, double staticBatteryUsage, SensorConf[] sensors) {
        this(type, new Double[]{moveSpeedMean, moveSpeedStdDev}, maxVelocity, maxBatteryPower, staticBatteryUsage,
            new ArrayList<SensorConf>(Arrays.asList(sensors)));
    }

    public VictimSpec(double moveSpeedMean, double moveSpeedStdDev, double maxVelocity, double maxBatteryPower,
            double staticBatteryUsage, SensorConf[] sensors) {
        this(VictimType.DEFAULT, new Double[]{moveSpeedMean, moveSpeedStdDev}, maxVelocity, maxBatteryPower,
            staticBatteryUsage, new ArrayList<SensorConf>(Arrays.asList(sensors)));
    }

    public VictimSpec(VictimType type, Double[] moveSpeedDistParams, double maxVelocity, double maxBatteryPower,
            double staticBatteryUsage,SensorConf[] sensors) {
        this(type, moveSpeedDistParams, maxVelocity, maxBatteryPower, staticBatteryUsage,
            new ArrayList<SensorConf>(Arrays.asList(sensors)));
    }

    public VictimSpec(Double[] moveSpeedDistParams, double maxVelocity, double maxBatteryPower,
            double staticBatteryUsage, SensorConf[] sensors) {
        this(VictimType.DEFAULT, moveSpeedDistParams, maxVelocity, maxBatteryPower, staticBatteryUsage,
            new ArrayList<SensorConf>(Arrays.asList(sensors)));
    }

    public VictimSpec(VictimType type, double moveSpeedMean, double moveSpeedStdDev, double maxVelocity,
            double maxBatteryPower, double staticBatteryUsage, ArrayList<SensorConf> sensors) {
        this(type, new Double[]{moveSpeedMean, moveSpeedStdDev}, maxVelocity, maxBatteryPower, staticBatteryUsage,
            sensors);
    }

    public VictimSpec(double moveSpeedMean, double moveSpeedStdDev, double maxVelocity, double maxBatteryPower,
            double staticBatteryUsage, ArrayList<SensorConf> sensors) {
        this(VictimType.DEFAULT, new Double[]{moveSpeedMean, moveSpeedStdDev}, maxVelocity, maxBatteryPower,
            staticBatteryUsage, sensors);
    }

    public VictimSpec(Double[] moveSpeedDistParams, double maxVelocity, double maxBatteryPower,
            double staticBatteryUsage, ArrayList<SensorConf> sensors) {
        this(VictimType.DEFAULT, moveSpeedDistParams, maxVelocity, maxBatteryPower, staticBatteryUsage, sensors);
    }

    public VictimSpec(VictimType type, Double[] moveSpeedDistParams, double maxVelocity,
            double maxBatteryPower, double staticBatteryUsage, ArrayList<SensorConf> sensors) {
        this.type = type;
        this.moveSpeedDistParams = moveSpeedDistParams;
        this.maxVelocity = maxVelocity;
        this.maxBatteryPower = maxBatteryPower;
        this.staticBatteryUsage = staticBatteryUsage;
        this.sensors = sensors;
    }

    @Override
    public void decode(JSONObject json) {
        this.type = VictimType.values()[json.getInt(VictimSpec.VICTIM_TYPE)];
        JSONArray jsonParams = json.getJSONArray(VictimSpec.VICTIM_MOVE_SPEED_PARAMS);
        this.moveSpeedDistParams = new Double[jsonParams.length()];
        for (int i = 0; i < this.moveSpeedDistParams.length; i++) {
            this.moveSpeedDistParams[i] = jsonParams.getDouble(i);
        }
        this.maxVelocity = json.getDouble(VictimSpec.VICTIM_MAX_VELOCITY);
        this.maxBatteryPower = json.getDouble(VictimSpec.VICTIM_MAX_BATTERY);
        this.staticBatteryUsage = json.getDouble(VictimSpec.VICTIM_STATIC_BATTERY_USAGE);
        this.sensors = new ArrayList<>();
        JSONArray jsonSensors = json.getJSONArray(VictimSpec.VICTIM_SENSORS);
        for (int i = 0; i < jsonSensors.length(); i++) {
            this.sensors.add(new SensorConf(jsonSensors.getJSONObject(i)));
        }
    }

    public String getLabel() {
        return String.format("v%s", this.type.getLabel());
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

    public SensorConf getSensor(int idx) {
        if (!this.hasSensor(idx)) {
            Debugger.logger.err(String.format("No sensor at idx %d found on victim spec %s",
                idx, this.type.getLabel()));
            return null;
        }
        return this.sensors.get(idx);
    }

    public SensorConf getSensor(String remote) {
        for (SensorConf conf : this.sensors) {
            if (conf.hasRemote(remote)) {
                return conf;
            }
        }
        Debugger.logger.err(String.format("No sensor with ID %s found on victim spec %s",
            remote, this.type.getLabel()));
        return null;
    }

    public ArrayList<SensorConf> getSensors() {
        return this.sensors;
    }

    public ArrayList<SensorConf> getSensorsWithType(SensorType type) {
        ArrayList<SensorConf> confs = new ArrayList<>();
        for (SensorConf conf : this.sensors) {
            if (conf.getSpecification().getType().equals(type)) {
                confs.add(conf);
            }
        }
        if (confs.isEmpty()) {
            Debugger.logger.err(String.format("No sensor with type %s found on victim spec %s",
                type.getLabel(), this.type.getLabel()));
        }
        return confs;
    }

    public double getStaticBatteryUsage() {
        return this.staticBatteryUsage;
    }

    public VictimType getType() {
        return this.type;
    }

    public boolean hasSensor(int idx) {
        return 0 <= idx && idx < this.sensors.size();
    }

    public boolean hasSensor(String remote) {
        for (SensorConf conf : this.sensors) {
            if (conf.hasRemote(remote)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasSensors() {
        return !this.sensors.isEmpty();
    }

    public boolean hasSensorWithType(SensorType type) {
        for (SensorConf conf : this.sensors) {
            if (conf.getSpecification().getType().equals(type)) {
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
        json.put(VictimSpec.VICTIM_TYPE, this.type.getType());
        JSONArrayBuilder jsonParams = JSONBuilder.Array();
        for (int i = 0; i < this.moveSpeedDistParams.length; i++) {
            jsonParams.put(this.moveSpeedDistParams[i]);
        }
        json.put(VictimSpec.VICTIM_MOVE_SPEED_PARAMS, jsonParams.toJSON());
        json.put(VictimSpec.VICTIM_MAX_VELOCITY, this.maxVelocity);
        json.put(VictimSpec.VICTIM_MAX_BATTERY, this.maxBatteryPower);
        json.put(VictimSpec.VICTIM_STATIC_BATTERY_USAGE, this.staticBatteryUsage);
        JSONArrayBuilder jsonSensors = JSONBuilder.Array();
        for (SensorConf conf : this.sensors) {
            jsonSensors.put(conf.toJSON());
        }
        json.put(VictimSpec.VICTIM_SENSORS, jsonSensors.toJSON());
        return json.toJSON();
    }

    public boolean equals(VictimSpec spec) {
        return this.type.equals(spec.type) && this.maxBatteryPower == spec.maxBatteryPower &&
            this.staticBatteryUsage == spec.staticBatteryUsage &&
            Arrays.equals(this.moveSpeedDistParams, spec.moveSpeedDistParams) && this.sensors.equals(spec.sensors);
    }

}
