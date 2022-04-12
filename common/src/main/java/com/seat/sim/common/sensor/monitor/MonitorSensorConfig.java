package com.seat.sim.common.sensor.monitor;

import java.util.Collection;

import com.seat.sim.common.json.JSONException;
import com.seat.sim.common.json.JSONOption;
import com.seat.sim.common.sensor.SensorConfig;

/** A serializable configuration for a Monitor Sensor. */
public class MonitorSensorConfig extends SensorConfig {

    public MonitorSensorConfig(MonitorSensorProto proto, int count, boolean active) {
        super(proto, count, active);
    }

    public MonitorSensorConfig(MonitorSensorProto proto, Collection<String> sensorIDs, boolean active) {
        super(proto, sensorIDs, active);
    }

    public MonitorSensorConfig(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    public MonitorSensorProto getProto() {
        return (MonitorSensorProto) super.getProto();
    }

}
