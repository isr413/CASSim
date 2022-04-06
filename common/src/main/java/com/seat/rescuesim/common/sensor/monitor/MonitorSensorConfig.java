package com.seat.rescuesim.common.sensor.monitor;

import java.util.Collection;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.sensor.SensorConfig;

/** A serializable configuration for a Monitor Sensor. */
public class MonitorSensorConfig extends SensorConfig {

    public MonitorSensorConfig(MonitorSensorProto spec, int count) {
        super(spec, count);
    }

    public MonitorSensorConfig(MonitorSensorProto spec, Collection<String> sensorIDs) {
        super(spec, sensorIDs);
    }

    public MonitorSensorConfig(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected MonitorSensorProto decodeSpec(JSONOption jsonSpec) throws JSONException {
        return new MonitorSensorProto(jsonSpec);
    }

    @Override
    public MonitorSensorProto getSpec() {
        return (MonitorSensorProto) super.getSpec();
    }

    @Override
    public MonitorSensorType getSpecType() {
        return (MonitorSensorType) super.getSpecType();
    }

}
