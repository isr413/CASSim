package com.seat.rescuesim.common.drone;

import java.util.ArrayList;
import java.util.Arrays;

import com.seat.rescuesim.common.json.*;
import com.seat.rescuesim.common.math.Vector;
import com.seat.rescuesim.common.remote.RemoteSpecification;
import com.seat.rescuesim.common.sensor.SensorConf;
import com.seat.rescuesim.common.sensor.SensorType;
import com.seat.rescuesim.common.util.Debugger;

/** A serializable class to represent the specification of a Drone. */
public class DroneSpec extends JSONAble implements RemoteSpecification {
    private static final String DRONE_BATTERY_USAGE = "battery_usage";
    private static final String DRONE_LOCATION = "location";
    private static final String DRONE_MAX_ACCELERATION = "max_acceleration";
    private static final String DRONE_MAX_BATTERY = "max_battery";
    private static final String DRONE_MAX_JERK = "max_jerk";
    private static final String DRONE_MAX_VELOCITY = "max_velocity";
    private static final String DRONE_SENSORS = "sensors";
    private static final String DRONE_TYPE = "drone_type";

    public static DroneSpec None() {
        return new DroneSpec(DroneType.NONE, 0, new Vector(), new Vector(), 0, 0, 0, new ArrayList<SensorConf>());
    }

    private Vector batteryUsage; // [static (hovering), horizontal movement, vertical movement]
    private Vector initialLocation;
    private double maxAcceleration;
    private double maxBatteryPower;
    private double maxJerk;
    private double maxVelocity;
    private ArrayList<SensorConf> sensors;
    private DroneType type;

    public DroneSpec(JSONObject json) {
        super(json);
    }

    public DroneSpec(JSONOption option) {
        super(option);
    }

    public DroneSpec(String encoding) {
        super(encoding);
    }

    public DroneSpec() {
        this(DroneType.DEFAULT, 0, new Vector(), new Vector(), 0, 0, 0, new ArrayList<SensorConf>());
    }

    public DroneSpec(DroneType type, double maxBatteryPower, Vector batteryUsage, Vector location) {
        this(type, maxBatteryPower, batteryUsage, location, 0, 0, 0, new ArrayList<SensorConf>());
    }

    public DroneSpec(double maxBatteryPower, Vector batteryUsage, Vector location) {
        this(DroneType.DEFAULT, maxBatteryPower, batteryUsage, location, 0, 0, 0, new ArrayList<SensorConf>());
    }

    public DroneSpec(DroneType type, double maxBatteryPower, Vector batteryUsage, Vector location,
            SensorConf[] sensors) {
        this(type, maxBatteryPower, batteryUsage, location, 0, 0, 0, sensors);
    }

    public DroneSpec(double maxBatteryPower, Vector batteryUsage, Vector location, SensorConf[] sensors) {
        this(DroneType.DEFAULT, maxBatteryPower, batteryUsage, location, 0, 0, 0, sensors);
    }

    public DroneSpec(DroneType type, double maxBatteryPower, Vector batteryUsage, Vector location,
            ArrayList<SensorConf> sensors) {
        this(type, maxBatteryPower, batteryUsage, location, 0, 0, 0, sensors);
    }

    public DroneSpec(double maxBatteryPower, Vector batteryUsage, Vector location, ArrayList<SensorConf> sensors) {
        this(DroneType.DEFAULT, maxBatteryPower, batteryUsage, location, 0, 0, 0, sensors);
    }

    public DroneSpec(DroneType type, double maxBatteryPower, Vector batteryUsage, Vector location,
            double maxVelocity, double maxAccleration, double maxJerk) {
        this(type, maxBatteryPower, batteryUsage, location, maxVelocity, maxAccleration, maxJerk,
            new ArrayList<SensorConf>());
    }

    public DroneSpec(double maxBatteryPower, Vector batteryUsage, Vector location,
            double maxVelocity, double maxAccleration, double maxJerk) {
        this(DroneType.DEFAULT, maxBatteryPower, batteryUsage, location, maxVelocity, maxAccleration, maxJerk,
            new ArrayList<SensorConf>());
    }

    public DroneSpec(DroneType type, double maxBatteryPower, Vector batteryUsage, Vector location,
            double maxVelocity, double maxAccleration, double maxJerk, SensorConf[] sensors) {
        this(type, maxBatteryPower, batteryUsage, location, maxVelocity, maxAccleration, maxJerk,
            new ArrayList<SensorConf>(Arrays.asList(sensors)));
    }

    public DroneSpec(double maxBatteryPower, Vector batteryUsage, Vector location,
            double maxVelocity, double maxAccleration, double maxJerk, SensorConf[] sensors) {
        this(DroneType.DEFAULT, maxBatteryPower, batteryUsage, location, maxVelocity, maxAccleration, maxJerk,
            new ArrayList<SensorConf>(Arrays.asList(sensors)));
    }

    public DroneSpec(double maxBatteryPower, Vector batteryUsage, Vector location,
            double maxVelocity, double maxAccleration, double maxJerk, ArrayList<SensorConf> sensors) {
        this(DroneType.DEFAULT, maxBatteryPower, batteryUsage, location, maxVelocity, maxAccleration, maxJerk,
            sensors);
    }

    public DroneSpec(DroneType type, double maxBatteryPower, Vector batteryUsage, Vector location,
            double maxVelocity, double maxAcceleration, double maxJerk,
            ArrayList<SensorConf> sensors) {
        this.type = type;
        this.maxBatteryPower = maxBatteryPower;
        this.batteryUsage = batteryUsage;
        this.initialLocation = location;
        this.maxVelocity = maxVelocity;
        this.maxAcceleration = maxAcceleration;
        this.maxJerk = maxJerk;
        this.sensors = sensors;
    }

    @Override
    protected void decode(JSONObject json) {
        this.type = DroneType.values()[json.getInt(DroneSpec.DRONE_TYPE)];
        this.maxBatteryPower = json.getDouble(DroneSpec.DRONE_MAX_BATTERY);
        this.batteryUsage = new Vector(json.getJSONArray(DroneSpec.DRONE_BATTERY_USAGE));
        this.initialLocation = new Vector(json.getJSONArray(DroneSpec.DRONE_LOCATION));
        this.maxVelocity = json.getDouble(DroneSpec.DRONE_MAX_VELOCITY);
        this.maxAcceleration = json.getDouble(DroneSpec.DRONE_MAX_ACCELERATION);
        this.maxJerk = json.getDouble(DroneSpec.DRONE_MAX_JERK);
        this.sensors = new ArrayList<>();
        JSONArray jsonSensors = json.getJSONArray(DroneSpec.DRONE_SENSORS);
        for (int i = 0; i < jsonSensors.length(); i++) {
            this.sensors.add(new SensorConf(jsonSensors.getJSONObject(i)));
        }
    }

    public Vector getBatteryUsage() {
        return this.batteryUsage;
    }

    public Vector getInitialLocation() {
        return this.initialLocation;
    }

    public double getHorizontalKineticBatteryUsage() {
        return this.batteryUsage.getY();
    }

    public String getLabel() {
        return String.format("d%s", this.type.getLabel());
    }

    public double getMaxAcceleration() {
        return this.maxAcceleration;
    }

    public double getMaxBatteryPower() {
        return this.maxBatteryPower;
    }

    public double getMaxJerk() {
        return this.maxJerk;
    }

    public double getMaxVelocity() {
        return this.maxVelocity;
    }

    public SensorConf getSensor(int idx) {
        if (idx < 0 || this.sensors.size() <= idx) {
            Debugger.logger.err(String.format("No sensor at index %d found on drone spec %s",
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
        Debugger.logger.err(String.format("No sensor with ID %s found on drone spec %s",
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
            Debugger.logger.err(String.format("No sensor with type %s found on drone spec %s",
                type.getLabel(), this.type.getLabel()));
        }
        return confs;
    }

    public double getStaticBatteryUsage() {
        return this.batteryUsage.getX();
    }

    public DroneType getType() {
        return this.type;
    }

    public double getVerticalKineticBatteryUsage() {
        return this.batteryUsage.getZ();
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

    public boolean isDisabled() {
        return !this.isEnabled();
    }

    public boolean isEnabled() {
        return this.maxBatteryPower > 0;
    }

    public boolean isKinetic() {
        return this.maxVelocity > 0 && this.maxAcceleration > 0 && this.maxJerk > 0;
    }

    public JSONOption toJSON() {
        JSONObjectBuilder json = JSONBuilder.Object();
        json.put(DroneSpec.DRONE_TYPE, this.type.getType());
        json.put(DroneSpec.DRONE_MAX_BATTERY, this.maxBatteryPower);
        json.put(DroneSpec.DRONE_BATTERY_USAGE, this.batteryUsage.toJSON());
        json.put(DroneSpec.DRONE_LOCATION, this.initialLocation.toJSON());
        json.put(DroneSpec.DRONE_MAX_VELOCITY, this.maxVelocity);
        json.put(DroneSpec.DRONE_MAX_ACCELERATION, this.maxAcceleration);
        json.put(DroneSpec.DRONE_MAX_JERK, this.maxJerk);
        JSONArrayBuilder jsonSensors = JSONBuilder.Array();
        for (SensorConf conf : this.sensors) {
            jsonSensors.put(conf.toJSON());
        }
        json.put(DroneSpec.DRONE_SENSORS, jsonSensors.toJSON());
        return json.toJSON();
    }

    public boolean equals(DroneSpec spec) {
        return this.type.equals(spec.type) && this.maxBatteryPower == spec.maxBatteryPower &&
            this.batteryUsage.equals(spec.batteryUsage) && this.initialLocation.equals(spec.initialLocation) &&
            this.maxVelocity == spec.maxVelocity && this.maxAcceleration == spec.maxAcceleration &&
            this.maxJerk == spec.maxJerk && this.sensors.equals(spec.sensors);
    }

}
