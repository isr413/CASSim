package com.seat.rescuesim.common.sensor.comms;

import java.util.Collection;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.sensor.SensorConfig;

/** A serializable configuration for a Comms Sensor. */
public class CommsSensorConfig extends SensorConfig {

    public CommsSensorConfig(CommsSensorSpec spec, int count) {
        super(spec, count);
    }

    public CommsSensorConfig(CommsSensorSpec spec, Collection<String> sensorIDs) {
        super(spec, sensorIDs);
    }

    public CommsSensorConfig(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected CommsSensorSpec decodeSpec(JSONOption jsonSpec) throws JSONException {
        return new CommsSensorSpec(jsonSpec);
    }

    @Override
    public CommsSensorSpec getSpec() {
        return (CommsSensorSpec) this.spec;
    }

    @Override
    public CommsSensorType getSpecType() {
        return (CommsSensorType) this.spec.getSpecType();
    }

}
