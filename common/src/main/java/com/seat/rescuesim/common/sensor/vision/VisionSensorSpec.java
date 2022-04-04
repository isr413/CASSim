package com.seat.rescuesim.common.sensor.vision;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.JSONObjectBuilder;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.sensor.SensorSpec;
import com.seat.rescuesim.common.sensor.SensorType;

/** A serializable representation of a Vision Sensor spec. */
public class VisionSensorSpec extends SensorSpec {

    protected VisionSensorType specType;

    public VisionSensorSpec() {
        this(VisionSensorType.GENERIC);
    }

    public VisionSensorSpec(double range) {
        this(VisionSensorType.GENERIC, range);
    }

    public VisionSensorSpec(double range, double accuracy) {
        this(VisionSensorType.GENERIC, range, accuracy);
    }

    public VisionSensorSpec(double range, double accuracy, double batteryUsage) {
        this(VisionSensorType.GENERIC, range, accuracy, batteryUsage);
    }

    public VisionSensorSpec(VisionSensorType specType) {
        super(SensorType.VISION);
        this.specType = specType;
    }

    public VisionSensorSpec(VisionSensorType specType, double range) {
        super(SensorType.VISION, range);
        this.specType = specType;
    }

    public VisionSensorSpec(VisionSensorType specType, double range, double accuracy) {
        super(SensorType.VISION, range, accuracy);
        this.specType = specType;
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
        json.put(VisionSensorConst.VISION_SENSOR_TYPE, this.specType.getType());
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
