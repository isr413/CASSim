package com.seat.rescuesim.common.sensor.comms;

import java.util.Collection;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.sensor.SensorConfig;

/** A serializable configuration for a Comms Sensor. */
public class CommsSensorConfig extends SensorConfig {

    public CommsSensorConfig(CommsSensorProto spec, int count) {
        super(spec, count);
    }

    public CommsSensorConfig(CommsSensorProto spec, Collection<String> sensorIDs) {
        super(spec, sensorIDs);
    }

    public CommsSensorConfig(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected CommsSensorProto decodeProto(JSONOption jsonSpec) throws JSONException {
        return new CommsSensorProto(jsonSpec);
    }

    @Override
    public CommsSensorProto getProto() {
        return (CommsSensorProto) super.getProto();
    }

    @Override
    public CommsSensorType getSpecType() {
        return (CommsSensorType) super.getSpecType();
    }

}
