package com.seat.sim.common.sensor.monitor;

import com.seat.sim.common.json.JSONException;
import com.seat.sim.common.json.JSONOption;
import com.seat.sim.common.sensor.SensorProto;

/** A serializable prototype of a Monitor Sensor. */
public class MonitorSensorProto extends SensorProto {
    public static final String DEFAULT_MONITOR_SENSOR_MODEL = "monitor";
    public static final String MONITOR_TYPE = "monitor_type";

    public MonitorSensorProto(double range, double accuracy, double batteryUsage) {
        this(MonitorSensorProto.DEFAULT_MONITOR_SENSOR_MODEL, range, accuracy, batteryUsage);
    }

    public MonitorSensorProto(String sensorModel, double range, double accuracy, double batteryUsage) {
        super(sensorModel, range, accuracy, batteryUsage);
    }

    public MonitorSensorProto(JSONOption option) throws JSONException {
        super(option);
    }

}
