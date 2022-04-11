package com.seat.rescuesim.common.sensor.comms;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.JSONObjectBuilder;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.sensor.SensorProto;

/** A serializable prototype for a Comms Sensor. */
public class CommsSensorProto extends SensorProto {
    public static final String COMMS_TYPE = "comms_type";
    public static final String DEFAULT_COMMS_TYPE = "comms";
    public static final String DELAY = "delay";

    protected static final double DEFAULT_DELAY = 0.0;

    private String commsType;
    private double delay;

    public CommsSensorProto(double range, double accuracy, double batteryUsage) {
        this(CommsSensorProto.DEFAULT_COMMS_TYPE, range, accuracy, batteryUsage, CommsSensorProto.DEFAULT_DELAY);
    }

    public CommsSensorProto(String commsType, double range, double accuracy, double batteryUsage) {
        this(commsType, range, accuracy, batteryUsage, CommsSensorProto.DEFAULT_DELAY);
    }

    public CommsSensorProto(double range, double accuracy, double batteryUsage, double delay) {
        this(CommsSensorProto.DEFAULT_COMMS_TYPE, range, accuracy, batteryUsage, delay);
    }

    public CommsSensorProto(String commsType, double range, double accuracy, double batteryUsage, double delay) {
        super(range, accuracy, batteryUsage);
        this.commsType = commsType;
        this.delay = delay;
    }

    public CommsSensorProto(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        super.decode(json);
        this.commsType = (json.hasKey(CommsSensorProto.COMMS_TYPE)) ?
            json.getString(CommsSensorProto.COMMS_TYPE) :
            CommsSensorProto.DEFAULT_COMMS_TYPE;
        this.delay = (json.hasKey(CommsSensorProto.DELAY)) ?
            json.getDouble(CommsSensorProto.DELAY) :
            CommsSensorProto.DEFAULT_DELAY;
    }

    @Override
    protected JSONObjectBuilder getJSONBuilder() throws JSONException {
        JSONObjectBuilder json = super.getJSONBuilder();
        json.put(CommsSensorProto.COMMS_TYPE, this.commsType);
        json.put(CommsSensorProto.DELAY, this.delay);
        return json;
    }

    public String getCommsType() {
        return this.commsType;
    }

    public double getDelay() {
        return this.delay;
    }

    @Override
    public String getLabel() {
        return String.format("s:<%s>", this.commsType);
    }

    public boolean hasDelay() {
        return this.delay > 0;
    }

    public boolean equals(CommsSensorProto proto) {
        if (proto == null) return false;
        return super.equals(proto) && this.commsType.equals(proto.commsType) && this.delay == proto.delay;
    }

}
