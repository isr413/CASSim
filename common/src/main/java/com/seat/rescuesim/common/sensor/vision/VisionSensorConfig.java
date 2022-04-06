package com.seat.rescuesim.common.sensor.vision;

import java.util.Collection;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.sensor.SensorConfig;

/** A serializable configuration for a Vision Sensor. */
public class VisionSensorConfig extends SensorConfig {

    public VisionSensorConfig(VisionSensorProto spec, int count) {
        super(spec, count);
    }

    public VisionSensorConfig(VisionSensorProto spec, Collection<String> sensorIDs) {
        super(spec, sensorIDs);
    }

    public VisionSensorConfig(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected VisionSensorProto decodeSpec(JSONOption jsonSpec) throws JSONException {
        return new VisionSensorProto(jsonSpec);
    }

    @Override
    public VisionSensorProto getSpec() {
        return (VisionSensorProto) super.getSpec();
    }

    @Override
    public VisionSensorType getSpecType() {
        return (VisionSensorType) super.getSpecType();
    }

}
