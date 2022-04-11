package com.seat.rescuesim.common.sensor.monitor;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.JSONObjectBuilder;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.sensor.SensorProto;

/** A serializable prototype of a Monitor Sensor. */
public class MonitorSensorProto extends SensorProto {
    public static final String DEFAULT_MONITOR_TYPE = "monitor";
    public static final String MONITOR_TYPE = "monitor_type";

    private String monitorType;

    public MonitorSensorProto(double range, double accuracy, double batteryUsage) {
        this(MonitorSensorProto.DEFAULT_MONITOR_TYPE, range, accuracy, batteryUsage);
    }

    public MonitorSensorProto(String monitorType, double range, double accuracy, double batteryUsage) {
        super(range, accuracy, batteryUsage);
        this.monitorType = monitorType;
    }

    public MonitorSensorProto(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        super.decode(json);
        this.monitorType = (json.hasKey(MonitorSensorProto.MONITOR_TYPE)) ?
            json.getString(MonitorSensorProto.MONITOR_TYPE) :
            MonitorSensorProto.MONITOR_TYPE;
    }

    @Override
    protected JSONObjectBuilder getJSONBuilder() throws JSONException {
        JSONObjectBuilder json = super.getJSONBuilder();
        json.put(MonitorSensorProto.MONITOR_TYPE, this.monitorType);
        return json;
    }

    @Override
    public String getLabel() {
        return String.format("s:<%s>", this.monitorType);
    }

    public String getMonitorType() {
        return this.monitorType;
    }

    public boolean equals(MonitorSensorProto proto) {
        if (proto == null) return false;
        return super.equals(proto) && this.monitorType.equals(proto.monitorType);
    }

}
