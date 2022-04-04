package com.seat.rescuesim.common.sensor.vision;

import java.util.ArrayList;
import java.util.HashSet;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.sensor.SensorConfig;
import com.seat.rescuesim.common.sensor.SensorType;

/** A serializable configuration for a Vision Sensor. */
public class VisionSensorConfig extends SensorConfig {

    public VisionSensorConfig(VisionSensorSpec spec, int count) {
        super(SensorType.VISION, spec, count);
    }

    public VisionSensorConfig(VisionSensorSpec spec, ArrayList<String> sensorIDs) {
        super(SensorType.VISION, spec, sensorIDs);
    }

    public VisionSensorConfig(VisionSensorSpec spec, HashSet<String> sensorIDs) {
        super(SensorType.VISION, spec, sensorIDs);
    }

    public VisionSensorConfig(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected VisionSensorSpec decodeSpec(JSONOption jsonSpec) throws JSONException {
        return new VisionSensorSpec(jsonSpec);
    }

    @Override
    public VisionSensorSpec getSpec() {
        return (VisionSensorSpec) this.spec;
    }

    @Override
    public VisionSensorType getSpecType() {
        return (VisionSensorType) this.spec.getSpecType();
    }

}
