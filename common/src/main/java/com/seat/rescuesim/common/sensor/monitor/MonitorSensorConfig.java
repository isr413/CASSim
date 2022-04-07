package com.seat.rescuesim.common.sensor.monitor;

import java.util.Collection;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.sensor.SensorConfig;

/** A serializable configuration for a Monitor Sensor. */
public class MonitorSensorConfig extends SensorConfig {

    public MonitorSensorConfig(MonitorSensorProto proto, int count) {
        super(proto, count);
    }

    public MonitorSensorConfig(MonitorSensorProto proto, Collection<String> sensorIDs) {
        super(proto, sensorIDs);
    }

    public MonitorSensorConfig(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected MonitorSensorProto decodeProto(JSONOption option) throws JSONException {
        return new MonitorSensorProto(option);
    }

    @Override
    public MonitorSensorProto getProto() {
        return (MonitorSensorProto) super.getProto();
    }

}
