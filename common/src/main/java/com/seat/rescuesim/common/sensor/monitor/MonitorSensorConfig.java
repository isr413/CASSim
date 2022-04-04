package com.seat.rescuesim.common.sensor.monitor;

import java.util.ArrayList;
import java.util.HashSet;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.sensor.SensorConfig;
import com.seat.rescuesim.common.sensor.SensorType;

/** A serializable configuration for a Monitor Sensor. */
public class MonitorSensorConfig extends SensorConfig {

    public MonitorSensorConfig(MonitorSensorSpec spec, int count) {
        super(SensorType.MONITOR, spec, count);
    }

    public MonitorSensorConfig(MonitorSensorSpec spec, ArrayList<String> sensorIDs) {
        super(SensorType.MONITOR, spec, sensorIDs);
    }

    public MonitorSensorConfig(MonitorSensorSpec spec, HashSet<String> sensorIDs) {
        super(SensorType.MONITOR, spec, sensorIDs);
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
        return (MonitorSensorSpec) this.spec;
    }

    @Override
    public MonitorSensorType getSpecType() {
        return (MonitorSensorType) this.spec.getSpecType();
    }

}
