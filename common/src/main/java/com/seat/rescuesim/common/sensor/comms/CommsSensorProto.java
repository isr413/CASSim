package com.seat.rescuesim.common.sensor.comms;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.JSONObjectBuilder;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.sensor.SensorProto;
import com.seat.rescuesim.common.sensor.SensorType;

public class CommsSensorProto extends SensorProto {
    public static final String DELAY = "delay";

    protected static final double DEFAULT_DELAY = 0.0;
    protected static final CommsSensorType DEFAULT_SPEC_TYPE = CommsSensorType.GENERIC;

    private double delay;
    private CommsSensorType specType;

    public CommsSensorProto() {
        this(CommsSensorProto.DEFAULT_SPEC_TYPE, SensorProto.DEFAULT_RANGE, SensorProto.DEFAULT_ACCURACY,
            SensorProto.DEFAULT_BATTERY_USAGE, CommsSensorProto.DEFAULT_DELAY);
    }

    public CommsSensorProto(double range) {
        this(CommsSensorProto.DEFAULT_SPEC_TYPE, range, SensorProto.DEFAULT_ACCURACY, SensorProto.DEFAULT_BATTERY_USAGE,
            CommsSensorProto.DEFAULT_DELAY);
    }

    public CommsSensorProto(double range, double accuracy) {
        this(CommsSensorProto.DEFAULT_SPEC_TYPE, range, accuracy, SensorProto.DEFAULT_BATTERY_USAGE,
            CommsSensorProto.DEFAULT_DELAY);
    }

    public CommsSensorProto(double range, double accuracy, double batteryUsage) {
        this(CommsSensorProto.DEFAULT_SPEC_TYPE, range, accuracy, batteryUsage, CommsSensorProto.DEFAULT_DELAY);
    }

    public CommsSensorProto(double range, double accuracy, double batteryUsage, double delay) {
        this(CommsSensorProto.DEFAULT_SPEC_TYPE, range, accuracy, batteryUsage, delay);
    }

    public CommsSensorProto(CommsSensorType specType) {
        this(specType, SensorProto.DEFAULT_RANGE, SensorProto.DEFAULT_ACCURACY, SensorProto.DEFAULT_BATTERY_USAGE,
            CommsSensorProto.DEFAULT_DELAY);
    }

    public CommsSensorProto(CommsSensorType specType, double range) {
        this(specType, range, SensorProto.DEFAULT_ACCURACY, SensorProto.DEFAULT_BATTERY_USAGE,
            CommsSensorProto.DEFAULT_DELAY);
    }

    public CommsSensorProto(CommsSensorType specType, double range, double accuracy) {
        this(specType, range, accuracy, SensorProto.DEFAULT_BATTERY_USAGE, CommsSensorProto.DEFAULT_DELAY);
    }

    public CommsSensorProto(CommsSensorType specType, double range, double accuracy, double batteryUsage) {
        this(specType, range, accuracy, batteryUsage, CommsSensorProto.DEFAULT_DELAY);
    }

    public CommsSensorProto(CommsSensorType specType, double range, double accuracy, double batteryUsage, double delay) {
        super(SensorType.COMMS, range, accuracy, batteryUsage);
        this.specType = specType;
        this.delay = delay;
    }

    public CommsSensorProto(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        super.decode(json);
        this.specType = CommsSensorType.decodeType(json);
        this.delay = json.getDouble(CommsSensorProto.DELAY);
    }

    @Override
    protected JSONObjectBuilder getJSONBuilder() {
        JSONObjectBuilder json = super.getJSONBuilder();
        json.put(CommsSensorType.COMMS_SENSOR_TYPE, this.specType.getType());
        json.put(CommsSensorProto.DELAY, this.delay);
        return json;
    }

    public double getSensorDelay() {
        return this.delay;
    }

    @Override
    public CommsSensorType getSpecType() {
        return this.specType;
    }

    public boolean equals(CommsSensorProto spec) {
        return super.equals(spec) && this.specType.equals(spec.specType) && this.delay == spec.delay;
    }

}
