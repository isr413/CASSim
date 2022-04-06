package com.seat.rescuesim.common.sensor.comms;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.JSONObjectBuilder;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.sensor.SensorSpec;
import com.seat.rescuesim.common.sensor.SensorType;

public class CommsSensorSpec extends SensorSpec {
    public static final String DELAY = "delay";

    protected static final double DEFAULT_DELAY = 0.0;
    protected static final CommsSensorType DEFAULT_SPEC_TYPE = CommsSensorType.GENERIC;

    private double delay;
    private CommsSensorType specType;

    public CommsSensorSpec() {
        this(CommsSensorSpec.DEFAULT_SPEC_TYPE, SensorSpec.DEFAULT_RANGE, SensorSpec.DEFAULT_ACCURACY,
            SensorSpec.DEFAULT_BATTERY_USAGE, CommsSensorSpec.DEFAULT_DELAY);
    }

    public CommsSensorSpec(double range) {
        this(CommsSensorSpec.DEFAULT_SPEC_TYPE, range, SensorSpec.DEFAULT_ACCURACY, SensorSpec.DEFAULT_BATTERY_USAGE,
            CommsSensorSpec.DEFAULT_DELAY);
    }

    public CommsSensorSpec(double range, double accuracy) {
        this(CommsSensorSpec.DEFAULT_SPEC_TYPE, range, accuracy, SensorSpec.DEFAULT_BATTERY_USAGE,
            CommsSensorSpec.DEFAULT_DELAY);
    }

    public CommsSensorSpec(double range, double accuracy, double batteryUsage) {
        this(CommsSensorSpec.DEFAULT_SPEC_TYPE, range, accuracy, batteryUsage, CommsSensorSpec.DEFAULT_DELAY);
    }

    public CommsSensorSpec(double range, double accuracy, double batteryUsage, double delay) {
        this(CommsSensorSpec.DEFAULT_SPEC_TYPE, range, accuracy, batteryUsage, delay);
    }

    public CommsSensorSpec(CommsSensorType specType) {
        this(specType, SensorSpec.DEFAULT_RANGE, SensorSpec.DEFAULT_ACCURACY, SensorSpec.DEFAULT_BATTERY_USAGE,
            CommsSensorSpec.DEFAULT_DELAY);
    }

    public CommsSensorSpec(CommsSensorType specType, double range) {
        this(specType, range, SensorSpec.DEFAULT_ACCURACY, SensorSpec.DEFAULT_BATTERY_USAGE,
            CommsSensorSpec.DEFAULT_DELAY);
    }

    public CommsSensorSpec(CommsSensorType specType, double range, double accuracy) {
        this(specType, range, accuracy, SensorSpec.DEFAULT_BATTERY_USAGE, CommsSensorSpec.DEFAULT_DELAY);
    }

    public CommsSensorSpec(CommsSensorType specType, double range, double accuracy, double batteryUsage) {
        this(specType, range, accuracy, batteryUsage, CommsSensorSpec.DEFAULT_DELAY);
    }

    public CommsSensorSpec(CommsSensorType specType, double range, double accuracy, double batteryUsage, double delay) {
        super(SensorType.COMMS, range, accuracy, batteryUsage);
        this.specType = specType;
        this.delay = delay;
    }

    public CommsSensorSpec(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        super.decode(json);
        this.specType = CommsSensorType.decodeType(json);
        this.delay = json.getDouble(CommsSensorSpec.DELAY);
    }

    @Override
    protected JSONObjectBuilder getJSONBuilder() {
        JSONObjectBuilder json = super.getJSONBuilder();
        json.put(CommsSensorType.COMMS_SENSOR_TYPE, this.specType.getType());
        json.put(CommsSensorSpec.DELAY, this.delay);
        return json;
    }

    public double getSensorDelay() {
        return this.delay;
    }

    @Override
    public CommsSensorType getSpecType() {
        return this.specType;
    }

    public boolean equals(CommsSensorSpec spec) {
        return super.equals(spec) && this.specType.equals(spec.specType) && this.delay == spec.delay;
    }

}
