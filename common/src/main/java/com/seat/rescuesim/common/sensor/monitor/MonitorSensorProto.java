package com.seat.rescuesim.common.sensor.monitor;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.sensor.SensorProto;
import com.seat.rescuesim.common.sensor.SensorType;

/** A serializable prototype of a Monitor Sensor. */
public class MonitorSensorProto extends SensorProto {
    public static final SensorType DEFAULT_MONITOR_SENSOR_TYPE = SensorType.MONITOR;

    public MonitorSensorProto(double range, double accuracy, double batteryUsage) {
        super(MonitorSensorProto.DEFAULT_MONITOR_SENSOR_TYPE, range, accuracy, batteryUsage);
    }

    public MonitorSensorProto(SensorType sensorType, double range, double accuracy, double batteryUsage) {
        super(sensorType, range, accuracy, batteryUsage);
    }

    public MonitorSensorProto(JSONOption option) throws JSONException {
        super(option);
    }

}
