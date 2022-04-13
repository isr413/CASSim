package com.seat.sim.common.remote.mobile.aerial;

import java.util.Collection;

import com.seat.sim.common.json.JSONException;
import com.seat.sim.common.json.JSONObject;
import com.seat.sim.common.json.JSONObjectBuilder;
import com.seat.sim.common.json.JSONOption;
import com.seat.sim.common.math.Vector;
import com.seat.sim.common.remote.mobile.MobileRemoteProto;
import com.seat.sim.common.sensor.SensorConfig;

/** A serializable prototype of an Aerial Remote. */
public class AerialRemoteProto extends MobileRemoteProto {
    public static final String BATTERY_USAGE = "battery_usage";

    private Vector batteryUsage; // [static (hovering), horizontal movement, vertical movement]

    public AerialRemoteProto(double maxBatteryPower, double maxVelocity, double maxAcceleration, Vector batteryUsage) {
        this(null, maxBatteryPower, null, maxVelocity, maxAcceleration, batteryUsage);
    }

    public AerialRemoteProto(Vector location, double maxBatteryPower, double maxVelocity, double maxAcceleration,
            Vector batteryUsage) {
        this(location, maxBatteryPower, null, maxVelocity, maxAcceleration, batteryUsage);
    }

    public AerialRemoteProto(double maxBatteryPower, Collection<SensorConfig> sensors, double maxVelocity,
            double maxAcceleration, Vector batteryUsage) {
        this(null, maxBatteryPower, sensors, maxVelocity, maxAcceleration, batteryUsage);
    }

    public AerialRemoteProto(Vector location, double maxBatteryPower, Collection<SensorConfig> sensors,
            double maxVelocity, double maxAcceleration, Vector batteryUsage) {
        super(location, maxBatteryPower, sensors, maxVelocity, maxAcceleration);
        this.batteryUsage = batteryUsage;
    }

    public AerialRemoteProto(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        super.decode(json);
        this.batteryUsage = (json.hasKey(AerialRemoteProto.BATTERY_USAGE)) ?
            new Vector(json.getJSONOption(AerialRemoteProto.BATTERY_USAGE)) :
            new Vector();
    }

    @Override
    protected JSONObjectBuilder getJSONBuilder() throws JSONException {
        JSONObjectBuilder json = super.getJSONBuilder();
        json.put(AerialRemoteProto.BATTERY_USAGE, this.batteryUsage.toJSON());
        return json;
    }

    public Vector getBatteryUsage() {
        return this.batteryUsage;
    }

    public double getHorizontalKineticBatteryUsage() {
        return this.batteryUsage.getY();
    }

    @Override
    public String getLabel() {
        return "r:<aerial>";
    }

    public double getStaticBatteryUsage() {
        return this.batteryUsage.getX();
    }

    public double getVerticalKineticBatteryUsage() {
        return this.batteryUsage.getZ();
    }

    public boolean hasBatteryUsage() {
        return this.batteryUsage.getMagnitude() > 0;
    }

    public boolean hasHorizontalKineticBatteryUsage() {
        return this.getHorizontalKineticBatteryUsage() > 0;
    }

    public boolean hasStaticBatteryUsage() {
        return this.getStaticBatteryUsage() > 0;
    }

    public boolean hasVerticalKineticBatteryUsage() {
        return this.getVerticalKineticBatteryUsage() > 0;
    }

    @Override
    public boolean isAerial() {
        return true;
    }

    public boolean equals(AerialRemoteProto proto) {
        if (proto == null) return false;
        return super.equals(proto) && this.batteryUsage.equals(proto.batteryUsage);
    }

}
