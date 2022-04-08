package com.seat.rescuesim.common.sensor.vision;

import java.util.Collection;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.sensor.SensorConfig;

/** A serializable configuration for a Vision Sensor. */
public class VisionSensorConfig extends SensorConfig {

    public VisionSensorConfig(VisionSensorProto proto, int count) {
        super(proto, count);
    }

    public VisionSensorConfig(VisionSensorProto proto, int count, boolean active) {
        super(proto, count, active);
    }

    public VisionSensorConfig(VisionSensorProto proto, Collection<String> sensorIDs) {
        super(proto, sensorIDs);
    }

    public VisionSensorConfig(VisionSensorProto proto, Collection<String> sensorIDs, boolean active) {
        super(proto, sensorIDs, active);
    }

    public VisionSensorConfig(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    public VisionSensorProto getProto() {
        return (VisionSensorProto) super.getProto();
    }

}
