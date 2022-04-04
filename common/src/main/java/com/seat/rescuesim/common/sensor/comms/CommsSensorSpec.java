package com.seat.rescuesim.common.sensor.comms;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.JSONObjectBuilder;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.sensor.SensorSpec;
import com.seat.rescuesim.common.sensor.SensorType;

public class CommsSensorSpec extends SensorSpec {

    protected double delay;
    protected CommsSensorType specType;

    public CommsSensorSpec() {
        this(CommsSensorType.GENERIC);
    }

    public CommsSensorSpec(double range) {
        this(CommsSensorType.GENERIC, range);
    }

    public CommsSensorSpec(double range, double accuracy) {
        this(CommsSensorType.GENERIC, range, accuracy);
    }

    public CommsSensorSpec(double range, double accuracy, double batteryUsage) {
        this(CommsSensorType.GENERIC, range, accuracy, batteryUsage);
    }

    public CommsSensorSpec(double range, double accuracy, double batteryUsage, double delay) {
        this(CommsSensorType.GENERIC, range, accuracy, batteryUsage, delay);
    }

    public CommsSensorSpec(CommsSensorType specType) {
        this(specType, Double.POSITIVE_INFINITY, 1, 0, 0);
    }

    public CommsSensorSpec(CommsSensorType specType, double range) {
        this(specType, range, 1, 0, 0);
    }

    public CommsSensorSpec(CommsSensorType specType, double range, double accuracy) {
        this(specType, range, accuracy, 0, 0);
    }

    public CommsSensorSpec(CommsSensorType specType, double range, double accuracy, double batteryUsage) {
        this(specType, range, accuracy, batteryUsage, 0);
    }

    public CommsSensorSpec(CommsSensorType specType, double range, double accuracy, double batteryUsage,
            double delay) {
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
        this.delay = json.getDouble(CommsSensorConst.DELAY);
    }

    @Override
    protected JSONObjectBuilder getJSONBuilder() {
        JSONObjectBuilder json = super.getJSONBuilder();
        json.put(CommsSensorConst.COMMS_SENSOR_TYPE, this.specType.getType());
        json.put(CommsSensorConst.DELAY, this.delay);
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
