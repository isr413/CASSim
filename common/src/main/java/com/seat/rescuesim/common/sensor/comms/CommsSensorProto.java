package com.seat.rescuesim.common.sensor.comms;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.JSONObjectBuilder;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.sensor.SensorProto;
import com.seat.rescuesim.common.sensor.SensorType;

/** A serializable prototype for a Comms Sensor. */
public class CommsSensorProto extends SensorProto {
    public static final SensorType DEFAULT_COMMS_SENSOR_TYPE = SensorType.COMMS;
    public static final String DELAY = "delay";

    protected static final double DEFAULT_DELAY = 0.0;

    private double delay;

    public CommsSensorProto() {
        this(CommsSensorProto.DEFAULT_COMMS_SENSOR_TYPE, SensorProto.DEFAULT_RANGE, SensorProto.DEFAULT_ACCURACY,
            SensorProto.DEFAULT_BATTERY_USAGE, CommsSensorProto.DEFAULT_DELAY);
    }

    public CommsSensorProto(double range) {
        this(CommsSensorProto.DEFAULT_COMMS_SENSOR_TYPE, range, SensorProto.DEFAULT_ACCURACY,
            SensorProto.DEFAULT_BATTERY_USAGE, CommsSensorProto.DEFAULT_DELAY);
    }

    public CommsSensorProto(double range, double accuracy) {
        this(CommsSensorProto.DEFAULT_COMMS_SENSOR_TYPE, range, accuracy, SensorProto.DEFAULT_BATTERY_USAGE,
            CommsSensorProto.DEFAULT_DELAY);
    }

    public CommsSensorProto(double range, double accuracy, double batteryUsage) {
        this(CommsSensorProto.DEFAULT_COMMS_SENSOR_TYPE, range, accuracy, batteryUsage, CommsSensorProto.DEFAULT_DELAY);
    }

    public CommsSensorProto(double range, double accuracy, double batteryUsage, double delay) {
        this(CommsSensorProto.DEFAULT_COMMS_SENSOR_TYPE, range, accuracy, batteryUsage, delay);
    }

    public CommsSensorProto(SensorType sensorType) {
        this(sensorType, SensorProto.DEFAULT_RANGE, SensorProto.DEFAULT_ACCURACY, SensorProto.DEFAULT_BATTERY_USAGE,
            CommsSensorProto.DEFAULT_DELAY);
    }

    public CommsSensorProto(SensorType sensorType, double range) {
        this(sensorType, range, SensorProto.DEFAULT_ACCURACY, SensorProto.DEFAULT_BATTERY_USAGE,
            CommsSensorProto.DEFAULT_DELAY);
    }

    public CommsSensorProto(SensorType sensorType, double range, double accuracy) {
        this(sensorType, range, accuracy, SensorProto.DEFAULT_BATTERY_USAGE, CommsSensorProto.DEFAULT_DELAY);
    }

    public CommsSensorProto(SensorType sensorType, double range, double accuracy, double batteryUsage) {
        this(sensorType, range, accuracy, batteryUsage, CommsSensorProto.DEFAULT_DELAY);
    }

    public CommsSensorProto(SensorType sensorType, double range, double accuracy, double batteryUsage, double delay) {
        super(sensorType, range, accuracy, batteryUsage);
        this.delay = delay;
    }

    public CommsSensorProto(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        super.decode(json);
        this.delay = (json.hasKey(CommsSensorProto.DELAY)) ?
            json.getDouble(CommsSensorProto.DELAY) :
            CommsSensorProto.DEFAULT_DELAY;
    }

    @Override
    protected JSONObjectBuilder getJSONBuilder() throws JSONException {
        JSONObjectBuilder json = super.getJSONBuilder();
        json.put(CommsSensorProto.DELAY, this.delay);
        return json;
    }

    public double getSensorDelay() {
        return this.delay;
    }

    public boolean equals(CommsSensorProto proto) {
        if (proto == null) return false;
        return super.equals(proto) && this.delay == proto.delay;
    }

}
