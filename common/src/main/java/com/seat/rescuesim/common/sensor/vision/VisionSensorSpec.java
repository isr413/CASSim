package com.seat.rescuesim.common.sensor.vision;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.JSONObjectBuilder;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.sensor.SensorSpec;
import com.seat.rescuesim.common.sensor.SensorType;

/** A serializable representation of a Vision Sensor spec. */
public class VisionSensorSpec extends SensorSpec {

    protected static final VisionSensorType DEFAULT_SPEC_TYPE = VisionSensorType.GENERIC;

    private VisionSensorType specType;

    public VisionSensorSpec() {
        this(VisionSensorSpec.DEFAULT_SPEC_TYPE, SensorSpec.DEFAULT_RANGE, SensorSpec.DEFAULT_ACCURACY,
            SensorSpec.DEFAULT_BATTERY_USAGE);
    }

    public VisionSensorSpec(double range) {
        this(VisionSensorSpec.DEFAULT_SPEC_TYPE, range, SensorSpec.DEFAULT_ACCURACY, SensorSpec.DEFAULT_BATTERY_USAGE);
    }

    public VisionSensorSpec(double range, double accuracy) {
        this(VisionSensorSpec.DEFAULT_SPEC_TYPE, range, accuracy, SensorSpec.DEFAULT_BATTERY_USAGE);
    }

    public VisionSensorSpec(double range, double accuracy, double batteryUsage) {
        this(VisionSensorSpec.DEFAULT_SPEC_TYPE, range, accuracy, batteryUsage);
    }

    public VisionSensorSpec(VisionSensorType specType) {
        this(specType, SensorSpec.DEFAULT_RANGE, SensorSpec.DEFAULT_ACCURACY, SensorSpec.DEFAULT_BATTERY_USAGE);
    }

    public VisionSensorSpec(VisionSensorType specType, double range) {
        this(specType, range, SensorSpec.DEFAULT_ACCURACY, SensorSpec.DEFAULT_BATTERY_USAGE);
    }

    public VisionSensorSpec(VisionSensorType specType, double range, double accuracy) {
        this(specType, range, accuracy, SensorSpec.DEFAULT_BATTERY_USAGE);
    }

    public VisionSensorSpec(VisionSensorType specType, double range, double accuracy, double batteryUsage) {
        super(SensorType.VISION, range, accuracy, batteryUsage);
        this.specType = specType;
    }

    public VisionSensorSpec(JSONOption option) throws JSONException {
        super(option);
    }

    @Override
    protected void decode(JSONObject json) throws JSONException {
        super.decode(json);
        this.specType = VisionSensorType.decodeType(json);
    }

    @Override
    protected JSONObjectBuilder getJSONBuilder() {
        JSONObjectBuilder json = super.getJSONBuilder();
        json.put(VisionSensorType.VISION_SENSOR_TYPE, this.specType.getType());
        return json;
    }

    @Override
    public VisionSensorType getSpecType() {
        return (VisionSensorType) this.specType;
    }

    public boolean equals(VisionSensorSpec spec) {
        return super.equals(spec) && this.specType.equals(spec.specType);
    }

}
