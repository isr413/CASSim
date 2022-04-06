package com.seat.rescuesim.common.sensor.vision;

import com.seat.rescuesim.common.json.JSONException;
import com.seat.rescuesim.common.json.JSONObject;
import com.seat.rescuesim.common.json.JSONObjectBuilder;
import com.seat.rescuesim.common.json.JSONOption;
import com.seat.rescuesim.common.sensor.SensorProto;
import com.seat.rescuesim.common.sensor.SensorType;

/** A serializable prototype of a Vision Sensor. */
public class VisionSensorProto extends SensorProto {

    protected static final VisionSensorType DEFAULT_SPEC_TYPE = VisionSensorType.GENERIC;

    private VisionSensorType specType;

    public VisionSensorProto() {
        this(VisionSensorProto.DEFAULT_SPEC_TYPE, SensorProto.DEFAULT_RANGE, SensorProto.DEFAULT_ACCURACY,
            SensorProto.DEFAULT_BATTERY_USAGE);
    }

    public VisionSensorProto(double range) {
        this(VisionSensorProto.DEFAULT_SPEC_TYPE, range, SensorProto.DEFAULT_ACCURACY,
            SensorProto.DEFAULT_BATTERY_USAGE);
    }

    public VisionSensorProto(double range, double accuracy) {
        this(VisionSensorProto.DEFAULT_SPEC_TYPE, range, accuracy, SensorProto.DEFAULT_BATTERY_USAGE);
    }

    public VisionSensorProto(double range, double accuracy, double batteryUsage) {
        this(VisionSensorProto.DEFAULT_SPEC_TYPE, range, accuracy, batteryUsage);
    }

    public VisionSensorProto(VisionSensorType specType) {
        this(specType, SensorProto.DEFAULT_RANGE, SensorProto.DEFAULT_ACCURACY, SensorProto.DEFAULT_BATTERY_USAGE);
    }

    public VisionSensorProto(VisionSensorType specType, double range) {
        this(specType, range, SensorProto.DEFAULT_ACCURACY, SensorProto.DEFAULT_BATTERY_USAGE);
    }

    public VisionSensorProto(VisionSensorType specType, double range, double accuracy) {
        this(specType, range, accuracy, SensorProto.DEFAULT_BATTERY_USAGE);
    }

    public VisionSensorProto(VisionSensorType specType, double range, double accuracy, double batteryUsage) {
        super(SensorType.VISION, range, accuracy, batteryUsage);
        this.specType = specType;
    }

    public VisionSensorProto(JSONOption option) throws JSONException {
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

    public boolean equals(VisionSensorProto spec) {
        return super.equals(spec) && this.specType.equals(spec.specType);
    }

}
