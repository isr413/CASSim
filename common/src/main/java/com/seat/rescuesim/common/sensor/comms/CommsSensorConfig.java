package com.seat.rescuesim.common.sensor.comms;

import java.util.Collection;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.sensor.SensorConfig;

/** A serializable configuration for a Comms Sensor. */
public class CommsSensorConfig extends SensorConfig {

    public CommsSensorConfig(CommsSensorProto proto, int count) {
        super(proto, count);
    }

    public CommsSensorConfig(CommsSensorProto proto, int count, boolean active) {
        super(proto, count, active);
    }

    public CommsSensorConfig(CommsSensorProto proto, Collection<String> sensorIDs) {
        super(proto, sensorIDs);
    }

    public CommsSensorConfig(CommsSensorProto proto, Collection<String> sensorIDs, boolean active) {
        super(proto, sensorIDs, active);
    }

    public CommsSensorConfig(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected CommsSensorProto decodeProto(JSONOption option) throws JSONException {
        return new CommsSensorProto(option);
    }

    @Override
    public CommsSensorProto getProto() {
        return (CommsSensorProto) super.getProto();
    }

}
