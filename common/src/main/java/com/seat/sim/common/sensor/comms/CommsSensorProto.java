package com.seat.sim.common.sensor.comms;

import com.seat.sim.common.json.JSONException;
import com.seat.sim.common.json.JSONObject;
import com.seat.sim.common.json.JSONObjectBuilder;
import com.seat.sim.common.json.JSONOptional;
import com.seat.sim.common.sensor.SensorProto;

/** A serializable prototype for a Comms Sensor. */
public class CommsSensorProto extends SensorProto {
    public static final String DEFAULT_COMMS_SENSOR_MODEL = "comms";
    public static final String DELAY = "delay";

    protected static final double DEFAULT_DELAY = 0.0;

    private double delay;

    public CommsSensorProto(double range, double accuracy, double batteryUsage) {
        this(CommsSensorProto.DEFAULT_COMMS_SENSOR_MODEL, range, accuracy, batteryUsage,
            CommsSensorProto.DEFAULT_DELAY);
    }

    public CommsSensorProto(String sensorModel, double range, double accuracy, double batteryUsage) {
        this(sensorModel, range, accuracy, batteryUsage, CommsSensorProto.DEFAULT_DELAY);
    }

    public CommsSensorProto(double range, double accuracy, double batteryUsage, double delay) {
        this(CommsSensorProto.DEFAULT_COMMS_SENSOR_MODEL, range, accuracy, batteryUsage, delay);
    }

    public CommsSensorProto(String sensorModel, double range, double accuracy, double batteryUsage, double delay) {
        super(sensorModel, range, accuracy, batteryUsage);
        this.delay = delay;
    }

    public CommsSensorProto(JSONOptional optional) throws JSONException {
        super(optional);
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

    public double getDelay() {
        return this.delay;
    }

    public boolean hasDelay() {
        return this.delay > 0;
    }

    public boolean equals(CommsSensorProto proto) {
        if (proto == null) return false;
        return super.equals(proto) && this.delay == proto.delay;
    }

}
