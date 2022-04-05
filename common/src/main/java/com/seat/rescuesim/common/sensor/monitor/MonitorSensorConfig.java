package com.seat.rescuesim.common.sensor.monitor;

import java.util.Collection;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.sensor.SensorConfig;

/** A serializable configuration for a Monitor Sensor. */
public class MonitorSensorConfig extends SensorConfig {

    public MonitorSensorConfig(MonitorSensorSpec spec, int count) {
        super(spec, count);
    }

    public MonitorSensorConfig(MonitorSensorSpec spec, Collection<String> sensorIDs) {
        super(spec, sensorIDs);
    }

    public MonitorSensorConfig(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected MonitorSensorSpec decodeSpec(JSONOption jsonSpec) throws JSONException {
        return new MonitorSensorSpec(jsonSpec);
    }

    @Override
    public MonitorSensorSpec getSpec() {
        return (MonitorSensorSpec) super.getSpec();
    }

    @Override
    public MonitorSensorType getSpecType() {
        return (MonitorSensorType) super.getSpecType();
    }

}
