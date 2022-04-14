package com.seat.sim.common.sensor.comms;

import java.util.Collection;

import com.seat.sim.common.json.JSONException;
import com.seat.sim.common.json.JSONOptional;
import com.seat.sim.common.sensor.SensorConfig;

/** A serializable configuration for a Comms Sensor. */
public class CommsSensorConfig extends SensorConfig {

    public CommsSensorConfig(CommsSensorProto proto, int count, boolean active) {
        super(proto, count, active);
    }

    public CommsSensorConfig(CommsSensorProto proto, Collection<String> sensorIDs, boolean active) {
        super(proto, sensorIDs, active);
    }

    public CommsSensorConfig(JSONOptional optional) throws JSONException {
        super(optional);
    }

    @Override
    public CommsSensorProto getProto() {
        return (CommsSensorProto) super.getProto();
    }

}
