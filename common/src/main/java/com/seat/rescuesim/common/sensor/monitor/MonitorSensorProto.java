package com.seat.rescuesim.common.sensor.monitor;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.sensor.SensorProto;

/** A serializable prototype of a Monitor Sensor. */
public class MonitorSensorProto extends SensorProto {

    public MonitorSensorProto(double range, double accuracy, double batteryUsage) {
        super(range, accuracy, batteryUsage);
    }

    public MonitorSensorProto(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    public String getLabel() {
        return "s:<monitor>";
    }

}
